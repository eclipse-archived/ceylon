package com.redhat.ceylon.tools;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.tools.annotation.Description;
import com.redhat.ceylon.tools.annotation.Rest;
import com.redhat.ceylon.tools.annotation.Summary;

/**
 * Main entry point for the {@code ceylon} command; the top level tool; the 
 * plugin with no name.
 */
@Summary("Top level Ceylon tool")
@Description("The <command> is the name of a ceylon tool. The available tools are:")
public class Tool implements Plugin {

    public static final String VERSION = "0.4 (Supercalifragilisticexpialidocious)";
    private static final String ARG_VERSION = "--version";
    private static final int SC_OK = 0;
    private static final int SC_ARGS = 1;
    private static final int SC_TOOL_CREATION = 1;
    private static final int SC_NO_SUCH_TOOL = 2;
    private static final int SC_TOOL_EXCEPTION = 3;
    
    private String toolName;
    private List<String> allArgs;
    private List<String> myArgs;
    private List<String> toolArgs;
    private final ArgumentParserFactory argParserFactory;
    
    public Tool() {
        this.argParserFactory = new ArgumentParserFactory();
    }
    
    @Rest
    public void setArgs(List<String> args) {
        this.allArgs = args;
        for (int ii = 0; ii < args.size(); ii++) {
            String arg = args.get(ii);
            if (arg.equals("--help")) {
                this.toolName = "help";
                this.toolArgs = allArgs.subList(ii+1, allArgs.size());
                this.myArgs = allArgs.subList(0, ii);
                break;
            } else if (!arg.startsWith("-")) {
                this.toolName = arg;
                this.toolArgs = allArgs.subList(ii+1, allArgs.size());
                this.myArgs = allArgs.subList(0, ii);
                break;
            }
        }
        if (toolName == null) {
            this.toolName = "help";
            this.toolArgs = Collections.emptyList();
            this.myArgs = Collections.emptyList();
        }
    }

    static String progName() {
        return System.getProperty("com.redhat.ceylon.tools.progname", "ceylon");
    }
    
    private static void version(PrintStream out) {
        out.println(progName() + " version " + VERSION);
    }
    
    private static void exit(String message, int sc) {
        if (message != null) {
            System.err.println(progName() + ": " + message);
        }
        System.exit(sc);
    }
    
    public static void main(String[] args) {
        // TODO compile
        // TODO doc
        // TODO import-jar
         
        // TODO tool-name --help ==> help tool-name
        // TODO --debug & logging
        // TODO --verbose
        // TODO I18N
        if (args.length > 0 && ARG_VERSION.equals(args[0])) {
            version(System.out);
            exit(null, SC_OK);
        } else {
            Java7Checker.check();
            Tool main = new Tool();
            main.setArgs(Arrays.asList(args));
            main.run();
        }

    }

    static void printToolSuggestions(PluginLoader toolLoader, WordWrap wrap, String toolName) {
        wrap.print(progName() + ": " + toolName + " is not a ceylon command. See 'ceylon --help'.").println();
        final List<String> suggestions = toolLoader.typo(toolName);
        if (suggestions != null 
                && !suggestions.isEmpty()) {
            wrap.println();
            wrap.print("Did you mean:");
            wrap.setIndent(4);
            wrap.println();
            for (String suggestion : suggestions) {
                wrap.print(suggestion).println();
            }
            wrap.println();
        }
    }
    
    @Override
    public int run() {
        Plugin tool = null;
        try {
            // TODO Need to sort out how we load tools -- We need to be clear 
            // on where external tools should be put, and should it be 
            // possible to replace the standard tools?
            final PluginLoader loader = new PluginLoader(argParserFactory);
            final PluginModel<?> model = loader.loadToolModel(toolName);
            if (model == null) {
                printToolSuggestions(loader, new WordWrap(), toolName);
                return SC_NO_SUCH_TOOL;
            }
            tool = new PluginFactory(argParserFactory).bindArguments(model, toolArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return SC_TOOL_CREATION;
        }
        
        try {
            // Pass off control
            return tool.run() << 2;// note we reserve codes 0..4 for ourselves
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SC_TOOL_EXCEPTION;
    }
    
}
