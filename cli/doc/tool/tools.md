# Tools

The ceylon tools infrastructure aims to make writing tools simple, 
alleviating the following problems typically faced by tool authors:

* The need to write OS-specific wrapper scripts to run their tool

* The need to document their tool in a way that's accessible to users

* The need to write tedious option parsing code.
  This includes keeping the `--help` option up to date with 
  the options which the tool actually accepts and generating helpful error 
  messages in the event of unrecognised options. 

## What is a tool?

A tool is a JavaBean which adheres to a set conventions. The options and 
arguments accepted by the tool correspond to annotated JavaBean setters. 
Documentation is also provided using annotations (though it is also possible
to write translations of this documentation which are stored in resource 
files).

### The tool class and its annotations

The following conventions must be observed:

* Tools must be concrete classes which implement the `Tool` interface. 
* Tools must have a `public` no-argument constructors.
* The `ToolLoader` also imposes a naming convention on tool classes. 
  The `CeylonToolLoader` requires that the class corresponding to 
  `ceylon foo-bar` be named `CeylonFooBarTool`.
* To be found at runtime, tools need to be registered in a file called
  `META-INF/services/com.redhat.ceylon.common.tool.Tool`
  within the `.jar` containing the tool class.

The `@Summary` annotation provides a summary of the tool in Markdown format. 
It should be a single-sentence description of 
what the tool does. All tools should have this annotation because without it 
users won't know what the tool does since it is used in the output 
of `ceylon help`. 

The `@Description` annotation provides a more detailed descripton of the tool 
in Markdown format.
This corresponds to the `DESCRIPTION` help section. The description should 
also document the arguments that the tool accepts (but not the options, the 
documentation for those is generated automatically).

The `@Hidden` annotation can be added to tools which are low level, and which 
users are unlikely to want to run themselves (such low level tools are 
usually run by scripts, or possibly by other tools).

The `@RemainingSections` annotation may be added to tools which want to add 
additional sections in their `ceylon help` documentation. Like the other 
documentation annotations it uses Markdown format. Headings are adjusted so 
that the most prominent headings used in this annotation are given equal 
prominence as the `DESCRIPTION`, `SYNOPSIS` and `OPTIONS` sections in the 
help output.


### Options 

The options accepted by a tool correspond to annotated JavaBean setters
on the tool class. Note that:

* inherited setters are not considered.
* you do not have to provide getters. 

A setter annotated `@Option` corresponds to a pure option that takes no 
argument. Pure options must be of `boolean` type. For example

    @Option
    setFoo(boolean foo) {
        this.foo = foo;
    }
    
The `@Option` annotation accepts `longName` and `shortName` parameters. If no 
`longName` is given the option name is inferred from the name of the property
itself. `shortNames` are not inferred in this way, and must always be given 
explicitly. For example the `setFoo()` method in the example would result in a 
`--foo` option.

Notes: 

* A command line is allowed to have multiple occurances of an option, and this 
  will result in multiple invocations of the setter. 
* No facility is provided for pairs of options which negate each other
  for example `--foo` and `--no-foo`. You would need to have two setters to do 
  this, and handle the case of their both being present (e.g. using a 
  `@PostConstruct` method)

A setter annotated `@OptionArgument` corresponds to an option that takes an 
argument. It supports the same `longName` and `shortName` parameters as
`@Option` and also an `argumentName`, which is used for documentation. For 
example:

    @OptionArgument(argumentName="file")
    setBar(String f) {
        this.f = f;
    }

This would result in the option being documented as `--bar=file`

Using the `multiplicity` parameter it's possible to constrain how many times 
the option is allowed to appear on the command line. If something is allowed 
to appear more than once the setter's parameter needs to be a 
`java.util.List` and the list element type is implied from the 
type argument (which therefore cannot itself be a type parameter).

It is permitted to annotate setters with both `@Option` and `@OptionArgument`. 
This results in an option with an optional argument. An example of this is 
`ceylon compile --verbose`, which is declared something like this:

    @Option
    @OptionArgument
    setVerbose(String flags) {
        // ...
    }

