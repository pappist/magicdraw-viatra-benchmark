#!/bin/bash
 
if [ -z "$MD_HOME" ]; then
    echo "MD_HOME environment variable not set, please set it to the MagicDraw installation folder"
    exit 1
fi
 
if [ "$OS" = Windows_NT ]; then
    md_home_url_leader=$(echo "$MD_HOME" | sed -e 's/^/\//' -e 's/ /%20/g' -e 's/\\/\//g')
    md_home_url_base=$(echo "$MD_HOME" | sed -e 's/:/%3A/g' -e 's/ /%20/g' \
                                                    -e 's/\//%2F/g' -e 's/\\/%5C/g')
    cp_delim=";"
else
    md_home_url_leader=$(echo "$MD_HOME" | sed -e 's/ /%20/g')
    md_home_url_base=$(echo "$MD_HOME" | sed -e 's/ /%20/g')
    cp_delim=":"
fi
 
md_cp_url=file:$md_home_url_leader/bin/magicdraw.properties?base=$md_home_url_base#CLASSPATH
 
OSGI_LAUNCHER=$(echo "$MD_HOME"/lib/com.nomagic.osgi.launcher-*.jar)
OSGI_FRAMEWORK=$(echo "$MD_HOME"/lib/bundles/org.eclipse.osgi_*.jar)
MD_OSGI_FRAGMENT=$(echo "$MD_HOME"/lib/bundles/com.nomagic.magicdraw.osgi.fragment_*.jar)
 

CP="${OSGI_LAUNCHER}${cp_delim}${OSGI_FRAMEWORK}${cp_delim}${MD_OSGI_FRAGMENT}${cp_delim}\
`  `$MD_HOME/lib/md_api.jar${cp_delim}$MD_HOME/lib/md_common_api.jar${cp_delim}\
`  `$MD_HOME/lib/md.jar${cp_delim}$MD_HOME/lib/md_common.jar${cp_delim}\
`  `$MD_HOME/lib/jna.jar"

# Setup benchmark
if [ -z "$BENCHMARK_ENGINES" ]; then
#BENCHMARK_ENGINES="RETE, LOCAL_SEARCH, LOCAL_SEARCH_HINTS-CONDITION_FIRST, LOCAL_SEARCH_HINTS-TC_FIRST, HYBRID"
BENCHMARK_ENGINES="RETE, LOCAL_SEARCH, HYBRID"
fi
echo "Selected engines: ${BENCHMARK_ENGINES}"

if [ -z "$BENCHMARK_QUERIES" ]; then
#BENCHMARK_QUERIES="blocksOrRequirementsOrConstraints, alphabeticalDependencies, circularDependencies, loopTransitionWithTriggerEffectEventNoGuard, stateWithMostSubstates, transitiveSubstatesWithCheck3, allBenchMarkedQueries"
BENCHMARK_QUERIES="transitiveSubstatesWithCheck3"
fi
echo "Selected queries: ${BENCHMARK_QUERIES}"

if [ -z "$BENCHMARK_SIZES" ]; then
#BENCHMARK_SIZES="300000, 540000, 780000, 1040000, 1200000"
BENCHMARK_SIZES="300000"
fi
echo "Selected sizes: ${BENCHMARK_SIZES}"

if [ -z "$BENCHMARK_RUNS" ]; then
BENCHMARK_RUNS=1
fi
echo "Number of runs: ${BENCHMARK_RUNS}"

if [ -z $WORKSPACE ]; then 
    OUTPUT_DIR="results"
else 
    OUTPUT_DIR="$WORKSPACE/com.incquerylabs.magicdraw.benchmark/results"
fi

IFS=', ' read -r -a engines <<< "$BENCHMARK_ENGINES"
IFS=', ' read -r -a queries <<< "$BENCHMARK_QUERIES"
IFS=', ' read -r -a modelsizes <<< "$BENCHMARK_SIZES"

# Run benchmark
for runIndex in `seq 1 "$BENCHMARK_RUNS"`;
do
	
	echo "Run: $runIndex"
	for engine in "${engines[@]}";
	do
		echo "Engine: $engine"
		for size in "${modelsizes[@]}";
		do
			echo "Model size: $size"
			
			for query in "${queries[@]}";
			do
				echo "Query: $query"
				echo "Running measurement on $query with $engine (model size: $size ; runIndex: $runIndex )"
				# Call MD
				java -Xmx8G -Xms4G -Xss1024K \
					-Dmd.class.path=$md_cp_url \
					-Dcom.nomagic.osgi.config.dir="$MD_HOME/configuration" \
					-Desi.system.config="$MD_HOME/data/application.conf" \
					-Dlogback.configurationFile="$MD_HOME/data/logback.xml" \
					-Dmd.plugins.dir="$MD_HOME/plugins${cp_delim}target/plugin-release/files/plugins${cp_delim}../com.incquerylabs.benchmark.performance/target/plugin-release/files/plugins" \
					-Dcom.nomagic.magicdraw.launcher=com.nomagic.magicdraw.commandline.CommandLineActionLauncher \
					-Dcom.nomagic.magicdraw.commandline.action=com.incquerylabs.magicdraw.benchmark.PerformanceBenchmarkRunner \
					-cp "$CP" \
					com.nomagic.osgi.launcher.ProductionFrameworkLauncher "$@ -engine $engine -query $query -index $runIndex -size $size -model '${MD_HOME}/performance/inputs/TMT$size.mdzip' -warmup '${MD_HOME}/performance/inputs/Warmup.mdzip' -output '${OUTPUT_DIR}'"
			done
		done
	done
done

