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
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleWildcardsHelper;
import com.redhat.ceylon.compiler.java.launcher.Main;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.sun.tools.javac.main.JavacOption;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.main.RecognizedOptions;
import com.sun.tools.javac.main.RecognizedOptions.OptionHelper;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;

@Summary("Compiles Ceylon and Java source code and directly produces module " +
		"and source archives in a module repository.")
@Description("The default module repositories are `modules` and " +
		Constants.REPO_URL_CEYLON+", while the default source directory is `source` " +
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
        OutputRepoUsingTool.DOCSECTION_CONFIG_COMPILER +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES +
        "\n\n" +
        "## Specifying `javac` options" +
        "\n\n" +
        "It is possible to pass options to the `javac` compiler by prefixing them " +
        "with `--javac=` and separating the javac option from its argument (if any) " +
        "using another `=`. For example:" +
        "\n\n" +
        "* The option `--javac=-target=1.6` is equivalent to `javac`'s `-target 1.6` and,\n" +
        "* the option `--javac=-g:none` is equivalent to `javac`'s `-g:none`" +
        "\n\n" +
        "Execute `ceylon compile --javac=-help` for a list of the standard javac " +
        "options, and ceylon compile --javac=-X for a list of the non-standard javac " +
        "options." +
        "\n\n" +
        "**Important note**: There is no guarantee that any particular `javac` " +
        "option or combination of options will work, or continue to work in " +
        "future releases.")
public class CeylonCompileTool extends OutputRepoUsingTool {

    private static final class Helper implements OptionHelper {
        String lastError;

        @Override
        public void setOut(PrintWriter out) {
            
        }

        @Override
        public void printXhelp() {
            
        }

        @Override
        public void printVersion() {
            
        }

        @Override
        public void printHelp() {
            
        }

        @Override
        public void printFullVersion() {
            
        }

        @Override
        public void error(String key, Object... args) {
            lastError = Main.getLocalizedString(key, args);
        }

        @Override
        public void addFile(File f) {
        }

        @Override
        public void addClassName(String s) {
        }
    }
    
    private static final Helper HELPER = new Helper();

    private List<File> sources = DefaultToolOptions.getCompilerSourceDirs();
    private List<File> resources = DefaultToolOptions.getCompilerResourceDirs();
    private List<String> modulesOrFiles = Arrays.asList("*");
    private boolean continueOnErrors;
    private List<String> javac = Collections.emptyList();
    private String encoding;
    private boolean noOsgi;
    private boolean pack200;

    public CeylonCompileTool() {
        super(CeylonCompileMessages.RESOURCE_BUNDLE);
    }
    
    @Option(longName="no-osgi")
    @Description("Indicates that the generated car file should not contain OSGi module declarations.")
    public void setNoOsgi(boolean noOsgi) {
        this.noOsgi = noOsgi;
    }
    
    @Option(longName="pack200")
    @Description("Try to make the generated car file smaller by repacking it using `pack200`.")
    public void setPack200(boolean pack200) {
        this.pack200 = pack200;
    }

    @OptionArgument(longName="src", argumentName="dirs")
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
    
