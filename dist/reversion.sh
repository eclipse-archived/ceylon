#!/bin/sh

if [[ "$#" -ne 2 ]]; then
    echo "Usage: reversion <old-version> <new-version>"
    echo ""
    echo "WARNING: this is just a stupid recursive replace in all files in the project!"
    exit
fi

function unregex {
   sed -e 's/[]\/()$*.^|[]/\\&/g' <<< "$1"
}

# script=$(readlink -f "$0")
# dir="$(dirname $(dirname "$script"))"

old=$(unregex "$1")
new=$(unregex "$2")

echo "Replacing $1 with $2 in $dir ..."
find . -type f -exec sed -i "s/$old/$new/g" {} +

