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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.CeylonConfigFinder;
import com.redhat.ceylon.common.tool.AnnotatedToolModel;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.FatalToolError;
import com.redhat.ceylon.common.tool.ModelException;
import com.redhat.ceylon.common.tool.NoSuchToolException;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.ScriptToolModel;
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
        "Otherwise `<tool-arguments>` should begin with the name of a ceylon tool " +
        "or a list of comma-separated tool names which will be invoked one after " +
        "the other with the same remaining command line arguments. " +
        "The named tools are loaded and configured with the remaining command " +
        "line arguments and control passes to those tools.")
@RemainingSections(
"## CONFIGURATION MECHANISM\n\n" +
"Ceylon uses a simple text format to store customizations that are per repository and are per user. Such a configuration file may look like this:"+
"\n\n"+
"    #\n"+
"    # A '#' or ';' character indicates a comment.\n"+
"    #\n"+
"    \n"+
"    ; global settings\n"+
"    [defaults]\n"+
"        encoding = utf8\n"+
"        pager = false\n"+
"    \n"+
"    ; local repository\n"+
"    [repository \"LOCAL\"]\n"+
"        url = ./modules\n"+
"\n\n"+
"Various commands read from the configuration file and adjust their operation accordingly. See ceylon-config(1) for a list and more details about the configuration mechanism."
)
public class CeylonTool implements Tool {

    private static final String ARG_LONG_VERSION = "--version";
    private static final String ARG_SHORT_VERSION = "-v";
    
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
    private List<String> toolArgs = Collections.<String>emptyList();
    private boolean stacktraces = false;
    private ToolLoader pluginLoader;
    private ToolFactory pluginFactory;
    private boolean version;
    private Tool toolCache;
    private Boolean paginate;
    private File cwd;
    private File config;
    CeylonConfig oldConfig = null;
    
    public CeylonTool() {
    }
    
    @Option(shortName='v')
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
    
    @Option
    @Description("Pipe all Ceylon tool output into less (or if set, $CEYLON_PAGER or $PAGER) if standard output is a terminal. This overrides the `help.pager` and `defaults.pager` configuration options (see the \"Configuration Mechanism\" section below).")
    public void setPaginate(boolean paginate) {
        this.paginate = Boolean.TRUE;
    }
    
    public boolean getPaginate() {
        return paginate != null && paginate;
    }

    @Option
    @Description("Do not pipe Ceylon tool output into a pager.")
    public void setNoPager(boolean noPager) {
        this.paginate = Boolean.FALSE;
    }
    
    public boolean getNoPager() {
        return paginate != null && !paginate;
    }

    public File getCwd() {
        return cwd;
    }
    
    @OptionArgument(longName="cwd", argumentName="dir")
    @Description("Specifies the current working directory for this tool. " +
            "(default: the directory where the tool is run from)")
    public void setCwd(File cwd) {
        this.cwd = cwd;
    }
    
    public File getConfig() {
        return config;
    }
    
