# Ceylon distribution

This is the Milestone 1 "Newton" release of the Ceylon command line tools (version 
0.1). This is a pre-release version of the platform for review by the community.

Ceylon is a programming language for writing large programs in a team environment. 
The language is elegant, highly readable, extremely typesafe, and makes it easy to 
get things done. And it's easy to learn for programmers who are familiar with 
mainstream languages used in business computing. Ceylon has a full-featured 
Eclipse-based development environment, allowing developers to take best advantage 
of the powerful static type system. Programs written in Ceylon execute on any JVM.

Read more about Ceylon at <http://ceylon-lang.org>.

## Distribution layout

- `bin`            - Unix/Windows commands
- `doc`            - The Ceylon spec in HTML and PDF format
- `lib`            - Required libraries for the Ceylon commands
- `repo`           - Required bootstrap Ceylon modules (language, tools)
- `runtime-repo`   - Required runtime Ceylon modules (module system)
- `samples`        - Sample Ceylon modules
- `LICENSE-ASL`    - The Ceylon ASL license
- `LICENSE-GPL-CP` - The Ceylon GPL/CP license
- `README.md`      - This file

The command line tools are located in the `bin` directory.

- `bin/ceylon`     - Run a Ceylon program
- `bin/ceylonc`    - Compile a Ceylon program
- `bin/ceylond`    - Document a Ceylon program

The API documentation for the language module `ceylon.language` may be found here:

- `repo/ceylon/language/0.1/module-doc`

## Running the sample programs

To compile and run the samples, start from the distribution directory containing
this file.

To run the "hello world" program, type:

    cd samples/helloworld
    ../../bin/ceylonc com.acme.helloworld
    ../../bin/ceylond -non-shared -source-code com.acme.helloworld
    ../../bin/ceylon com.acme.helloworld/1.0.0 John

To run a program defined in the default module, type:

    cd samples/no-module
    ../../bin/ceylonc default
    ../../bin/ceylond -non-shared -source-code default
    ../../bin/ceylon default

## Tool usage

To see a list of command line options, type one of:

    ./bin/ceylonc -help
    ./bin/ceylond -help
    ./bin/ceylon -help

## Ant tasks for Ceylon

We include support for Ceylon ant tasks which are documented in `README-ANT.md`.

## Source code

Source code is available from GitHub:

<http://github.com/ceylon>

## Issues

Bugs and suggestions may be reported in GitHub's issue tracker.

## Systems where Ceylon is known to work

Since Ceylon is running on the JVM it should work on every platform where the JVM
is supported. However we have tested the following platforms to make sure it works:

### Linux

- Ubuntu "oneiric" 11.10 (64 bit) JDK 1.7.0_147-icedtea, 1.6.0_26
- Fedora 16 JDK 1.6.0_22-icedtea

### Windows

- Windows 7 (64 bit) JDK 1.7, 1.6.0_30

### OSX

- OSX 10 Snow Leopard (10.6.8) JDK 1.7.0-b217, 1.6.0_29, 1.6.0_26
- OSX 10 Lion (10.7.2) JDK 1.6.0_29

## License

The Ceylon distribution is and contains work released

- partly under the ASL v2.0 as provided in the `LICENSE-ASL` file that accompanied 
  this code, and
- partly under the GPL v2 + Classpath Exception as provided in the `LICENSE-GPL-CP`
  file that accompanied this code.

### License terms for 3rd Party Works

This software uses a number of other works, the license terms of which are 
documented in the `NOTICE` file that accompanied this code.

### Repository

The content of this code repository, [available here on GitHub][ceylon-dist], 
is released under the ASL v2.0 as provided in the `LICENSE-ASL` file that accompanied 
this code.

[ceylon-dist]: https://github.com/ceylon/ceylon-dist

By submitting a "pull request" or otherwise contributing to this repository, you
agree to license your contribution under the license mentioned above.

## Acknowledgement

We're deeply indebted to the community volunteers who contributed a substantial part
of the current Ceylon codebase, working often in their own spare time. The following 
people have contributed to this release:

Gavin King, Stephane Epardaud, Tako Schotanus, Gary Benson, Emmanuel Bernard, Andrew Haley, 
Tom Bentley, Ales Justin, David Festal, Flavio Oliveri, Sergej Koshchejev, Max Rydahl Andersen, 
Mladen Turk, James Cobb, Ben Keating, Michael Brackx, Ross Tate, Ivo Kasiuk, Gertjan Assies,
Nicolas Leroux, Julien Viet.
