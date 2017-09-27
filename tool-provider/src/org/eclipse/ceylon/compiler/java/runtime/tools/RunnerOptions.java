package org.eclipse.ceylon.compiler.java.runtime.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.common.config.CeylonConfig;

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

    /**
     * Create a new <code>RunnerOptions</code> object initialized with the
     * settings read from the default Ceylon configuration
     * @return An initialized <code>RunnerOptions</code> object
     */
    public static RunnerOptions fromConfig() {
        return fromConfig(CeylonConfig.get());
    }

    /**
     * Create a new <code>RunnerOptions</code> object initialized with the
     * settings read from the given configuration
     * @param config The <code>CeylonConfig</code> to take the settings from
     * @return An initialized <code>RunnerOptions</code> object
     */
    public static RunnerOptions fromConfig(CeylonConfig config) {
        RunnerOptions options = new RunnerOptions();
        options.mapOptions(config);
        return options;
    }

}
