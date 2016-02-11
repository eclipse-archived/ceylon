How to do a release of Ceylon.

# Before (the code)

1. Find every occurence of the previous version `1.2.0` and turn it into `1.2.1`, take special care with `Versions.java`
2. Find every occurence of the previous code name `Analytical Engine` and turn it into the new one
3. If required, bump all the `@Ceylon(major = X)` annotations in `ceylon.language` and the compiler tests' `.src` files
  - Note that most likely you'll need a new Herd as well (good luck)
4. Check that external sample code (in particular https://github.com/ceylon/ceylon-examples) complies and runs OK.

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

# Before (packaging)

1. Make sure the RedHat packaging is up-to-date by following the steps in the `dist/redhat/README` file
2. Do the same for the Debian packaging by reading `dist/debian/README`

# The release

1. Tag every project
  -  $ git tag 1.2.1
  -  $ git push --tags
2. Do the release zip
  -  $ mkdir /tmp/ceylon
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-build:latest
3. Copy the zip to downloads.ceylon-lang.org:
  -  $ scp /tmp/ceylon/ceylon-1.2.1.zip ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

# Build the Debian file

1. Add a new changelog entry:
    ceylon-dist $ dch -i
2. Update the versions and rename some files in `debian/` to match the new version
3. Package it
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-deb:latest
4. Copy the zip to downloads.ceylon-lang.org:
  -  $ scp /tmp/ceylon/ceylon-1.2.1_1.2.1_all.deb ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
5. Rebuild the Debian repo at ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
  -  $ cd ../ceylon-debian-repo
  -  $ vim build.sh # Add the new release
  -  $ ./build.sh
  -  $ rsync -rv --dry-run dists ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
  -  $ ssh ceylon-lang.org
  -  $ cd /var/www/downloads.ceylonlang/apt/
  -  $ ~stephane/src/ceylon-debian-repo/makepool.sh ../cli/*.deb

# Build the RedHat file

1. Build it
  -  $ docker run -t --rm -v /tmp/ceylon:/output ceylon/ceylon-package-rpm:latest
2. Copy the rpm to downloads.ceylon-lang.org:
  -  $ scp /tmp/ceylon/ceylon-1.2.1-1.2.1-0.noarch.rpm ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
3. Rebuild the RPM repo at ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/
  -  $ ssh ceylon-lang.org
  -  $ cd /var/www/downloads.ceylonlang/rpm/
  -  $ vim ~stephane/src/ceylon-rpm-repo/build.sh # Add the new release
  -  $ ~stephane/src/ceylon-rpm-repo/build.sh

# Publishing to the Herd

1. First create an Upload on the server
2. Publish the official distribution to it
  - $ docker run -t --rm -v /tmp/ceylon:/output -e PUBLISH_VERSION=1.2.1 -e HERD_REPO="https://modules.ceylon-lang.org/uploads/**XXX**/repo/" -e HERD_USER=**user** -e HERD_PASS=**password** ceylon/ceylon-publish
3. Publish the SDK modules by running the following in the `ceylon-sdk` project:
  - $ ant copy-herd -Dherd.repo=https://modules.ceylon-lang.org/uploads/**XXX**/repo/ -Dherd.user=**user** -Dherd.pass=**password**

# Update the web site

 - See [this README](https://github.com/ceylon/ceylon-lang.org/blob/master/RELEASE.md)

# Update OpenShift

 - [openshift-cartridge](https://github.com/ceylon/openshift-cartridge)
 - [ceylon.openshift](https://github.com/ceylon/ceylon.openshift)

# Update the Web IDE

 - [ceylon-web-ide-backend](https://github.com/ceylon/ceylon-web-ide-backend)

# Update the brew formula for ceylon

1. Fork it on https://github.com/mxcl/homebrew
2. Update the file `Library/Formula/ceylon.rb`
3. Make a pull-request

# Update the SDKMAN candidate

This is done via simple `curl` commands, but requires a key and token that will not be posted here for security reasons.

1. First, release the candidate with `curl -X POST -H "consumer_key: KKKKKKK" -H "consumer_token: TTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","version":"<release version>","url":"https://downloads.ceylon-lang.org/cli/ceylon-<release version>.zip"}' https://sdkman-vendor.herokuapp.com/release`. This should return something like `{"status":201,"id":"XXXXX","message":"released ceylon version: <release version>"}`
2. Next, set the new version as default with `curl -X PUT -H "consumer_key: KKKKKKKK" -H "consumer_token: TTTTTTTT" -H "Content-Type: application/json" -H "Accept: application/json" -d '{"candidate":"ceylon","default":"<release version>"}' https://sdkman-vendor.herokuapp.com/default`. This should return something like `{"status":202,"id":"XXXXXXXX","message":"default ceylon version: <release version>"}`
