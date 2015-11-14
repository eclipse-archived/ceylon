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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import ceylon.modules.CeylonRuntimeException;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.compiler.java.runtime.tools.Backend;
import com.redhat.ceylon.compiler.java.runtime.tools.CeylonToolProvider;
import com.redhat.ceylon.compiler.java.runtime.tools.JavaRunnerOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;

@Summary("Executes a Ceylon program")
@Description(
        "Executes the Ceylon program specified as the `<module>` argument. " +
                "The `<module>` may optionally include a version."
)
@RemainingSections(
        "## Configuration file" +
        "\n\n" +
        "The run tool accepts the following option from the Ceylon configuration file: " +
        "`runtool.compile` " +
        "(the equivalent option on the command line always has precedence)." +
        "\n\n" +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute the `com.example.foobar` module:" +
         "\n\n" +
         "    ceylon run com.example.foobar/1.0.0" +
         "\n\n" +
         "The following would execute the `bob` function in the `com.example.foobar.gee` package of the same previous module:" +
         "\n\n" +
         "    ceylon run --run com.example.foobar.gee::bob com.example.foobar/1.0.0"
)
public class CeylonRunTool extends RepoUsingTool {
    private static final String CEYLON_RUNTIME = "ceylon.runtime";
    
    private static volatile Module runtimeModule;

    private String moduleNameOptVersion;
    private String run;
    private String compileFlags;
    private boolean flatClasspath;
    private List<String> args = Collections.emptyList();

    private boolean autoExportMavenDependencies;

    public CeylonRunTool() {
        super(CeylonMessages.RESOURCE_BUNDLE);
    }
    
    @Option(longName="flat-classpath")
    @Description("Launches the Ceylon module using a flat classpath.")
    public void setFlatClasspath(boolean flatClasspath) {
        this.flatClasspath = flatClasspath;
    }

    @Argument(argumentName = "module", multiplicity = "1", order = 1)
    public void setModule(String moduleNameOptVersion) {
        this.moduleNameOptVersion = moduleNameOptVersion;
    }

    @Rest
    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Option(longName="auto-export-maven-dependencies")
    @Description("When using JBoss Modules (the default), treats all module dependencies between " +
                 "Maven modules as shared.")
    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
    }

    @OptionArgument(longName = "run", argumentName = "toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class with no parameters. " +
            "The format is: `qualified.package.name::classOrMethodName` with `::` acting as separator " +
            "between the package name and the toplevel class or method name.")
    public void setRun(String run) {
        this.run = run;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @Option(shortName='d')
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`, `cmr`.")
    public void setVerbose(String verbose) {
        super.setVerbose(verbose);
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }

    @Override
    public void run() throws IOException {
        if (compileFlags == null) {
            compileFlags = DefaultToolOptions.getRunToolCompileFlags();
            if (compileFlags.isEmpty()) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_ONCE;
        }
        
        String module = ModuleUtil.moduleName(moduleNameOptVersion);
        String version = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                module,
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                compileFlags);
        if (version == null) {
            return;
        }
        if (!version.isEmpty()) {
            moduleNameOptVersion = ModuleUtil.makeModuleName(module, version);
        }
        
        if (flatClasspath) {
            startInFlatClasspath(module, version);
            return;
        }
        // else go on with JBoss modules

        ArrayList<String> argList = new ArrayList<String>();

        String ceylonVersion = System.getProperty(Constants.PROP_CEYLON_SYSTEM_VERSION);
        if (ceylonVersion == null) {
            ceylonVersion = Versions.CEYLON_VERSION_NUMBER;
        }
        
        String sysRep;
        if (systemRepo != null) {
            sysRep = systemRepo;
        } else {
            sysRep = System.getProperty(Constants.PROP_CEYLON_SYSTEM_REPO);
        }

        if (run != null) {
            argList.add("-run");
            argList.add(run);
        }

        if (offline) {
            argList.add("-offline");
        }

        if (verbose != null) {
            argList.add("-verbose");
            if (verbose.isEmpty()) {
                argList.add("all");
            } else {
                argList.add(verbose);
            }
        }

        argList.add("-sysrep");
        argList.add(sysRep);

        if (cacheRepo != null) {
            argList.add("-cacherep");
            argList.add(cacheRepo);
        }

        if (overrides != null) {
            argList.add("-overrides");
            argList.add(overrides);
        }

        if (noDefRepos) {
            argList.add("-nodefreps");
        }

        if (autoExportMavenDependencies) {
            argList.add("-auto-export-maven-dependencies");
        }

        if (repo != null) {
            for (URI repo : this.repo) {
                argList.add("-rep");
                argList.add(repo.toString());
            }
        }

        argList.add(moduleNameOptVersion);
        argList.addAll(args);

        try {
            if (runtimeModule == null) {
                synchronized (ModuleLoader.class) {
                    if (runtimeModule == null) {
                        org.jboss.modules.Module.setModuleLogger(new NoGreetingJDKModuleLogger());
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
            // get around a class loader issue
            if(e instanceof CeylonRuntimeException == false
                    && e.getClass().getName().equals("ceylon.modules.CeylonRuntimeException"))
                throw new CeylonRuntimeException(e.getMessage());
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private void startInFlatClasspath(String module, String version) {
        JavaRunnerOptions options = new JavaRunnerOptions();
        if(repo != null) {
            for (URI userRepository : repo) {
                options.addUserRepository(userRepository.toASCIIString());
            }
        }
        options.setOffline(offline);
        options.setSystemRepository(systemRepo);
        options.setVerboseCategory(verbose);
        options.setRun(run);
        options.setOverrides(overrides);
        Runner runner = CeylonToolProvider.getRunner(Backend.Java, options, module, version);
        runner.run(args.toArray(new String[args.size()]));
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
