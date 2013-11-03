/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;

/**
 * An example tool which demonstrates how to write a {@link Tool}.
 * @author tom
 */
@Summary("An example tool which demonstrates how to write a Tool.")
@Description("A tool must implement the `Plugin` interface, and it has annotated " +
	  "setters for each option/argument it recieves from the command line arguments." +
	  "The name of the tool is derived from the name of the class, which must " +
	  "begin with `Ceylon` and end with `Tool`")
public class CeylonExampleTool implements Tool {

    private boolean longName;
    private String shortName;
    private boolean pureOption;
    private List<String> listOption;
    private List<String> listArgument;
    private File file;
    private Thread.State threadState;
    private boolean inited;
    private boolean run;
    

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

    public boolean isLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public List<String> getListOption() {
        return listOption;
    }

    public List<String> getListArgument() {
        return listArgument;
    }

    public CeylonExampleTool() {
        
    }
    @Option(longName="long-name", shortName='F')
    @Description("An example of a plain option. " +
    		"Options are setters annotated with `@Option`. " +
    		"If no name is given then a name is derived from the name of the setter. " +
    		"If no `@Option.shortName` is given the option has no short name. " +
    		"The type of a pure option must be boolean (it will be called " +
    		"with a true argument if the option is present on the command line)")
    public void setLongName(boolean foo) {
        this.longName = foo;
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
    public void setShortName(String bar) {
        this.shortName = bar;
    }
    
    @OptionArgument(argumentName="bars")
    @Description("An example of a multivalued option argument. " +
          "Multivalued option arguments are setters annotated with `@Option` " +
          "which take a java.util.List argument. " +
          "The element type of the list must be given be a class " +
          "(not an interface, type parameter or wildcard). " +
          "If no name is given then a name is derived from the name of the setter. " +
          "If no `@Option.shortName` is given the option has no short name. " +
          "The setter may throw `IllegalArgumentException` with a suitable message " +
          "if, for example, the list has too many or too few elements.")
    public void setListOption(List<String> bars) {
        this.listOption = bars;
    }
    
    @Option()
    public void setPureOption(boolean pureOption) {
        this.pureOption = pureOption;
    }

    public boolean isPureOption() {
        return pureOption;
    }

    @Argument(argumentName="args", multiplicity="*", order=0)
    public void setListArgument(List<String> bazes) {
        this.listArgument = bazes;
    }
    
    public File getFile() {
        return file;
    }

    @OptionArgument
    public void setFile(File file) {
        this.file = file;
    }

    public Thread.State getThreadState() {
        return threadState;
    }

    @OptionArgument
    public void setThreadState(Thread.State threadState) {
        this.threadState = threadState;
    }

    /**
     * The initialize methods initializes the tool
     */
    @Override
    public void initialize() {
        this.inited = true;
    }
    
    /**
     * The run method runs the tool.
     */
    @Override
    public void run() {
        this.run = true;
    }

}
