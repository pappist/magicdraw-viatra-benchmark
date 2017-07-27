#!/bin/bash
cd "$( cd "$( dirname "$0" )" && pwd )"

# Ensure old results are removed
rm -rf ${WORKSPACE}/benchmark/results
mkdir ${WORKSPACE}/benchmark/results

cd ${WORKSPACE}/com.incquerylabs.benchmark.test/results

for i in $(ls -d */); do
  echo ${i};
  
  if [ "warmup/" == ${i} ]; then
    echo "SKIP"
  else
    echo "PROCESS"
	mkdir ${WORKSPACE}/benchmark/results/${i}
	python3 ${WORKSPACE}/mondo-sam/reporting/convert_results.py --source ${WORKSPACE}/com.incquerylabs.benchmark.test/results/${i} \
--jsonfile ${WORKSPACE}/benchmark/results/${i}results.json \
--csvfile ${WORKSPACE}/benchmark/results/${i}results.csv
  fi
  
done


