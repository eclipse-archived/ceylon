title: Ceylon specification and type analyzer
author: Gavin King
        Emmanuel Bernard

Specification
-------------

To build the PDF and HTML specifications, in the root 
directory type:

    ant

The specification is compiled into `build/en`.

To build just the PDF, type:

    ant pdf.doc

The PDF specification is compiled then copied into the 
root directory.

Directory structure:

* `en`      - the docbook source of the language 
              specification
* `support` - the docbook build

Type analyzer
-------------

To run the type analyzer tests, in the root directory 
type:

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

Repository
----------
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
