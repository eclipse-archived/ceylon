Ceylon JS compiler
==================

Compiling and running
---------------------

The ceylon-spec project must be located at `../ceylon-spec`.

First, compile and publish the typechecker by typing:

    ant publish

in the `ceylon-spec/` directory. 

Now, to compile the tests, type:

    ant srctest

The generated code may be found in the `build/test/node_modules`
directory.

To run the tests, you first need to install `node.js`, Then, 
in the root directory, type:

    ant

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
