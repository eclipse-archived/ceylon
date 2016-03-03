package com.redhat.ceylon.tools.jigsaw;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.SortedSet;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

@Summary("Tools to interop with Java 9 (Jigsaw) modules")
@Description("There is currently one mode of action:\n"+
        "\n"+
        "- `create-mlib`: Generates an mlib folder suitable for Java 9 tools to run a given Ceylon module\n"+
        "\n"+
        "The list of modules specified can have their versions set, but if missing we will try to find the\n"+
        "version from the current source path. If the list of modules is omitted, we will use the list of\n"+
        "modules found in the current source path."
)
public class CeylonJigsawTool extends ModuleLoadingTool {

    private String moduleNameOptVersion;
    private boolean force;
    private Mode mode;
	private File out = new File("mlib");

    public static enum Mode {
        create_mlib;
    }

    @Argument(order = 0, argumentName="mode", multiplicity = "1")
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    @Argument(order = 1, argumentName="module", multiplicity = "1")
    public void setModule(String module) {
        this.moduleNameOptVersion = module;
    }

    @Description("Folder in which to place the resulting jars (defaults to `mlib`).")
    @OptionArgument(shortName = 'o', argumentName="dir")
    public void setOut(File out) {
        this.out = out;
    }

    @Option(longName="force")
    @Description("Force generation of mlib folder with multiple versions of the same module.")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public void run() throws Exception {
        String module = ModuleUtil.moduleName(moduleNameOptVersion);
        String version = checkModuleVersionsOrShowSuggestions(
                getRepositoryManager(),
                module,
                ModuleUtil.moduleVersion(moduleNameOptVersion),
                ModuleQuery.Type.JVM,
                Versions.JVM_BINARY_MAJOR_VERSION,
                Versions.JVM_BINARY_MINOR_VERSION,
                null, null, // JS binary but don't care since JVM
                null);
        if(version == null)
            return;
        loadModule(module, version);

        if(!force)
            errorOnConflictingModule(module, version);
        
        if(!out.exists()){
        	if(!out.mkdirs()){
    	        throw new ToolUsageError(Messages.msg(bundle, "jigsaw.folder.error", out));
        	}
        }
        for(ArtifactResult entry : this.loadedModules.values()){
            // since we even add missing modules there to avoid seeing them twice, let's skip them now
            if(entry == null)
                continue;
            File file = entry.artifact();
            if(file == null)
                continue;
            // on duplicate, let's only keep the last version
            SortedSet<String> versions = loadedModuleVersions.get(entry.name());
            if(version != null && !versions.isEmpty() && entry.version() != null && !entry.version().equals(versions.last()))
                continue;
            append(file.getAbsolutePath());
            newline();
            
            String name = file.getName();
            if(name.endsWith(".car"))
            	name = name.substring(0, name.length()-4) + ".jar";
			Files.copy(file.toPath(), new File(out, name).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        flush();
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
    }
}
