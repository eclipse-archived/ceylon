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
    
    public static final String DOCSECTION_CONFIG_COMPILER =
            "## Configuration file" +
            "\n\n" +
            "The compile tool accepts the following options from the Ceylon configuration file: " +
            "`defaults.offline`, `defaults.encoding`, `compiler.source`, `compiler.resource` and `repositories` " +
            "(the equivalent options on the command line always have precedence).";
    
    public static final String DOCSECTION_OUTPUT =
            "## Output repositories" +
            "\n\n" +
            "Output repositories specified with the `--out` option can be file paths, HTTP urls " +
            "to remote servers or can be names of repositories when prepended with a `+` symbol. " +
            "These names refer to repositories defined in the configuration file or can be any of " +
            "the following predefined names `+SYSTEM`, `+CACHE`, `+LOCAL`, `+USER` or `+REMOTE`. " +
            "For more information see http://ceylon-lang.org/documentation/1.0/reference/tool/config";

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
