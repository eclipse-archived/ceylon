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

Directory structure
-------------------

* `bin`       - the Ceylon tools binaries
* `lib`       - compile/runtime dependencies
* `src`       - the Ceylon compiler backend sources
* `langtools` - the OpenJDK Javac compiler sources
* `test-src`  - the Ceylon compiler backend unit tests
* `samples`   - a few sample Ceylon programs

Build the compiler and tools
----------------------------

First you must make sure you have built the 
[ceylon.language](https://github.com/ceylon/ceylon.language), 
[ceylon-spec](https://github.com/ceylon/ceylon-spec) and
[ceylon-common](https://github.com/ceylon/ceylon-common) projects.
[ceylon-module-resolver](https://github.com/ceylon/ceylon-module-resolver) projects.

Go into `ceylon.language` first and run

    ant clean publish
    
Then go into `ceylon-common` and run

    ant clean publish

Then go into `ceylon-module-resolver` and run

    ant clean publish

Then go into `ceylon-spec` and run

    ant clean publish
    
To build and test the compiler return to `ceylon-compiler` and run

    ant clean publish
    
To run the tests type

    ant test

Note that some of the tests currently fail. See more info in README.tests.

Once built, the compiler lives in this jar:

    ~/.ceylon/repo/com/redhat/ceylon/compiler/java/0.5/com.redhat.ceylon.compiler.java-0.5.jar

If you want to build and run the Ceylon compiler in Eclipse
then please see README.eclipse for setup instructions.

Running the compiler
--------------------

The compiler can be run as follows:

    build/bin/ceylonc -src samples -out build/ceylon-cars samples/helloworld.ceylon 

The -src argument to the compiler is required in order for
the compiler to figure out each class's full name.  It is
a colon-separated path, much like javac's -sourcepath argument.

You can add -rep arguments to add module repositories. These can either be local paths
or HTTP URLs.

Running your Ceylon program
---------------------------

In order to run your Ceylon program you'll need the https://github.com/ceylon/ceylon-runtime
project. Clone that repository and follow the README instructions.

Generating the API documentation 
--------------------------------

The ceylondoc tool can be run as follows:

    build/bin/ceylond -out api-docs -src ../ceylon.language/languagesrc/current ceylon.language

Building the project on Mac with JDK7
-------------------------------------

1) Download JDK7: http://jdk7.java.net/download.html

2) Unpack .dmg

3) export JAVA_HOME

    Skywalker:ceylon-compiler alesj$ export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_08.jdk/Contents/Home/

    Skywalker:ceylon-compiler alesj$ java -version
    java version "1.7.0_06-ea"
    Java(TM) SE Runtime Environment (build 1.7.0_06-ea-b12)
    Java HotSpot(TM) 64-Bit Server VM (build 23.2-b03, mixed mode)

4) Build it (ant clean publish)

    publish:
        [copy] Copying 2 files to /Users/alesj/.ceylon/repo/com/redhat/ceylon/compiler/java/0.5
        [copy] Copying 2 files to /Users/alesj/.ceylon/repo/com/redhat/ceylon/ant/0.5

BUILD SUCCESSFUL

