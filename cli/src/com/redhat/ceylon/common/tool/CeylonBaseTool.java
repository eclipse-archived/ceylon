package com.redhat.ceylon.common.tool;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    
    protected boolean isVerbose(){
        return isVerbose("all");
    }
    
    protected boolean isVerbose(String category){
        if(verbose == null)
            return false;
        // all?
        if(verbose.isEmpty() || verbose.equals("all"))
            return true;
        if(category == null)
            return false;
        for(String cat : verbose.split(",")){
            if(cat.equals(category))
                return true;
        }
        return false;
    }
    
    protected Set<String> getVerboseCategories(String... morecats) {
        Set<String> categories = new TreeSet<String>();
        categories.add("all");
        categories.add("loader");
        for (String cat : morecats) {
            categories.add(cat);
        }
        return categories;
    }
    
    private void validateVerbose(String verbose, Set<String> categories) {
        if (verbose != null && !verbose.isEmpty()) {
            for (String cat : verbose.split(",")){
                if (!categories.contains(cat)) {
                    String cats = categories.toString();
                    cats = cats.substring(1, cats.length() - 1);
                    throw new IllegalArgumentException("Unknown verbose category '" + cat + "', should be one of: " + cats);
                }
            }
        }
    }
    
    protected void validateVerbose(String verbose) {
        validateVerbose(verbose, getVerboseCategories());
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
        validateVerbose(verbose);
    }
}
