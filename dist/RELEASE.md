How to do a release of Ceylon.

# Before (the code)

1. Find every occurence of the previous version `0.5` and turn it into `0.6`
  - Beware that in ceylon-runtime you need to rename some folders whose name is the version number, and `module.xml` contents.
2. Find every occurence of the previous code name `Analytical Engine` and turn it into the new one
3. If required, bump `AbstractTransformer.BINARY_MAJOR_VERSION` and all the `@Ceylon(major = X)` annotations in `ceylon.language`
   and the compiler tests' `.src` files
  - Note that most likely you'll need a new Herd as well (good luck)
4. Check that external sample code (in particular https://github.com/ceylon/ceylon-examples)
complies and runs OK.

# Before (requirements & testing)

1. Make sure the following projects are up-to-date or at the proper version
  - ceylon-spec
  - ceylon-compiler
  - ceylon-common
  - ceylon-model
  - ceylon-js
  - ceylon.language
  - ceylon-module-resolver
  - ceylon-runtime
  - ceylon-dist
2. Make sure you're running Java 7
3. Run all the compiler tests in Eclipse
  - Concurrent tests
  - Integration tests
  - CeylonDoc tests
4. Make a test distribution
    $ cd ceylon-dist
    ceylon-dist $ ant clean publish-all
5. Run the language tests
    $ cd ceylon.language
    ceylon.language $ ant test
6. Check that you can compile the SDK
    $ cd ceylon-sdk
    ceylon-sdk $ ant clean compile test

# The release

1. Tag every project
    $ git tag 0.6
    $ git push --tags
2. Do the release zip
    $ cd ceylon-dist
    ceylon-dist $ ant release
3. Copy the zip to downloads.ceylon-lang.org:
    $ scp ceylon-0.6.zip ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

# Build the Debian file

On a Debian system:

1. Add a new changelog entry:
    ceylon-dist $ dch -i
2. Update the versions and rename some files in `debian/` to match the new version
3. Package it
    ceylon-dist $ fakeroot ./debian/rules clean binary
4. Copy the zip to downloads.ceylon-lang.org:
    $ scp ceylon-0.6.deb ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

# Build the RedHat file

1. Follow the steps in the README in the `redhat` folder
2. Copy the rpm to downloads.ceylon-lang.org:
    $ scp ceylon-0.6-noarch.rpm ceylon-lang.org:/var/www/downloads.ceylonlang/cli/

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
4. Put the new spec
    $ sudo cp -r ceylon-0.6/doc/en/spec/{html,html_single,shared} /var/www/ceylonlang/documentation/1.0/spec/
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/spec
    $ sudo mv /var/www/ceylonlang/documentation/1.0/spec/pdf/Ceylon\* /var/www/ceylonlang/documentation/1.0/spec/pdf/ceylon-language-specification.pdf
5. Put the new tooldocs
    $ sudo cp -r ceylon-0.6/doc/en/toolset /var/www/ceylonlang/documentation/1.0/reference/tool/ceylon/subcommands
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/reference/tool/ceylon/subcommands

# Update the brew formula for ceylon

1. Fork it on https://github.com/mxcl/homebrew
2. Update the file `Library/Formula/ceylon.rb`
3. Make a pull-request
