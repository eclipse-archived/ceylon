package ceylon.modules.bootstrap;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.AbstractTestTool;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
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

    private boolean flatClasspath;
    private boolean autoExportMavenDependencies;
    private boolean linkWithCurrentDistribution;

    public CeylonTestTool() {
        super(CeylonMessages.RESOURCE_BUNDLE, ModuleQuery.Type.JVM, 
        		Versions.JVM_BINARY_MAJOR_VERSION, Versions.JVM_BINARY_MINOR_VERSION, 
        		// JS binary but don't care since JVM
        		null, null);
    }

    @Option(longName="flat-classpath")
    @Description("Launches the Ceylon module using a flat classpath.")
    public void setFlatClasspath(boolean flatClasspath) {
        this.flatClasspath = flatClasspath;
    }

    @Option
    @Description("Link modules which were compiled with a more recent "
            + "version of the distribution to the version of that module "
            + "present in this distribution (" + Versions.CEYLON_VERSION_NUMBER + "). "
            + "This might fail with a linker error at runtime. For example "
            + "if the module depended on an API present in the more "
            + "recent version, but absent from " + Versions.CEYLON_VERSION_NUMBER +". "
                    + "Allowed arguments are upgrade, downgrade or abort. Default: upgrade")
    public void setLinkWithCurrentDistribution(boolean linkWithCurrentDistribution) {
        this.linkWithCurrentDistribution = linkWithCurrentDistribution;
    }
    
    @Option(longName="auto-export-maven-dependencies")
    @Description("When using JBoss Modules (the default), treats all module dependencies between " +
                 "Maven modules as shared.")
    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
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
        ceylonRunTool.setFlatClasspath(flatClasspath);
        ceylonRunTool.setLinkWithCurrentDistribution(linkWithCurrentDistribution);
        ceylonRunTool.setAutoExportMavenDependencies(autoExportMavenDependencies);
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