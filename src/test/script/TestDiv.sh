#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024


cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"





if test_context src/test/deca/context/valid/provided/test_division.deca 2>&1 | \
    grep -q '1 5 0.75'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi