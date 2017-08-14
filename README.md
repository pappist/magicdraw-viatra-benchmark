# The MagicDraw VIATRA Query performance benchmark

This benchmark is intended to showcase the performance of the [VIATRA](http://eclipse.org/viatra) [Query](https://wiki.eclipse.org/VIATRA/Query) engine when run directly on MagicDraw SysML models, through the EMF interface.

## Getting Started

### Cloning this repository

Use the green "Clone or download" link on the GitHub web to clone the repository and get the source.

From now on, the folder that contains this README.md file on your computer is called `WORKSPACE`

```bash
export WORKSPACE=c:/git/magicdraw-viatra-benchmark
```

### Get the instance models

You need to put the set of instance models (`mdzip` files) that the benchmark runs on in a `benchmarking` folder inside the MagicDraw installation folder (`MD_HOME`).

You can download the models from this link: http://static.incquerylabs.com/projects/magicdraw/benchmark/models/models-tmt.zip

### Running the benchmark

After you clone this repository, you can run the benchmark by executing the following commands in the `WORKSPACE` folder:

```bash
#!/bin/bash
MD_HOME=<MagicDraw Installation folder>

# compile and execute benchmark
mvn clean install -Dmd.home=$MD_HOME
```

You need to have a working MagicDraw 18.4 on your system at the location set in the MD_HOME variable (you can set it as an environment variable as well).
In addition, you need to have Maven and Java installed to run the build (we use Maven 3.3.9 and Oracle JDK 8).

The results of the benchmark will be available in the `com.incquerylabs.benchmark.test/results` folder.

### Create diagrams from results

Reporting is done using the [MONDO-SAM](https://github.com/FTSRG/mondo-sam/) tool, we are using the 0.1-maintenance branch.
MONDO-SAM requires [R](https://www.r-project.org/) and [Python 3](https://www.python.org/) to be installed and available in your PATH.
You can find information on how to setup MONDO-SAM on their repository.

The following script downloads MONDO-SAM, converts the results from `com.incquerylabs.benchmark.test/results/<query>` to `benchmark/results` then generates the diagrams to `benchmark/diagrams`:

```bash
#!/bin/bash

# clone and checkout MONDO-SAM for reporting scripts
./benchmark/dep-mondo-sam.sh

# converts results from individual JSON files to CSV
./benchmark/convert_results.sh

# generates diagrams with R
./benchmark/report.sh
```

You need to set the `WORKSPACE` environment variable to the repository root as the scripts use relative paths from based from that variable.

### Configuring the benchmark

By default, the benchmark runs on a very small subset of queries, with the smallest model and only once per measurement.
This behaviour is configured by four variables and affects how `com.incquerylabs.benchmark.test/run.sh` behaves:

The name of the variables and their possible values are the following:
* `BENCHMARK_ENGINES`: Which query engine implementations to measure
  * Default (all possible values): `RETE, LOCAL_SEARCH, LOCAL_SEARCH_HINTS-CONDITION_FIRST, LOCAL_SEARCH_HINTS-TC_FIRST, HYBRID`
* `BENCHMARK_QUERIES`: Which queries to measure
  * Default: `transitiveSubstatesWithCheck3`
  * Possible values: `blocksOrRequirementsOrConstraints, alphabeticalDependencies, circularDependencies, loopTransitionWithTriggerEffectEventNoGuard, stateWithMostSubstates, transitiveSubstatesWithCheck3, allBenchMarkedQueries`
* `BENCHMARK_SIZES`: Which models to measure
  * Default: `300000`
  * Possible values: `300000, 540000, 780000, 1040000, 1200000`
* `BENCHMARK_RUNS`: How many times the measurements should be run for each configuration
  * Default: `1`
  
You can either set this in environment variables before running the benchmark or modify the `run.sh` script directly.

<!-- end of Getting started (only edit outside of section, please) -->

## Repository structure
* [Benchmark plugin](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/tree/master/com.incquerylabs.magicdraw.benchmark/src/com/incquerylabs/magicdraw/benchmark)
  * [Benchmark queries](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/tree/master/com.incquerylabs.magicdraw.benchmark/src/com/incquerylabs/magicdraw/benchmark/queries) 
  * [Benchmark plugin entry point](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/blob/master/com.incquerylabs.magicdraw.benchmark/src/com/incquerylabs/magicdraw/benchmark/MagicDrawPerformancePlugin.java)
  * [Benchmark execution script](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/blob/master/com.incquerylabs.magicdraw.benchmark/run.sh)
* [Report generation](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/tree/master/benchmark)
* [MagicDraw target platform definition](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/tree/master/MagicDraw)
* [Mondo-SAM benchmarking framework bundle](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/tree/master/eu.mondo.sam.bundle)

## [Benchmark details and results wiki page](https://github.com/IncQueryLabs/magicdraw-viatra-benchmark/wiki/MagicDraw-VIATRA-benchmark-results)
