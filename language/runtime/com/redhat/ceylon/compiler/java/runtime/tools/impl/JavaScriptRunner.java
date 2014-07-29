package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.util.Arrays;

import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;
import com.redhat.ceylon.compiler.java.runtime.tools.RuntimeOptions;
import com.redhat.ceylon.compiler.js.CeylonRunJsTool;

public class JavaScriptRunner implements Runner {

    private CeylonRunJsTool tool;
    private String moduleSpec;

    public JavaScriptRunner(final RuntimeOptions options, String module, String version) {
        tool = new CeylonRunJsTool();
        moduleSpec = ModuleUtil.makeModuleName(module, version);
        tool.setThrowOnError(true);
        tool.setModuleVersion(moduleSpec);
        try {
            tool.setRepositoryAsStrings(options.getUserRepositories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tool.setSystemRepository(options.getSystemRepository());
        tool.setOffline(options.isOffline());
        if(options.isVerbose())
            tool.setVerbose("");
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
