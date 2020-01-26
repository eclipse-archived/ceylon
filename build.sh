#!/bin/zsh

# Determine APP_HOME directory (the parent of this file)
PRG="$0"
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
cd "`dirname \"$PRG\"`/.." >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

# Work from the APP_HOME directory
cd "$APP_HOME"

# Setup Maven environment
export MAVEN_HOME="$APP_HOME/apache-maven-3.5.3"
export PATH="$MAVEN_HOME/bin:$PATH"

# Build
COMPONENTS="ceylon ceylon-ide-common ceylon-ide-eclipse \
    ceylon-sdk ceylon.formatter ceylon.tool.converter.java2ceylon \
    ceylon-ide-intellij"

(cd ceylon && ant dist sdk eclipse)

