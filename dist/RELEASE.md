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

1. Tag every project
  -  $ git tag **1.2.1**
  -  $ git push --tags
2. Do the release zip
  -  $ mkdir /tmp/ceylon
  -  $ docker pull ceylon/ceylon-build
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build **1.2.1**
3. Copy the zip to downloads.ceylon-lang.org:
  -  $ scp /tmp/ceylon/ceylon-**1.2.1**.zip **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

# Build the Debian file

1. Check out the [`ceylon-debian-repo`](https://github.com/ceylon/ceylon-debian-repo) repository
2. Make sure you're on `master` and run
  - $ ./new-version.sh **1.2.1**
3. Edit the `dist-pkg/debian/changelog` file by hand or use:
  - $ dch -i
4. Commit and push the new branch
5. Package it
  - $ docker pull ceylon/ceylon-package-deb
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-deb **1.2.1**
6. Copy the zip to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-**1.2.1_1.2.1**_all.deb **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
7. Build the Debian repo at ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
  - Make sure the [repo build file](https://github.com/ceylon/ceylon-debian-repo/blob/master/repo/build.sh) is up to date
  - $ docker pull ceylon/ceylon-repo-deb
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-deb **1.2.1**
8. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv --dry-run /tmp/ceylon/{db,dists,pool} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/apt/

NB: To be able to sign packages the user running the docker command for generating the repo must have the "Ceylon Debian Archive Signing Key" (59935387) imported into their local key ring.

# Build the RedHat file

1. Check out the [`ceylon-rpm-repo`](https://github.com/ceylon/ceylon-rpm-repo) repository
2. Create a new branch:
  - $ git checkout -b **1.2.1**
3. Edit the `dist-pkg/ceylon.spec` file
4. Commit and push the new branch
5. Build it
  - $ docker pull ceylon/ceylon-package-rpm
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-rpm **1.2.1**
6. Copy the rpm to downloads.ceylon-lang.org:
  - $ scp /tmp/ceylon/ceylon-**1.2.1-1.2.1-0**.noarch.rpm **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
7. Rebuild the RPM repo at ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/
  - Make sure the [repo build file](https://github.com/ceylon/ceylon-rpm-repo/blob/master/repo/build.sh) is up to date
  - $ docker pull ceylon/ceylon-repo-rpm
  - $ docker run -ti --rm -v /tmp/ceylon:/output -v ~/.gnupg:/gnupg ceylon/ceylon-repo-rpm **1.2.1**
8. Copy the packages to downloads.ceylon-lang.org:
  - $ rsync -rv --dry-run /tmp/ceylon/{\*.noarch.rpm,repodata} **user**@ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/

NB: To be able to sign packages the user running the docker command for generating the repo must have the "Ceylon RPM Archive Signing Key" (E024C8B2) imported into their local key ring.

# Publishing to the Herd

1. First create an Upload on the server
2. Publish the official distribution to it
  - $ docker pull ceylon/ceylon-publish
  - $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-publish **1.2.1** https://modules.ceylon-lang.org/uploads/**XXX**/repo/ **user** **password**
3. Publish the SDK modules by running the following in the `ceylon-sdk` project:
  - $ ant copy-herd -Dherd.repo=https://modules.ceylon-lang.org/uploads/**XXX**/repo/ -Dherd.user=**user** -Dherd.pass=**password**

# Update the web site

 - See [this README](https://github.com/ceylon/ceylon-lang.org/blob/master/RELEASE.md)

# Update the brew formula for ceylon

1. Fork it on https://github.com/mxcl/homebrew
2. Update the file `Library/Formula/ceylon.rb`
3. Make a pull-request

# Update the SDKMAN candidate

This is done via simple `curl` commands, but requires a key and token that will not be posted here for security reasons.

1. First, release the candidate with `curl -X POST -H "consumer_key: KKKKKKK" -H "consumer_token: TTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","version":"<release version>","url":"https://downloads.ceylon-lang.org/cli/ceylon-<release version>.zip"}' https://sdkman-vendor.herokuapp.com/release`. This should return something like `{"status":201,"id":"XXXXX","message":"released ceylon version: <release version>"}`
2. Next, set the new version as default with `curl -X PUT -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","default":"<release version>"}' https://sdkman-vendor.herokuapp.com/default`. This should return something like `{"status":202,"id":"XXXXXXXX","message":"default ceylon version: <release version>"}`
3. Finally, to broadcast an announcement of the new release: `curl -X POST -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate": "ceylon", "version": "<release version>", "hashtag": "ceylonlang"}' https://sdkman-vendor.herokuapp.com/announce/struct`

# Update OpenShift

 - [openshift-cartridge](https://github.com/ceylon/openshift-cartridge)
 - [ceylon.openshift](https://github.com/ceylon/ceylon.openshift)

# Update the Web IDE

 - [ceylon-web-ide-backend](https://github.com/ceylon/ceylon-web-ide-backend)

# After

1. Find every occurence of the previous version `1.2.0` and turn it into `1.2.1`, take special care with `Versions.java`
2. Find every occurence of the previous code name `Analytical Engine` and turn it into the new one
3. If required, bump all the `@Ceylon(major = X)` annotations in `ceylon.language` and the compiler tests' `.src` files
  - Note that most likely you'll need a new Herd as well (good luck)

