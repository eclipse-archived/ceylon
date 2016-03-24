package com.redhat.ceylon.compiler.java.runtime.tools;

import java.util.HashMap;
import java.util.Map;

public class RunnerOptions extends Options {
    private Map<String,String> extraModules = new HashMap<String,String>();

    public Map<String, String> getExtraModules() {
        return extraModules;
    }

    public void setExtraModules(Map<String, String> extraModules) {
        this.extraModules = extraModules;
    }

    public void addExtraModule(String module, String version) {
        this.extraModules.put(module, version);
    }

}
