#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"


result_file="./src/test/deca/syntax/valid/provided/decompile_results/decompile_result.deca"
result_file2="./src/test/deca/syntax/valid/provided/decompile_results/decompile_result2.deca"


echo -e "\033[33mTesting Decompile\033[0m"

# tester la decompilation de print,println
input_file="./src/test/deca/syntax/valid/provided/test_include_decompile.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
echo  -e "\033[32m Decompile test #1 PASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi


# Tester la decompilation de la declaration de variable 
input_file="./src/test/deca/syntax/valid/provided/test_declaration_decompile.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
echo  -e "\033[32m Decompile test #2 PASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi



# Tester la decompilation de l'initialization de variable 
input_file="./src/test/deca/syntax/valid/provided/test_initialization_decompile.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
echo  -e "\033[32m Decompile test #3 PASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi



# Tester la decompilation de ifelse 
input_file="./src/test/deca/syntax/valid/provided/test_ifelse_decompile.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
echo  -e "\033[32m Decompile test #4 PASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi




# Tester la decompilation de while




# Tester la decompilation de print et println





# Tester la decompilation de 



