# This file is intended to be sourced, not run directly

# Set CEYLON_HOME
CEYLON_HOME=$(dirname $(readlink -f $0))/..

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
