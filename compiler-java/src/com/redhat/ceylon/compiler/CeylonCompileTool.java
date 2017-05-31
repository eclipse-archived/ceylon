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
package com.redhat.ceylon.compiler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.ceylon.ShaSigner;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.EnumUtil;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.NonFatalToolMessage;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleWildcardsHelper;
import com.redhat.ceylon.common.tools.OutputRepoUsingTool;
import com.redhat.ceylon.common.tools.RepoUsingTool;
import com.redhat.ceylon.common.tools.SourceArgumentsResolver;
import com.redhat.ceylon.common.tools.SourceDependencyResolver;
import com.redhat.ceylon.compiler.java.launcher.Main;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.langtools.tools.javac.main.Main.Result;
import com.redhat.ceylon.langtools.tools.javac.util.Context;
import com.redhat.ceylon.langtools.tools.javac.util.Log;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.model.cmr.ArtifactResult;

@Summary("Compiles Ceylon and Java source code and directly produces module " +
		"and source archives in a module repository.")
@Description("The default module repositories are `modules` and `" +
		Constants.REPO_URL_CEYLON+"`, while the default source directory is `source` " +
		"and the default resource directory is `resource`. " +
		"The default output module repository is `modules`." +
		"\n\n" +
		"The `<moduleOrFile>` arguments can be either module names (without versions) " +
		"or file paths specifying the Ceylon or Java source code to compile." +
		"\n\n" +
		"When `<moduleOrFile>` specifies a module the compiler searches for " +
		"compilation units and resource files belonging to the specified modules " +
		"in the specified source and resource directories. " +
		"For each specified module, the compiler generates a module archive, " +
		"source archive, and their checksum files in the specified output module " +
		"repository." +
		"\n\n"+
		"When `<moduleOrFile>` specifies a source file only that file is compiled and " +
		"the module archive is created or updated with the .class files produced. " +
		"The source file path is treated as relative to the current directory " +
		"(it still needs to be located either in the default source folder or in " +
		"any folder defined by the configuration file or `--source` options!)."+
        "\n\n" +
        "When `<moduleOrFile>` specifies a resource file only that file is added to " +
        "the module archive. " +
        "The resource file path is treated as relative to the current directory " +
        "(it still needs to be located either in the default resource folder or in " +
        "any folder defined by the configuration file or `--resource` options!)."+
        "\n\n" +
        "All program elements imported by a compilation unit must belong to the " +
        "same module as the compilation unit, or must belong to a module that " +
        "is explicitly imported in the module descriptor." +
        "\n\n" +
        "The compiler searches for dependencies in the following locations:" +
        "\n\n" +
        "* module archives in the specified repositories,\n"+
        "* source archives in the specified repositories, and\n"+
        "* module directories in the specified source directories.\n")
@RemainingSections(
        RepoUsingTool.DOCSECTION_INCLUDE_DEPS +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_CONFIG_COMPILER +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES +
        "\n\n" +
        "## Specifying `javac` options" +
        "\n\n" +
        "It is possible to pass options to the `javac` compiler by prefixing them " +
        "with `--javac=` and separating the javac option from its argument (if any) " +
        "using another `=`. For example, the option `--javac=-g:none` is equivalent to `javac`'s `-g:none`" +
        "\n\n" +
        "Execute `ceylon compile --javac=-help` for a list of the standard javac " +
        "options, and ceylon compile --javac=-X for a list of the non-standard javac " +
        "options." +
        "\n\n" +
        "**Important note**: There is no guarantee that any particular `javac` " +
        "option or combination of options will work, or continue to work in " +
        "future releases.")
public class CeylonCompileTool extends OutputRepoUsingTool {

    private static final class Helper extends com.redhat.ceylon.langtools.tools.javac.main.OptionHelper {
        String lastError = null;
        private final HashMap<String,String> options = new HashMap<String,String>();
        private final HashMap<String,List<String>> multiOptions = new HashMap<String,List<String>>();
        @Override
        public String get(com.redhat.ceylon.langtools.tools.javac.main.Option option) {
            return options.get(option.text);
        }

