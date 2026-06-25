#!/bin/bash
rm -r tmp/ generated/
mkdir -p generated/ tmp/
cp -R data/* tmp/

#Create asset and data directory structure.
find tmp -type f > tmp/cache
find data/data -type d | sed -e "s/^data/generated/" | xargs mkdir
find data/assets -type d | sed -e "s/^data/generated/" | xargs mkdir

#Copy handcrafted files into generated/
for i in $( grep -E "((\.json)|(\.png))$" < tmp/cache ) ; do
  echo "$i" | tee >( echo "$(</dev/stdin)") | sed '$s/^tmp/generated/' | sed 's/\n/ /' | xargs cp
done

errored=0

#GTMPG 
./gtmpg.pl
if [[ $errored != 0 ]]; then
	echo "autocopy.sh: Error: Assembly failed." >&2
else
	cp -R ./generated/java/registry/* ../../src/main/java/net/kjentytek303/additional_transfurs/init/
	cp -R ./generated/java/transfurs/* ../../src/main/java/net/kjentytek303/additional_transfurs/entity/generated/
	cp -R ./generated/java/renderers/* ../../src/main/java/net/kjentytek303/additional_transfurs/client/renderer/generated/
	cp -R ./generated/data/* ../../src/main/resources/data/
	cp -R ./generated/assets/* ../../src/main/resources/assets/
fi

exit $errored
