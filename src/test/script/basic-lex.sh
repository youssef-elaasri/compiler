#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Base pour un script de test de la lexicographie.
# On teste un fichier valide et un fichier invalide.
# Il est conseillé de garder ce fichier tel quel, et de créer de
# nouveaux scripts (en s'inspirant si besoin de ceux fournis).

# Il faudrait améliorer ce script pour qu'il puisse lancer test_lex
# sur un grand nombre de fichiers à la suite.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

RED='\e[0;31m'
GREEN='\e[0;32m'
RESET='\e[0m'


# Testing  synthax
echo "Testing  Synthax..."
if test_lex "./src/test/deca/syntax/valid/provided/Synthax_test.deca" 2>&1 \
            | grep -q  -e 'while' \
    # && test_lex "./src/test/deca/syntax/valid/provided/Synthax_test.deca" 2>&1 \
    # | grep -q -e 'if'\
    # && test_lex "./src/test/deca/syntax/valid/provided/Synthax_test.deca" 2>&1 \
    # | grep -q -e 'println'\
    # && test_lex "./src/test/deca/syntax/valid/provided/Synthax_test.deca" 2>&1 \
    # | grep -q -e 'else'
then
    echo "${GREEN}PASSED${RESET}"
else
    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing  include
echo "Testing  include..."

if test_lex "./src/test/deca/syntax/valid/provided/Include_test.deca" 2>&1 \
            | grep -q -e 'println'
then
    echo "${GREEN}PASSED${RESET}"
else
    echo "${RED}FAILED${RESET}"
    exit 1
fi




# Testing Circular include
echo "Testing circual include..."

if test_lex "./src/test/deca/syntax/invalid/provided/circularInclude_test.deca" 2>&1 \
            | grep -q -e ' Circular include for file'
then
    echo "${GREEN}PASSED${RESET}"
else
    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing not found include
echo "Testing invalid file include..."

if test_lex "./src/test/deca/syntax/invalid/provided/Include_not_found_test.deca" 2>&1 \
            | grep -q -e ' include file not found'
then
    echo "${GREEN}PASSED${RESET}"
else
    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing Wrong braces
echo " Testing wrong braces ..."
if test_lex ./src/test/deca/syntax/invalid/provided/wrong_braces_lex.deca 2>&1 \
    | grep -q -e 'wrong_braces_lex.deca:[0-9]:'
then
   echo "${GREEN}PASSED${RESET}"
else
echo "${RED}FAILED${RESET}"
exit 1
fi

# Testing comments 

echo " Testing comments..."
if test_lex ./src/test/deca/syntax/invalid/provided/Comments_text.deca 2>&1 \
    | grep -q -e 'comment'
then
   echo "${RED}FAILED${RESET}"
    exit 1
else
echo "${GREEN}PASSED${RESET}"
fi


# Testing chaine incomplete

echo " Testing incomplete String..."
if test_lex ./src/test/deca/syntax/invalid/provided/chaine_incomplete_test.deca 2>&1 \
    | grep -q -e 'chaine_incomplete_test.deca:[0-9]'
then
   echo "${GREEN}PASSED${RESET}"
    exit 1
else
echo "${RED}FAILED${RESET}"
fi


exit 0