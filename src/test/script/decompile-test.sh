#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1



result_file="src/test/deca/codegen/valid/provided/decompile_results/decompile_result.deca"
result_file2="src/test/deca/codegen/valid/provided/decompile_results/decompile_result2.deca"


echo -e "\033[33mTesting Decompile\033[0m"

# tester la decompilation de print,println
input_file="./src/test/deca/codegen/valid/provided/test_include.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if !diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de la declaration de variable 
input_file="./src/test/deca/codegen/valid/provided/test_declaration.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if !diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de l'initialization de variable 
input_file=".src/test/deca/codegen/valid/provided/test_initialization.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if !diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[31mTest Failed\e[0m"
fi


# Tester la decompilation de ifelse 
input_file="./src/test/deca/codegen/valid/provided/test_ifelse.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if !diff "$result_file" "$result_file2" &> /dev/null; then
  echo -e "\e[31mTest Failed\e[0m"
fi


echo -e "\033[32mPASSED\033[0m"
