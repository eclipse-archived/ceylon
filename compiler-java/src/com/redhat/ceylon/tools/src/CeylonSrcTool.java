package com.redhat.ceylon.tools.src;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.tools.ModuleSpec;
import com.redhat.ceylon.tools.ModuleSpec.Option;

@Summary("Fetches source archives from a repository and extracts their contents into a source directory")
@Description("Fetches the source archive of the given `module` from the " +
		"first configured repository to contain the module and extracts " +
		"the source code into the output source directory. Multiple modules " +
		"can be given.\n" +
		"\n" +
		"This tool is especially useful for working with example projects.")
@RemainingSections("## Examples\n" +
		"\n" +
		"A typical workflow might be:\n" +
		"\n" +
		"    mkdir my-project\n" +
		"    cd my-project\n" +
		"    ceylon src org.example.foo\n" +
		"    ceylon compile org.example.foo\n" +
		"    ceylon run org.example.foo\n" +
		"")
public class CeylonSrcTool implements Tool {
    
    private String src = "source";
    
    private String sysrep;
    private List<String> rep;

    private List<ModuleSpec> modules;
    
    /*
    private String user;
    
    private String pass;
    */
    
    @Description("The output source directory (default: `./source`)")
    @OptionArgument(argumentName="dir")
    public void setSrc(String directory) {
        this.src = directory;
    }
    
    @Description("A module repository containing source archives " +
    		"(default: `./modules`, http://modules.ceylon-lang.org")
    @OptionArgument(argumentName="repo")
    public void setRep(List<String> rep) {
        this.rep = rep;
    }
    
    @OptionArgument(longName="sysrep", argumentName="url")
    @Description("Specifies the system repository containing essential modules. " +
            "(default: `$CEYLON_HOME/repo`)")
    public void setSystemRepository(String systemRepo) {
        this.sysrep = systemRepo;
    }
    
    /*
    @Description("Sets the user name for use with an authenticated repository" +
            "(no default).")
    @OptionArgument(argumentName="user")
    public void setUser(String user) {
        this.user = user;
    }
    
    @Description("Sets the password for use with an authenticated output repository" +
            "(no default).")
    @OptionArgument(argumentName="secret")
    public void setPass(String pass) {
        this.pass = pass;
    }
    */
    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules, Option.VERSION_REQUIRED));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }
    
    @Override
    public void run() throws Exception {
        RepositoryManager rm = CeylonUtils.repoManager()
                .systemRepo(sysrep)
                .userRepos(rep).buildManager();
        for (ModuleSpec module : modules) {
            ArtifactResult srcArchive = rm.getArtifactResult(new ArtifactContext(module.getName(), module.getVersion(), ArtifactContext.SRC));
            if (srcArchive == null) {
                System.err.println(Tools.progName() + " src: " + 
                        CeylonSrcMessages.msg("module.not.found", 
                                module, rm.getRepositoriesDisplayString()));
                continue;
            }
            extractArchive(srcArchive, new File(src));
        }
    }

    private void extractArchive(ArtifactResult srcArchive, File dir) throws IOException {
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new RuntimeException(CeylonSrcMessages.msg("not.dir.src.dir", dir.getAbsolutePath()));
            }
        } else {
            if (!dir.mkdirs()) {
                throw new RuntimeException(CeylonSrcMessages.msg("unable.create.src.dir", dir.getAbsolutePath()));
            }
        }
        
        ZipFile zf = new ZipFile(srcArchive.artifact());
        try {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                try {
                    File out = new File(dir, entryName);
                    if (entry.isDirectory()) {
                        mkdirs(out);
                        continue;
                    }
                    mkdirs(out.getParentFile());
                    InputStream zipIn = zf.getInputStream(entry);
                    try {
                        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(out));
                        try {
                            IOUtils.copyStream(zipIn, fileOut);
                        } finally {
                            fileOut.close();
                        }
                    } finally {
                        zipIn.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(CeylonSrcMessages.msg("unable.extract.entry", entryName, srcArchive.artifact().getAbsolutePath()), e);
                }
            }
        } finally {
            zf.close();
        }
    }
    
    private File mkdirs(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException(CeylonSrcMessages.msg("unable.create.dir", dir.getAbsolutePath()));
        }
        return dir;
    }

}
