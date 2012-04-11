
Ceylon common
=============

License
-------

The content of this repository is released under the GPL v2 + Classpath Exception
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.

Directory structure
-------------------

* `src`       - the Ceylon common module sources
* `test-src`  - the Ceylon common module unit tests

Build the module
----------------

To build the module run

    ant clean publish
    
To run the tests type

    ant test

Once built, the module lives in this jar:

    ~/.ceylon/repo/com/redhat/ceylon/common/0.2/com.redhat.ceylon.common-0.2.jar

