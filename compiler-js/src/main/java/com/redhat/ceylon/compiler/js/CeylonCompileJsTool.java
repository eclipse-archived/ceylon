package com.redhat.ceylon.compiler.js;

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.compiler.Options;

@Summary("Compiles Ceylon source code to JavaScript and directly produces " +
        "module and source archives in a module repository")
public class CeylonCompileJsTool implements Tool {

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
    private List<String> module = Collections.emptyList();

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

    @Option(longName="no-module")
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
    @Description("Equivalent to `--no-indent` `--no-comments`")
    public void setCompact(boolean compact) {
        this.setNoIndent(compact);
        this.setNoComments(compact);
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

    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository" +
            "(no default).")
    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository" +
            "(no default).")
    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getRepos() {
        return repos;
    }

    @OptionArgument(longName="rep", argumentName="url")
    @Description("Specifies a module repository containing dependencies. Can be specified multiple times. " +
            "(default: `modules`, `~/.ceylon/repo`, http://modules.ceylon-lang.org)")
    public void setRepos(List<String> repos) {
        this.repos = repos;
    }

    public List<String> getSrc() {
        return src;
    }

    @OptionArgument(longName="src", argumentName="dirs")
    @Description("Path to source files (default: `./source`). " +
    		"Can be specified multiple times; you can also specify several " +
    		"paths separated by your operating system's `PATH` separator")
    public void setSrc(List<String> src) {
        this.src = src;
    }

    public String getOut() {
        return out;
    }

    @OptionArgument(argumentName="url")
    @Description("Specifies the output module repository (which must be publishable). " +
            "(default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }

    @Argument(argumentName="moduleOrFile", multiplicity="+")
    public void setModule(List<String> moduleOrFile) {
        this.module = moduleOrFile;
    }

    @Override
    public void run() throws Exception {
        final Options opts = new Options(getRepos(), getSrc(), null, getOut(), getUser(), getPass(), isOptimize(),
                isModulify(), isIndent(), isComments(), isVerbose(), isProfile(), false, true);
        Main.run(opts, module);
    }

}