This means `--verbose` can arguments though they're not required 
(`ceylon compile --verbose=ast,cmr`). This works as followS:

* A command line including a 'bare' `--verbose` results in a call to the 
  setter with a `null` argument.
* A command line including an empty `--verbose=` results in a call to the 
  setter with the given argument (in this case the empty String).
* A command line including a non-empty `--verbose=flag1,flag2` results in a 
  call to the setter with the given argument (in this case the 
  String `"flag1,flag2"`, which the tool must parse itself).

Note that when a setter is annotated with both `@Option` and `@OptionArgument` 
the `longName`, `shortName` parameters (if given) must match. 

Setters annotated with `@Option` and/or `@OptionArgument` should be 
annotated with `@Description` which contains Markdown formatted documentation 
about that option.

Setters annotated with `@Option` and/or `@OptionArgument` may be 
annotated with `@Hidden` which can be used for options that users would not 
normally be interested in (for example, options useful only for scripts or 
other tools).

### Arguments

The setters for command arguments are annotated with `@Argument`. Like 
`@OptionArgument` the `argumentName` parameter names the argument for 
documentary purposes, and the same rules apply for `multiplicity`.

Setters annotated with `@Argument` cannot be annotated with `@Description`. 
This is because the documentation of arguments should be done in the 
class-level `@Description` annotation.

### Other annotations 

It is possible for a tool to accept arbitrary additional options with a setter 
annotated `@Rest`. It is the tools responsibility to validate and document 
these additional options. Note that this only passes the unknown options and 
option arguments. Unknown arguments can be handled by a `@Argument` which 
accepts a `List<String>`.

Any number of methods may be annotated `@PostConstruct`. In normal operation 
such methods are invoked after all the setters have been invoked, 
but before the `run()` method is called. 


### Example

Here's an example tool class:

    @Summary("An example tool")
    @Description("If no arguments are given prints \"hello, world\" to the " +
                 "standard output, otherwise prints a greeting for each of " +
                 "the arguments")
    class CeylonHelloWorldTool implements Tool {
    
        private boolean useError;
        private String greeting = "hello";
        private List<String> names = Collections.singletonList("world");
    
        @Option
        @Description("Print the greeting to standard error instead of " + 
                     "standard output")
        public void setUseError(boolean useError) {
            this.useError = useError;
        }
    
        @OptionArgument
        @Description("The greeting to use (default: `hello`)")
        void setGreeting(String greeting) {
            this.greeting = greeting;
        }
        
        @Argument
        void setNames(List<String> names) {
            this.names = names;
        }
        
        void run() {
            for (String name : this.names) {
                (useError ? System.err : System.out).println(greeting + ", "+ name);
            }
        }
    }

A `META-INF/services/com.redhat.ceylon.common.tool.Tool` file would be 
required in the `.jar` file containing the `CeylonHelloWorldTool.class` file. 
The file would need to contain the fully qualified class name of the 
`CeylonHelloWorldTool` class on a line of its own.

And here are some example invocations of it:

    ceylon hello-word

would print `hello, world` on the standard output.

    ceylon --use-error hello-word

would print `hello, world` on the standard error.

    ceylon hello-word tom
    
would print `hello, tom` on the standard output.

    ceylon hello-word --greeting=hi
    
would print `hi, world` on the standard output.

    ceylon hello-word --greeting=hi tom
    
would print `hi, tom` on the standard output.


## Tool execution

The `CeylonTool` is the top level tool which corresponds to the `ceylon` 
command. It proceeds approximately as follows:

1. It bootstraps itself (since it's also a `Tool`). 
2. It looks at the command line arguments and determines if a tool name was given.
3. If so, it delegates to its `ToolLoader` to load the `ToolModel` for that tool.
4. If a `ToolModel` was successfully loaded the `CeylonTool` delegates to 
   its `ToolFactory` to configure an instance of the `Tool` according to 
   its `ToolModel` and the remaining command line arguments.
5. If a tool instance was successfully instantiated and configured the 
   `CeylonTool` invokes `run()`.

The exit code produced by the `CeylonTool` depends on what went wrong. 


