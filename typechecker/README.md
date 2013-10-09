# Ceylon specification and type analyzer

This project contains:

* The Ceylon Language Specification, in docbook format.
* A parser for the language, implemented using ANTLR.
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

* `en`      - the docbook source of the language 
              specification
* `support` - the docbook build

## Build and test the type analyzer

For setting up the development environment and compiling and building the distribution
take a look at [ceylon-dist](https://github.com/ceylon/ceylon-spec/README.md).

If after having built the distribution you want to build and test the type analyzer
return to `ceylon-spec` and run

    ant clean publish
    
To run the tests type

    ant test

To (re)generate the parser and and syntax tree, type:

    ant clean tree

Directory structure:

* `Ceylon.g`     - the ANTLR grammar for the language
* `Ceylon.nodes` - the specification of the syntax tree
* `src/`         - the Java implementation of the type 
                   analyzer 
* `languagesrc/` - the Ceylon implementation of the 
                   language module
* `test`         - the tests for the type analyzer,
                   written in Ceylon, with a special
                   `@assertion` syntax extension
* `lib/`         - required dependencies for building 
                   and running the tests
* `treegen/`     - the ANTLR grammars that generate
                   the syntax tree
* `gensrc/`      - the generated Java implementation
                   of the parser and syntax tree

## Repository

The local repository is created under `~/.ceylon/repo`

To publish the type checker and language module (this
is required before building the compiler), type:

    ant publish

Other commands:

* `ant clean.repo`              - clean local repository
* `ant publish.language.module` - publish `ceylon.language` 
                                  module as `.template` 
                                  file in the local repo
* `ant publish.typechecker`     - publish typechecker jar 
                                  in the local repo
* `ant publish`                 - publish both

## License

The content of this repository is released under the ASL v2.0
as provided in the LICENSE file that accompanied this code.

By submitting a "pull request" or otherwise contributing to 
this repository, you agree to license your contribution under 
the license mentioned above.
