#!/bin/bash

. release-common.sh

##
## Now test distrib

cd ../ceylon

log "Testing distrib"
ant test-quick  2>&1 >> $LOG_FILE || fail "Testing distrib"

log "Testing SDK"
cd ../ceylon-sdk
ant -Dceylon.home=../ceylon/dist/dist test-quick  2>&1 >> $LOG_FILE || fail "Testing SDK"
cd ../ceylon

