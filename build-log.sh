#!/bin/sh

set -eu

# Determine APP_HOME directory
PRG="${BASH_SOURCE-$0}"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" > /dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" > /dev/null

# Setup environment
. "$APP_HOME/env.sh"

# Work from the APP_HOME directory
cd "$APP_HOME" > /dev/null

# Run build.sh
mkdir -p build
time ./build.sh 2>&1 | tee "build/log-`date "+%Y-%m-%dT%H%M%S"`.txt"

