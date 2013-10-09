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

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the language module
return to `ceylon.language` and run

    ant clean publish
    
To run the tests type

    ant test

Other commands:

* `ant test`         - run the tests         
* `ant clean.repo`   - clean local repository
* `ant publish`      - publish module `ceylon.language` 
                       to the local repository
