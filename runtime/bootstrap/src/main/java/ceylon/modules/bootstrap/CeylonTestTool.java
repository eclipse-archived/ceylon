package ceylon.modules.bootstrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
                "The test tool accepts the following option from the Ceylon configuration file: " +
                "`testtool.compile` " +
                "(the equivalent option on the command line always has precedence)." +
                "## EXAMPLE" +
                "\n\n" +
                "The following would execute tests in the `com.example.foobar` module:" +
                "\n\n" +
        "    ceylon test com.example.foobar/1.0.0")
public class CeylonTestTool extends RepoUsingTool {

    private static final String TEST_MODULE_NAME = "com.redhat.ceylon.test";
    private static final String TEST_RUN_FUNCTION = "com.redhat.ceylon.test.run";

    private List<String> moduleNameOptVersionList;
    private List<String> testList;
    private List<String> argumentList;
    private String compileFlags;
    private String version;
    private boolean tap;

    public CeylonTestTool() {
        super(CeylonMessages.RESOURCE_BUNDLE);
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
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @OptionArgument(argumentName = "version")
    @Description("Specifies which version of the test module to use, defaults to " + Versions.CEYLON_VERSION_NUMBER + ".")
    public void setVersion(String version) {
        this.version = version;
    }

    @Option(longName = "tap")
    @Description("Enables the Test Anything Protocol v13.")
    public void setTap(boolean tap) {
        this.tap = tap;
    }

    @Rest
    public void setArgs(List<String> argumentList) {
        this.argumentList = argumentList;
    }

    @Override
    public void run() throws Exception {
        List<String> args = new ArrayList<String>();

        if (version == null) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(
                    getRepositoryManager(),
                    TEST_MODULE_NAME,
                    null,
                    ModuleQuery.Type.JVM,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION);

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
                args.add("--module");
                args.add(moduleAndVersion);
            }
        }
        if (testList != null) {
            for (String test : testList) {
                args.add("--test");
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

        if (tap) {
            args.add("--tap");
        }

        CeylonRunTool ceylonRunTool = new CeylonRunTool();
        ceylonRunTool.setModule(TEST_MODULE_NAME + "/" + version);
        ceylonRunTool.setRun(TEST_RUN_FUNCTION);
        ceylonRunTool.setArgs(args);
        ceylonRunTool.setRepository(repo);
        ceylonRunTool.setSystemRepository(systemRepo);
        ceylonRunTool.setCacheRepository(cacheRepo);
        ceylonRunTool.setMavenOverrides(mavenOverrides);
        ceylonRunTool.setNoDefRepos(noDefRepos);
        ceylonRunTool.setOffline(offline);
        ceylonRunTool.setVerbose(verbose);
        ceylonRunTool.setDefine(defines);
        ceylonRunTool.setCompile(compileFlags);
        ceylonRunTool.setCwd(cwd);
        ceylonRunTool.run();
    }

    private String resolveModuleAndVersion(String moduleNameOptVersion) throws IOException {
        String moduleName = ModuleUtil.moduleName(moduleNameOptVersion);
        String moduleVersion = ModuleUtil.moduleVersion(moduleNameOptVersion);

        moduleVersion = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                moduleName,
                moduleVersion,
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                compileFlags);

        return moduleVersion != null ? moduleName + "/" + moduleVersion : null;
    }

}