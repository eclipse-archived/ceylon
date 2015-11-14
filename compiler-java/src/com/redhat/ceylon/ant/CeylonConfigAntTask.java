package com.redhat.ceylon.ant;

import org.apache.tools.ant.BuildException;

import com.redhat.ceylon.common.config.CeylonConfig;

public class CeylonConfigAntTask extends CeylonConfigBaseTask {
    
    private String prefix;
    private String infix;
    
    public String getPrefix() {
        return (prefix != null) ? prefix : "CEYLON.";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getInfix() {
        return (infix != null) ? infix : ",";
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        CeylonConfig config = getConfig();
        for (String key : config.getOptionNames(null)) {
            String[] values = config.getOptionValues(key);
            setConfigValueAsProperty(values, getPrefix() + key);
        }
    }

}