    @OptionArgument(longName="config", argumentName="file")
    @Description("Specifies the configuration file to use for this tool. " +
            "(default: `./.ceylon/config`)")
    public void setConfig(File config) {
        this.config = config;
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
    
    public String[] getToolNames() {
        if(toolName == null)
            return new String[0];
        if(toolName.indexOf(',') != -1)
            return toolName.split(",");
        return new String[]{toolName};
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
                } else if (arg.equals(ARG_LONG_VERSION) || arg.equals(ARG_SHORT_VERSION)) {
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
                    int helpIndex = rest.indexOf("--help");
                    if (helpIndex == -1) {
                        helpIndex = rest.indexOf("-h");
                    }
                    if (helpIndex != -1) {
                        int doubleDashIndex = rest.indexOf("--");
                        if (doubleDashIndex == -1 || (helpIndex < doubleDashIndex)) {
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
        int exit = start(args);
        // WARNING: NEVER CALL EXIT IF WE STILL HAVE DAEMON THREADS RUNNING AND WE'VE NO REASON TO EXIT WITH A NON-ZERO CODE
        if(exit != 0)
            System.exit(exit);
    }

    public static int start(String... args) throws Exception {
        if (args.length > 0 && (ARG_LONG_VERSION.equals(args[0]) || ARG_SHORT_VERSION.equals(args[0]))) {
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
        try {
            ToolModel<CeylonTool> model = getToolModel("");
            List<String> myArgs = rearrangeArgs(CommandLine.parse(args));
            getPluginFactory().bindArguments(model, this, this, myArgs);
            oldConfig = setupConfig();
            result = SC_OK;
        } catch (Exception e) {
            result = handleException(this, e);
        }
        System.out.flush();
        return result;
    }

    // Warning: this method called by reflection in Launcher
    public int execute() throws Exception {
        int result = SC_OK;
        try {
            String[] names = (toolName != null) ? getToolNames() : new String[] { null };
            for (String singleToolName : names) {
                ToolModel<Tool> model = getToolModel(singleToolName);
                Tool tool = getTool(model);
                CeylonConfig oldConfig2 = null;
                if (oldConfig == null) {
                    oldConfig2 = setupConfig(tool);
                }
                syncCwd(tool);
                try {
                    run(model, tool);
                    result = SC_OK;
                } finally {
                    if (oldConfig2 != null) {
                        CeylonConfig.set(oldConfig2);
                    }
                }
            }
        } catch (Exception e) {
            result = handleException(this, e);
        } finally {
            if (oldConfig != null) {
                CeylonConfig.set(oldConfig);
                oldConfig = null;
            }
        }
        System.out.flush();
        return result;
    }

    public static int handleException(CeylonTool mainTool, Exception ex) throws Exception {
        int result;
        if (ex instanceof NoSuchToolException) {
            result = SC_NO_SUCH_TOOL;
        } else if (ex instanceof ModelException) {
            result = SC_TOOL_CREATION;
        } else if (ex instanceof OptionArgumentException) {
            result = SC_ARGS;
        } else if (ex instanceof FatalToolError) {
            result = SC_TOOL_BUG;
        } else if (ex instanceof ToolError) {
            result = SC_TOOL_ERROR;
        } else {
            result = SC_TOOL_EXCEPTION;
        }
        Usage.handleException(mainTool, mainTool.getToolName(), ex);
        return result;
    }

    // Here we set up the global configuration for this thread
    // if (and only if) the setup deviates from the default
    // (meaning either `cwd` or `config` was set for the main tool)
    private CeylonConfig setupConfig() throws IOException {
        File cwd = getCwd();
        File cfgFile = getConfig();
        if (cfgFile != null) {
            File absCfgFile = FileUtil.applyCwd(cwd, cfgFile);
            CeylonConfig config = CeylonConfigFinder.DEFAULT.loadConfigFromFile(absCfgFile);
            return CeylonConfig.set(config);
        }
        if (cwd != null && cwd.isDirectory()) {
            CeylonConfig config = CeylonConfigFinder.loadDefaultConfig(cwd);
            return CeylonConfig.set(config);
        }
        return null;
    }

    // Here we set up the global configuration for this thread
    // if (and only if) the setup deviates from the default
    // (meaning `cwd` was set for the given tool)
    private CeylonConfig setupConfig(Tool tool) throws IOException {
        if (tool instanceof CeylonBaseTool) {
            CeylonBaseTool cbt = (CeylonBaseTool)tool;
            File cwd = cbt.getCwd();
            if (cwd != null && cwd.isDirectory()) {
                CeylonConfig config = CeylonConfigFinder.loadDefaultConfig(cwd);
                return CeylonConfig.set(config);
            }
            if (getCwd() != null) {
                // If the main tool's `cwd` options is set it
                // always overrides the one in the given tool
                cbt.setCwd(getCwd());
            }
        }
        return null;
    }

    // Here we set up the global configuration for this thread
    // if (and only if) the setup deviates from the default
    // (meaning `cwd` was set for the given tool)
    private void syncCwd(Tool tool) throws IOException {
        if (tool instanceof CeylonBaseTool) {
            CeylonBaseTool cbt = (CeylonBaseTool)tool;
            if (getCwd() != null) {
                // If the main tool's `cwd` options is set it
                // always overrides the one in the given tool
                cbt.setCwd(getCwd());
            }
        }
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public void run() throws Exception {
        // do nothing?
    }

    private void run(ToolModel<Tool> model, Tool tool) throws Exception {
        if (version) {
            // --version is also handled in main(), so that you can at least do 
            // --version with a Java <7 JVM, but also do it here for consistency
            version(System.out);
        } else {
            if(model instanceof ScriptToolModel){
                runScript((ScriptToolModel<Tool>)model);
            }else{
                // Run the tool
                tool.run();
            }
        }
    }

    private void runScript(ScriptToolModel<?> model) {
        List<String> args = new ArrayList<String>(3+toolArgs.size());
        if (OSUtil.isWindows()) {
            args.add("cmd.exe");
            args.add("/C");
        }
        args.add(model.getScriptName());
        args.addAll(toolArgs);
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        setupScriptEnvironment(processBuilder, model.getScriptName());
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

    public static void setupScriptEnvironment(ProcessBuilder processBuilder, String script) {
        Map<String, String> env = processBuilder.environment();
        String ceylonHome = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if (ceylonHome != null) {
            env.put(Constants.ENV_CEYLON_HOME_DIR, ceylonHome);
            String ceylonBin = ceylonHome + File.separator + "bin" + File.separator + "ceylon";
            if (OSUtil.isWindows()) {
                ceylonBin += ".bat";
            }
            env.put("CEYLON", ceylonBin);
        }
        // FIXME: more info?
        env.put("JAVA_HOME", System.getProperty("java.home"));
        env.put("CEYLON_VERSION_MAJOR", Integer.toString(Versions.CEYLON_VERSION_MAJOR));
        env.put("CEYLON_VERSION_MINOR", Integer.toString(Versions.CEYLON_VERSION_MINOR));
        env.put("CEYLON_VERSION_RELEASE", Integer.toString(Versions.CEYLON_VERSION_RELEASE));
        env.put("CEYLON_VERSION", Versions.CEYLON_VERSION_NUMBER);
        env.put("CEYLON_VERSION_FULL", Versions.CEYLON_VERSION);
        env.put("CEYLON_VERSION_NAME", Versions.CEYLON_VERSION_NAME);
        env.put("SCRIPT", script);
        env.put("SCRIPT_DIR", (new File(script)).getParent());
    }

    // WARNING: this is called by reflection in Launcher: do not REMOVE!!!
    public Tool[] getTools() {
        String[] toolNames = getToolNames();
        Tool[] tools = new Tool[toolNames.length];
        for(int i=0;i<toolNames.length;i++)
            tools[i] = getTool(getToolModel(toolNames[i]));
        return tools;
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
        if(!(model instanceof AnnotatedToolModel))
            return null;
        boolean useCache = false;
        if(toolName != null && toolName.equals(model.getName()))
            useCache = true;
        if(useCache && toolCache != null)
            return toolCache;
        tool = getPluginFactory().bindArguments(model, this, toolArgs);
        if(useCache)
            toolCache = tool;
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
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //ClassLoader classLoader = this.getClass().getClassLoader();
            pluginLoader = new CeylonToolLoader(classLoader);
        }
        return pluginLoader;
    }
    
    public void setToolLoader(ToolLoader toolLoader) {
        this.pluginLoader = toolLoader;
    }

    public Boolean getWantsPager() {
        return paginate;
    }
}
