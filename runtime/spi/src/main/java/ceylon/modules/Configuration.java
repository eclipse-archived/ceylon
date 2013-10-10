/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ceylon.modules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.ArgumentType;

import com.redhat.ceylon.common.Versions;

/**
 * @author Stephane Epardaud
 * @author Ales Justin
 */
public class Configuration {

    /**
     * Ceylon module to run
     */
    public String module;

    /**
     * Ceylon program arguments
     */
    public String[] arguments;

    /**
     * The current working directory to use (default = null)
     */
    public String cwd;
    
    /**
     * List of module repositories
     */
    public List<String> repositories = new LinkedList<String>();

    /**
     * The path to the system repository (default = null)
     */
    public String systemRepository;

    /**
     * The path to the cache folder (default = null)
     */
    public String cacheRepository;

    /**
     * Indicates that the default repositories should not be used
     */
    public boolean noDefaultRepositories;

    /**
     * Name of class or toplevel method to run
     */
    public String run;

    /**
     * Source path
     */
    public String src;

    /**
     * Verbose debug output
     */
    public boolean verbose;

    /**
     * Offline mode
     */
    public boolean offline;
    
    // Non-Ceylon
    public String executable;
    public boolean cacheContent;
    /**
     * names of implementation classes by interface name
     */
    public Map<String, String> impl = new HashMap<String, String>();

    /**
     * Sets an argument and checks its required number of parameters
     *
     * @param name   argument name without the prefix -/+
     * @param type   type of option
     * @param values list of arguments
     * @param i      index of the argument name
     * @return the index of the last argument required eaten by this argument
     */
    public int setArgument(String name, ArgumentType type, String[] values, int i) {
        Argument argument = Argument.forArgumentName(name, type);
        if (argument == null)
            throw new CeylonRuntimeException("Unknown argument: " + name);

        if (i + argument.getRequiredValues() >= values.length)
            throw new CeylonRuntimeException("Missing argument value: " + name);

        int arg = i + 1;
        switch (argument) {
            case EXECUTABLE:
                executable = values[arg];
                break;
            case CACHE_CONTENT:
                cacheContent = true;
                break;
            case IMPLEMENTATION:
                impl.put(values[arg], values[arg + 1]);
                break;
            case SOURCE:
                src = values[arg];
                break;
            case RUN:
                run = values[arg];
                break;
            case CWD:
                cwd = values[arg];
                break;
            case SYSTEM_REPOSITORY:
                systemRepository = values[arg];
                break;
            case CACHE_REPOSITORY:
                cacheRepository = values[arg];
                break;
            case NO_DEFAULT_REPOSITORIES:
                noDefaultRepositories = true;
                break;
            case REPOSITORY:
                repositories.add(values[arg]);
                break;
            case VERBOSE:
                verbose = true;
                break;
            case OFFLINE:
                offline = true;
                break;
            case HELP:
                printUsage(0);
                break;
            case VERSION:
                printVersion();
                break;
        }

        return i + argument.getRequiredValues();
    }

    private void printVersion() {
        System.out.println("Version: ceylon " + Versions.CEYLON_VERSION);
        SecurityActions.exit(0);
    }

    private void printUsage(int exitCode) {
        System.err.print("Usage: ceylon [options...] moduleName/version [args...]\n"
                + "\n"
                + " -run qualified-name: Name of a class or toplevel method to run\n"
                + "                      (default: use the module descriptor)\n"
                + " -rep path:           Module repository path (can be specified more than once)\n");
        System.err.print(" -src path:           Source path (default: source)\n"
                + " -verbose:            Output messages about what the runtime is doing\n"
                + " -help:               Prints help usage\n"
                + " -version:            Prints version number\n"
                + " moduleName/version:  Module name and version to run (required)\n"
        );
        SecurityActions.exit(exitCode);
    }

    public void check() {
        if (executable == null
                || executable.isEmpty()) {
            System.err.println("Error: Missing +executable parameter\n");
            printUsage(1);
        }
        if (module == null
                || module.isEmpty()) {
            System.err.println("Error: Missing module name\n");
            printUsage(1);
        }
    }
}
