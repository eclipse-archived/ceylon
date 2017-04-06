#!/bin/bash

. release-common-sh

rm $LOG_FILE
log "Starting release" 

./release-dist-branch.sh
./release-sdk-branch.sh
./release-dist-sdk-test-branch.sh

./release-dist-master.sh
./release-sdk-master.sh

./release-git-push.sh

./release-dist-zip.sh

