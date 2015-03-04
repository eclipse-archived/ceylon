package com.redhat.ceylon.tools.classpath;

import java.io.File;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Prints a classpath suitable for passing to Java tools to run a given Ceylon module")
@Description("Will print a classpath for a given Ceylon module, suitable for use with Java tools to " +
        "run a given Ceylon module outside of the regular JBoss Modules container used in `ceylon run`.")
public class CeylonClasspathTool extends ModuleLoadingTool {

    private String moduleNameOptVersion;

    @Argument(argumentName="module", multiplicity = "1")
    public void setModule(String module) {
        this.moduleNameOptVersion = module;
    }

    @Override
    public void run() throws Exception {
        setSystemProperties();

        String module = ModuleUtil.moduleName(moduleNameOptVersion);
        String version = ModuleUtil.moduleVersion(moduleNameOptVersion);
        loadModule(module, version);

        errorOnConflictingModule(module, version);
        
        boolean once = true;
        for(ArtifactResult entry : this.loadedModules.values()){
            // since we even add missing modules there to avoid seeing them twice, let's skip them now
            if(entry == null)
                continue;
            File file = entry.artifact();
            if(file == null)
                continue;
            if(once)
                once = false;
            else
                append(File.pathSeparator);
            append(file.getAbsolutePath());
        }
        flush();
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
    }
}
