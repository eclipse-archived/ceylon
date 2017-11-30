#!/bin/sh

if [[ "$#" -ne 1 ]]; then
    echo "Usage: checkout <branch>"
    echo ""
    echo "Checks out the given branch for the distribution, SDK and IDEs + deps"
    exit
fi

script=$(readlink -f "$0")
ceylondir="$(dirname $(dirname "$script"))"

echo "Checking out $1 ..."
pushd "$ceylondir"
git fetch origin "$1" ; git checkout $1
cd "../ceylon-sdk"
git fetch origin "$1" ; git checkout $1
cd "../ceylon.formatter"
git fetch origin "$1" ; git checkout $1
cd "../ceylon.tool.converter.java2ceylon"
git fetch origin "$1" ; git checkout $1
cd "../ceylon-ide-common"
git fetch origin "$1" ; git checkout $1
cd "../ceylon-ide-eclipse"
git fetch origin "$1" ; git checkout $1
cd "../ceylon-ide-intellij"
git fetch origin "$1" ; git checkout $1
popd

