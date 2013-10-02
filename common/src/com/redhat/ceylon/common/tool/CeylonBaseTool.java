package com.redhat.ceylon.common.tool;

public abstract class CeylonBaseTool implements Tool {
    public String verbose;

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

}
