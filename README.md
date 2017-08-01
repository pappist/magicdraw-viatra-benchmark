# magicdraw-viatra-benchmark

## Getting Started

### Cloning this repository

Use the green "Clone or download" link on the GitHub web to clone the repository and get the source.

From now on, the folder that contains this README.md file on your computer is called `WORKSPACE`
```
export WORKSPACE=c:/git/magicdraw-viatra-benchmark
```

### Get the instance models

You need to 

### Running the benchmark

After you clone this repository, you can run the benchmark by executing the following commands in the `WORKSPACE` folder:
```
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
```
#!/bin/bash

# clone and checkout MONDO-SAM for reporting scripts
./benchmark/dep-mondo-sam.sh

# converts results from individual JSON files to CSV
./benchmark/convert_results.sh

# generates diagrams with R
./benchmark/report.sh
```

You need to set the `WORKSPACE` environment variable to the repository root as the scripts use relative paths from based from that variable.

### 

<!-- end of Getting started (only edit outside of section, please) -->

## Repository structure

TODO @lunkpeter
