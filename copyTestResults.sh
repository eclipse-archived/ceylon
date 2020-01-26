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

# Copy test results
mkdir -p build/artifacts/test-reports
rsync -av --exclude '/build' --include "**/" --include "test-reports/***" --exclude "*" . build/artifacts/test-reports/

