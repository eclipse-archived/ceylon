package com.redhat.ceylon.tools.importjar;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.tools.Plugin;
import com.redhat.ceylon.tools.annotation.OptionArgument;
import com.redhat.ceylon.tools.annotation.Argument;
import com.redhat.ceylon.tools.annotation.Option;
import com.redhat.ceylon.tools.annotation.Summary;

@Summary("Imports a jar file into a Ceylon module repository")
public class ImportJarTool implements Plugin {

    public ImportJarTool() {
    }
    
    private String user;
    private String pass;
    private boolean d;
    private String out;
    private String module;
    private File jar;
    private boolean verbose;

    public String getUser() {
        return user;
    }

    @OptionArgument(argumentName="name")
    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    @OptionArgument(argumentName="secret")
    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isVerbose() {
        return verbose;
    }
    
    @Option
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isD() {
        return d;
    }
    
    @Option
    public void setD(boolean d) {
        this.d = d;
    }

    public String getOut() {
        return out;
    }

    @OptionArgument(argumentName="dir-or-url")
    public void setOut(String out) {
        this.out = out;
    }
    
    @Argument(argumentName="module", multiplicity="{1}", order=0)
    public void setModuleSpec(String module) {
        this.module = module;
    }
    
    @Argument(argumentName="jar-file", multiplicity="{1}", order=1)
    public void setFile(File jar) {
        this.jar = jar;
    }


    @Override
    public int run() {
        ImportJar importJar = new ImportJar(module, out, user, pass, jar.getPath(), verbose);
        importJar.publish();
        return 0;
    }
    
    

}
