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

java -Xmx8G -Xms4G -Xss1024K \
     -Dmd.class.path=$md_cp_url \
     -Dcom.nomagic.osgi.config.dir="$MD_HOME/configuration" \
     -Desi.system.config="$MD_HOME/data/application.conf" \
     -Dlogback.configurationFile="$MD_HOME/data/logback.xml" \
     -Dmd.plugins.dir="$MD_HOME/plugins${cp_delim}target/plugin-release/files/plugins${cp_delim}../com.incquerylabs.instaschema.performance/target/plugin-release/files/plugins" \
     -Dcom.nomagic.magicdraw.launcher=com.nomagic.magicdraw.commandline.CommandLineActionLauncher \
     -Dcom.nomagic.magicdraw.commandline.action=com.incquerylabs.magicdraw.validation.test.runner.TestRunner \
     -cp "$CP" \
     com.nomagic.osgi.launcher.ProductionFrameworkLauncher "$@"