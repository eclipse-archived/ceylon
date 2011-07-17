# This file is intended to be sourced, not run directly

# Set CEYLON_HOME
CEYLON_HOME=$(dirname $0)/..

unset USER_CP
unset ARGS

while (( $# > 0 ))
do
    if [[ $1 == -cp ]] || [[ $1 == -classpath ]]
    then
        USER_CP=$2
        shift
    else
        ARGS="$ARGS $1"
    fi

    shift
done
