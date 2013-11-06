package ceylon.modules.bootstrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Summary;

@Summary("Executes tests")
@Description(
        "Executes tests in specified `<modules>`. " +
		"The `<modules>` arguments are the names of the modules to test with an optional version.")
@RemainingSections("##EXAMPLE\n" +
        "\n" +
        "The following would execute tests in the `com.example.foobar` module:\n" +
        "\n" +
        "    ceylon test com.example.foobar/1.0.0")
public class CeylonTestTool extends RepoUsingTool {
    
    private static final String CEYLON_TEST_MODULE = "com.redhat.ceylon.test/" + Versions.CEYLON_VERSION_NUMBER;
    private static final String CEYLON_TEST_RUN_FUNCTION = "com.redhat.ceylon.test.run";
    
    private List<String> moduleNameOptVersionList;
    private List<String> testList;
    private List<String> argumentList;

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

    @Rest
    public void setArgs(List<String> argumentList) {
        this.argumentList = argumentList;
    }

    @Override
    public void run() throws Exception {
        List<String> args = new ArrayList<String>();
        
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
        
        CeylonRunTool ceylonRunTool = new CeylonRunTool();
        ceylonRunTool.setModule(CEYLON_TEST_MODULE);
        ceylonRunTool.setRun(CEYLON_TEST_RUN_FUNCTION);
        ceylonRunTool.setArgs(args);
        ceylonRunTool.setRepository(repo);
        ceylonRunTool.setSystemRepository(systemRepo);
        ceylonRunTool.setCacheRepository(cacheRepo);
        ceylonRunTool.setMavenOverrides(mavenOverrides);
        ceylonRunTool.setNoDefRepos(noDefRepos);
        ceylonRunTool.setOffline(offline);
        ceylonRunTool.setVerbose(verbose);
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
                Versions.JVM_BINARY_MINOR_VERSION);

        return moduleVersion != null ? moduleName + "/" + moduleVersion : null;
    }

}