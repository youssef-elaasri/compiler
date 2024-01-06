#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

# On ne teste qu'un fichier. Avec une boucle for appropriée, on
# pourrait faire bien mieux ...
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/cond0.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/provided/cond0.ass ]; then
    echo "Fichier cond0.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/cond0.ass) || exit 1
rm -f ./src/test/deca/codegen/valid/provided/cond0.ass

# On code en dur la valeur attendue.
attendu=ok

if [ "$resultat" = "$attendu" ]; then
    echo "Tout va bien"
else
    echo "Résultat inattendu de ima:"
    echo "$resultat"
    exit 1
fi



# Tester la declaration et initialisation d'un int et float 
rm -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_declaration_init.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass ]; then
    echo "Fichier test_declaration_init.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_declaration_init.ass) || exit 1
rm -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass

# On code en dur la valeur attendue.
attendu="54.5"

if [ "$resultat" = "$attendu" ]; then
    echo "PASSED"
else
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester la conversion de int en float
rm -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_conv_float.deca || exit 1
if [ ! -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass ]; then
    echo "Fichier test_conv_float.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_conv_float.ass) || exit 1
rm -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass

# On code en dur la valeur attendue.
attendu="7.5"

if [ "$resultat" = "$attendu" ]; then
    echo "PASSED"
else
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi

