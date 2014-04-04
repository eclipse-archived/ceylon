package com.redhat.ceylon.cmr.ceylon;

import java.util.ResourceBundle;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;

public abstract class OutputRepoUsingTool extends RepoUsingTool {
    protected String out;
    protected String user;
    protected String pass;

    private RepositoryManager outrm;
    
    public OutputRepoUsingTool(ResourceBundle bundle) {
        super(bundle);
    }
    
    @OptionArgument(argumentName="url")
    @Description("Specifies the output module repository (which must be publishable). " +
            "(default: `./modules`)")
    public void setOut(String out) {
        this.out = out;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository" +
            "(no default).")
    public void setUser(String user) {
        this.user = user;
    }
    
    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository" +
            "(no default).")
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    @Override
    protected CeylonUtils.CeylonRepoManagerBuilder createRepositoryManagerBuilder() {
        return super.createRepositoryManagerBuilder()
                .outRepo(out)
                .user(user)
                .password(pass);
    }
    
    protected synchronized RepositoryManager getOutputRepositoryManager() {
        if (outrm == null) {
            CeylonUtils.CeylonRepoManagerBuilder rmb = createRepositoryManagerBuilder();
            outrm = rmb.buildOutputManager();
        }
        return outrm;
    }
}
