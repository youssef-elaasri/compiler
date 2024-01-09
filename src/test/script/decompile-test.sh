#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

result_file="./src/test/script/launchers/decompile_result.deca"
result_file2="./src/test/script/launchers/decompile_result2.deca"

# tester la decompilation de print,println
input_file="./src/test/script/launchers/test_include.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[32mTest Passed\e[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de la declaration de variable 
input_file="./src/test/script/launchers/decompile_tests/test_declaration.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[32mTest Passed\e[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de l'initialization de variable 
input_file="./src/test/script/launchers/decompile_tests/test_initialization.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[32mTest Passed\e[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de ifelse 
input_file="./src/test/script/launchers/decompile_tests/test_ifelse.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[32mTest Passed\e[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
fi