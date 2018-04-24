/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.impl.IOUtils;
import org.eclipse.ceylon.cmr.impl.ShaSigner;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.OSUtil;
import org.eclipse.ceylon.common.config.DefaultToolOptions;
import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Description;
import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.OptionArgument;
import org.eclipse.ceylon.common.tool.ParsedBy;
import org.eclipse.ceylon.common.tool.RemainingSections;
import org.eclipse.ceylon.common.tool.StandardArgumentParsers;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tool.ToolError;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.common.tools.OutputRepoUsingTool;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.typechecker.model.Module;

@Summary("Manages Ceylon command-line plugins")
@Description(
        "There are four modes of action:\n"+
        "\n"+
        "- `list`: will list the scripts installed\n"+
        "- `install`: installs the scripts for the modules listed on the command line. The scripts must either be packed in a\n"+
        "repository, or found in the current script folders\n"+
        "- `uninstall`: uninstalls the scripts for the modules listed on the command line\n"+
        "- `pack`: packs the scripts for the modules listed on the command line and publishes them to the specified output repository\n"+
        "\n"+
        "The list of modules specified can have their versions set, but if missing we will try to find the\n"+
        "version from the current source path. If the list of modules is omitted, we will use the list of\n"+
        "modules found in the current source path."
)
@RemainingSections(
"##EXAMPLE\n" +
"\n" +
"The following would list the plugins currently installed:\n" +
"\n" +
"    ceylon plugin list\n\n"+
"The following would install the ceylon.build plugins:\n" +
"\n" +
"    ceylon plugin install ceylon.build.engine\n"
)
public class CeylonPluginTool extends OutputRepoUsingTool {

    @SuppressWarnings("serial")
    public static class CeylonPluginException extends ToolError {
        public CeylonPluginException(String msgKey, Exception cause, Object... args) {
            super(CeylonPluginMessages.msg(msgKey, args), cause);
        }
        public CeylonPluginException(String msgKey, Object... args) {
            super(CeylonPluginMessages.msg(msgKey, args));
        }
    }

    public static enum Mode {
        pack, list, install, uninstall;
    }

    private List<ModuleSpec> modules;
    private List<File> scriptFolders = DefaultToolOptions.getCompilerScriptDirs();
    private List<File> sourceFolders = DefaultToolOptions.getCompilerSourceDirs();
    private boolean force;
    private boolean system;
    private boolean local;
    private Mode mode;

    public CeylonPluginTool() {
        super(CeylonPluginMessages.RESOURCE_BUNDLE);
    }
    
