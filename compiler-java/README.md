Ceylon compiler and tools
=========================

License
-------

The content of this repository is released under the GPL v2 + Classpath Exception
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.

License terms for 3rd Party Works
---------------------------------

This software uses a number of other works, the license terms of which are 
documented in the NOTICE file that accompanied this code.

Directory structure:

* `bin`       - the Ceylon tools binaries
* `lib`       - compile/runtime dependencies
* `src`       - the Ceylon compiler backend sources
* `langtools` - the OpenJDK Javac compiler sources
* `test-src`  - the Ceylon compiler backend unit tests
* `samples`   - a few sample Ceylon programs

Build the compiler and tools
----------------------------

First you must make sure you have built the **ceylon.language** and **ceylon-spec** projects.
Go into **ceylon.language** first and run

    ant clean publish
    
Then go into **ceylon-spec** and run

    ant clean tree publish
    
To build and test the compiler return to **ceylon-compiler** and run

    ant clean build
    
To run the tests type

    ant test

Note that some of the tests currently fail. See more info in README.tests.

Once built, the compiler lives in this jars:

  build/lib/compiler.jar

If you want to build and run the Ceylon compiler in Eclipse
then please see README.eclipse for setup instructions.

Running the compiler
--------------------

The compiler can be run as follows:

    bin/ceylonc -src samples -out build/ceylon-cars samples/helloworld.ceylon 

The -src argument to the compiler is required in order for
the compiler to figure out each class's full name.  It is
a colon-separated path, much like javac's -sourcepath argument.

You can add -rep arguments to add module repositories. Note that this is
currently limited to local folders.

Running your Ceylon program
---------------------------

Right now the Ceylon runner doesn't support module repositories so you have to give it a
valid classpath (this will be fixed soon):

    bin/ceylon -cp build/ceylon-cars/unversioned/default_module-unversioned.car helloworld

Generating the API documentation 
--------------------------------

The ceylondoc tool can be run as follows:

    bin/ceylond -dest-dir api-docs -src ../ceylon.language/languagesrc/current  
