package com.redhat.ceylon.tools.example;

import java.util.List;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.tools.Plugin;
import com.redhat.ceylon.tools.annotation.OptionArgument;
import com.redhat.ceylon.tools.annotation.Argument;
import com.redhat.ceylon.tools.annotation.Description;
import com.redhat.ceylon.tools.annotation.ExitCode;
import com.redhat.ceylon.tools.annotation.ExitCodes;
import com.redhat.ceylon.tools.annotation.Option;
import com.redhat.ceylon.tools.annotation.Summary;

/**
 * An example tool which demonstrates how to write a {@link Plugin}.
 * @author tom
 */
@Summary("An example tool which demonstrates how to write a Tool.")
@Description("A tool must implement the `Plugin` interface, and it has annotated " +
	  "setters for each option/argument it recieves from the command line arguments." +
	  "The name of the tool is derived from the name of the class, which must end with `Tool`")
public class ExampleTool implements Plugin {

    private boolean foo;
    private String bar;
    private List<String> bazes;
    private boolean inited;
    private boolean run;
    private List<String> bars;

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public boolean isFoo() {
        return foo;
    }

    public String getBar() {
        return bar;
    }

    public List<String> getBars() {
        return bars;
    }

    public List<String> getBazes() {
        return bazes;
    }

    public ExampleTool() {
        
    }
    @Option(longName="foo", shortName='F')
    @Description("An example of a plain option. " +
    		"Options are setters annotated with `@Option`. " +
    		"If no name is given then a name is derived from the name of the setter. " +
    		"If no `@Option.shortName` is given the option has no short name. " +
    		"The type of a pure option must be boolean (it will be called " +
    		"with a true argument if the option is present on the command line)")
    public void setFoo(boolean foo) {
        this.foo = foo;
    }
    
    @OptionArgument(shortName='b')
    @Description("An example of a single-valued option argument. " +
		  "Single-valued option arguments are setters annotated with `@OptionArgument` " +
		  "which take a 'simple' argument." +
		  "The type of an option argument is implied by the type of the setter parameter." +
          "If no name is given then a name is derived from the name of the setter. " +
          "If no `@Option.shortName` is given the option has no short name. " +
          "An error will be generated when parsing the command line if the " +
          "option argument appears more than once. ")
    public void setBar(String bar) {
        this.bar = bar;
    }
    
    /**
     * An option with multiple values is a setter annotated with @Option
     * and @ArgumentList.
     * The same naming rules apply as for pure options.
     * The type of the list element must be given explicitly
     */
    @OptionArgument()
    @Description("An example of a multivalued option argument. " +
          "Multivalued option arguments are setters annotated with `@Option` " +
          "which take a java.util.List argument. " +
          "The element type of the list must be given be a class " +
          "(not an interface, type parameter or wildcard). " +
          "If no name is given then a name is derived from the name of the setter. " +
          "If no `@Option.shortName` is given the option has no short name. " +
          "The setter may throw `IllegalArgumentException` with a suitable message " +
          "if, for example, the list has too many or too few elements.")
    public void setBars(List<String> bars) {
        this.bars = bars;
    }
    
    @Argument(argumentName="args", multiplicity="*", order=0)
    @Description("An example of an argument")
    public void setBazes(List<String> bazes) {
        this.bazes = bazes;
    }
    
    /**
     * Tools can have zero or more public no-arg @PostConstruct-annotated
     * methods which will be called before {@link #run()}.
     */
    @PostConstruct
    public void init() {
        this.inited = true;
    }
    
    /**
     * The run method runs the tool.
     */
    @Override
    @ExitCodes({
            @ExitCode(value=3, doc="")
    })
    public int run() {
        this.run = true;
        return 0;
    }

}
