#!/bin/bash

if [[ "$#" -ne 4 ]]; then
    echo "Usage: sdkman [candidate|announce] <ceylon-version> <key> <token>"
    echo ""
    echo "The action 'candidate' registers a new version while 'announce' broadcasts it to all the users"
    exit
fi

VERSION=$2
KEY=$3
TOKEN=$4

if [[ "$1" == "candidate" ]]; then

	curl -X POST -H "consumer_key: $KEY" -H "consumer_token: $TOKEN" -H "Content-Type: application/json" -H "Accept: application/json" -d "{\"candidate\":\"ceylon\",\"version\":\"$VERSION\",\"url\":\"https://downloads.ceylon-lang.org/cli/ceylon-$VERSION.zip\"}" https://vendors.sdkman.io/release

	curl -X PUT -H "consumer_key: $KEY" -H "consumer_token: $TOKEN" -H "Content-Type: application/json" -H "Accept: application/json" -d "{\"candidate\":\"ceylon\",\"version\":\"$VERSION\"}" https://vendors.sdkman.io/default

elif [[ "$1" == "announce" ]]; then

	curl -X POST -H "consumer_key: $KEY" -H "consumer_token: $TOKEN" -H "Content-Type: application/json" -H "Accept: application/json" -d "{\"candidate\": \"ceylon\", \"version\": \"$VERSION\", \"hashtag\": \"ceylonlang\"}" https://vendors.sdkman.io/announce/struct

else

	echo "Unknown action '$1', should be 'candidate' or 'announce'"

fi