    @Argument(order = 1, argumentName="module", multiplicity="*")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }
    
    @Argument(order = 0, argumentName="mode", multiplicity = "1")
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Option(shortName='f')
    @Description("Force installation even if a previous version exists")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Option
    @Description("Install to or uninstall from the system folder")
    public void setSystem(boolean system) {
        this.system = system;
    }

    @Option
    @Description("Install to or uninstall from a local folder")
    public void setLocal(boolean local) {
        this.local = local;
    }

    @OptionArgument(shortName='s', longName="src", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing Ceylon and/or Java/JavaScript source code (default: `./source`)")
    public void setSrcFolders(List<File> sourceFolders) {
        this.sourceFolders = sourceFolders;
    }
    
    @OptionArgument(longName="source", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSourceFolders(List<File> source) {
        setSrcFolders(source);
    }
    
    @OptionArgument(shortName='x', longName="script", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing your module documentation (default: `./script`)")
    public void setScriptFolders(List<File> scriptFolders) {
        this.scriptFolders = scriptFolders;
    }

    @Override
    protected List<File> getSourceDirs() {
        return sourceFolders;
    }

    @Override
    public void run() throws Exception {
        // make sure we have a list of modules to work on if required
        if(modules == null)
            modules = new ArrayList<ModuleSpec>();
        boolean errorIfMissing = true;
        if(mode != Mode.list){
            if(modules.isEmpty()){
                modules.addAll(getSourceModules(applyCwd(sourceFolders)));
                errorIfMissing = false;
            }
            if(modules.isEmpty()){
                throw new CeylonPluginException("error.no.module.specified", sourceFolders);
            }
        }

        boolean worked = false;
        switch(mode){
        case pack:
        {
            RepositoryManager outputRepositoryManager = getOutputRepositoryManager();
            for(ModuleSpec module : modules){
                worked |= addScripts(outputRepositoryManager, module, errorIfMissing);
            }
            break;
        }
        case install:
        {
            RepositoryManager repositoryManager = getRepositoryManager();
            for(ModuleSpec module : modules){
                worked |= installScripts(repositoryManager, module, errorIfMissing);
            }
            break;
        }
        case uninstall:
        {
            for(ModuleSpec module : modules){
                worked |= uninstallScripts(module, errorIfMissing);
            }
            break;
        }
        case list:
            listScripts();
            worked = true;
            break;
        }
        if(!worked)
            throw new CeylonPluginException("error.no.script.found");
        flush();
    }

    private void listScripts() throws IOException {
        Set<String> scripts = new TreeSet<String>();
        // Look in /etc/ceylon/bin/ and /etc/ceylon/bin/{moduleName}
        File systemDir = new File(FileUtil.getSystemConfigDir(), Constants.CEYLON_BIN_DIR);
        listScripts(systemDir, "system", scripts);
        // They are in ~/.ceylon/bin/ and ~/.ceylon/bin/{moduleName}
        File defUserDir = new File(FileUtil.getDefaultUserDir(), Constants.CEYLON_BIN_DIR);
        listScripts(defUserDir, "user", scripts);
        // They are in ./.ceylon/bin/ and ./.ceylon/bin/{moduleName}
        File localDir = applyCwd(new File(Constants.CEYLON_CONFIG_DIR, Constants.CEYLON_BIN_DIR));
        listScripts(localDir, "local", scripts);
        for (String script : scripts) {
            append(script);
            newline();
        }
    }

    private void listScripts(File dir, String location, Collection<String> scripts) throws IOException {
        File[] children = dir.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isDirectory()) {
                    File[] modfiles = child.listFiles();
                    if (modfiles != null) {
                        for (File f : modfiles) {
                            if (isScript(f) || isPlugin(f)) {
                                scripts.add(scriptName(f) + " (from " + location + " module '" + getModuleNameVersion(f) + "')");
                            }
                        }
                    }
                } else if (isScript(child) || isPlugin(child)) {
                    scripts.add(scriptName(child));
                }
            }
        }
    }

    // Retrieve plugin module name given it's installation folder
    // If possible return the version as well
    private String getModuleNameVersion(File f) {
        String mod = f.getParentFile().getName();
        if (mod.contains("-")) {
            // Is new plugin folder name format that includes a version
            mod = mod.replaceFirst("-", "/");
        } else {
            if (isPlugin(f)) {
                // Fall-back for determining plugin versions
                Properties pluginProperties = new Properties();
                try (InputStream is = new FileInputStream(f)) {
                    pluginProperties.load(is);
                    mod = pluginProperties.getProperty("module", mod);
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return mod;
    }

    private boolean isScript(File f) {
        if (f.isFile() && f.getName().startsWith("ceylon-") && f.canExecute()) {
            boolean isWinScript = OSUtil.isWindows() && f.getName().toLowerCase().endsWith(".bat");
            boolean isUnixScript = !OSUtil.isWindows() && !f.getName().toLowerCase().endsWith(".bat");
            return isWinScript || isUnixScript;
        }
        return false;
    }
    
    private boolean isPlugin(File f) {
        return (f.isFile() && f.getName().startsWith("ceylon-") && f.getName().endsWith(".plugin"));
    }
    
    private String scriptName(File f) {
        String name = f.getName().substring(7);
        if (name.toLowerCase().endsWith(".bat")) {
            name = name.substring(0, name.length() - 4);
        } else if (name.toLowerCase().endsWith(".plugin")) {
            name = name.substring(0, name.length() - 7);
        }
        return name;
    }
    
    private boolean installScripts(RepositoryManager repositoryManager, ModuleSpec module, boolean errorIfMissing) throws IOException {
        String version = module.getVersion();
        if((version == null || version.isEmpty()) && !module.getName().equals(Module.DEFAULT_MODULE_NAME)){
            version = checkModuleVersionsOrShowSuggestions(module.getName(), null, 
                    ModuleQuery.Type.ALL, null, null, null, null);
            if(version == null)
                return false;
        }
        module = new ModuleSpec(module.getNamespace(), module.getName(), version);
        
        File zipSource = null;
        List<File> existingScriptFolders = null;
        if(isSourceModule(module.getName(), version, applyCwd(sourceFolders))){
            // copy it directly from the source
            existingScriptFolders = findExistingScriptFolders(module.getName(), errorIfMissing);
            
            if(existingScriptFolders.isEmpty()){
                return false;
            }
        }else{
            // obtain it from the repo
            ArtifactContext context = new ArtifactContext(null, module.getName(), version, ArtifactContext.SCRIPTS_ZIPPED);
            ArtifactResult result = repositoryManager.getArtifactResult(context);
            if(result == null){
                String err = getModuleNotFoundErrorMessage(repositoryManager, module.getName(), version);
                errorAppend(err);
                errorNewline();
                return false;
            }
            zipSource = result.artifact();
        }

        File moduleScriptDir = getModuleScriptDir(module);
        ModuleSpec unversioned = new ModuleSpec(module.getNamespace(), module.getName(), "");
        if (existScripts(unversioned)) {
            if (force) {
                uninstallScripts(unversioned, false);
            } else {
                errorMsg("error.module.already.installed", module.getName(), moduleScriptDir);
                return false;
            }
        }
        if(!FileUtil.mkdirs(moduleScriptDir)){
            errorMsg("error.unable.create.dest.dir", moduleScriptDir);
            return false;
        }
        if(zipSource != null)
            extractScripts(zipSource, moduleScriptDir);
        else{
            copyScripts(existingScriptFolders, moduleScriptDir);
        }
        msg("success.installed", module.getName(), moduleScriptDir);
        newline();
        return true;
    }

    private boolean existScripts(ModuleSpec module) throws IOException {
        File moduleScriptDir = getModuleScriptDir(module);
        if (moduleScriptDir.exists()) {
            return true;
        }
        if (module.getVersion() == null || module.getVersion().isEmpty()) {
            // Find any new-style plugin directories (that is: 'module.name-*')
            final String modPrefix = module.getName() + "-";
            File[] files = moduleScriptDir.getParentFile().listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() && f.getName().startsWith(modPrefix);
                }
            });
            return files != null && files.length > 0;
        }
        return false;
    }

    private boolean uninstallScripts(ModuleSpec module, boolean errorIfMissing) throws IOException {
        boolean deleted = false;
        File moduleScriptDir = getModuleScriptDir(module);
        if (moduleScriptDir.exists()) {
            FileUtil.delete(moduleScriptDir);
            msg("success.uninstalled", module.getName(), moduleScriptDir);
            newline();
            deleted = true;
        }
        if (module.getVersion() == null || module.getVersion().isEmpty()) {
            // Delete any new-style plugin directories (that is: 'module.name-*')
            final String modPrefix = module.getName() + "-";
            File[] files = moduleScriptDir.getParentFile().listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() && f.getName().startsWith(modPrefix);
                }
            });
            if (files != null) {
                for (File f : files) {
                    FileUtil.delete(f);
                    msg("success.uninstalled", module.getName(), f);
                    newline();
                    deleted = true;
                }
            }
        }
        if (!deleted && errorIfMissing) {
            errorMsg("error.no.script.installed.for.module", module.getName(), moduleScriptDir);
        }
        return deleted;
    }

    private File getModuleScriptDir(ModuleSpec module) {
        File installDir;
        if (system) {
            // Put them in /etc/ceylon/bin/{moduleName} (or equivalent on Windows / MacOS)
            installDir = FileUtil.getSystemConfigDir();
        } if (local) {
            // Put them in ./.ceylon/bin/{moduleName}
            installDir = new File(Constants.CEYLON_CONFIG_DIR);
        } else {
            // Put them in ~/.ceylon/bin/{moduleName}
            installDir = FileUtil.getDefaultUserDir();
        }
        File binDir = new File(installDir, Constants.CEYLON_BIN_DIR);
        File moduleScriptDir;
        if (module.getVersion() != null && !module.getVersion().isEmpty()) {
            moduleScriptDir = new File(binDir, module.getName() + "-" + module.getVersion());
        } else {
            moduleScriptDir = new File(binDir, module.getName());
        }
        return moduleScriptDir;
    }

    private void extractScripts(File zip, File dir) throws IOException {
        try{
            IOUtils.extractArchive(zip, dir);
        }catch(IOUtils.UnzipException x){
            switch(x.failure){
            case CannotCreateDestination:
                throw new RuntimeException(CeylonPluginMessages.msg("error.unable.create.dest.dir", x.dir));
            case CopyError:
                throw new RuntimeException(CeylonPluginMessages.msg("error.unable.extract.entry", x.entryName, zip.getAbsolutePath()), x.getCause());
            case DestinationNotDirectory:
                throw new RuntimeException(CeylonPluginMessages.msg("error.not.dir.dest.dir", x.dir));
            default:
                throw x;
            }
        }
        // make all the files executable since we can't preserve that info the the zip file
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                if(Files.isRegularFile(path)){
                    path.toFile().setExecutable(true);
                }
                return FileVisitResult.CONTINUE;
            }
        });

    }

    private void copyScripts(List<File> existingScriptFolders, File moduleScriptDir) throws IOException {
        for(File root : existingScriptFolders){
            FileUtil.copyAll(root, moduleScriptDir);
        }
    }

    private boolean addScripts(RepositoryManager outputRepositoryManager, ModuleSpec module, boolean errorIfMissing) throws IOException {
        // find all doc folders to zip
        List<File> existingScriptFolders = findExistingScriptFolders(module.getName(), errorIfMissing);
        
        if(existingScriptFolders.isEmpty()){
            return false;
        }

        String version;
        if (!module.getName().equals(Module.DEFAULT_MODULE_NAME)){
            ModuleVersionDetails mvd = getModuleVersionDetailsFromSource(module.getName());
            if (mvd == null) {
                errorMsg("error.no.script.version", module.getName());
                return false;
            }
            version = mvd.getVersion();
        } else {
            version = null;
        }
        ArtifactContext artifactScriptsZip = new ArtifactContext(null, module.getName(), version, ArtifactContext.SCRIPTS_ZIPPED);
        
        // make the doc zip roots
        IOUtils.ZipRoot[] roots = new IOUtils.ZipRoot[existingScriptFolders.size()];
        int d=0;
        for(File scriptFolder : existingScriptFolders){
            roots[d] = new IOUtils.ZipRoot(scriptFolder, "");
        }
        File scriptZipFile = IOUtils.zipFolders(roots);
        File scriptZipSha1File = ShaSigner.sign(scriptZipFile, getLogger(), verbose != null);
        
        if(!repositoryRemoveArtifact(outputRepositoryManager, artifactScriptsZip)) return true;
        if(!repositoryRemoveArtifact(outputRepositoryManager, artifactScriptsZip.getSha1Context())) return true;
        
        if(!repositoryPutArtifact(outputRepositoryManager, artifactScriptsZip, scriptZipFile)) return true;
        if(!repositoryPutArtifact(outputRepositoryManager, artifactScriptsZip.getSha1Context(), scriptZipSha1File)) return true;
        
        scriptZipFile.delete();
        scriptZipSha1File.delete();

        msg("success.packed", module.getName());
        newline();

        return true;
    }

    private List<File> findExistingScriptFolders(String moduleName, boolean errorIfMissing) throws IOException {
        String scriptPath = moduleName.replace('.', File.separatorChar);
        List<File> existingScriptFolders = new ArrayList<File>(scriptFolders.size());
        for(File scriptFolder : scriptFolders){
            File moduleScriptFolder = new File(applyCwd(scriptFolder), scriptPath);
            if(moduleScriptFolder.exists() && FileUtil.containsFile(moduleScriptFolder))
                existingScriptFolders.add(moduleScriptFolder);
        }
        if(errorIfMissing && existingScriptFolders.isEmpty()){
            errorMsg("error.no.script.found.for.module", moduleName, scriptFolders, scriptPath);
        }
        return existingScriptFolders;
    }

    private boolean repositoryRemoveArtifact(RepositoryManager outputRepository, ArtifactContext artifactContext) throws IOException {
        try {
            outputRepository.removeArtifact(artifactContext);
            return true;
        } catch (Exception e) {
            errorMsg("error.failed.remove.artifact", artifactContext, e.getLocalizedMessage());
            return false;
        }
    }

    private boolean repositoryPutArtifact(RepositoryManager outputRepository, ArtifactContext artifactContext, File content) throws IOException {
        try {
            outputRepository.putArtifact(artifactContext, content);
            return true;
        } catch (Exception e) {
            errorMsg("error.failed.write.artifact", artifactContext, e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        super.initialize(mainTool);
        if (system && local) {
            throw new IllegalArgumentException(Messages.msg(bundle, "conflicting.destinations"));
        }
        if (mode == Mode.pack) {
            for(ModuleSpec module : modules){
                if (module.isVersioned()) {
                    throw new IllegalArgumentException(Messages.msg(bundle, "invalid.module.or.file", module.getName()));
                }
            }
        }
    }
}
