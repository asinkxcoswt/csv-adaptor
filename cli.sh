#!/bin/bash

load_test_prepare() {
  _doc 'Prepare load test data' && return 1
  alert load_test_prepare
  mkdir -p .temp
  curl -L https://docs.google.com/spreadsheets/d/1CwW3gTLvQMhWJmFVbglJkwL8b-EbPhjwjtPmDheXPx4/export?format=csv > .temp/csv-adaptor-performance-test-data.csv
  head -10000 .temp/csv-adaptor-performance-test-data.csv > .temp/csv-adaptor-performance-test-data-10k.csv
}

load_test_run() {
    _doc 'Run load test' && return 1
    alert load_test_run

    test -f ".temp/csv-adaptor-performance-test-data.csv" || error "Missing file .temp/csv-adaptor-performance-test-data.csv, please run ./cli.sh load_test_prepare"
    test -f ".temp/csv-adaptor-performance-test-data-10k.csv" || error "Missing file .temp/csv-adaptor-performance-test-data-10k.csv, please run ./cli.sh load_test_prepare"

    alert parallel_10k
    _load_test_exec ".temp/outputs_parallel_10k" ".temp/csv-adaptor-performance-test-data-10k.csv" ".temp/parallel_10k.log" "true"

    alert single_10k
    _load_test_exec ".temp/outputs_single_10k" ".temp/csv-adaptor-performance-test-data-10k.csv" ".temp/single_10k.log" "false"

    alert parallel_100k
    _load_test_exec ".temp/outputs_parallel_100k" ".temp/csv-adaptor-performance-test-data.csv" ".temp/parallel_100k.log" "true"

    alert single_100k
    _load_test_exec ".temp/outputs_single_100k" ".temp/csv-adaptor-performance-test-data.csv" ".temp/single_100k.log" "false"

}

_load_test_exec() {
    output_dir="$1"
    input_file="$2"
    log_file="$3"
    parallel="$4"
    test -d "${output_dir}" && rm -rf "${output_dir}"
    java -Xms1G -Xmx1G -jar ./target/csv-adaptor-1.0.jar \
      --input-file="${input_file}" \
      --output-dir="${output_dir}" \
      --record-type=example \
      --parallel="${parallel}" \
      --output-format=json > "${log_file}" 2>&1 &

    app_pid=$!
    jcmd $app_pid GC.heap_info
  #  jcmd $app_pid JFR.start duration=60s filename=myrecording.jfr
    jstat -gc -t $app_pid 1s
    cat "${log_file}"
}

source ./template.sh
