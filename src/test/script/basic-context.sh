#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


# Test variable declarations

if test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q 'definition: variable defined at \[5, 8\], type=int'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q 'definition: variable defined at \[7, 8\], type=int'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi


if test_context src/test/deca/context/valid/provided/test_declaration_float.deca 2>&1 | \
    grep -q 'definition: variable defined at \[5, 10\], type=float'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q '\[9, 4\] Assign'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/test_conv_Float.deca 2>&1 | \
    grep -q 'ConvFloat'
then
    echo "PASSED"
else
    echo "FAILED"
    exit 1
fi



# if test_context src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
#     grep -q -e 'hello-world.deca:[0-9]'
# then
#     echo "Echec inattendu pour test_context"
#     exit 1
# else
#     echo "Succes attendu de test_context"
# fi



# # Testing Variable Declaration Feature

# if test_context src/test/deca/context/invalid/provided/variable-deja-declare.deca 2>&1 | \
#     grep -q -e '' # A Remplir
# then
#     echo "Echec attendu pour test_context"
# else
#     echo "Succes inattendu de test_context"
#     exit 1
# fi


# if test_context src/test/deca/context/invalid/provided/not-initialized-variable.deca 2>&1 | \
#     grep -q -e # A Remplir
# then
#     echo "Echec attendu pour test_context"
# else
#     echo "Succes inattendu de test_context"
#     exit 1
# fi

# if test_context src/test/deca/context/invalid/provided/variable-not-used.deca 2>&1 | \
#     grep -q -e # A Remplir
# then
#     echo "Echec attendu pour test_context"
# else
#     echo "Succes inattendu de test_context"
#     exit 1
# fi


# if test_context src/test/deca/context/valid/provided/IntToFloat.deca 2>&1 | \
#     grep -q -e # A remplir
# then
#     echo "Echec inattendu pour test_context"
#     exit 1
# else
#     echo "Succes attendu de test_context"
# fi

# if test_context src/test/deca/context/valid/provided/affectationCompatible.deca 2>&1 | \
#     grep -q -e # A remplir
# then
#     echo "Echec inattendu pour test_context"
#     exit 1
# else
#     echo "Succes attendu de test_context"
# fi