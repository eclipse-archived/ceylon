package com.redhat.ceylon.common.tool;

import java.io.File;

public abstract class CeylonBaseTool implements Tool {
    protected File cwd;
    public String verbose;

    public String getVerbose() {
        return verbose;
    }
    
    @OptionArgument(longName="cwd", argumentName="dir")
    @Description("Specifies the current working directory for this tool. " +
            "(default: the directory where the tool is run from)")
    public void setCwd(File cwd) {
        this.cwd = cwd;
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

}
