package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
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
    
    private static final String CEYLON_TEST_MODULE = "com.redhat.ceylon.test/" + Versions.CEYLON_VERSION_NUMBER;
    private static final String CEYLON_TEST_RUN_FUNCTION = "com.redhat.ceylon.test.run";
    
    private List<String> moduleNameOptVersionList;
    private List<String> testList;
    private List<String> argumentList;
    private String compileFlags;

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
    @Description("Determines if and how compilation should be handled. " +
            "Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
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
        
        if (compileFlags == null) {
            compileFlags = DefaultToolOptions.getTestToolCompileFlags();
            if (compileFlags.isEmpty()) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_ONCE;
        }
        
        CeylonRunJsTool ceylonRunTool = new CeylonRunJsTool();
        ceylonRunTool.setModuleVersion(CEYLON_TEST_MODULE);
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
                Versions.JVM_BINARY_MINOR_VERSION,
                compileFlags);

        return moduleVersion != null ? moduleName + "/" + moduleVersion : null;
    }

}