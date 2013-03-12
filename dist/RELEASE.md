How to do a release of Ceylon.

# Before

1. Find every occurence of the previous version `0.3.1` and turn it into `0.3.2`
  - Beware that in ceylon-runtime you need to rename some folders whose name is the version number, and `module.xml` contents.
1. Find every occurence of the previous code name `Analytical Engine` and turn it into the new one
1. If required, bump `AbstractTransformer.BINARY_MAJOR_VERSION` and all the `@Ceylon(major = X)` annotations in `ceylon.language`
   and the compiler tests' `.src` files
  - Note that most likely you'll need a new Herd as well (good luck)

# The release process

1. Make sure the following projects are up-to-date or at the proper version
  - ceylon-spec
  - ceylon-compiler
  - ceylon-js
  - ceylon.language
  - ceylon-module-resolver
  - ceylon-runtime
  - ceylon-dist
1. Make sure you're running Java 7
1. Run all the compiler tests in Eclipse
  - Concurrent tests
  - Integration tests
  - CeylonDoc tests
1. Make a test distribution
    $ cd ceylon-dist
    ceylon-dist $ ant clean siblings package
1. Run the language tests
    $ cd ceylon.language
    ceylon.language $ ant test
1. Check that you can compile the SDK
    $ cd ceylon-sdk
    ceylon-sdk $ ant clean compile test
1. Tag every project
    $ git tag 0.5
    $ git push --tags
1. Do the release zip
    $ cd ceylon-dist
    ceylon-dist $ ant release
1. Push the zip to https://github.com/ceylon/ceylon-dist/downloads

# Build the Debian file

On a Debian system:

1. Add a new changelog entry:
    ceylon-dist $ dch -i
1. Update the versions and rename some files in `debian/` to match the new version
1. Package it
    ceylon-dist $ fakeroot ./debian/rules clean binary
1. Push the .deb to https://github.com/ceylon/ceylon-dist/downloads

# Build the RedHat file

Ask Tako ;)

# Update the web site

1. Update the downloads page
1. Update the `.htaccess` page
1. Update the pages that mention the latest release
1. Write a blog post

# Update the API docs and spec

Log on `ceylonlang.org`:

WARNING: try those on for size before you run them, especially the `sudo` ones, as you should
never copy and paste `sudo` commands!!! So read them carefully and type them by hand.

1. Get the release
       $ wget https://github.com/downloads/ceylon/ceylon-dist/ceylon-0.3.1.zip
    $ unzip ceylon-0.3.1.zip
1. Remove the old API
    $ sudo rm -rf /var/www/ceylonlang/documentation/1.0/api/ceylon/language
1. Put the new API
    $ sudo cp -r ceylon-0.3.1/repo/ceylon/language/0.3.1/module-doc /var/www/ceylonlang/documentation/1.0/api/ceylon/language
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/api/ceylon/language
1. Remove the old spec
    $ sudo rm -rf /var/www/ceylonlang/documentation/1.0/spec/{html,html_single,pdf,shared}
1. Put the new spec
    $ sudo cp -r ceylon-0.3.1/doc/en/* /var/www/ceylonlang/documentation/1.0/spec/
    $ sudo chown -R webhook. /var/www/ceylonlang/documentation/1.0/spec
    $ sudo mv /var/www/ceylonlang/documentation/1.0/spec/pdf/Ceylon\* /var/www/ceylonlang/documentation/1.0/spec/pdf/ceylon-language-specification.pdf