    @OptionArgument(longName="resource", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("Path to directory containing resource files. " +
            "Can be specified multiple times; you can also specify several " +
            "paths separated by your operating system's `PATH` separator." +
            " (default: `./resource`)")
    public void setResource(List<File> resource) {
        this.resources = resource;
    }
    
    @Hidden
    @Option(longName="continue-on-errors")
    @Description("Set to continue compiling even when errors are found.")
    public void setContinueOnErrors(boolean continueOnErrors) {
        this.continueOnErrors = continueOnErrors;
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files" +
            "(default: platform-specific).")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="*")
    public void setModule(List<String> moduleOrFile) {
        this.modulesOrFiles = moduleOrFile;
    }
    
    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`, `ast`, `code`, `cmr`, `benchmark`.")
    public void setVerbose(String verbose) {
        super.setVerbose(verbose);
    }
    
    @OptionArgument(argumentName="option")
    @Description("Passes an option to the underlying java compiler.")
    public void setJavac(List<String> javac) {
        this.javac = javac;
    }

    private List<String> arguments;
    
    private Main compiler;
    
    private static void validateWithJavac(Options options, JavacOption encodingOpt, String option, String argument, String key) {
        if (!encodingOpt.matches(option)) {
            throw new IllegalArgumentException(CeylonCompileMessages.msg(key, option));
        }
        HELPER.lastError = null;
        if (encodingOpt.hasArg()) {
            if (encodingOpt.process(options, option, argument)
                    || HELPER.lastError != null) {
                throw new IllegalArgumentException(HELPER.lastError);
            }
        } else {
            if (encodingOpt.process(options, option)
                    || HELPER.lastError != null) {
                throw new IllegalArgumentException(HELPER.lastError);
            }
        }
    }
    
    @Override
    public void initialize() {
        setSystemProperties();
        compiler = new Main("ceylon compile");
        Options options = Options.instance(new Context());
        
        if (modulesOrFiles.isEmpty() &&
                !javac.contains("-help") &&
                !javac.contains("-X") &&
                !javac.contains("-version")) {
            throw new IllegalStateException("Argument moduleOrFile should appear at least 1 time(s)");
        }
        arguments = new ArrayList<>();
        for (File source : this.sources) {
            arguments.add("-src");
            arguments.add(source.getPath());
            options.addMulti(OptionName.SOURCEPATH, source.getPath());
        }
        
        for (File resource : this.resources) {
            arguments.add("-res");
            arguments.add(resource.getPath());
            //options.addMulti(OptionName.RESOURCEPATH, resource.getPath());
        }
        
        if (continueOnErrors) {
            arguments.add("-continue");
        }
        
        if (offline) {
            arguments.add("-offline");
        }
        
        if (mavenOverrides != null) {
            arguments.add("-maven-overrides");
            arguments.add(mavenOverrides);
        }

        if (noOsgi){
            arguments.add("-noosgi");
        }
        
        if (pack200){
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
            fileEncoding = CeylonConfig.get(DefaultToolOptions.DEFAULTS_ENCODING);
        }
        if (fileEncoding != null) {
            JavacOption encodingOpt = getJavacOpt(OptionName.ENCODING.toString());
            validateWithJavac(options, encodingOpt, OptionName.ENCODING.toString(), fileEncoding, "option.error.syntax.encoding");
            arguments.add(OptionName.ENCODING.toString());
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
        
        if (repo != null) {
            for (URI uri : this.repo) {
                arguments.add("-rep");
                arguments.add(uri.toString());
            }
        }
        
        addJavacArguments(arguments);
        
        JavacOption sourceFileOpt = getJavacOpt(OptionName.SOURCEFILE.toString());
        
        List<File> srcs = FileUtil.applyCwd(this.cwd, this.sources);
        List<String> expandedModulesOrFiles = ModuleWildcardsHelper.expandWildcards(srcs , this.modulesOrFiles);
        for (String moduleOrFile : expandedModulesOrFiles) {
            if (sourceFileOpt != null) {
                validateWithJavac(options, sourceFileOpt, moduleOrFile, moduleOrFile, "argument.error");
            }
            File file = new File(moduleOrFile);
            if (file.isFile()) {
                // It's a single (re)source file instead of a module name, so let's check
                // if it's really located in one of the defined (re)source folders
                if (moduleOrFile.endsWith(Constants.CEYLON_SUFFIX)
                        || moduleOrFile.endsWith(Constants.JAVA_SUFFIX)
                        || moduleOrFile.endsWith(Constants.JS_SUFFIX)) {
                    if (LanguageCompiler.getSrcDir(this.sources, file) == null) {
                        String srcPath = this.sources.toString();
                        throw new IllegalStateException(CeylonCompileMessages.msg("error.not.in.source.path", moduleOrFile, srcPath));
                    }
                } else {
                    if (LanguageCompiler.getSrcDir(this.resources, file) == null) {
                        String resrcPath = this.resources.toString();
                        throw new IllegalStateException(CeylonCompileMessages.msg("error.not.in.resource.path", moduleOrFile, resrcPath));
                    }
                }
            }
            arguments.add(moduleOrFile);
        }
        
        if (verbose != null) {
            System.out.println(arguments);
            System.out.flush();
        }
    }

    private static JavacOption getJavacOpt(String optionName) {
        for (com.sun.tools.javac.main.JavacOption o : RecognizedOptions.getJavaCompilerOptions(HELPER)) {
            if (optionName.equals(o.getName().toString())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Run the compilation
     * @throws CompilerErrorException If the source code had errors
     * @throws SystemErrorException If there was a system error
     * @throws CompilerBugException If a bug in the compiler was detected.
     */
    @Override
    public void run() {
        int result = compiler.compile(arguments.toArray(new String[arguments.size()]));
        handleExitCode(result, compiler.exitState);
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

    private void addJavacArguments(List<String> arguments) {
        Options options = Options.instance(new Context());
        for (String argument : javac) {
            HELPER.lastError = null;
            String value = null;
            int index = argument.indexOf('=');
            if (index != -1) {
                value = index < argument.length() ? argument.substring(index+1) : "";
                argument = argument.substring(0, index);
            }
            
            JavacOption javacOpt = getJavacOpt(argument.replaceAll(":.*", ":"));
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
                if (javacOpt.process(options, argument, value)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, HELPER.lastError));
                }
                
            
            } else {
                if (javacOpt.hasArg()) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, "Missing expected argument"));
                }
                if (!javacOpt.matches(argument)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.javac", argument));
                }
                if (javacOpt.process(options, argument)) {
                    throw new IllegalArgumentException(CeylonCompileMessages.msg("option.error.syntax.javac", argument, HELPER.lastError));
                }
            }
            
            arguments.add(argument);
            if (value != null) {
                arguments.add(value);
            }
        }
        
    
    }
}
