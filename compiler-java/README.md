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

Build the compiler and tools
----------------------------

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the compiler
return to `ceylon-compiler` and run

    ant clean publish
    
To run the tests type

    ant test

Note that some of the tests currently fail. See more info in README.tests.

Once built, the compiler lives in this jar:

    ~/.ceylon/repo/com/redhat/ceylon/compiler/java/X.Y.Z/com.redhat.ceylon.compiler.java-X.Y.Z.jar

If you want to build and run the Ceylon compiler in Eclipse
then please see README.eclipse for setup instructions.

Running the compiler
--------------------

For help on the usage of the ceylon compiler you can either type

    ceylon help compile
    
or you can go to the online documentation for [ceylon compile](/documentation/current/reference/tool/ceylon/subcommands/ceylon-compile.html)

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
        [copy] Copying 2 files to /Users/alesj/.ceylon/repo/com/redhat/ceylon/compiler/java/0.6
        [copy] Copying 2 files to /Users/alesj/.ceylon/repo/com/redhat/ceylon/ant/0.6

BUILD SUCCESSFUL