        @Override
        public void put(String name, String value) {
            options.put(name, value);
        }

        @Override
        public void remove(String name) {
            options.remove(name);
        }

        @Override
        public Log getLog() {
            return null;
        }

        @Override
        public String getOwnName() {
            return null;
        }

        @Override
        protected void error(String key, Object... args) {
            lastError = Main.getLocalizedString(key, args);
        }

        @Override
        protected void addFile(File f) {
            
        }

        @Override
        protected void addClassName(String s) {
            
        }

        @Override
        public List<String> getMulti(com.redhat.ceylon.langtools.tools.javac.main.Option option) {
            return multiOptions.get(option.text);
        }

        @Override
        public void addMulti(String name, String value) {
            List<String> list = multiOptions.get(name);
            if (list == null) {
                list = new ArrayList<String>(2);
                multiOptions.put(name, list);
            }
            list.add(value);
        }
    }
    
    private Helper helper = new Helper();

    private List<File> sources = DefaultToolOptions.getCompilerSourceDirs();
    private List<File> resources = DefaultToolOptions.getCompilerResourceDirs();
    private List<String> modulesOrFiles = DefaultToolOptions.getCompilerModules(Backend.Java);
    private boolean continueOnErrors;
    private boolean progress = DefaultToolOptions.getCompilerProgress();
    private List<String> javac = DefaultToolOptions.getCompilerJavac();
    private String encoding;
    private String includeDependencies;
    private boolean incremental = DefaultToolOptions.getCompilerIncremental();
    private String resourceRoot = DefaultToolOptions.getCompilerResourceRootName();
    private boolean noOsgi = DefaultToolOptions.getCompilerNoOsgi();
    private String osgiProvidedBundles = DefaultToolOptions.getCompilerOsgiProvidedBundles();
    private boolean noPom = DefaultToolOptions.getCompilerNoPom();
    private boolean pack200 = DefaultToolOptions.getCompilerPack200();
    private EnumSet<Warning> suppressWarnings = EnumUtil.enumsFromPossiblyInvalidStrings(Warning.class, DefaultToolOptions.getCompilerSuppressWarnings());
    private boolean flatClasspath = DefaultToolOptions.getDefaultFlatClasspath();
    private boolean autoExportMavenDependencies = DefaultToolOptions.getDefaultAutoExportMavenDependencies();
    private boolean fullyExportMavenDependencies = DefaultToolOptions.getDefaultFullyExportMavenDependencies();
    private boolean jigsaw = DefaultToolOptions.getCompilerGenerateModuleInfo();
    private Long targetVersion = DefaultToolOptions.getCompilerTargetVersion();
    private boolean ee = DefaultToolOptions.getCompilerEe();
    private List<String> eeImport = DefaultToolOptions.getCompilerEeImport();
    private List<String> eeAnnotation = DefaultToolOptions.getCompilerEeAnnotation();
    
    private ModuleSpec jdkProvider;
    {
        String jdkProvider = DefaultToolOptions.getCompilerJdkProvider();
        this.jdkProvider = jdkProvider != null ? ModuleSpec.parse(jdkProvider) : null;
    }
    private List<ModuleSpec> aptModules;
    {
        String[] aptModules = DefaultToolOptions.getCompilerAptModules();
        if(aptModules != null)
            setAptModule(Arrays.asList(aptModules));
    }

    public CeylonCompileTool() {
        super(CeylonCompileMessages.RESOURCE_BUNDLE);
    }

    @OptionArgument(longName="jdk-provider", argumentName="module")
    @Description("Specifies the name of the module providing the JDK (default: the underlying JDK).")
    public void setJdkProvider(String jdkProvider) {
        setJdkProviderSpec(ModuleSpec.parse(jdkProvider));
    }
    
    public void setJdkProviderSpec(ModuleSpec jdkProvider) {
        this.jdkProvider = jdkProvider;
    }

