#!/bin/bash

. release-common.sh

#cd ../ceylon
#
#git checkout master
#git branch -D version-$CEYLON_RELEASE_VERSION
#git tag -d $CEYLON_RELEASE_VERSION
#git push origin :version-$CEYLON_RELEASE_VERSION
#git push origin :$CEYLON_RELEASE_VERSION
#git reset --hard $CEYLON_BRANCHING_TAG
#
#cd ../ceylon-sdk
#
#git checkout master
#git branch -D version-$CEYLON_RELEASE_VERSION
#git tag -d $CEYLON_RELEASE_VERSION
#git push origin :version-$CEYLON_RELEASE_VERSION
#git push origin :$CEYLON_RELEASE_VERSION
#git reset --hard $CEYLON_BRANCHING_TAG
#
cd ../ceylon-debian-repo

git checkout master
git branch -D version-$CEYLON_RELEASE_VERSION
git tag -d $CEYLON_RELEASE_VERSION
git push origin :version-$CEYLON_RELEASE_VERSION
git push origin :$CEYLON_RELEASE_VERSION
git reset --hard $CEYLON_BRANCHING_TAG

cd ../ceylon
