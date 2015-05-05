# Ceylon distribution

This is the 1.1 _"Pif Gadget"_ release of the Ceylon 
command line tools (version 1.1.1). This is a production version of 
the platform.

Ceylon is a modern, modular, statically typed programming language 
for the Java and JavaScript virtual machines. The language features 
a flexible and very readable syntax, a unique and uncommonly elegant 
static type system, a powerful module architecture, and excellent 
tooling, including an awesome Eclipse-based IDE.

Ceylon enables the development of cross-platform modules which 
execute portably in both virtual machine environments. Alternatively, 
a Ceylon module may target one or the other platform, in which case 
it may interoperate with native code written for that platform.

Read more about Ceylon at <http://ceylon-lang.org>.

## Distribution layout

- `bin`            - Unix/Windows commands
- `contrib`        - Sample Ceylon command-line plugins
- `doc`            - Documentation about Ceylon including the spec in HTML and PDF format
- `lib`            - Required libraries for the Ceylon commands
- `repo`           - Required bootstrap Ceylon modules (language, tools)
- `samples`        - Sample Ceylon modules
- `templates`      - Templates for new Ceylon projects
- `LICENSE-ASL`    - The Ceylon ASL license
- `LICENSE-GPL-CP` - The Ceylon GPL/CP license
- `README.md`      - This file

## Building the distribution

To begin, make sure you have:

- the [Java 7 JDK][] and [Ant 1.8+][] installed, with both 
  functioning correctly,
- [Git set up][] correctly, and
- [GitHub SSH][] access set up correctly.

[Java 7 JDK]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[Ant 1.8+]: http://ant.apache.org/
[Git set up]: https://help.github.com/articles/set-up-git
[GitHub SSH]: https://help.github.com/articles/generating-ssh-keys

For the rest of the steps we will assume you have cloned the 
`ceylon-dist` repository and you are currently inside it reading 
this file. If not, first:

- create a new directory for the Ceylon project, and then
- inside that directory, clone `ceylon-dist` by typing:

<!-- lang: bash -->
    $ git clone git@github.com:ceylon/ceylon-dist.git

Now you have everything you need continue with the following steps.

- Go into the newly created `ceylon-dist` directory and run the 
  setup:

<!-- lang: bash -->
    $ cd ceylon-dist ; ant setup

- Now, to build the complete distribution, run:

<!-- lang: bash -->
    $ ant clean publish-all

After this you'll have a newly built distribution in the `dist` 
folder of your current directory. You can run the `ceylon` command 
without any further setup or installation by simply running

<!-- lang: bash -->
    $ dist/bin/ceylon

But it's advisable to add the `ceylon` command to your `PATH` 
environment variable (either by adding the `bin` folder to your 
`PATH` or by creating a symbolic link to it in an appropriate place 
like `~/bin/`).

If at any time you want to update the distribution to the latest 
code from GitHub just run

<!-- lang: bash -->
    $ ant update-all
    $ ant clean publish-all

NB: The `update-all` command assumes that your projects are "clean", 
that is you don't have uncommitted changes. If that's not the case 
you'll have to manually update those projects or first stash your 
changes (using `git stash`).

After the build finishes the command line tools will be located in 
the `bin` directory.

- `bin/ceylon`     - The ceylon tool which provides at least the following subcommands:
    * `new`        - Create a new Ceylon project
    * `compile`    - Compile a Ceylon program for the Java backend
    * `compile-js` - Compile a Ceylon program for the JavaScript backend
    * `doc`        - Document a Ceylon program
    * `import-jar` - Import a Java `.jar` file into a Ceylon module repository
    * `info`       - Print information about modules in repositories
    * `new`        - Generate a new Ceylon project
    * `plugin`     - Package or install command-line plugins
    * `run`        - Run a Ceylon program on the Java VM
    * `run-js`     - Run a Ceylon program on node.js (JavaScript)
    * `src`        - Fetch source archives from a repository and extract them
    * `test`       - Test a Ceylon program on the Java VM
    * `version`    - Show and update version numbers in module descriptors
    * `help`       - Displays help about another tool

The API documentation for the language module `ceylon.language` may 
be found here:

- `repo/ceylon/language/1.1.0/module-doc/api`

## Running the sample programs

To compile and run the samples, start from the distribution 
directory containing this file.

### Sample module

To run the "hello world" program, type:

    cd samples/helloworld

For Java:

    ../../bin/ceylon compile com.example.helloworld
    ../../bin/ceylon doc --non-shared --source-code com.example.helloworld
    ../../bin/ceylon run com.example.helloworld/1.0.0 John

