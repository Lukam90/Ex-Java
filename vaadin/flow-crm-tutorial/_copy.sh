date=`date +"%H.%M"`
name="Vaadin"

target="$HOME/Téléchargements/Copies/CP-$name-$date"

if [ ! -d $target ]; then
    mkdir $target

    cp pom.xml $target

    cp -r frontend $target
    cp -r src $target
fi

echo "Copie $name - $date"