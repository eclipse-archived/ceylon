package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
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

@Summary("Executes tests")
@Description(
"Executes tests in specified `<modules>`. " +
        "The `<modules>` arguments are the names of the modules to test with an optional version.")
@RemainingSections(
"## Configuration file" +
        "\n\n" +
        "The test-js tool accepts the following option from the Ceylon configuration file: " +
        "`testtool.compile` " +
        "(the equivalent option on the command line always has precedence)." +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute tests in the `com.example.foobar` module:" +
        "\n\n" +
        "    ceylon test-js com.example.foobar/1.0.0")
public class CeylonTestJsTool extends RepoUsingTool {

    private static final String TEST_MODULE_NAME = "com.redhat.ceylon.testjs";
    private static final String TEST_RUN_FUNCTION = "com.redhat.ceylon.testjs.run";

    private List<String> moduleNameOptVersionList;
    private List<String> testList;
    private List<String> argumentList;
    private String version;
    private String compileFlags;
    private String nodeExe;
    private boolean debug = true;

    public CeylonTestJsTool() {
        super(CeylonRunJsMessages.RESOURCE_BUNDLE);
    }

    @Override
    public void initialize() throws Exception {
        // noop
    }

    @Argument(argumentName = "modules", multiplicity = "+")
    public void setModules(List<String> moduleNameOptVersionList) {
        this.moduleNameOptVersionList = moduleNameOptVersionList;
    }

    @OptionArgument(longName = "test", argumentName = "test")
    @Description("Specifies which tests will be run.")
    public void setTests(List<String> testList) {
        this.testList = testList;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @OptionArgument(argumentName = "version")
    @Description("Specifies version of test module, which will be used, if no value is set, then the latest available version will be used.")
    public void setVersion(String version) {
        this.version = version;
    }

    @OptionArgument(argumentName = "node-exe")
    @Description("The path to the node.js executable. Will be searched in standard locations if not specified.")
    public void setNodeExe(String nodeExe) {
        this.nodeExe = nodeExe;
    }

    @OptionArgument(argumentName = "debug")
    @Description("Shows more detailed output in case of errors.")
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Rest
    public void setArgs(List<String> argumentList) {
        this.argumentList = argumentList;
    }

    @Override
    public void run() throws Exception {
        final List<String> args = new ArrayList<String>();
        final List<String> moduleAndVersionList = new ArrayList<String>();

        if (version == null) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(
                    getRepositoryManager(),
                    TEST_MODULE_NAME,
                    null,
                    ModuleQuery.Type.JS,
                    Versions.JS_BINARY_MAJOR_VERSION,
                    Versions.JS_BINARY_MINOR_VERSION);

            if (versions == null || versions.isEmpty()) {
                version = Versions.CEYLON_VERSION_NUMBER;
            } else {
                ModuleVersionDetails mdv = versions.toArray(new ModuleVersionDetails[] {})[versions.size() - 1];
                version = mdv.getVersion();
            }
        }

        if (moduleNameOptVersionList != null) {
            for (String moduleNameOptVersion : moduleNameOptVersionList) {
                String moduleAndVersion = resolveModuleAndVersion(moduleNameOptVersion);
                if (moduleAndVersion == null) {
                    return;
                }
                args.add("__module");
                args.add(moduleAndVersion);
                moduleAndVersionList.add(moduleAndVersion);
            }
        }
        if (testList != null) {
            for (String test : testList) {
                args.add("__test");
                args.add(test);
            }
        }
        if (argumentList != null) {
            args.addAll(argumentList);
        }

        if (compileFlags == null) {
            compileFlags = DefaultToolOptions.getTestToolCompileFlags();
            if (compileFlags.isEmpty()) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_ONCE;
        }

        CeylonRunJsTool ceylonRunJsTool = new CeylonRunJsTool() {
            @Override
            protected void customizeDependencies(Set<File> localRepos, RepositoryManager repoman) throws IOException {
                for (String moduleAndVersion : moduleAndVersionList) {
                    String modName = ModuleUtil.moduleName(moduleAndVersion);
                    String modVersion = ModuleUtil.moduleVersion(moduleAndVersion);
                    File artifact = getArtifact(modName, modVersion, repoman);
                    localRepos.add(getRepoDir(modName, artifact));
                    loadDependencies(localRepos, repoman, artifact);
                }
            };
        };
        ceylonRunJsTool.setModuleVersion(TEST_MODULE_NAME + "/" + version);
        ceylonRunJsTool.setRun(TEST_RUN_FUNCTION);
        ceylonRunJsTool.setArgs(args);
        ceylonRunJsTool.setRepository(repo);
        ceylonRunJsTool.setSystemRepository(systemRepo);
        ceylonRunJsTool.setCacheRepository(cacheRepo);
        ceylonRunJsTool.setMavenOverrides(mavenOverrides);
        ceylonRunJsTool.setNoDefRepos(noDefRepos);
        ceylonRunJsTool.setOffline(offline);
        ceylonRunJsTool.setVerbose(verbose);
        ceylonRunJsTool.setNodeExe(nodeExe);
        ceylonRunJsTool.setDebug(debug);
        ceylonRunJsTool.setDefine(defines);
        ceylonRunJsTool.setCompile(compileFlags);
        ceylonRunJsTool.setCwd(cwd);
        ceylonRunJsTool.run();
    }

    private String resolveModuleAndVersion(String moduleNameOptVersion) throws IOException {
        String modName = ModuleUtil.moduleName(moduleNameOptVersion);
        String modVersion = ModuleUtil.moduleVersion(moduleNameOptVersion);

        modVersion = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                modName,
                modVersion,
                ModuleQuery.Type.JS,
                Versions.JS_BINARY_MAJOR_VERSION,
                Versions.JS_BINARY_MINOR_VERSION,
                compileFlags);

        return modVersion != null ? modName + "/" + modVersion : null;
    }
}