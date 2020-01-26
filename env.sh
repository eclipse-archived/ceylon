#!/bin/sh

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

# Start from a clean path
export PATH=/usr/local/sbin:/usr/sbin:/sbin:/usr/local/bin:/usr/bin:/bin

# Add Java to path
export PATH="$JAVA_HOME/bin:$PATH"

# Setup Maven environment
export MAVEN_HOME="$APP_HOME/apache-maven"
export PATH="$MAVEN_HOME/bin:$PATH"

# Setup Ant environment
export ANT_HOME="$APP_HOME/apache-ant"
export PATH="$ANT_HOME/bin:$PATH"

# Setup Ceylon environment
export CEYLON_HOME="$APP_HOME/ceylon/dist/dist"
export PATH="$CEYLON_HOME/bin:$PATH"

# Clear Java OPTS
unset JAVA_OPTS
unset ANT_OPTS

