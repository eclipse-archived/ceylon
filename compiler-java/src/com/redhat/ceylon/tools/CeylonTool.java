package com.redhat.ceylon.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Java7Checker;
import com.redhat.ceylon.common.tool.ModelException;
import com.redhat.ceylon.common.tool.NoSuchToolException;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;


/**
 * Main entry point for the {@code ceylon} command; the top level tool; the 
 * plugin with no name.
 */
@Summary("The top level Ceylon tool is used to execute other Ceylon tools")
@Description(
        "If `--version` is present, print version information and exit. "+
        "Otherwise `<tool-arguments>` should begin with the name of a ceylon tool. " +
		"The named tool is loaded and configured with the remaining command " +
        "line arguments and control passes to that tool.")
public class CeylonTool implements Tool {

    public static final String VERSION = "0.4 (Supercalifragilisticexpialidocious)";
    private static final String ARG_VERSION = "--version";
    public static final int SC_OK = 0;
    public static final int SC_TOOL_EXCEPTION = 1;
    public static final int SC_ARGS = 2;
    public static final int SC_NO_SUCH_TOOL = 3;
    public static final int SC_TOOL_CREATION = 4;
    
    private String toolName;
    private List<String> toolArgs;
    private boolean stacktraces = false;
    private ToolLoader pluginLoader;
    private ToolFactory pluginFactory;
    private boolean version;
    
    public CeylonTool() {
    }
    
    @Option
    @Description("Print version information and exit, " +
    		"*ignoring all other options and arguments*.")
    public void setVersion(boolean version) {
        this.version = version;
    }
    
    @Option
    @Description("If an error propogates to the top level tool, print its stacktrace.")
    public void setStacktraces(boolean stacktraces) {
        this.stacktraces = stacktraces;
    }
    
    public boolean getStacktraces() {
        return stacktraces;
    }
    
    @Argument(argumentName="tool-arguments", multiplicity="*")
    public void setArgs(List<String> args) {
        this.toolName = args.get(0);
        this.toolArgs = args.subList(1, args.size());
    }

    /** The name of the tool to run */
    public String getToolName() {
        return toolName;
    }
    
    /** The arguments passed to the tool to run */
    public List<String> getToolArguments() {
        return toolArgs;
    }
    
    private static List<String> rearrangeArgs(String[] args) {
        // cey
        // ceylon --help
        // ceylon help
        // ceylon foo --help
        // ceylon --help foo
        // ceylon --stacktraces
        // ceylon foo --bar 
        ArrayList<String> result = new ArrayList<String>(args.length + 1);
        if (args.length == 0) {
            result.add("help");
        } else {
            List<String> argsList = Arrays.asList(args);
            for (int ii = 0; ii < args.length; ii++) {
                String arg = args[ii];
                if (arg.equals("--help") || arg.equals("-h") || arg.equals("help")) {
                    result.add("help");
                    result.add("--");
                } else if (arg.equals("--version")) {
                    //result.clear();
                    result.add("--version");
                    break;
                } else if (arg.startsWith("-")) {
                    result.add(arg);
                } else {
                    if (arg.isEmpty()) {
                        // Trying to get the top level tool to run itself 
                        // (ceylon's argument is the empty string)
                        result.clear();
                        result.add("help");
                        break;
                    }
                    List<String> rest = argsList.subList(ii, args.length);
                    if (rest.indexOf("--help") != -1) {
                        if (rest.indexOf("--") == -1 
                            || (rest.indexOf("--help") < rest.indexOf("--"))) {
                            result.add("help");
                            result.add(arg);
                            break;
                        }
                    }
                    result.add("--");
                    result.addAll(rest);
                    break;
                }
            }
        }
        return result;
    }

    private static void version(PrintStream out) {
        out.println(Tools.progName() + " version " + VERSION);
    }
    
    private int exit(int sc, String toolName, Exception t) throws Exception {
        if (t != null) {
            String msg = CeylonToolMessages.msg("fatal.error");
            if (t.getLocalizedMessage() != null) {
                msg += ": " + t.getLocalizedMessage();
            }
            System.err.println(Tools.progName() + 
                    (toolName != null ? " " + toolName : "") +
                    ": " + msg);
            if (stacktraces) {
                t.printStackTrace(System.err);
            }
        }
        return sc;
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && ARG_VERSION.equals(args[0])) {
            version(System.out);
            System.exit(SC_OK);
        } else {
            Java7Checker.check();
            System.exit(new CeylonTool().bootstrap(args));
        }
    }

    /**
     * Bootstraps this tool instance.
     * <ol>
     * <li>Use the given command line arguments to configure this instance</li>
     * <li>Calls {@link #run()} on this instance</li>
     * <li>Returns the status code that {@link #main(String[])} would invoke 
     * {@link System#exit(int)} with.</li>
     * </ol>
     * @param args Command line arguments
     * @return The exit code
     * @throws Exception
     */
    public int bootstrap(String[] args) throws Exception {
        try {
            ToolModel<CeylonTool> model = getToolModel("");
            List<String> myArgs = rearrangeArgs(args);
            getPluginFactory().bindArguments(model, this, myArgs);
            run();
        } catch (NoSuchToolException e) {
            return exit(SC_NO_SUCH_TOOL, null, e);
        } catch (ModelException e) {
            return exit(SC_TOOL_CREATION, getToolName(), e);
        } catch (OptionArgumentException e) {
            return exit(SC_ARGS, getToolName(), e);
        } catch (Exception e) {
            return exit(SC_TOOL_EXCEPTION, getToolName(), e);
        }
        return exit(SC_OK, null, null);
    }

    @Override
    public void run() throws Exception {
        if (version) {
            // --version is also handled in main(), so that you can at least do 
            // --version with a Java <7 JVM, but also do it here for consistency
            version(System.out);
        } else {   
            Tool tool = getTool();
            // Run the tool
            tool.run();
        }
    }

    Tool getTool() {
        Tool tool = null;
        
        // TODO Need to sort out how we load tools -- We need to be clear 
        // on where external tools should be put, and should it be 
        // possible to replace the standard tools?
        final ToolModel<?> model = getToolModel(getToolName());
        if (model == null) {
            Tools.printToolSuggestions(getPluginLoader(), new WordWrap(), getToolName());
            throw new NoSuchToolException();
        }
        tool = getPluginFactory().bindArguments(model, toolArgs);
        return tool;
    }

    <T extends Tool> ToolModel<T> getToolModel(String toolName) {
        final ToolModel<T> model = getPluginLoader().loadToolModel(toolName);
        return model;
    }
    
    public <T extends Tool> ToolModel<T> getToolModel() {
        return getToolModel(getToolName());
    }

    ToolFactory getPluginFactory() {
        if (pluginFactory == null) {
            pluginFactory =new ToolFactory();
        }
        return pluginFactory;
    }
    
    public void setPluginFactory(ToolFactory toolFactory) {
        this.pluginFactory = toolFactory;
    }

    ToolLoader getPluginLoader() {
        if (pluginLoader == null) {
            pluginLoader = new CeylonToolLoader(ClassLoader.getSystemClassLoader());
        }
        return pluginLoader;
    }
    
    public void setToolLoader(ToolLoader toolLoader) {
        this.pluginLoader = toolLoader;
    }
}
