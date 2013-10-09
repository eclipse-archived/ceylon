Ceylon JS compiler
==================

Compiling and running
---------------------

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the compiler
return to `ceylon-js` and run

    ant clean publish

To run the tests type:

    ant test

The generated code may be found in the `build/test/node_modules`
directory.

To run the tests, you first need to install `node.js`, Then, 
in the root directory, type:

    ant nodetest

To compile the compiler, type:

    ant compile

Directory structure:
--------------------

* `src/`     - the Java implementation of the Ceylon JS compiler
* `runtime/` - the JS implementation of the Ceylon language module
* `test/`    - the tests

License
-------

The content of this repository is released under the ASL v2.0
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this 
repository, you agree to license your contribution under the 
license mentioned above.
