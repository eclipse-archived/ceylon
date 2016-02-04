How to do a release of Ceylon.

# Before (the code)

1. Find every occurence of the previous version `0.5` and turn it into `0.6`, take special care with `Versions.java`
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
    $ cd ceylon
    ceylon$ ant clean-all dist sdk eclipse
5. Run the language tests
    $ cd ceylon.language
    ceylon.language $ ant test

# Before (packaging)

1. Make sure the RedHat packaging is up-to-date by following the steps in the `dist/redhat/README` file
2. Do the same for the Debian packaging by reading `dist/debian/README`

# The release

1. Tag every project
    $ git tag 0.6
    $ git push --tags
2. Do the release zip
    $ mkdir /tmp/ceylon
    $ docker run -t -v /tmp/ceylon:/output ceylon/ceylon-build:latest
3. Copy the zip to downloads.ceylon-lang.org:
    $ scp /tmp/ceylon/ceylon-0.6.zip ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

# Build the Debian file

1. Add a new changelog entry:
    ceylon-dist $ dch -i
2. Update the versions and rename some files in `debian/` to match the new version
3. Package it
    $ docker run -t -v /tmp/ceylon:/output ceylon/ceylon-package-deb:latest
4. Copy the zip to downloads.ceylon-lang.org:
    $ scp /tmp/ceylon/ceylon-0.6.deb ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
5. Rebuild the Debian repo at ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
    $ cd ../ceylon-debian-repo
    $ vim build.sh # Add the new release
    $ ./build.sh
    $ rsync -rv --dry-run dists ceylon-lang.org:/var/www/downloads.ceylonlang/apt/
    $ ssh ceylon-lang.org
    $ cd /var/www/downloads.ceylonlang/apt/
    $ ~stephane/src/ceylon-debian-repo/makepool.sh ../cli/*.deb

# Build the RedHat file

1. Build it
    $ docker run -t -v /tmp/ceylon:/output ceylon/ceylon-package-rpm:latest
2. Copy the rpm to downloads.ceylon-lang.org:
    $ scp /tmp/ceylon/ceylon-0.6-noarch.rpm ceylon-lang.org:/var/www/downloads.ceylonlang/cli/
3. Rebuild the RPM repo at ceylon-lang.org:/var/www/downloads.ceylonlang/rpm/
    $ ssh ceylon-lang.org
    $ cd /var/www/downloads.ceylonlang/rpm/
    $ vim ~stephane/src/ceylon-rpm-repo/build.sh # Add the new release
    $ ~stephane/src/ceylon-rpm-repo/build.sh

# Update the web site

1. Update the downloads page
2. Update the `.htaccess` page
3. Update the pages that mention the latest release
4. Write a blog post

# Update the API docs and spec

Log on `ceylonlang.org`:

WARNING: try those on for size before you run them, especially the `sudo` ones, as you should
never copy and paste `sudo` commands!!! So read them carefully and type them by hand.

1. Get the release
    $ unzip /var/www/downloads.ceylonlang/cli/ceylon-0.6.zip
2. Remove the old API
    $ sudo rm -rf /var/www/ceylonlang/documentation/1.0/api/ceylon/language
3. Remove the old spec
    $ sudo rm -rf /var/www/ceylonlang/documentation/1.0/spec/{html,html_single,pdf,shared}
4. Make sure you tell the website hooks to not remove your precious files:
    $ sudo vim /var/www/ceylonlang/hooks/rsync-excludes
5. Put the new spec
    $ sudo cp -r ceylon-0.6/doc/en/spec/{html,html_single,shared,pdf} /var/www/ceylonlang/documentation/1.0/spec/
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/spec
    $ sudo mv /var/www/ceylonlang/documentation/1.0/spec/pdf/Ceylon* /var/www/ceylonlang/documentation/1.0/spec/pdf/ceylon-language-specification.pdf
6. Put the new tooldocs
    $ sudo cp -r ceylon-0.6/doc/en/toolset /var/www/ceylonlang/documentation/1.0/reference/tool/ceylon/subcommands
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/reference/tool/ceylon/subcommands

# Update the brew formula for ceylon

1. Fork it on https://github.com/mxcl/homebrew
2. Update the file `Library/Formula/ceylon.rb`
3. Make a pull-request
