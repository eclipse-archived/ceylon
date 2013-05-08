/*
 * Copyright 2012 Red Hat inc. and third party contributors as noted
 * by the author tags.
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

package ceylon.modules.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

@Summary("Executes a Ceylon program")
@Description(
        "Executes the ceylon program specified as the `<module>` argument. " +
                "The `<module>` may optionally include a version."
)
@RemainingSections(
        "##EXAMPLE\n" +
                "\n" +
                "The following would execute the `com.example.foobar` module:\n" +
                "\n" +
                "    ceylon run com.example.foobar/1.0.0"
)
public class CeylonRunTool implements Tool {
    private static final String CEYLON_RUNTIME = "ceylon.runtime";
    
    private static volatile Module runtimeModule;

    private String moduleNameOptVersion;
    private String run;
    private List<String> repo = new ArrayList<String>(1);
    private String systemRepo;
    private boolean disableDefault;
    private boolean verbose = false;
    private String verboseFlags = "";
    private boolean offline;
    private List<String> args = Collections.emptyList();

    @Argument(argumentName = "module", multiplicity = "1", order = 1)
    public void setModule(String moduleNameOptVersion) {
        this.moduleNameOptVersion = moduleNameOptVersion;
    }

    @Argument(argumentName = "args", multiplicity = "*", order = 2)
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @OptionArgument(longName = "run", argumentName = "toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class with no parameters.")
    public void setRun(String run) {
        this.run = run;
    }

    @OptionArgument(longName = "rep", argumentName = "url")
    @Description("Specifies a module repository.")
    public void setRepo(List<String> repo) {
        this.repo = repo;
    }

    @OptionArgument(longName = "sysrep", argumentName = "url")
    @Description("Specifies the system repository.")
    public void setSytemRepo(String systemRepo) {
        this.systemRepo = systemRepo;
    }

    @Option(longName = "d")
    @Description("Disables the default module repositories and source directory.")
    public void setDisableDefault(boolean disableDefault) {
        this.disableDefault = disableDefault;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be vebose about the flags which are present. " +
            "Allowed flags include: `cmr`.")
    public void setVerbose(String verboseFlags) {
        this.verbose = true;
        this.verboseFlags = verboseFlags;
    }

    @Option(longName="offline")
    @Description("Enables offline mode that will prevent the module loader from connecting to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    @Override
    public void run() {
        ArrayList<String> argList = new ArrayList<String>();

        String ceylonVersion = System.getProperty("ceylon.system.version");
        
        String sysRep;
        if (systemRepo != null) {
            sysRep = systemRepo;
        } else {
            sysRep = System.getProperty("ceylon.system.repo");
        }

        if (run != null) {
            argList.add("-run");
            argList.add(run);
        }

        if (disableDefault) {
            argList.add("-d");
        }

        if (offline) {
            argList.add("-offline");
        }

        if (verbose) {
            if (verboseFlags == null || verboseFlags.isEmpty()) {
                argList.add("-verbose");
            } else {
                argList.add("-verbose:" + verboseFlags);
            }
        }

        argList.add("-sysrep");
        argList.add(sysRep);

        for (String repo : this.repo) {
            argList.add("-rep");
            argList.add(repo);
        }

        argList.add(moduleNameOptVersion);
        argList.addAll(args);

        try {
            if (runtimeModule == null) {
                synchronized (ModuleLoader.class) {
                    if (runtimeModule == null) {
                        org.jboss.modules.Main.main(setupArguments(argList, sysRep, ceylonVersion));
                        // set runtime module
                        ModuleLoader ml = Module.getBootModuleLoader();
                        runtimeModule = ml.loadModule(ModuleIdentifier.create(CEYLON_RUNTIME, ceylonVersion));
                    } else {
                        runtimeModule.run(moduleArguments(argList));
                    }
                }
            } else {
                runtimeModule.run(moduleArguments(argList));
            }
        } catch (Error err) {
            throw err;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private String[] setupArguments(List<String> argList, String sysRep, String ceylonVersion) {
        ArrayList<String> setupArgs = new ArrayList<String>();
        setupArgs.addAll(Arrays.asList(
                "-mp", sysRep,
                CEYLON_RUNTIME + ":" + ceylonVersion,
                "+executable", "ceylon.modules.jboss.runtime.JBossRuntime"));
        setupArgs.addAll(argList);
        String[] args = setupArgs.toArray(new String[setupArgs.size()]);
        return args;
    }
    
    private String[] moduleArguments(List<String> argList) {
        ArrayList<String> moduleArgs = new ArrayList<String>();
        moduleArgs.addAll(Arrays.asList(
                "+executable", "ceylon.modules.jboss.runtime.JBossRuntime"));
        moduleArgs.addAll(argList);
        String[] args = moduleArgs.toArray(new String[moduleArgs.size()]);
        return args;
    }
}