For JavaScript:

    ../../bin/ceylon compile-js com.example.helloworld
    ../../bin/ceylon run-js com.example.helloworld/1.0.0

### Sample with no module

To run a program defined in the default module, type:

    cd samples/no-module

For Java:

    ../../bin/ceylon compile default
    ../../bin/ceylon doc --non-shared --source-code default
    ../../bin/ceylon run default

For JavaScript:

    ../../bin/ceylon compile-js default
    ../../bin/ceylon run-js default

### Sample module with Java interoperability

To run the "Java interop" program, type:

    cd samples/interop-java
    ../../bin/ceylon compile com.example.interop
    ../../bin/ceylon doc --non-shared --source-code com.example.interop
    ../../bin/ceylon run com.example.interop/1.0.0 John

Note: this is only available for the Java backend.

## Tool usage

To see a list of command line options for a particular subcommand,
use the `help` subcommand. For example, to get help on the `compile` 
tool:

    ./bin/ceylon help compile

## Ant tasks for Ceylon

We include support for Ceylon ant tasks which are documented at 
<http://ceylon-lang.org/documentation/1.0/reference/tool/ant/>.

To run the "hello world" program using ant, type:

    cd samples/helloworld
    ant

## Source code

Source code is available from GitHub:

<http://github.com/ceylon>

## Issues

Bugs and suggestions may be reported in GitHub's issue tracker.

## Systems where Ceylon is known to work

Since Ceylon is running on the JVM it should work on every platform 
that supports a Java 7 compatible JVM. However we have tested the 
following platforms to make sure it works:

### Linux

- Ubuntu "quantal" 12.10 (64 bit) JDK 1.7.0_09 (IcedTea) Node 0.10.15
- Fedora 17 (64 bit) JDK 1.7.0_09 (IcedTea)
- Fedora 16 (64 bit), JDK 1.7.0_b147 (IcedTea)

### Windows

- Windows 7 (64 bit) 1.7.0_05 (Oracle)
- Windows Server 2008 R2 SP1 JDK 1.7.0_04

### OSX

- OSX 10 Lion (10.8.5) JDK 1.7.0_40 (Oracle) Node 0.10.17

## License

The Ceylon distribution is and contains work released

- partly under the ASL v2.0 as provided in the `LICENSE-ASL` file 
  that accompanied this code, and
- partly under the GPL v2 + Classpath Exception as provided in the 
  `LICENSE-GPL-CP` file that accompanied this code.

### License terms for 3rd Party Works

This software uses a number of other works, the license terms of 
which are documented in the `NOTICE` file that accompanied this code.

### Repository

The content of this code repository, [available here on GitHub][ceylon-dist], 
is released under the ASL v2.0 as provided in the `LICENSE-ASL` file 
that accompanied this code.

[ceylon-dist]: https://github.com/ceylon/ceylon-dist

By submitting a "pull request" or otherwise contributing to this 
repository, you agree to license your contribution under the license 
mentioned above.

## Acknowledgement

We're deeply indebted to the community volunteers who contributed a 
substantial part of the current Ceylon codebase, working often in 
their own spare time. The following people have contributed to this 
release:

Gavin King, Stéphane Épardaud, Tako Schotanus, Emmanuel Bernard, 
Tom Bentley, Aleš Justin, David Festal, Max Rydahl Andersen, 
Mladen Turk, James Cobb, Tomáš Hradec, Ross Tate, Ivo Kasiuk, 
Enrique Zamudio, Roland Tepp, Diego Coronel, Daniel Rochetti, 
Loic Rouchon, Matej Lazar, Lucas Werkmeister, Akber Choudhry, 
Corbin Uselton, Julien Viet, Stephane Gallès, Paco Soberón, 
Renato Athaydes, Michael Musgrove, Flavio Oliveri, Michael Brackx,
Brent Douglas, Lukas Eder, Markus Rydh, Julien Ponge, Pete Muir, 
Henning Burdack, Nicolas Leroux, Brett Cannon, Geoffrey De Smet, 
Guillaume Lours, Gunnar Morling, Jeff Parsons, Jesse Sightler, 
Oleg Kulikov, Raimund Klein, Sergej Koščejev, Chris Marshall, 
Simon Thum, Maia Kozheva, Shelby, Aslak Knutsen, Fabien Meurisse, 
Sjur Bakka, Xavier Coulon, Ari Kast, Dan Allen, Deniz Türkoglu, 
F. Meurisse, Jean-Charles Roger, Johannes Lehmann, Alexander Altman, 
allentc, Nikolay Tsankov, Chris Horne, gabriel-mirea, Georg Ragaller, 
Griffin DeJohn, Harald Wellmann, klinger, Luke, Oliver Gondža, 
Stephen Crawley.