    @OptionArgument(longName="apt", argumentName="module")
    @Description("Specifies the list of modules to use as Java annotation-processing modules (default: none). Experimental.")
    public void setAptModule(List<String> aptModules) {
        if(aptModules != null){
            this.aptModules = new ArrayList<ModuleSpec>(aptModules.size());
            for(String mod : aptModules)
                this.aptModules.add(ModuleSpec.parse(mod));
        }else{
            this.aptModules = null;
        }
    }

    @Option(shortName='F', longName="flat-classpath")
    @Description("Compiles the Ceylon module using a flat classpath.")
    public void setFlatClasspath(boolean flatClasspath) {
        this.flatClasspath = flatClasspath;
    }

    @Option(longName="auto-export-maven-dependencies")
    @Description("When using JBoss Modules (the default), treats all module dependencies between " +
                 "Maven modules as shared.")
    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
    }

    @Option(longName="fully-export-maven-dependencies")
    @Description("When using JBoss Modules (the default), treats all module dependencies between " +
                 "Maven modules as shared, even to Ceylon modules.")
    public void setFullyExportMavenDependencies(boolean fullyExportMavenDependencies) {
        this.fullyExportMavenDependencies = fullyExportMavenDependencies;
    }

    @Option(longName="no-osgi")
    @Description("Indicates that the generated car file should not contain OSGi module declarations.")
    public void setNoOsgi(boolean noOsgi) {
        this.noOsgi = noOsgi;
    }

    @OptionArgument(longName="osgi-provided-bundles", argumentName="modules")
    @Description("Comma-separated list of module names. "
            + "The listed modules are expected to be OSGI bundles provided by the framework, "
            + "and will be omitted from the generated MANIFEST 'Required-Bundle' OSGI header.")
    public void setOsgiProvidedBundles(String osgiProvidedBundles) {
        this.osgiProvidedBundles = osgiProvidedBundles;
    }

    @Option(longName="generate-module-info")
    @Description("Generate Java 9 (Jigsaw) `module-info.class` module descriptor in the generated Ceylon archive.")
    public void setJigsaw(boolean jigsaw) {
        this.jigsaw = jigsaw;
    }

    @Option(longName="no-pom")
    @Description("Indicates that the generated car file should not contain Maven POM module declarations.")
    public void setNoPom(boolean noPom) {
        this.noPom = noPom;
    }

    @Option(longName="pack200")
    @Description("Try to make the generated car file smaller by repacking it using `pack200`.")
    public void setPack200(boolean pack200) {
        this.pack200 = pack200;
    }

    @OptionArgument(shortName='s', longName="src", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("Path to directory containing source files. " +
            "Can be specified multiple times; you can also specify several " +
            "paths separated by your operating system's `PATH` separator." +
            " (default: `./source`)")
    public void setSrc(List<File> source) {
        this.sources = source;
    }
    
    @OptionArgument(longName="source", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSource(List<File> source) {
        setSrc(source);
    }
    
    @OptionArgument(shortName='r', longName="resource", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("Path to directory containing resource files. " +
            "Can be specified multiple times; you can also specify several " +
            "paths separated by your operating system's `PATH` separator." +
            " (default: `./resource`)")
    public void setResource(List<File> resource) {
        this.resources = resource;
    }

    @OptionArgument(shortName='R', argumentName="folder-name")
    @Description("Sets the special resource folder name whose files will " +
            "end up in the root of the resulting module CAR file (default: ROOT).")
    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }
    
    @Hidden
    @Option(longName="continue-on-errors")
    @Description("Set to continue compiling even when errors are found.")
    public void setContinueOnErrors(boolean continueOnErrors) {
        this.continueOnErrors = continueOnErrors;
    }

    @Option(longName="progress")
    @Description("Print progress information.")
    public void setProgress(boolean progress) {
        this.progress = progress;
    }

    @OptionArgument(shortName='E', argumentName="encoding")
    @Description("Sets the encoding used for reading source files" +
            "(default: platform-specific).")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation of dependencies should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setIncludeDependencies(String includeDependencies) {
        this.includeDependencies = includeDependencies;
    }

    @Option(longName="incremental")
    @Description("Enables incremental compilation.")
    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="*")
    public void setModule(List<String> moduleOrFile) {
        this.modulesOrFiles = moduleOrFile;
    }
    
    @Option(shortName='d')
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`, `ast`, `code`, `cmr`, `benchmark`.")
    public void setVerbose(String verbose) {
        super.setVerbose(verbose);
    }
    
    protected Set<String> getVerboseCategories(String... morecats) {
        return super.getVerboseCategories("ast", "code", "cmr", "benchmark");
    }
    
    @OptionArgument(argumentName="option")
    @Description("Passes an option to the underlying java compiler.")
    public void setJavac(List<String> javac) {
        this.javac = javac;
    }
    
    @Option(shortName='W')
    @OptionArgument(argumentName = "warnings")
    @Description("Suppress the reporting of the given warnings. " +
            "If no `warnings` are given then suppresss the reporting of all warnings, " +
            "otherwise just suppresss those which are present. " +
            "Allowed flags include: " +
            "`filenameNonAscii`, `filenameCaselessCollision`, `deprecation`, "+
            "`compilerAnnotation`, `doclink`, `expressionTypeNothing`, "+
            "`unusedDeclaration`, `unusedImport`, `ceylonNamespace`, "+
            "`javaNamespace`, `suppressedAlready`, `suppressesNothing`, "+
            "`unknownWarning`, `ambiguousAnnotation`, `similarModule`, "+
            "`importsOtherJdk`, `javaAnnotationElement`.")
    public void setSuppressWarning(EnumSet<Warning> warnings) {
        this.suppressWarnings = warnings;
    }
    
    @OptionArgument(shortName='t', argumentName="version")
    @Description("The JVM that generated .class files should target. Use `7` to target Java 7 JVMs "
            + "or `8` to target Java 8 JVMs.")
    public void setTarget(Long version) {
        validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option.TARGET, "-target", version.toString());
        validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option.SOURCE, "-source", version.toString());
        this.targetVersion = version;
    }
    
    @Option
    @Description("Enable \"EE mode\" globally for all declarations in the compilation")
    public void setEe(boolean ee) {
        this.ee = ee;
    }
    
    @OptionArgument
    @Description("Override the default module imports which trigger \"EE mode\" "
            + "with the given module imports."
            + "When a module *directly* imports any of the listed modules EE mode "
            + "will be enabled for all declarations in the module."
            + "For example if this option includes the value `javax.javaeeapi` "
            + "or `maven:\"javax.javaee-api\"` then EE mode would be "
            + "enabled for any declaration in any module which had that *direct* "
            + "module import.")
    public void setEeImport(List<String> eeImports) {
        this.eeImport = eeImports;
    }
    
    @OptionArgument
    @Description("Override the default annotation types which trigger \"EE mode\" "
            + "with the given fully-qualified Java annotation type name."
            + "When a declaration is annotated with any of the listed annotations EE "
            + "module will be enabled for the top-level declaration containing "
            + "that annotated declaration. "
            + "For example if this option includes the value `javax.inject.Inject` "
            + "then EE mode would be enabled for any class with an attribute annotated"
            + "with `javax.inject::inject`.")
    public void setEeAnnotation(List<String> eeAnnotations) {
        this.eeAnnotation = eeAnnotations;
    }

    private List<String> arguments;
    
    private Main compiler;
    
    @Override
    protected List<File> getSourceDirs() {
        return sources;
    }

    @Override
    protected List<File> getResourceDirs() {
        return resources;
    }

    private void validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option option, String argument, String value) {
        helper.lastError = null;
        if (option.hasArg()) {
            if (option.process(helper, option.text, value)
                    || helper.lastError != null) {
                throw new IllegalArgumentException(helper.lastError);
            }
        } else {
            if (option.process(helper, argument)
                    || helper.lastError != null) {
                throw new IllegalArgumentException(helper.lastError);
            }
        }
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        compiler = new Main("ceylon compile");
        helper.options.clear();
        Options.instance(new Context());
        
        includeDependencies = processCompileFlags(includeDependencies, DefaultToolOptions.getCompilerIncludeDependencies());
        
        if (modulesOrFiles.isEmpty() &&
                !javac.contains("-help") &&
                !javac.contains("-X") &&
                !javac.contains("-version")) {
            throw new IllegalStateException("Argument moduleOrFile should appear at least 1 time(s)");
        }
        
        arguments = new ArrayList<>();
        
        if (cwd != null) {
            arguments.add("-cwd");
            arguments.add(cwd.getPath());
            validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option.CEYLONCWD, "-cwd", cwd.getPath());
        }
        
        if(jdkProvider != null){
            arguments.add("-jdk-provider");
            arguments.add(jdkProvider.toString());
        }

        if(aptModules != null){
            for(ModuleSpec mod : aptModules){
                arguments.add("-apt");
                arguments.add(mod.toString());
            }
        }

        for (File source : applyCwd(this.sources)) {
            arguments.add("-src");
            arguments.add(source.getPath());
            validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option.CEYLONSOURCEPATH, "-sourcepath", source.getPath());
        }
        
        for (File resource : applyCwd(this.resources)) {
            arguments.add("-res");
            arguments.add(resource.getPath());
        }
        
        if (resourceRoot != null) {
            arguments.add("-resroot");
            arguments.add(resourceRoot);
        }
        
        if (continueOnErrors) {
            arguments.add("-continue");
        }

        if (progress) {
            arguments.add("-progress");
        }

        if (offline) {
            arguments.add("-offline");
        }

        if (timeout != -1) {
            arguments.add("-timeout");
            arguments.add(String.valueOf(timeout));
        }

        if (flatClasspath) {
            arguments.add("-flat-classpath");
        }

        if (autoExportMavenDependencies) {
            arguments.add("-auto-export-maven-dependencies");
        }

        if (fullyExportMavenDependencies) {
            arguments.add("-fully-export-maven-dependencies");
        }

        if (overrides != null) {
            arguments.add("-overrides");
            if (overrides.startsWith("classpath:")) {
                arguments.add(overrides);
            } else {
                arguments.add(applyCwd(new File(overrides)).getPath());
            }
        }

        if (jigsaw) {
            arguments.add("-module-info");
        }

        if (noOsgi) {
            arguments.add("-noosgi");
        }

        if (osgiProvidedBundles != null
                && ! osgiProvidedBundles.isEmpty()) {
            arguments.add("-osgi-provided-bundles");
            arguments.add(osgiProvidedBundles);
        }

        if (noPom) {
            arguments.add("-nopom");
        }

        if (pack200) {
            arguments.add("-pack200");
        }
        
        if (verbose != null) {
            if (verbose.isEmpty()) {
                arguments.add("-verbose");
            } else {
                arguments.add("-verbose:" + verbose);
            }
        }
        
        if (out != null) {
            arguments.add("-out");
            arguments.add(out);
        }
        
        if (user != null) {
            arguments.add("-user");
            arguments.add(user);
        }
        if (pass != null) {
            arguments.add("-pass");
            arguments.add(pass);
        }

        String fileEncoding = encoding;
        if (fileEncoding == null) {
            fileEncoding = DefaultToolOptions.getDefaultEncoding();
        }
        if (fileEncoding != null) {
            
            try {
                Charset.forName(fileEncoding);
            } catch (IllegalCharsetNameException|UnsupportedCharsetException e) {
                throw new IllegalArgumentException("Unsupported encoding: "+ fileEncoding);
            }
            arguments.add(com.redhat.ceylon.langtools.tools.javac.main.Option.ENCODING.text);
            arguments.add(fileEncoding);
            
        }

        if (systemRepo != null) {
            arguments.add("-sysrep");
            arguments.add(systemRepo);
        }
        
        if (cacheRepo != null) {
            arguments.add("-cacherep");
            arguments.add(cacheRepo);
        }
        
        if (noDefRepos) {
            arguments.add("-nodefreps");
        }
        
        if (repos != null) {
            for (URI uri : this.repos) {
                arguments.add("-rep");
                arguments.add(uri.toString());
            }
        }
        
        if (suppressWarnings != null) {
            arguments.add("-suppress-warnings");
            arguments.add(EnumUtil.enumsToString(suppressWarnings));
        }
        
        if (targetVersion != null) {
            arguments.add("-source");
            arguments.add(targetVersion.toString());
            arguments.add("-target");
            arguments.add(targetVersion.toString());
        }
        
        if (ee) {
            arguments.add("-ee");
        }
        
        if (eeImport != null) {
            for (String eeImport: this.eeImport) {
                arguments.add("-ee-import");
                arguments.add(eeImport);
            }
        }
        
        if (eeAnnotation != null) {
            for (String eeAnnotation: this.eeAnnotation) {
                arguments.add("-ee-annotation");
                arguments.add(eeAnnotation);
            }
        }
        
        addJavacArguments(arguments, javac);
        
        List<File> srcs = applyCwd(this.sources);
        Collection<String> expandedModulesOrFiles = ModuleWildcardsHelper.expandWildcards(srcs , this.modulesOrFiles, Backend.Java);
        expandedModulesOrFiles = normalizeFileNames(expandedModulesOrFiles);
        if (expandedModulesOrFiles.isEmpty()) {
            String msg = CeylonCompileMessages.msg("error.no.sources");
            if (ModuleWildcardsHelper.onlyGlobArgs(this.modulesOrFiles)) {
                throw new NonFatalToolMessage(msg);
            } else {
                throw new ToolUsageError(msg);
            }
        }
        
        for (String moduleOrFile : expandedModulesOrFiles) {
            if (!com.redhat.ceylon.langtools.tools.javac.main.Option.SOURCEFILE.matches(moduleOrFile)) {
                throw new IllegalArgumentException(CeylonCompileMessages.msg("argument.error", moduleOrFile));
            }
            validateWithJavac(com.redhat.ceylon.langtools.tools.javac.main.Option.SOURCEFILE, moduleOrFile, moduleOrFile);
        }
        
        // We validate that all source arguments are correct
        SourceArgumentsResolver sar = new SourceArgumentsResolver(this.sources, this.resources, Constants.CEYLON_SUFFIX, Constants.JAVA_SUFFIX);
        sar.cwd(cwd).parse(expandedModulesOrFiles);
        
        if (includeDependencies != null && !COMPILE_NEVER.equals(includeDependencies)) {
            // Determine any dependencies that might need compiling as well
            SourceDependencyResolver sdr = new SourceDependencyResolver(getModuleVersionReader(), this.sources, Backends.JAVA);
            if (sdr.traverseDependencies(sar.getSourceFiles())) {
                for (ModuleVersionDetails mvd : sdr.getAdditionalModules()) {
                    if (COMPILE_FORCE.equals(includeDependencies)
                            || (COMPILE_CHECK.equals(includeDependencies) && shouldRecompile(getOfflineRepositoryManager(), mvd.getModule(), mvd.getVersion(), ModuleQuery.Type.JVM, true))
                            || (COMPILE_ONCE.equals(includeDependencies) && shouldRecompile(getOfflineRepositoryManager(), mvd.getModule(), mvd.getVersion(), ModuleQuery.Type.JVM, false))) {
                        expandedModulesOrFiles.add(mvd.getModule());
                        if (incremental) {
                            sar.parse(expandedModulesOrFiles);
                        }
                    }
                }
            }
        }
        
        if (incremental) {
            next_module:
            for (String module : sar.getModules()) {
                // Determine module version from source
                ModuleVersionDetails mvd = getModuleVersionReader().fromSource(module);
                if (mvd != null) {
                    // Find the module's CAR file
                    ArtifactResult artifact = getModuleArtifact(getOfflineRepositoryManager(), mvd.getModule(), mvd.getVersion(), ModuleQuery.Type.JVM);
                    if (artifact != null) {
                        // Check the language module version
                        for (ArtifactResult dep : artifact.dependencies()) {
                            if ("ceylon.language".equals(dep.name())) {
                                if (!Versions.CEYLON_VERSION_NUMBER.equals(dep.version())) {
                                    // Skip handling of --incremental on this module
                                    continue next_module;
                                }
                                break;
                            }
                        }
                        File carFile = artifact.artifact();
                        // Check if it has META-INF/errors.txt
                        Properties errors = getMetaInfErrors(carFile);
                        if (errors != null && !errors.isEmpty()) {
                            // If the module has errors we skip handling of
                            // --incremental on it and go to the next one
                            // TODO handle this incrementally
                            continue;
                        }
                        // Check if it has META-INF/hashes.txt
                        Properties oldHashes = getMetaInfHashes(carFile);
                        if (oldHashes != null) {
                            // Get the hashes for the new files
                            List<File> files = sar.getFilesByModule().get(module);
                            Properties newHashes = getFileHashes(module, files);
                            // Compare the two and make list of changed files
                            Collection<String> changedFiles = determineChangedFiles(module, oldHashes, newHashes, carFile);
                            if (changedFiles == null) {
                                // This shouldn't happen, but if it does we just skip any
                                // special treatment and compile this module normally
                            } else if (changedFiles.isEmpty()) {
                                // No files were changed, we shouldn't compile the module
                                expandedModulesOrFiles.remove(module);
                                // And we remove its files too if any were mentioned
                                Collection<String> remove = filesToStrings(module, files);
                                expandedModulesOrFiles.removeAll(remove);
                            } else {
                                if (expandedModulesOrFiles.contains(module)) {
                                    // The module itself was mentioned on the command line
                                    if (changedFiles.size() < files.size()) {
                                        // There were fewer changed files than the total number
                                        // So we remove the module
                                        expandedModulesOrFiles.remove(module);
                                        // And we remove its files too if any were mentioned
                                        Collection<String> remove = filesToStrings(module, files);
                                        expandedModulesOrFiles.removeAll(remove);
                                        // And then we add only those files that were changed
                                        expandedModulesOrFiles.addAll(changedFiles);
                                    }
                                } else {
                                    // Separate source files were mentioned on the command line
                                    // So we remove the unchanged files
                                    Collection<String> unchanged = filesToStrings(module, files);
                                    unchanged.removeAll(changedFiles);
                                    expandedModulesOrFiles.removeAll(unchanged);
                                }
                            }
                        }
                    }
                }
            }
            if (expandedModulesOrFiles.isEmpty()) {
                String msg = CeylonCompileMessages.msg("error.no.need");
                throw new NonFatalToolMessage(msg);
            }
        }
        
        arguments.addAll(expandedModulesOrFiles);
        
        if (verbose != null) {
            System.out.println(arguments);
            System.out.flush();
        }
    }

    private Collection<String> normalizeFileNames(Collection<String> modulesOrFiles) {
        Set<String> result = new LinkedHashSet<String>();
        for (String mof : modulesOrFiles) {
            if (mof.contains("/") || mof.contains("\\")) {
                // It's a file
                String path = FileUtil.relativeFile(sources, mof);
                File norm = FileUtil.applyPath(allDirs(), path);
                if (norm != null) {
                    result.add(norm.getPath());
                } else {
                    result.add(mof);
                }
            } else {
                // It's a module
                result.add(mof);
            }
        }
        return result;
    }
    
    private Properties getFileHashes(String moduleName, List<File> files) {
        Properties hashes = new Properties();
        for (File f : files) {
            String name = FileUtil.relativeFile(allDirs(), f.getPath());
            name = handleResourceRoot(moduleName, name);
            hashes.put(name, ShaSigner.sha1(f, null));
        }
        return hashes;
    }

    private String handleResourceRoot(String moduleName, String relFileName) {
        String rrp = resourceRoot.isEmpty() ? "" : ModuleUtil.moduleToPath(moduleName).getPath() + "/" + resourceRoot + "/";
        if (!rrp.isEmpty() && relFileName.startsWith(rrp)) {
            relFileName = relFileName.substring(rrp.length());
        }
        return relFileName;
    }
    
    private Collection<String> determineChangedFiles(String moduleName, Properties oldHashes, Properties newHashes, File carFile) {
        HashMap<String,String> diff = new HashMap<String,String>();
        // First we add all the new files and hashes to our diff
        for (String name : newHashes.stringPropertyNames()) {
            diff.put(name, newHashes.getProperty(name));
        }
        // Then we remove those that can be found in the old hashes
        for (String name : oldHashes.stringPropertyNames()) {
            String hash = oldHashes.getProperty(name);
            if (hash.equals(diff.get(name))) {
                diff.remove(name);
            }
        }
        // And finally we create a list of applied paths, but for the
        // ones that were not among the old hashes (meaning they are
        // either new or they are source files that didn't result in
        // any code being generated) we  first check the file time stamp
        // and don't add them if they are older than the CAR file
        List<String> result = new ArrayList<String>(diff.size());
        for (String name : diff.keySet()) {
            File full = FileUtil.applyPath(allDirs(), name);
            if (full == null) {
                // This really shouldn't happen
                return null;
            }
            try {
                String hash = oldHashes.getProperty(name);
                if (hash != null
                        || isModuleArtifactOutOfDate(carFile, moduleName, Type.JVM)) {
                    result.add(full.getPath());
                }
            } catch (IOException e) {
                // Ignore
            }
        }
        return result;
    }
    
    private Collection<String> filesToStrings(String moduleName, Collection<File> files) {
        List<String> result = new ArrayList<String>(files.size());
        for (File f : files) {
            String norm = handleResourceRoot(moduleName, f.getPath());
            result.add(norm);
        }
        return result;
    }

    /**
     * Run the compilation
     * @throws IOException 
     * @throws CompilerErrorException If the source code had errors
     * @throws SystemErrorException If there was a system error
     * @throws CompilerBugException If a bug in the compiler was detected.
     */
    @Override
    public void run() throws IOException {
        Result result = compiler.compile(arguments.toArray(new String[arguments.size()]));
        handleExitCode(result.exitCode, compiler.exitState);
    }

    private void handleExitCode(
            int javacExitCode,
            Main.ExitState exitState) {
        if (exitState == null) {
            throw new IllegalStateException("Missing ExitState, " + javacExitCode);
        }
        CeylonState ceylonState = exitState.ceylonState;
        switch (ceylonState) {
        case OK:
            break;
        case ERROR:
            throw new CompilerErrorException(exitState.errorCount);
        case SYS:
            throw new SystemErrorException(exitState.abortingException);
        case BUG:
            throw new CompilerBugException(exitState);
        default:
            throw new IllegalStateException("Unexpected CeylonState " + ceylonState);
        }
    }

    public static void addJavacArguments(List<String> arguments, List<String> javac) {
        Helper helper = new Helper();
        for (String argument : javac) {
            helper.lastError = null;
            String value = null;
            int index = argument.indexOf('=');
            if (index != -1 &&
                    !com.redhat.ceylon.langtools.tools.javac.main.Option.A.matches(argument)) {
                value = index < argument.length() ? argument.substring(index+1) : "";
                argument = argument.substring(0, index);
            }
            
            com.redhat.ceylon.langtools.tools.javac.main.Option javacOpt = getJavacOpt(argument);
            if (javacOpt == null) {
                throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.javac", argument));
            }
            
            
            if (value != null) {
                if (!javacOpt.hasArg()) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, "Unexpected argument given"));
                }
                if (!javacOpt.matches(argument)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.javac", argument));
                }
                if (javacOpt.process(helper, argument, value)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, helper.lastError));
                }
                
            
            } else {
                if (javacOpt.hasArg()) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, "Missing expected argument"));
                }
                if (!javacOpt.matches(argument)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.javac", argument));
                }
                if (javacOpt.process(helper, argument)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, helper.lastError));
                }
            }
            
            arguments.add(argument);
            if (value != null) {
                arguments.add(value);
            }
        }
    }

    private static com.redhat.ceylon.langtools.tools.javac.main.Option getJavacOpt(String optionName) {
        for (com.redhat.ceylon.langtools.tools.javac.main.Option o : com.redhat.ceylon.langtools.tools.javac.main.Option.getJavaCompilerOptions()) {
            if (o.matches(optionName)) {
                return o;
            }
        }
        return null;
    }
}
