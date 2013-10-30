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
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.tools.ModuleSpec;

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
public class CeylonSrcTool extends RepoUsingTool {
    
    private String src = DefaultToolOptions.getCompilerSourceDirs().get(0).getPath();
    
    private List<ModuleSpec> modules;
    
    public CeylonSrcTool() {
        super(CeylonSrcMessages.RESOURCE_BUNDLE);
    }
    
    @Description("The output source directory (default: `./source`)")
    @OptionArgument(argumentName="dir")
    public void setSrc(String directory) {
        this.src = directory;
    }
    
    @OptionArgument(longName="source", argumentName="dirs")
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSource(String source) {
        setSrc(source);
    }
    
    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }
    
    @Override
    public void run() throws Exception {
        setSystemProperties();
        // First check if all the arguments point to source archives
        for (ModuleSpec module : modules) {
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                if (checkModuleVersionsOrShowSuggestions(getRepositoryManager(), module.getName(), null, ModuleQuery.Type.SRC, null, null) == null) {
                    return;
                }
            }
        }
        // If all are correct we unpack them
        for (ModuleSpec module : modules) {
            String version = module.getVersion();
            if (module != ModuleSpec.DEFAULT_MODULE && !module.isVersioned()) {
                version = checkModuleVersionsOrShowSuggestions(getRepositoryManager(), module.getName(), null, ModuleQuery.Type.SRC, null, null);
            }
            ArtifactResult srcArchive = getRepositoryManager().getArtifactResult(new ArtifactContext(module.getName(), version, ArtifactContext.SRC));
            if (srcArchive == null) {
                errorMsg("module.not.found", module, getRepositoryManager().getRepositoriesDisplayString());
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
