# Ceylon specification and type analyzer

This project contains:

* The Ceylon Language Specification, in docbook format.
* A parser for the language, implemented using ANTLR 3.
* A syntax tree for the language.
* A type analyzer, implemented in Java.

For more information about the Ceylon, go to the community 
website:

<http://ceylon-lang.org> 

## Specification

To build the PDF and HTML specifications, in the root 
directory type:

    ant

The specification is compiled into `build/en`.

To build just the PDF, type:

    ant pdf

The PDF specification is compiled then copied into the root 
directory.

Directory structure:

* `en/modules` - the docbook source of the language 
                 specification
* `support`    - the docbook build

## Build and test the type analyzer

For setting up the development environment and compiling and 
building the distribution, follow the instructions for
building the [ceylon-dist][] project.

[ceylon-dist]: https://github.com/ceylon/ceylon-dist#ceylon-distribution

If after having built the distribution you want to build and 
test the type analyzer, return to `ceylon-spec/` and run:

    ant clean publish

This will publish a clean build of the typechecker to the
local repository `~/.ceylon/repo`.

To run the tests for the type analyzer type:

    ant test

To (re)generate the parser and and syntax tree, type:

    ant clean tree

Directory structure:

* `Ceylon.g`     - the ANTLR grammar for the language
* `Ceylon.nodes` - the specification of the syntax tree
* `src/`         - the Java implementation of the type 
                   analyzer 
* `test/`        - the tests for the type analyzer, written 
                   in Ceylon, written using a special 
                   `@assertion` syntax extension
* `lib/`         - required dependencies for building and 
                   running the tests
* `treegen/`     - the ANTLR grammars that generate the 
                   syntax tree
* `gensrc/`      - the generated Java implementation of the 
                   parser and syntax tree

## License

The content of this repository is released under the ASL v2.0
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to 
this repository, you agree to license your contribution under 
the license mentioned above.
