cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

echo -e "\033[33mTesting Sans Object Sript \033[0m"

rm -f src/test/deca/codegen/valid/provided/Sans-Objet.ass  2>/dev/null
decac src/test/deca/codegen/valid/provided/Sans-Objet.deca || exit 1
if [ ! -f src/test/deca/codegen/valid/provided/Sans-Objet.ass  ]; then
    echo "Fichier Sans-Objet.ass non généré."
    exit 1
fi

resultat=$(ima src/test/deca/codegen/valid/provided/Sans-Objet.ass) || exit 1
rm -f  ./src/test/script/launchers/Sans-Objet.ass

# On code en dur la valeur attendue.
attendu='1.56000e+01 is greater than 1521'

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



echo -e "\033[32mPASSED\033[0m"