package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.AbstractTestTool;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
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
        "\n\n" +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute tests in the `com.example.foobar` module:" +
        "\n\n" +
        "    ceylon test-js com.example.foobar/1.0.0")
public class CeylonTestJsTool extends AbstractTestTool {

    private static final String COLOR_RESET = "com.redhat.ceylon.common.tool.terminal.color.reset";
    private static final String COLOR_GREEN = "com.redhat.ceylon.common.tool.terminal.color.green";
    private static final String COLOR_RED = "com.redhat.ceylon.common.tool.terminal.color.red";

    private String nodeExe;
    private boolean debug = true;

    public CeylonTestJsTool() {
        super(CeylonRunJsMessages.RESOURCE_BUNDLE, ModuleQuery.Type.JS, Versions.JS_BINARY_MAJOR_VERSION, Versions.JS_BINARY_MINOR_VERSION);
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

    @Override
    public void run() throws Exception {
        final List<String> args = new ArrayList<String>();
        final List<String> moduleAndVersionList = new ArrayList<String>();

        processModuleNameOptVersionList(args, moduleAndVersionList);
        processTestList(args);
        processArgumentList(args);
        processCompileFlags();
        processTapOption(args);
        processReportOption(args);
        processColors(args);
        
        resolveVersion(moduleAndVersionList);

        CeylonRunJsTool ceylonRunJsTool = new CeylonRunJsTool() {
            @Override
            protected void customizeDependencies(List<File> localRepos, RepositoryManager repoman) throws IOException {
                for (String moduleAndVersion : moduleAndVersionList) {
                    String modName = ModuleUtil.moduleName(moduleAndVersion);
                    String modVersion = ModuleUtil.moduleVersion(moduleAndVersion);
                    File artifact = getArtifact(repoman, modName, modVersion, true);
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
        ceylonRunJsTool.setOverrides(overrides);
        ceylonRunJsTool.setNoDefRepos(noDefRepos);
        ceylonRunJsTool.setOffline(offline);
        ceylonRunJsTool.setVerbose(verbose);
        ceylonRunJsTool.setNodeExe(nodeExe);
        ceylonRunJsTool.setDebug(debug);
        ceylonRunJsTool.setCompile(compileFlags);
        ceylonRunJsTool.setCwd(cwd);
        ceylonRunJsTool.run();
    }

    private void processColors(final List<String> args) {
        if (System.getProperties().containsKey(COLOR_RESET)
                && System.getProperties().containsKey(COLOR_GREEN)
                && System.getProperties().containsKey(COLOR_RED)) {
            args.add("--" + COLOR_RESET);
            args.add(System.getProperty(COLOR_RESET));
            args.add("--" + COLOR_GREEN);
            args.add(System.getProperty(COLOR_GREEN));
            args.add("--" + COLOR_RED);
            args.add(System.getProperty(COLOR_RED));
        }
    }

}