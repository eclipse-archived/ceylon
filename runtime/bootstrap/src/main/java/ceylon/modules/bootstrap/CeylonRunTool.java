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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.impl.AssemblyRepositoryBuilder;
import com.redhat.ceylon.cmr.util.JarUtils;
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
import com.redhat.ceylon.common.tools.RepoUsingTool;
import com.redhat.ceylon.compiler.java.runtime.tools.Backend;
import com.redhat.ceylon.compiler.java.runtime.tools.CeylonToolProvider;
import com.redhat.ceylon.compiler.java.runtime.tools.JavaRunnerOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.ModuleNotFoundException;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;

import ceylon.modules.CeylonRuntimeException;
import ceylon.modules.bootstrap.loader.InitialModuleLoader;

@Summary("Executes a Ceylon program on the JVM")
@Description(
        "Executes the Ceylon program specified as the `module` argument. " +
                "The `module` may optionally include a version."
)
@RemainingSections(
        RepoUsingTool.DOCSECTION_COMPILE_FLAGS +
        "\n\n" +
        "## Configuration file" +
        "\n\n" +
        "The run tool accepts the following option from the Ceylon configuration file: " +
        "`runtool.compile`, `runtool.run`, `runtool.module` and multiple `runtool.arg` " +
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
    private File assembly;
    /** The (Ceylon) name of the functional to run, e.g. {@code foo.bar::baz} */
    private String run;
    private String compileFlags;
    private List<String> args;
    private boolean flatClasspath = DefaultToolOptions.getDefaultFlatClasspath();
    private boolean autoExportMavenDependencies = DefaultToolOptions.getDefaultAutoExportMavenDependencies();
    private boolean upgradeDist = DefaultToolOptions.getLinkWithCurrentDistribution();
    private Map<String,String> extraModules = new HashMap<String,String>();

    public CeylonRunTool() {
        super(CeylonMessages.RESOURCE_BUNDLE);
    }
    
    @OptionArgument(argumentName="option")
    @Description("Passes an option to the underlying ceylon compiler.")
    public void setCompilerArguments(List<String> compilerArguments) {
        this.compilerArguments = compilerArguments;
    }
    
    @Option(shortName='F', longName="flat-classpath")
    @Description("Launches the Ceylon module using a flat classpath.")
    public void setFlatClasspath(boolean flatClasspath) {
        this.flatClasspath = flatClasspath;
    }

    @Argument(argumentName = "module", multiplicity = "?", order = 1)
    public void setModule(String moduleNameOptVersion) {
        this.moduleNameOptVersion = moduleNameOptVersion;
    }

    @Rest
    public void setArgs(List<String> args) {
        this.args = args;
    }
    
    @OptionArgument(shortName='a', argumentName="archive")
    @Description("Specifies the path to a Ceylon assembly archive that should be executed.")
    public void setAssembly(File assembly) {
        this.assembly = assembly;
    }

    @Option(longName="auto-export-maven-dependencies")
    @Description("When using JBoss Modules (the default), treats all module dependencies between " +
                 "Maven modules as shared.")
    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
    }

    @OptionArgument(shortName='x', longName = "run", argumentName = "toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class to run. " +
            "The indicated declaration must be shared by the `module` and have no parameters. " +
            "The format is: `qualified.package.name::classOrMethodName` with `::` acting as separator " +
            "between the package name and the toplevel class or method name. (default: `module.name::run`)")
    public void setRun(String run) {
        this.run = run;
    }

    @Option(shortName='c')
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`. " +
            "If no flags are specified, defaults to `check`.")
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
    
    protected Set<String> getVerboseCategories(String... morecats) {
        return super.getVerboseCategories("cmr");
    }
    
    @Option
    @Description("Link modules which were compiled with a more recent "
            + "version of the distribution to the version of that module "
            + "present in this distribution (" + Versions.CEYLON_VERSION_NUMBER + "). "
            + "This might fail with a linker error at runtime. For example "
            + "if the module depended on an API present in the more "
            + "recent version, but absent from " + Versions.CEYLON_VERSION_NUMBER +". "
            + "Allowed arguments are upgrade, downgrade or abort. Default: upgrade")
    public void setLinkWithCurrentDistribution(boolean downgradeDist) {
        this.upgradeDist = !downgradeDist;
    }
    
    public void addExtraModule(String module, String version) {
        this.extraModules.put(module, version);
    }

    @Override
    protected boolean shouldUpgradeDist() {
        return upgradeDist;
    }

    @Override
    protected Type getCompilerType() {
        return ModuleQuery.Type.JVM;
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        
        if (assembly != null) {
            // The --compile flag makes no sense in combination with --assembly
            compileFlags = COMPILE_NEVER;
            
            JarFile jar = JarUtils.validJar(assembly);
            if (jar != null) {
                Manifest manifest = jar.getManifest();
                if (manifest != null) {
                    // Add the assembly to the repositories
                    if (repos == null) {
                        repos = new ArrayList<URI>(1);
                    }
                    repos.add(0, new URI("assembly:" + assembly.getPath()));
                    
                    // Determine which module to execute
                    String mfMainMod = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_MAIN_MODULE);
                    if (mfMainMod == null) {
                        throw new IllegalArgumentException("Assembly manifest is missing required attribute '" + Constants.ATTR_ASSEMBLY_MAIN_MODULE + "'");
                    }
                    if (moduleNameOptVersion != null) {
                        // This is a bit of a hack, but when we use the --assembly
                        // argument we can't specify a module name/version anymore,
                        // but we can still specify arguments, so if a module name
                        // was specified on the CLI it must be an argument and we
                        // add it to the beginning of the list of arguments
                        args.add(0, moduleNameOptVersion);
                    }
                    moduleNameOptVersion = mfMainMod;
                    
                    // See if there is an overrides file
                    String mfOverrides = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_OVERRIDES);
                    if (mfOverrides != null) {
                        // At this point we need to pre-unpack the assembly
                        File assemblyFolder = AssemblyRepositoryBuilder.registerAssembly(assembly);
                        File ovrFile = new File(assemblyFolder, mfOverrides);
                        overrides = ovrFile.getPath();
                    }
                    
                    // See if we need to execute a specific toplevel
                    String mfRun = manifest.getMainAttributes().getValue(Constants.ATTR_ASSEMBLY_RUN);
                    if (mfRun != null) {
                        run = mfRun;
                    }
                } else {
                    throw new IllegalArgumentException("The assembly archive does not have a manifest");
                }
            } else {
                throw new IllegalArgumentException("The file specified by '--assembly' is not a valid Ceylon assembly archive: " + assembly);
            }
        }
        
        if (moduleNameOptVersion == null) {
            moduleNameOptVersion = DefaultToolOptions.getRunToolModule(com.redhat.ceylon.common.Backend.Java);
            if (moduleNameOptVersion != null) {
                if (run == null) {
                    run = DefaultToolOptions.getRunToolRun(com.redhat.ceylon.common.Backend.Java);
                }
                if (args == null || args.isEmpty()) {
                    args = DefaultToolOptions.getRunToolArgs(com.redhat.ceylon.common.Backend.Java);
                }
            } else {
                throw new IllegalArgumentException("Missing required argument 'module' to command 'run'");
            }
        }
    }

    @Override
    public void run() throws IOException {
        compileFlags = processCompileFlags(compileFlags, DefaultToolOptions.getRunToolCompileFlags(com.redhat.ceylon.common.Backend.Java));
        
        String module = ModuleUtil.moduleName(moduleNameOptVersion);
        String version = checkModuleVersionsOrShowSuggestions(
                module,
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                // JS binary but don't care since JVM
                null, null,
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
            argList.add(prependModuleName(module, run));
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
        
        if (!upgradeDist) {
            argList.add("-downgrade-dist");
        }

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

        if (repos != null) {
            for (URI repo : this.repos) {
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
                        System.setProperty("boot.module.loader", InitialModuleLoader.class.getName());
                        org.jboss.modules.Main.main(moduleArguments(argList, sysRep, ceylonVersion));
                        // set runtime module
                        ModuleLoader ml = Module.getBootModuleLoader();
                        runtimeModule = ml.loadModule(ModuleIdentifier.create(CEYLON_RUNTIME, ceylonVersion));
                    } else {
                        runtimeModule.run(moduleArguments(argList, null, null));
                    }
                }
            } else {
                runtimeModule.run(moduleArguments(argList, null, null));
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

    private static String prependModuleName(String module, String run) {
        if (run==null) return null;
        if (!com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME
                    .equals(module)
                && !run.contains("::")
                && !run.contains(".")) {
            return module + "::" + run;
        }
        else {
            return run;
        }
    }
    
    private void startInFlatClasspath(String module, String version) {
        JavaRunnerOptions options = new JavaRunnerOptions();
        if(repos != null) {
            for (URI userRepository : repos) {
                options.addUserRepository(userRepository.toASCIIString());
            }
        }
        if (getCwd() != null) {
            options.setWorkingDirectory(getCwd().getAbsolutePath());
        }
        options.setOffline(offline);
        options.setSystemRepository(systemRepo);
        options.setVerboseCategory(verbose);
        options.setRun(prependModuleName(module, run));
        options.setOverrides(overrides);
        options.setDowngradeDist(!upgradeDist);
        options.setExtraModules(extraModules);
        
        try {
            Runner runner = CeylonToolProvider.getRunner(Backend.Java, options, module, version);
            runner.run(args.toArray(new String[args.size()]));
        } catch (ModuleNotFoundException e) {
            throw new CeylonRuntimeException(e.getMessage());
        }
    }

    private String[] moduleArguments(List<String> argList, String sysRep, String ceylonVersion) {
        ArrayList<String> moduleArgs = new ArrayList<String>();
        if (sysRep != null) {
            moduleArgs.add("-mp");
            moduleArgs.add(sysRep);
        }
        if (ceylonVersion != null) {
            moduleArgs.add(CEYLON_RUNTIME + ":" + ceylonVersion);
        }
        moduleArgs.add("+executable");
        moduleArgs.add("ceylon.modules.jboss.runtime.JBossRuntime");
        if (cwd != null) {
            moduleArgs.add("-cwd");
            moduleArgs.add(cwd.getAbsolutePath());
        }
        moduleArgs.addAll(argList);
        String[] args = moduleArgs.toArray(new String[moduleArgs.size()]);
        return args;
    }
}
