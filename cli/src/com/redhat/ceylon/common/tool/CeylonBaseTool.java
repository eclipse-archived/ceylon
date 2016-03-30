package com.redhat.ceylon.common.tool;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tools.CeylonTool;

public abstract class CeylonBaseTool implements Tool {
    protected File cwd;
    public String verbose;

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
    
    @Option(shortName='d')
    @OptionArgument(argumentName = "flags")
    @Description("Produce verbose output. " +
            "If no `flags` are given then be verbose about everything, " +
            "otherwise just be verbose about the flags which are present. " +
            "Allowed flags include: `all`, `loader`.")
    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }
    
    protected File validCwd() {
        return (cwd != null) ? cwd : new File(".");
    }

    protected List<File> applyCwd(List<File> files) {
        return FileUtil.applyCwd(getCwd(), files);
    }

    protected File applyCwd(File file) {
        return FileUtil.applyCwd(getCwd(), file);
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        // Empty default implementation for simple tools that don't need initialization
    }
}
