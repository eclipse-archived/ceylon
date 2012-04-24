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

The default local module repository is created under: 

    ~/.ceylon/repo

To publish the Ceylon Module Resolver (this
is required before building the compiler and typechecker), type:

    ant publish

Other commands:

* `ant clean`        - clean the build (might be needed before `publish`)
* `ant test`         - run the tests         
* `ant clean.repo`   - clean local repository
* `ant publish`      - publish module `ceylon.language` 
                       to the local repository

Alternatively you can build this project using Maven:

    mvn install

But the maven build is known to not work on every system, prefer `ant clean publish`
to build the project.