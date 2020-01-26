#!/bin/sh

# ISSUES
#
# - If PATH contains anything that might lead to plugin discovery in
#   PathPlugins (which includes other versions of Ceylon) the build will
#   attempt to generate docs for those plugins! When building 1.3.4-SNAPSHOT,
#   `ant package` fails attempting to create docs for ceylon.formatter/1.3.3 if
#   Ceylon 1.3.3 is in the path.
#
# - ~/.ceylon is used for the build, and should be removed prior to each clean
#   build.
#
# - Directories named .ceylon contained within any ancestor directories may
#   affect the build... and therefore should not exist.

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

# Build
echo "----------------------------------------"
echo "Environment"
echo "----------------------------------------"
echo "JAVA_OPTS: ${JAVA_OPTS-}"
echo
echo "ANT_OPTS: ${ANT_OPTS-}"
echo
echo "PATH: ${PATH}"
echo
java -version
echo
ant -version
echo
mvn -version
echo
uname -a
echo
echo "----------------------------------------"
echo "Build"
echo "----------------------------------------"
[ -d ~/.ceylon ] && echo "** WARNING ** ~/.ceylon already exists, build may reuse old artifacts\n"

(cd ceylon && ant dist)
(cd ceylon-sdk && ant publish ide-quick)

(cd ceylon && ant test-quick)
#(cd ceylon-sdk && ant test-quick)

(cd ceylon.formatter && ant publish ide-quick)
(cd ceylon-ide-common && ant test publish ide-quick)
(cd ceylon.tool.converter.java2ceylon && ant test publish ide-quick)
(cd ceylon-ide-eclipse && mvn clean install -DskipTests)
(cd ceylon && ant clean package)

mkdir -p build/artifacts/ceylon
mkdir -p build/artifacts/ceylon-sdk
mkdir -p build/artifacts/ceylon-eclipse-plugin

cp -a ceylon/dist/ceylon*zip build/artifacts/ceylon
cp -a ceylon-sdk/modules build/artifacts/ceylon-sdk
cp -a ceylon-ide-eclipse/site/target/repository build/artifacts/ceylon-eclipse-plugin

