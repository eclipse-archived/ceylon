package com.redhat.ceylon.ant;

import java.io.File;

import org.apache.tools.ant.types.Commandline;

/**
 * Ant task wrapping the {@code ceylon import-jar} tool
 * @author tom
 */
public class CeylonImportJarAntTask extends CeylonAntTask {

    public CeylonImportJarAntTask() {
        super("import-jar");
    }

    @Override
    protected String getFailMessage() {
        return "import-jar failed";
    }
    
    private String out;
    
    private String pass;
    
    private String user;
    
    private String verbose = "false";
    
    private String module;
    
    private String jar;

    protected String getOut() {
        if (this.out == null) {
            return new File(getProject().getBaseDir(), "modules").getPath();
        }
        return this.out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVerbose() {
        return verbose;
    }

    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    @Override
    protected void completeCommandline(Commandline cmd) {
        appendVerboseOption(cmd, getVerbose());
        appendUserOption(cmd, getUser());
        appendPassOption(cmd, getPass());
        
        if (out != null) {
            cmd.createArgument().setValue("--out=" + getOut());
        }
        
        if (module != null) {
            cmd.createArgument().setValue(getModule());
        }
        
        if (jar != null) {
            cmd.createArgument().setValue(getJar());
        }
    }

}
