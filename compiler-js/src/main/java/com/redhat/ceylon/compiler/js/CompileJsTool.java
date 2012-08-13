package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.Summary;

@Summary("Compiles Ceylon source code to JavaScript and directly produces " +
        "module and source archives in a module repository")
public class CompileJsTool implements Plugin {

    private boolean profile = false;
    
    private boolean optimize = false;
    
    private boolean modulify = true;
    
    private boolean indent = true;
    
    private boolean comments = false;
    
    private boolean verbose = false;
    
    private String user = null;
    
    private String pass = null;
    
    private List<String> repos = Collections.emptyList();
    
    private List<String> src = Collections.singletonList("source");
    
    private String out = "modules";
    
    public boolean isProfile() {
        return profile;
    }
    
    @Option
    @Description("Time the compilation phases (results are printed to standard error)")
    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    public boolean isOptimize() {
        return optimize;
    }

    @Option
    @Description("Create prototype-style JS code")
    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean isModulify() {
        return modulify;
    }

    @Option(longName="nomodule")
    @Description("Do **not** wrap generated code as CommonJS module")
    public void setNoModulify(boolean nomodulify) {
        this.modulify = !nomodulify;
    }

    public boolean isIndent() {
        return indent;
    }

    @Option
    @Description("Do **not** indent code")
    public void setNoIndent(boolean noindent) {
        this.indent = !noindent;
    }

    @Option
    @Description("Equivalent to `--noindent` `--nocomments`")
    public void setNoCompact(boolean nocompact) {
        this.setNoIndent(nocompact);
        this.setNoComments(nocompact);
    }

    public boolean isComments() {
        return comments;
    }

    @Option
    @Description("Do **not** generate any comments")
    public void setNoComments(boolean nocomments) {
        this.comments = !nocomments;
    }

    public boolean isVerbose() {
        return verbose;
    }

    @Option
    @Description("Print messages while compiling")
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getUser() {
        return user;
    }

    @OptionArgument
    @Description("User name for output repository (HTTP only)")
    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    @OptionArgument
    @Description("Password for output repository (HTTP only)")
    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRepos() {
        return repos;
    }

    @OptionArgument(longName="rep")
    @Description("Module repository (default: `./modules`). Can be specified multiple times.")
    public void setRepos(List<String> repos) {
        this.repos = repos;
    }

    public List<String> getSrc() {
        return src;
    }

    @OptionArgument
    @Description("Path to source files (default: `./source`). " +
    		"Can be specified multiple times; you can also specify several " +
    		"paths separated by your operating system's `PATH` separator")
    public void setSrc(List<String> src) {
        this.src = src;
    }

    public String getOut() {
        return out;
    }

    @OptionArgument
    @Description("Output module repository (default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }

    @Override
    public void run() throws Exception {
        // For now, let's just create another command line and pass that to 
        // Main.main(). Long term we need to this class and Main need to be 
        // merged into one.
        ArrayList<String> argList = new ArrayList<String>();
        
        if (!comments) {
            argList.add("-nocomments");
        }
        
        if (!indent) {
            argList.add("-noindent");
        }
        
        if (!modulify) {
            argList.add("-nomodel");
        }
        
        if (optimize) {
            argList.add("-optimize");
        }
        
        if (profile) {
            argList.add("-profile");
        }
        
        if (verbose) {
            argList.add("-verbose");
        }
        
        if (user != null) {
            argList.add("-user");
            argList.add(user);
        }
        
        if (pass != null) {
            argList.add("-pass");
            argList.add(pass);
        }
        
        argList.add("-out");
        argList.add(out);
        
        for (String rep : getRepos()) {
            argList.add("-rep");
            argList.add(rep);
        }
        
        for (String src : getSrc()) {
            argList.add("-src");
            argList.add(src);
        }
        
        Main.main(argList.toArray(new String[argList.size()]));
    }

}
