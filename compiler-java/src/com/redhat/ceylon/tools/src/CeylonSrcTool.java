package com.redhat.ceylon.tools.src;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.model.cmr.ArtifactResult;

@Summary("Fetches source archives from a repository and extracts their contents into a source directory")
@Description("Fetches any sources, resources, documentation and scripts " +
        "that can be found for given `module` from the " +
		"first configured repository to contain the module and extracts " +
		"the them into their respective output directories. Multiple modules " +
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
    
    private File src = DefaultToolOptions.getCompilerSourceDirs().get(0);
    private File resource = DefaultToolOptions.getCompilerResourceDirs().get(0);
    private File doc = DefaultToolOptions.getCompilerDocDirs().get(0);
    private File script = DefaultToolOptions.getCompilerScriptDirs().get(0);
    private String resourceRoot = DefaultToolOptions.getCompilerResourceRootName();
    
    private List<ModuleSpec> modules;
    
    public CeylonSrcTool() {
        super(CeylonSrcMessages.RESOURCE_BUNDLE);
    }
    
    @OptionArgument(shortName='s', longName="src", argumentName="dir")
    @Description("The output source directory (default: `./source`)")
    public void setSrc(File directory) {
        this.src = directory;
    }
    
    @OptionArgument(longName="source", argumentName="dir")
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSource(File source) {
        setSrc(source);
    }
    
    @OptionArgument(shortName='r', longName="resource", argumentName="dir")
    @Description("The output resource directory (default: `./resource`)")
    public void setResource(File resource) {
        this.resource = resource;
    }
    
    @OptionArgument(longName="doc", argumentName="dirs")
    @Description("The output doc directory (default: `./doc`)")
    public void setDocFolders(File doc) {
        this.doc = doc;
    }
    
    @OptionArgument(shortName='x', longName="script", argumentName="dir")
    @Description("The output script directory (default: `./script`)")
    public void setScriptFolders(File script) {
        this.script = script;
    }

    @OptionArgument(shortName='R', argumentName="folder-name")
    @Description("Sets the special resource folder name whose files will " +
            "end up in the root of the resulting module CAR file (default: ROOT).")
    public void setResourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }
    
    @Argument(argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Override
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public void run() throws Exception {
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
            msg("retrieving.module", module).newline();
            ArtifactContext allArtifacts = new ArtifactContext(module.getName(), version, ArtifactContext.SRC, ArtifactContext.RESOURCES, ArtifactContext.DOCS, ArtifactContext.SCRIPTS_ZIPPED);
            List<ArtifactResult> results = getRepositoryManager().getArtifactResults(allArtifacts);
            if (results == null) {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                errorAppend(err);
                errorNewline();
                continue;
            }
            String modFolder = module.getName().replace('.', File.separatorChar);
            boolean hasSources = false;
            for (ArtifactResult result : results) {
                String suffix = ArtifactContext.getSuffixFromFilename(result.artifact().getName());
                if (ArtifactContext.SRC.equals(suffix)) {
                    append("    ").msg("extracting.sources").newline();
                    extractArchive(result, applyCwd(src), "source");
                    hasSources = true;
                } else if (ArtifactContext.SCRIPTS_ZIPPED.equals(suffix)) {
                    append("    ").msg("extracting.scripts").newline();
                    extractArchive(result, new File(applyCwd(script), modFolder), "script");
                } else if (ArtifactContext.RESOURCES.equals(suffix)) {
                    append("    ").msg("extracting.resources").newline();
                    copyResources(result, applyCwd(resource));
                } else if (ArtifactContext.DOCS.equals(suffix)) {
                    append("    ").msg("extracting.docs").newline();
                    copyFiles(result, "doc", new File(applyCwd(doc), modFolder), "doc");
                }
            }
            if (!hasSources) {
                msg("no.sources.found", module).newline();
            }
        }
    }

    private void extractArchive(ArtifactResult result, File dir, String name) throws IOException {
        try{
            IOUtils.extractArchive(result.artifact(), dir);
        }catch(IOUtils.UnzipException x){
            switch(x.failure){
            case CannotCreateDestination:
                throw new RuntimeException(CeylonSrcMessages.msg("unable.create.output.dir", name, x.dir));
            case CopyError:
                throw new RuntimeException(CeylonSrcMessages.msg("unable.extract.entry", x.entryName, result.artifact().getAbsolutePath()), x.getCause());
            case DestinationNotDirectory:
                throw new RuntimeException(CeylonSrcMessages.msg("not.dir.output.dir", name, x.dir));
            default:
                throw x;
            }
        }
    }

    private void copyFiles(ArtifactResult result, String fromSubDir, File destDir, String name) {
        File fromDir = result.artifact();
        if (fromSubDir != null) {
            fromDir = new File(fromDir, fromSubDir);
        }
        if (!fromDir.isDirectory()) {
            throw new RuntimeException(CeylonSrcMessages.msg("not.dir.input.dir", name, destDir));
        }
        if (!destDir.exists() && !destDir.mkdirs()) {
            throw new RuntimeException(CeylonSrcMessages.msg("unable.create.output.dir", name, destDir));
        }
        if (!destDir.isDirectory()) {
            throw new RuntimeException(CeylonSrcMessages.msg("not.dir.output.dir", name, destDir));
        }
        try {
            FileUtil.copyAll(fromDir, destDir);
        } catch (IOException ex) {
            throw new RuntimeException(CeylonSrcMessages.msg("unable.copy", name, fromDir), ex);
        }
    }

    private void copyResources(ArtifactResult result, File destDir) {
        String[] parts = result.name().split("\\.");
        // First we copy the main resource files
        copyFiles(result, parts[0], new File(destDir, parts[0]), "resource");
        // And now any root resources if they exist
        String modFolder = result.name().replace('.', File.separatorChar);
        File destRoot = new File(new File(destDir, modFolder), resourceRoot);
        for (File f : result.artifact().listFiles()) {
            if (!f.getName().equals(parts[0])) {
                // We have found a root resource
                try {
                    FileUtil.copyAll(f, destRoot);
                } catch (IOException ex) {
                    throw new RuntimeException(CeylonSrcMessages.msg("unable.copy", "resource", f), ex);
                }
            }
        }
    }
}
