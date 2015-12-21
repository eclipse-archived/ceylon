package ceylon.modules.bootstrap;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.AbstractTestTool;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.RemainingSections;
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
        "\n\n" +
        "## EXAMPLE" +
        "\n\n" +
        "The following would execute tests in the `com.example.foobar` module:" +
        "\n\n" +
        "    ceylon test com.example.foobar/1.0.0")
public class CeylonTestTool extends AbstractTestTool {

    public CeylonTestTool() {
        super(CeylonMessages.RESOURCE_BUNDLE, ModuleQuery.Type.JVM, Versions.JVM_BINARY_MAJOR_VERSION, Versions.JVM_BINARY_MINOR_VERSION);
    }

    @Override
    public void run() throws Exception {
        List<String> args = new ArrayList<String>();
        List<String> moduleAndVersionList = new ArrayList<String>();

        processModuleNameOptVersionList(args, moduleAndVersionList);
        processTestList(args);
        processTagList(args);
        processArgumentList(args);
        processCompileFlags();
        processTapOption(args);
        processReportOption(args);
        
        resolveVersion(moduleAndVersionList);

        CeylonRunTool ceylonRunTool = new CeylonRunTool();
        ceylonRunTool.setModule(TEST_MODULE_NAME + "/" + version);
        ceylonRunTool.setRun(TEST_RUN_FUNCTION);
        ceylonRunTool.setArgs(args);
        ceylonRunTool.setRepository(repo);
        ceylonRunTool.setSystemRepository(systemRepo);
        ceylonRunTool.setCacheRepository(cacheRepo);
        ceylonRunTool.setOverrides(overrides);
        ceylonRunTool.setNoDefRepos(noDefRepos);
        ceylonRunTool.setOffline(offline);
        ceylonRunTool.setVerbose(verbose);
        ceylonRunTool.setCompile(compileFlags);
        ceylonRunTool.setCwd(cwd);
        try{
            ceylonRunTool.run();
        }catch(Throwable x){
            // Get around class loader issues where we can't compare the class statically
            if(x.getClass().getCanonicalName().equals("ceylon.test.engine.internal.TestFailureException")) {
                throw new CeylonTestFailureError();
            }
            throw x;
        }
    }

}