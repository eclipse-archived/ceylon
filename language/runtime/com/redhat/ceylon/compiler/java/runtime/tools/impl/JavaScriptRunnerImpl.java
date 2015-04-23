package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;
import com.redhat.ceylon.compiler.java.runtime.tools.RunnerOptions;
import com.redhat.ceylon.compiler.js.CeylonRunJsTool;

public class JavaScriptRunnerImpl implements Runner {

    private CeylonRunJsTool tool;
    private String moduleSpec;

    public JavaScriptRunnerImpl(final RunnerOptions options, String module, String version) {
        tool = new CeylonRunJsTool() {
            @Override
            protected void customizeDependencies(List<File> localRepos, RepositoryManager repoman) throws IOException {
                for (Map.Entry<String,String> extraModule : options.getExtraModules().entrySet()) {
                    String modName = extraModule.getKey();
                    String modVersion = extraModule.getValue();
                    File artifact = getArtifact(repoman, modName, modVersion, true);
                    localRepos.add(getRepoDir(modName, artifact));
                    loadDependencies(localRepos, repoman, artifact);
                }
            };
        };

        moduleSpec = ModuleUtil.makeModuleName(module, version);
        tool.setThrowOnError(true);
        tool.setModuleVersion(moduleSpec);
        try {
            tool.setRepositoryAsStrings(options.getUserRepositories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tool.setNoDefRepos(options.isNoDefaultRepositories());
        tool.setSystemRepository(options.getSystemRepository());
        tool.setOffline(options.isOffline());
        if(options.getRun() != null)
            tool.setRun(options.getRun());
        if(options.isVerbose())
            tool.setVerbose(options.getVerboseCategory());
    }

    @Override
    public void run(String... arguments) {
        try {
            tool.setArgs(Arrays.asList(arguments));
            tool.run();
        } catch (Exception e) {
            throw new RuntimeException("Exception during run of "+moduleSpec, e);
        }
    }

    @Override
    public void cleanup() {
        // nothing to do
    }
}
