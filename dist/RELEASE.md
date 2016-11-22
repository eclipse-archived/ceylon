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
  -  $ cd ceylon
  -  ceylon$ ant clean-all dist sdk eclipse
5. Run the language tests
  -  $ cd ceylon.language
  -  ceylon.language$ ant test-quick
6. Check that external sample code (in particular https://github.com/ceylon/ceylon-examples) compiles and runs OK.

# Before (packaging)

1. Make sure the RedHat packaging is up-to-date by following the steps in the [`ceylon/ceylon-rpm-repo/README`](https://github.com/ceylon/ceylon-rpm-repo/blob/master/README.md) file
2. Do the same for the Debian packaging by reading [`ceylon/ceylon-debian-repo/README`](https://github.com/ceylon/ceylon-debian-repo/blob/master/README.md)

# The release

1. Create a release branch
  - $ git checkout -b version-**1.2.1**
  - $ git push --set-upstream origin version-**1.2.1**
2. Reversion the new branch
  -  $ ./dist/reversion **1.2.1**-SNAPSHOT **1.2.1**
  -  $ ./dist/reversion **1.2.1**.osgi-4 **1.2.1**.osgi-5
  - Update common/com/redhat/ceylon/common/Versions.java
  - Commit and push
3. Reversion master
  -  $ ./dist/reversion **1.2.1**-SNAPSHOT **1.2.2**-SNAPSHOT
  -  $ ./dist/reversion **1.2.1**.osgi-4 **1.2.2**.osgi-4
  - [Choose a new release name](https://en.wikipedia.org/wiki/List_of_spacecraft_in_the_Culture_series)
  -  $ ./dist/reversion "The Old Release Name" "The New Release Name"
  - Update common/com/redhat/ceylon/common/Versions.java
  - Commit and push
4. Tag every project
  -  $ git tag **1.2.1**
  -  $ git push origin **1.2.1**
5. Do the release zip
  -  $ mkdir /tmp/ceylon
  -  $ docker pull ceylon/ceylon-build
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build **1.2.1**
6. Copy the zip to downloads.ceylon-lang.org:
  -  $ scp /tmp/ceylon/ceylon-**1.2.1**.zip **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
7. Do the SDK release
  -  $ docker pull ceylon/ceylon-build-sdk
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build-sdk **1.2.1**

# Build the Debian file

1. Check out the [`ceylon-debian-repo`](https://github.com/ceylon/ceylon-debian-repo) repository
2. Make sure you're on `master` and that the last version was merged into it by using
  - $ git log
3. Then run this script that will make a branch for the new version and make most of the necessary changes
  - $ ./new-version.sh **1.2.1** **12010**
3. Edit the `dist-pkg/debian/changelog` file by hand or use:
  - $ dch -i
4. Make sure the [repo build file](https://github.com/ceylon/ceylon-debian-repo/blob/master/repo/build.sh) is up to date
5. Commit, push the new branch and merge it into master
6. Package it
  - $ docker pull ceylon/ceylon-package-deb
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-deb **1.2.1**
7. Copy the zip to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-**1.2.1_1.2.1**_all.deb **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
8. Build the Debian repo at ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
  - $ docker pull ceylon/ceylon-repo-deb
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-deb **1.2.1**
9. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv --dry-run /tmp/ceylon/{db,dists,pool} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/apt/

NB: To be able to sign packages the user running the docker command for generating the repo must have the "Ceylon Debian Archive Signing Key" (59935387) imported into their local key ring.

# Build the RedHat file

1. Check out the [`ceylon-rpm-repo`](https://github.com/ceylon/ceylon-rpm-repo) repository
2. Create a new branch:
  - $ git checkout -b **1.2.1**
3. Edit the `dist-pkg/ceylon.spec` file
4. Make sure the [repo build file](https://github.com/ceylon/ceylon-rpm-repo/blob/master/repo/build.sh) is up to date
5. Commit, push the new branch and merge it into master
6. Build it
  - $ docker pull ceylon/ceylon-package-rpm
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-rpm **1.2.1**
7. Copy the rpm to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-**1.2.1-1.2.1-0**.noarch.rpm **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
8. Rebuild the RPM repo at ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/
  - $ docker pull ceylon/ceylon-repo-rpm
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-rpm **1.2.1**
9. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv --dry-run /tmp/ceylon/{\*.noarch.rpm,repodata} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/

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
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-publish **1.2.1** https://modules.ceylon-lang.org/uploads/**XXX**/repo/ **user** **password**

# Update the web site

 - See [this README](https://github.com/ceylon/ceylon-lang.org/blob/master/RELEASE.md)

# Update GitHub Release Notes

 - Copy the blog entry created for the web site (see previous bullet point) to the release notes and add the release zip as well: https://github.com/ceylon/ceylon/tags

# Update the brew formula for ceylon

1. Fork it on https://github.com/Homebrew/homebrew-core
2. Update the file [`Library/Formula/ceylon.rb`](https://github.com/Homebrew/homebrew-core/blob/master/Formula/ceylon.rb)
3. Make a pull-request

# Update the SDKMAN candidate

This is done via simple `curl` commands, but requires a key and token that will not be posted here for security reasons.

1. First, release the candidate with `curl -X POST -H "consumer_key: KKKKKKK" -H "consumer_token: TTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","version":"<release version>","url":"https://downloads.ceylon-lang.org/cli/ceylon-<release version>.zip"}' https://vendors.sdkman.io/release`. This should return something like `{"status":201,"id":"XXXXX","message":"released ceylon version: <release version>"}`
2. Next, set the new version as default with `curl -X PUT -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","default":"<release version>"}' https://vendors.sdkman.io/default`. This should return something like `{"status":202,"id":"XXXXXXXX","message":"default ceylon version: <release version>"}`
3. Finally, to broadcast an announcement of the new release: `curl -X POST -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate": "ceylon", "version": "<release version>", "hashtag": "ceylonlang"}' https://vendors.sdkman.io/announce/struct`

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

