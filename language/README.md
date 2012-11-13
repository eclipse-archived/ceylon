Ceylon language module
======================

License
-------

The content of this repository is released under the ASL v2.0
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.

Directory structure:
--------------------

* `src/`          - the Ceylon implementation of the 
                   language module
* `runtime/`      - the Java implementation
* `test/`         - the tests

Build the compiler and tools
----------------------------

First you must make sure you have built the 
[ceylon-common](https://github.com/ceylon/ceylon-common) project.

Go into `ceylon-common` and run

    ant clean publish

Repository
----------

The default local module repository is created under: 

    ~/.ceylon/repo

To publish the type checker and language module (this
is required before building the compiler), type:

    ant publish

Other commands:

* `ant test`         - run the tests         
* `ant clean.repo`   - clean local repository
* `ant publish`      - publish module `ceylon.language` 
                       to the local repository
