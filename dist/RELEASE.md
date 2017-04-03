How to do a release of Ceylon.

# Very first step

1. **Create an issue using the contents of the [Release-Progress-Template](https://github.com/ceylon/ceylon/wiki/Release-Progress-Template) so you can publicly share the progress while going through the rest of this document**

# Before (requirements & testing)

1. Make sure the `ceylon` project is up-to-date or at the proper version
2. Make sure you're running Java 7
3. Run all the compiler tests in Eclipse
  - Concurrent tests
  - Integration tests
  - CeylonDoc tests
4. Make a test distribution
  - $ cd ceylon
  - ceylon$ ant clean-all dist sdk eclipse
5. Run the language tests
  - $ cd ceylon.language
  - ceylon.language$ ant test-quick
6. Check that external sample code (in particular https://github.com/ceylon/ceylon-examples) compiles and runs OK.

# The release

0. Set the version in the shells you're going to use to execute all the necessary commands for the release. Take good care with each of the commands you copy and paste, especially to adjust the parts marked in bold.
  - $ export RELVER=**THE_VERSION_TO_RELEASE**
1. Create a release branch
  - $ git checkout -b version-${RELVER}
  - $ git push --set-upstream origin version-${RELVER}
2. Reversion the new branch
  - $ ./dist/reversion.sh ${RELVER}-SNAPSHOT ${RELVER}
  - $ ./dist/reversion.sh ${RELVER}.osgi-4 ${RELVER}.osgi-5
  - Update `common/com/redhat/ceylon/common/Versions.java` (the `CEYLON_VERSION_QUALIFIER`)
  - Change `versionQualifier` in `language/src/ceylon/language/language.ceylon` from `"SNAPSHOT"` to `""`
  - Make sure `compiler-js/.../JsModuleSourceMapper.java#loadModuleFromMap()` has been updated!
  - Make sure `ceylon-module-resolver/.../dist-overrides.xml` has been updated!
  - Commit and push
3. Reversion master
  - $ git checkout master
  - $ ./dist/reversion.sh ${RELVER}-SNAPSHOT **NEW_VERSION**-SNAPSHOT
  - $ ./dist/reversion.sh ${RELVER}.osgi-4 **NEW_VERSION**.osgi-4
  - [Choose a new release name](https://en.wikipedia.org/wiki/List_of_spacecraft_in_the_Culture_series)
  - $ ./dist/reversion.sh "**The Old Release Name**" "**The New Release Name**"
  - Update `common/com/redhat/ceylon/common/Versions.java` (the `VERSION_*` and `V*_VERSION` constants and the `VersionDetails` arrays)
  - Commit and push
4. Tag every project
  - $ git tag ${RELVER}
  - $ git push origin ${RELVER}
5. Do the release zip
  - $ mkdir /tmp/ceylon
  - $ docker pull ceylon/ceylon-build
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build ${RELVER}
6. Copy the zip to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-${RELVER}.zip **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
7. Do the same for the SDK
  - Go into the SDK folder
  - $ git checkout -b version-${RELVER}
  - $ git push --set-upstream origin version-${RELVER}
  - $ ../ceylon/dist/reversion.sh ${RELVER}-SNAPSHOT ${RELVER}
  - Commit and push
  - $ git tag ${RELVER}
  - $ git push origin ${RELVER}
  - $ git checkout master
  - $ ../ceylon/dist/reversion.sh ${RELVER}-SNAPSHOT **NEW_VERSION**-SNAPSHOT
  - Commit and push
7. Do the SDK release
  - $ docker pull ceylon/ceylon-build-sdk
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build-sdk ${RELVER}

# Build the Debian file

1. Check out the [`ceylon-debian-repo`](https://github.com/ceylon/ceylon-debian-repo) repository
2. Update the [repo build file](https://github.com/ceylon/ceylon-debian-repo/blob/master/repo/build.sh)
3. Commit and push to master
4. Then run this script that will make a branch for the new version and make most of the necessary changes
  - $ ./new-version.sh ${RELVER}
5. Edit the `dist-pkg/debian/changelog` file by hand or use:
  - $ dch -i
6. Commit and push the new branch
7. Package it
  - $ docker pull ceylon/ceylon-package-deb
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-deb ${RELVER}
8. Copy the zip to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-${RELVER}_${RELVER}-0_all.deb **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
9. Build the Debian repo at ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
  - $ docker pull ceylon/ceylon-repo-deb
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-deb ${RELVER}
10. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv **--dry-run** /tmp/ceylon/{db,dists,pool} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/apt/

NB: To be able to sign packages the user running the docker command for generating the repo must have the "Ceylon Debian Archive Signing Key" (59935387) imported into their local key ring.

# Build the RedHat file

1. Check out the [`ceylon-rpm-repo`](https://github.com/ceylon/ceylon-rpm-repo) repository
2. Create a new branch:
  - $ git checkout -b ${RELVER}
3. Edit the `dist-pkg/ceylon.spec` file
4. Make sure the [repo build file](https://github.com/ceylon/ceylon-rpm-repo/blob/master/repo/build.sh) is up to date
5. Commit, push the new branch and merge it into master
6. Build it
  - $ docker pull ceylon/ceylon-package-rpm
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-rpm ${RELVER}
7. Copy the rpm to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-${RELVER}-${RELVER}-0.noarch.rpm **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
8. Rebuild the RPM repo at ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/
  - $ docker pull ceylon/ceylon-repo-rpm
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-rpm ${RELVER}
9. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv **--dry-run** /tmp/ceylon/{\*.noarch.rpm,repodata} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/

NB: To be able to sign packages the user running the docker command for generating the repo must have the "Ceylon RPM Archive Signing Key" (E024C8B2) imported into their local key ring.

# Update Docker

*This depends on the new release being available in both APT and YUM repositories!*

## ceylon-docker/ceylon

 - Check out [ceylon-docker/ceylon](https://github.com/ceylon-docker/ceylon)
 - Edit the `build.sh` and add the new version to the front of the `VERSIONS` list and change the `LATEST` value
 - Run `./build.sh --build`
 - If everything went ok run `./build.sh --push`
 - Edit the [Full Description](https://hub.docker.com/r/ceylon/ceylon/), adding a new image/tag line and moving the `latest` tag
 - Update the `README.md` to be the same as the full description
 - Commit all changes

## ceylon-docker/s2i-ceylon

 - Check out [ceylon-docker/s2i-ceylon](https://github.com/ceylon-docker/s2i-ceylon)
 - Edit the `build.sh` and add the new version to the front of the `VERSIONS` list and change the `LATEST` value
 - Run `./build.sh --build`
 - If everything went ok run `./build.sh --push`
 - Edit the [Full Description](https://hub.docker.com/r/ceylon/s2i-ceylon/), adding a new image/tag line and moving the `latest` tag
 - Update the `README.md` to be the same as the full description
 - Commit all changes

## ceylon-docker/source-runner

 - Check out [ceylon-docker/source-runner](https://github.com/ceylon-docker/source-runner)
 - Edit the `build.sh` and add the new version to the front of the `VERSIONS` list and change the `LATEST` value
 - Run `./build.sh --build`
 - If everything went ok run `./build.sh --push`
 - Edit the [Full Description](https://hub.docker.com/r/ceylon/source-runner/), adding a new image/tag line and moving the `latest` tag
 - Update the `README.md` to be the same as the full description
 - Commit all changes

# Publishing to the Herd

*This depends on the ceylon/ceylon Docker image!*

1. First create an Upload on the server
2. Publish the official distribution and SDK to it
  - $ docker pull ceylon/ceylon-publish
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-publish ${RELVER} https://modules.ceylon-lang.org/uploads/**XXX**/repo/ **user** **password**

# Update the web site

 - See [this README](https://github.com/ceylon/ceylon-lang.org/blob/master/RELEASE.md)

# Update GitHub Release Notes

 - Copy the blog entry created for the web site (see previous bullet point) to the release notes and add the release zip as well: https://github.com/ceylon/ceylon/tags

# Update the brew formula for ceylon

1. Fork it on https://github.com/Homebrew/homebrew-core
2. Update the file [`Formula/ceylon.rb`](https://github.com/Homebrew/homebrew-core/blob/master/Formula/ceylon.rb)
3. Make a pull-request

# Update the SDKMAN candidate

This is done via simple `curl` commands, but requires a key and token that will not be posted here for security reasons.

1. First, release the candidate with `curl -X POST -H "consumer_key: KKKKKKK" -H "consumer_token: TTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","version":"${RELVER}>","url":"https://downloads.ceylon-lang.org/cli/ceylon-${RELVER}.zip"}' https://vendors.sdkman.io/release`. This should return something like `{"status":201,"id":"XXXXX","message":"released ceylon version: <release version>"}`
2. Next, set the new version as default with `curl -X PUT -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","default":"${RELVER}"}' https://vendors.sdkman.io/default`. This should return something like `{"status":202,"id":"XXXXXXXX","message":"default ceylon version: <release version>"}`
3. Finally, to broadcast an announcement of the new release: `curl -X POST -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate": "ceylon", "version": "<release version>", "hashtag": "ceylonlang"}' https://vendors.sdkman.io/announce/struct`

You can now also use the `dist/sdkman.sh` script ot do the same:

1. `sdkman candidate ${RELVER} KKKKK TTTTTT` will do the same as first two steps above: register a new version and set it as the default
1. `sdkman announce ${RELVER} KKKKK TTTTTT` will do the same as the last step: announce the new version

# ArchLinux

Update the [ArchLinux package](https://aur.archlinux.org/packages/ceylon/). This means asking
Alex Szczuczko (aszczucz _AHT_ redhat _DOWT_ com).

# Update OpenShift

 - Update [openshift-cartridge](https://github.com/ceylon/openshift-cartridge) , which includes copying the entire distribution to `./usr`
 - Update [ceylon.openshift](https://github.com/ceylon/ceylon.openshift) and upload it to the Herd

# Update Ceylon Swarm

 - Update [ceylon.swarm](https://github.com/ceylon/ceylon.swarm) and upload it to the Herd

# Update the Web IDE

 - [ceylon-web-ide-backend](https://github.com/ceylon/ceylon-web-ide-backend)
 - DOn't forget to update the Ceylon Bootstrap files! (`ceylon bootstrap --force`)

# Update Maven support

 - [ceylon-maven-repo](https://github.com/ceylon/ceylon-maven-repo)
 - [ceylon-maven-plugin](https://github.com/ceylon/ceylon-maven-plugin)
 - Contact Julien Viet (julien _AHT_ julienviet _DOWT_ com) when done
 
