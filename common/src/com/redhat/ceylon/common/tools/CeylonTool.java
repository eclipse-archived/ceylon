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
package com.redhat.ceylon.common.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.FatalToolError;
import com.redhat.ceylon.common.tool.ModelException;
import com.redhat.ceylon.common.tool.NoSuchToolException;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolError;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;


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

    private static final String ARG_VERSION = "--version";
    /** Normal termination */
    public static final int SC_OK = 0;
    /** 
     * Tool threw an exception, but it's a no an unexpected error. 
     * (e.g. a compiler finding parse or type errors) 
     */
    public static final int SC_TOOL_ERROR = 1;
    /** 
     * Tool threw an exception
     */
    public static final int SC_TOOL_EXCEPTION = 2;
    /** 
     * The supplied tool arguments were bad in some way
     */
    public static final int SC_ARGS = 2;
    /** 
     * The supplied tool name doesn't exist
     */
    public static final int SC_NO_SUCH_TOOL = 3;
    /** 
     * The tool detected a bug 
     */
    public static final int SC_TOOL_BUG = 5;
    /** The tool instance couldn't be created 
     * (i.e. a programming error with the tools API or a bug in the tools API)
     */
    public static final int SC_TOOL_CREATION = 6;
    
    
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
    @Description("If an error propagates to the top level tool, print its stack trace.")
    public void setStacktraces(boolean stacktraces) {
        this.stacktraces = stacktraces;
    }
    
    public boolean getStacktraces() {
        return stacktraces;
    }
    
    //@Argument(argumentName="command", multiplicity="?", order=1)
    public void setCommand(String command) {
        this.toolName = command;
    }
    
    //@Argument(argumentName="command-arguments", multiplicity="*", order=2)
    public void setCommandArguments(List<String> commandArgs) {
        this.toolArgs = commandArgs;
    }
    
    @Argument(argumentName="tool-arguments", multiplicity="*")
    public void setArgs(List<String> args) {
        setCommand(args.get(0));
        setCommandArguments(args.subList(1, args.size()));
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
        out.println(Tools.progName() + " version " + Versions.CEYLON_VERSION);
    }
    
    public static void main(String... args) throws Exception {
        System.exit(start(args));
    }

    public static int start(String... args) throws Exception {
        if (args.length > 0 && ARG_VERSION.equals(args[0])) {
            version(System.out);
            return SC_OK;
        } else {
            return new CeylonTool().bootstrap(args);
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
    public int bootstrap(String... args) throws Exception {
        int result = setup(args);
        if (result == SC_OK) {
            result = execute();
        }
        return result;
    }

    public int setup(String... args) throws Exception {
        int result;
        Exception error = null;
        try {
            ToolModel<CeylonTool> model = getToolModel("");
            List<String> myArgs = rearrangeArgs(CommandLine.parse(args));
            getPluginFactory().bindArguments(model, this, myArgs);
            result = SC_OK;
        } catch (NoSuchToolException e) {
            error = e;
            result = SC_NO_SUCH_TOOL;
        } catch (ModelException e) {
            error = e;
            result = SC_TOOL_CREATION;
        } catch (OptionArgumentException e) {
            error = e;
            result = SC_ARGS;
        } catch (FatalToolError e) {
            error = e;
            result = SC_TOOL_BUG;
        } catch (ToolError e) {
            error = e;
            result = SC_TOOL_ERROR;
        } catch (Exception e) {
            error = e;
            result = SC_TOOL_EXCEPTION;
        }
        if (error != null) {
            Usage.handleException(this, getToolName(), error);
        }
        System.out.flush();
        return result;
    }

    public int execute() throws Exception {
        int result;
        Exception error = null;
        CeylonConfig oldConfig = null;
        try {
            oldConfig = setupConfig();
            run();
            result = SC_OK;
        } catch (NoSuchToolException e) {
            error = e;
            result = SC_NO_SUCH_TOOL;
        } catch (ModelException e) {
            error = e;
            result = SC_TOOL_CREATION;
        } catch (OptionArgumentException e) {
            error = e;
            result = SC_ARGS;
        } catch (FatalToolError e) {
            error = e;
            result = SC_TOOL_BUG;
        } catch (ToolError e) {
            error = e;
            result = SC_TOOL_ERROR;
        } catch (Exception e) {
            error = e;
            result = SC_TOOL_EXCEPTION;
        } finally {
            CeylonConfig.set(oldConfig);
        }
        if (error != null) {
            Usage.handleException(this, getToolName(), error);
        }
        System.out.flush();
        return result;
    }

    private CeylonConfig setupConfig() {
        Tool tool = getTool(getToolModel());
        if (tool instanceof CeylonBaseTool) {
            CeylonBaseTool cbt = (CeylonBaseTool)tool;
            File cwd = cbt.getCwd();
            if (cwd != null) {
                CeylonConfig config = CeylonConfig.createFromLocalDir(cwd);
                return CeylonConfig.set(config);
            }
        }
        return null;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void run() throws Exception {
        if (version) {
            // --version is also handled in main(), so that you can at least do 
            // --version with a Java <7 JVM, but also do it here for consistency
            version(System.out);
        } else {
            final ToolModel<?> model = getToolModel();
            if(model.isScript()){
                runScript(model);
            }else{
                Tool tool = getTool(model);
                // Run the tool
                tool.run();
            }
        }
    }

    private void runScript(ToolModel<?> model) {
        List<String> args = new ArrayList<String>(3+toolArgs.size());
        if (OSUtil.isWindows()) {
            args.add("cmd.exe");
            args.add("/C");
        }
        args.add(model.getScriptName());
        args.addAll(toolArgs);
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        setupScriptEnvironment(processBuilder);
        processBuilder.redirectError(Redirect.INHERIT);
        processBuilder.redirectInput(Redirect.INHERIT);
        if (OSUtil.isWindows()) {
            processBuilder.redirectOutput(Redirect.INHERIT);
        }
        try {
            Process process = processBuilder.start();
            if (!OSUtil.isWindows()) {
                InputStream in = process.getInputStream();
                InputStreamReader inread = new InputStreamReader(in);
                BufferedReader bufferedreader = new BufferedReader(inread);
                String line;
                while ((line = bufferedreader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int exit = process.waitFor();
            if(exit != 0)
                throw new ToolError("Script "+model.getScriptName()+" returned error exit code "+exit) {};
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setupScriptEnvironment(ProcessBuilder processBuilder) {
        Map<String, String> env = processBuilder.environment();
        String ceylonHome = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if(ceylonHome != null)
            env.put(Constants.ENV_CEYLON_HOME_DIR, ceylonHome);
        // FIXME: more info?
        env.put("JAVA_HOME", System.getProperty("java.home"));
        env.put("CEYLON_VERSION_MAJOR", Integer.toString(Versions.CEYLON_VERSION_MAJOR));
        env.put("CEYLON_VERSION_MINOR", Integer.toString(Versions.CEYLON_VERSION_MINOR));
        env.put("CEYLON_VERSION_RELEASE", Integer.toString(Versions.CEYLON_VERSION_RELEASE));
        env.put("CEYLON_VERSION", Versions.CEYLON_VERSION);
        env.put("CEYLON_VERSION_NAME", Versions.CEYLON_VERSION_NAME);
    }

    public Tool getTool(ToolModel<?> model) {
        Tool tool = null;
        if (model == null) {
            ArgumentModel<?> argumentModel = getToolModel("").getArguments().get(0);
            // XXX Very evil hack to work around the fact that the CeyonTool does 
            // not use subtools yet, and so the model used to generate help doc
            // doesn't quite work right.
            argumentModel.setName("command");
            throw new NoSuchToolException(argumentModel,
                    getToolName());
        }
        if(model.isScript())
            return null;
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
            pluginLoader = new CeylonToolLoader(this.getClass().getClassLoader());
        }
        return pluginLoader;
    }
    
    public void setToolLoader(ToolLoader toolLoader) {
        this.pluginLoader = toolLoader;
    }
}
