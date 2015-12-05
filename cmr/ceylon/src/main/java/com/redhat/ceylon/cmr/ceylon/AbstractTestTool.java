package com.redhat.ceylon.cmr.ceylon;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;

public abstract class AbstractTestTool extends RepoUsingTool {
    
    protected static final String TEST_MODULE_NAME = "ceylon.test";
    protected static final String TEST_RUN_FUNCTION = "ceylon.test.cli::run";
    
    protected List<String> moduleNameOptVersionList;
    protected List<String> testList;
    protected List<String> tagList;
    protected List<String> argumentList;
    protected String version;
    protected String compileFlags;
    protected boolean tap;
    protected boolean report;
    private final Type type;
    private final Integer binaryMajor;
    private final Integer binaryMinor;

    public AbstractTestTool(ResourceBundle bundle, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        super(bundle);
        this.type = type;
        this.binaryMajor = binaryMajor;
        this.binaryMinor = binaryMinor;
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
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
    
    @OptionArgument(longName = "tag", argumentName = "tag")
    @Description("Specifies which tests will be run according to their tags. It can be used as "
            + "include filter, so only tests with specified tag will be executed. But it can "
            + "be used also as exclude filter, if tag name is prefixed with !, "
            + "so only tests without specified tag will be executed..")
    public void setTags(List<String> tagList) {
        this.tagList = tagList;
    }

    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Determines if and how compilation should be handled. Allowed flags include: `never`, `once`, `force`, `check`.")
    public void setCompile(String compile) {
        this.compileFlags = compile;
    }

    @OptionArgument(argumentName = "version")
    @Description("Specifies which version of the test module to use.")
    public void setVersion(String version) {
        this.version = version;
    }
    
    @Option(longName = "tap")
    @Description("Enables the Test Anything Protocol v13.")
    public void setTap(boolean tap) {
        this.tap = tap;
    }

    @Option(longName = "report")
    @Description("Generates the test results report into HTML format, output directory is `reports/test` (experimental).")
    public void setReport(boolean report) {
        this.report = report;
    }

    @Rest
    public void setArgs(List<String> argumentList) {
        this.argumentList = argumentList;
    }
    
    protected String resolveModuleAndVersion(String moduleNameOptVersion) throws IOException {
        String moduleName = ModuleUtil.moduleName(moduleNameOptVersion);
        String moduleVersion = ModuleUtil.moduleVersion(moduleNameOptVersion);

        moduleVersion = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                moduleName,
                moduleVersion,
                type,
                binaryMajor,
                binaryMinor,
                compileFlags);

        return moduleVersion != null ? moduleName + "/" + moduleVersion : null;
    }
    
    protected void processModuleNameOptVersionList(List<String> args, List<String> moduleAndVersionList) throws IOException {
        if (moduleNameOptVersionList != null) {
            for (String moduleNameOptVersion : moduleNameOptVersionList) {
                String moduleAndVersion = resolveModuleAndVersion(moduleNameOptVersion);
                if (moduleAndVersion == null) {
                    return;
                }
                args.add("--module");
                args.add(moduleAndVersion);
                moduleAndVersionList.add(moduleAndVersion);
            }
        }
    }
    
    protected void processTestList(List<String> args) {
        if (testList != null) {
            for (String test : testList) {
                args.add("--test");
                args.add(test);
            }
        }
    }
    
    protected void processTagList(List<String> args) {
        if (tagList != null) {
            for (String tag : tagList) {
                args.add("--tag");
                args.add(tag);
            }
        }
    }
    
    protected void processArgumentList(List<String> args) {
        if (argumentList != null) {
            args.addAll(argumentList);
        }
    }

    protected void processCompileFlags() {
        if (compileFlags == null) {
            compileFlags = DefaultToolOptions.getTestToolCompileFlags();
            if (compileFlags.isEmpty()) {
                compileFlags = COMPILE_NEVER;
            }
        } else if (compileFlags.isEmpty()) {
            compileFlags = COMPILE_ONCE;
        }
    }

    protected void processReportOption(List<String> args) {
        if (report) {
            args.add("--report");
        }
    }

    protected void processTapOption(List<String> args) {
        if (tap) {
            args.add("--tap");
        }
    }
    
    protected void resolveVersion(List<String> moduleAndVersionList) throws IOException {
        if (version == null) {
            for (String moduleAndVersion : moduleAndVersionList) {
                String foundVersion = findTestVersionInDependecies(moduleAndVersion);
                if (version != null && foundVersion != null && !version.equals(foundVersion)) {
                    throw new ToolUsageError(Messages.msg(bundle, "test.version.ambiguous"));
                }
                if (foundVersion != null) {
                    version = foundVersion;
                }
            }
            if (version == null) {
                version = Versions.CEYLON_VERSION_NUMBER;
                msg("test.version.default", version).newline();
            }
        }
        if( verbose != null && (verbose.equals("") || verbose.equals("test")) ) {
            msg("test.version.info", version).newline();
        }
    }

    private String findTestVersionInDependecies(String moduleAndVersion) {
        String moduleName = ModuleUtil.moduleName(moduleAndVersion);
        String moduleVersion = ModuleUtil.moduleVersion(moduleAndVersion);
        ModuleDependencyInfo root = new ModuleDependencyInfo(moduleName, moduleVersion, false, false);
        Queue<ModuleDependencyInfo> queue = new LinkedList<ModuleDependencyInfo>();
        queue.add(root);
        
        while(!queue.isEmpty()) {
            String foundVersion = findTestVersionInDependecies(queue.poll(), queue);
            if( foundVersion != null ) {
                return foundVersion;
            }
        }
        
        return null;
    }

    private String findTestVersionInDependecies(ModuleDependencyInfo module, Queue<ModuleDependencyInfo> queue) {
        Collection<ModuleVersionDetails> moduleDetailsCollection = getModuleVersions(module.getName(), module.getVersion(), type, binaryMajor, binaryMinor);
        ModuleVersionDetails moduleDetails = moduleDetailsCollection.iterator().next();
        
        for (ModuleDependencyInfo dependency : moduleDetails.getDependencies()) {
            if( dependency.getName().equals("ceylon.test") ) {
                return dependency.getVersion();
            }
            if( dependency.getName().equals("ceylon.language") ) {
                continue;
            }
            queue.add(dependency);
        }
        
        return null;
    }
    
}