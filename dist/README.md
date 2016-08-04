# Ceylon

This is the 1.2.3 _"Total Internal Reflection"_ release of the Ceylon 
command line tools. This is a production version of the platform.

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

## First steps

If you installed Ceylon using your system's or a third-party's package manager
like apt-get, dnf, sdkman or brew there's nothing more for you to do, everything
should just work fine.

If on the other hand you downloaded and extracted the ZIP file you might want
to consider adding the `ceylon` command to your `PATH`. On Linux and Mac you
can do this either by adding the `bin` folder to your `PATH` environment variable
or by creating a symbolic link to `bin/ceylon` in an appropriate place like `~/bin`.
On Windows you can search for "advanced system settings", click the
"Environment Variables" button and add the path to the `bin` folder to the `PATH`
variable.

NB: If you don't add Ceylon to your path you will have to always type the path to
the `bin/ceylon` command. So in every example that follows you'll need to change
`ceylon` to `/path/to/unzipped/distribution/bin/ceylon`.

## Trying it out

To see if Ceylon was installed correctly type (the `$` is an indication of your
command line prompt and should *not* be typed when trying these examples):

    $ ceylon --version

You should see something like:

    ceylon version 1.2.3 1cac978 (Total Internal Reflection)

## Tool usage

To see the list of subcommand that `ceylon` supports just type

    $ ceylon

You should see a short synopsis and then a list of subcommands like this (you
might see more options):

    * `bootstrap`  - Generates a Ceylon bootstrap script in the current directory
    * `browse`     - Open module documentation in the browser
    * `classpath`  - Print a classpath suitable for passing to Java tools to run a given Ceylon module
    * `compile`    - Compile a Ceylon program for the Java backend
    * `compile-js` - Compile a Ceylon program for the JavaScript backend
    * `config`     - Manage Ceylon configuration files
    * `copy`       - Copy modules from one module repository to another
    * `doc`        - Document a Ceylon program
    * `fat-jar`    - Generate a Ceylon executable jar for a given module
    * `help`       - Display help about another tool
    * `info`       - Print information about modules in repositories
    * `jigsaw`     - Tools to interop with Java 9 (Jigsaw) modules
    * `import-jar` - Import a Java `.jar` file into a Ceylon module repository
    * `new`        - Generate a new Ceylon project
    * `plugin`     - Package or install command-line plugins
    * `run`        - Run a Ceylon program on the Java VM
    * `run-js`     - Run a Ceylon program on node.js (JavaScript)
    * `src`        - Fetch source archives from a repository and extract them
    * `test`       - Test a Ceylon program on the Java VM
    * `test-js`    - Test a Ceylon program on node.js (JavaScript)
    * `version`    - Show and update version numbers in module descriptors
    * `war`        - Generate a WAR file from a compiled `.car` file

Then to see a more detailed explanation of a particular subcommand, use the `help`
subcommand. For example, to get help on the `compile` subcommand type:

    $ ceylon help compile

## Running the sample programs

There is a folder with a couple of small sample programs for you take a look at.
To compile and run them, read the README.md contained in the `samples` sub folder
for further instructions.

## Write some code

A very easy we to quickly set up a module to compile is to use the `ceylon new`
command which creates the basic folder structure with the neccesary files.
Create an empty folder, change into it and type:

    $ ceylon new hello-world

It will then ask you a series of questions:

    Enter project folder name [helloworld]: .   
    Enter module name [com.example.helloworld]: my.first.module
    Enter module version [1.0.0]: 
    Would you like to generate Eclipse project files? (y/n) [y]: 
    Enter Eclipse project name [my.first.module]: 
    Would you like to generate an ant build.xml? (y/n) [y]: 

In the above example we've accepted almost all the defaults except for the
project folder name which we've changed to the current folder `.` and the
name of the module where we've chosen "my.first.module". After it has
finished the command will have created some source files, an Ant build file
and will have prepared the project to be opened in Eclipse if you want.

You can now type:

    $ ceylon compile my.first.module
    Note: Created module my.first.module/1.0.0
    $ ceylon run my.first.module/1.0.0
    Hello, World!

to compile and run the newly created module. (You can also type `ant` if
you let the tool create Ant build files).

We'll be making a small change to the code so you can see what files
are involved. For this run you favorite editor, here we'll just use `vi`:

    $ vi source/my/first/module/run.ceylon

Now change the second line to read:

    shared void hello(String name = "Ceylon") {

and save and exit. When you now repeat the compile and run commands
the output should read:

    Hello, Ceylon

This is the very simplisitc start of an interesting journey in learning
to program in Ceylon. For much more information continue to the next section.

## Learn more

If you want to learn more about Ceylon you can go to the [online documentation](http://www.ceylon-lang.org/documentation/current/)
of the Ceylon webite, where you can find a [tour](http://www.ceylon-lang.org/documentation/current/tour/),
a [walkthrough](http://www.ceylon-lang.org/documentation/current/walkthrough/)
and much more.

## Source code

The source code for Ceylon is Open Source and freely available from GitHub:

<http://github.com/ceylon>

## Issues

If you find any bugs or you have suggestions for features and improvements you may
reported them in GitHub's issue tracker.

<http://github.com/ceylon/ceylon/issues>

## Contributing

We're always looking for help, so if you would like to contribute in any way
look [here](http://www.ceylon-lang.org/code/contribute/) for more information.

## The Community

If you have any questions or want to join the developers in their discussions about
current and future developments of the Ceylon language or just chat with other users
you can find a list of possible channels right [here](http://www.ceylon-lang.org/community/),
good options are the [user mailing list](http://groups.google.com/group/ceylon-users)
and the [user Gitter channel](https://gitter.im/ceylon/user).

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

The content of this code repository, [available here on GitHub][ceylon], 
is released under the ASL v2.0 as provided in the `LICENSE-ASL` file 
that accompanied this code.

[ceylon]: https://github.com/ceylon/ceylon

By submitting a "pull request" or otherwise contributing to this 
repository, you agree to license your contribution under the license 
mentioned above.

## Acknowledgement

We're deeply indebted to the community volunteers who contributed a 
substantial part of the current Ceylon codebase, working often in 
their own spare time.

