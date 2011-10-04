Ceylon Language module
======================

Directory structure:

* `languagesrc/` - the Ceylon implementation of the 
                   language module

Repository
----------
The local repository is created under `~/.ceylon/repo`

To publish the type checker and language module (this
is required before building the compiler), type:

    ant publish

Other commands:

* `ant clean.repo`              - clean local repository
* `ant publish`                 - publish `ceylon.language` 
                                  module as `.template` 
