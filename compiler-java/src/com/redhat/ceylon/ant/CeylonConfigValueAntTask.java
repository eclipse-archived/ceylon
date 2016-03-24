package com.redhat.ceylon.ant;

import org.apache.tools.ant.BuildException;

import com.redhat.ceylon.common.config.CeylonConfig;

public class CeylonConfigValueAntTask extends CeylonConfigBaseTask {
    
    private String key;
    private String property;
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        if (key == null) {
            throw new BuildException("'key' is a required attribute for 'ceylon-config-value'");
        }
        if (property == null) {
            throw new BuildException("'property' is a required attribute for 'ceylon-config-value'");
        }
        CeylonConfig config = getConfig();
        String[] values = config.getOptionValues(key);
        setConfigValueAsProperty(values, property);
    }

}
