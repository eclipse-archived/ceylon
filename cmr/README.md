Ceylon Module Resolver
======================

License
-------

The content of this repository is released under the ASL v2.0
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.

Directory structure:
--------------------

* `api/`          - the main API of the CMR
* `spi/`          - the main SPI of the CMR
* `impl/`         - the CMR implementation
* `webdav/`       - CMR module which handles WebDAV repositories
* `maven/`        - CMR module which handles Maven repositories
* `lib/`          - librairies used by the ant build

Building
--------

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the module resolver
return to `ceylon-module-resolver` and run

    ant clean publish
    
To run the tests type

    ant test
    
Alternatively you can build this project using Maven:

    mvn install

But the maven build is known to not work on every system, prefer `ant clean publish`
to build the project.
