# This file is intended to be sourced, not run directly

# resolve links - $0 may be a softlink
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Set CEYLON_HOME
CEYLON_HOME=$(dirname "$PRG)")/..

unset USER_CP
unset ARGS
unset BOOTSTRAP

while (( $# > 0 ))
do
    if [[ $1 == -cp ]] || [[ $1 == -classpath ]]
    then
        USER_CP=$2
        shift
    else
        ARGS="$ARGS $1"
    fi

    if [[ $1 == -Xbootstrapceylon ]]
    then
        BOOTSTRAP=true
    fi

    shift
done
