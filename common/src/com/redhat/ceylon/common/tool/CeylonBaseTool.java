package com.redhat.ceylon.common.tool;

import java.io.File;
import java.util.List;

public abstract class CeylonBaseTool implements Tool {
    protected File cwd;
    public String verbose;
    protected List<String> defines;

    public File getCwd() {
        return cwd;
    }
    
    @OptionArgument(longName="cwd", argumentName="dir")
    @Description("Specifies the current working directory for this tool. " +
            "(default: the directory where the tool is run from)")
    public void setCwd(File cwd) {
        this.cwd = cwd;
    }
    
    public String getVerbose() {
        return verbose;
    }
    
    @Option
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`.")
    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }
    
    @OptionArgument(shortName='D', argumentName = "key>=<value")
    @Description("Set a system property")
    public void setDefine(List<String> defines) {
        this.defines = defines;
    }
    
    protected void setSystemProperties() {
        if (defines != null) {
            for (String prop : defines) {
                int p = prop.indexOf('=');
                if (p > 0) {
                    String key = prop.substring(0, p);
                    String val = prop.substring(p + 1);
                    System.setProperty(key, val);
                }
            }
        }
    }

}
