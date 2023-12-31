#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1


input_file="/home/youssef147/Projet_GL1/src/test/script/launchers/test_include.deca"
result_file="/home/youssef147/Projet_GL1/src/test/script/launchers/decompile_result.decac"
result_file2="/home/youssef147/Projet_GL1/src/test/script/launchers/decompile_result2.decac"

decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[32mTest Passed\e[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
fi
