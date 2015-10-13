package com.redhat.ceylon.common.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.tool.ToolUsageError;

public class SourceArgumentsResolver {
    private final Iterable<File> sourceDirs;
    private final Iterable<File> resourceDirs;
    private final String[] sourceSuffixes;

    private File cwd;
    private boolean expandSingleSources;
    
    private List<String> srcModules;
    private List<String> resModules;
    private List<File> srcFiles;
    private List<File> resFiles;
    
    public SourceArgumentsResolver(Iterable<File> sourceDirs, Iterable<File> resourceDirs, String... sourceSuffixes) {
        this.sourceDirs = sourceDirs;
        this.resourceDirs = resourceDirs;
        this.sourceSuffixes = sourceSuffixes;
        this.srcModules = new LinkedList<String>();
        this.resModules = new LinkedList<String>();
        this.srcFiles = new LinkedList<File>();
        this.resFiles = new LinkedList<File>();
    }
    
    public SourceArgumentsResolver cwd(File cwd) {
        this.cwd = cwd;
        return this;
    }

    /**
     * Setting this to true will cause single source files to be expanded to include
     * all other files belonging to their module. This is specifically done for
     * compiler backends that are unable to perform single-file compilations.
     * @param expandSingleSources
     * @return This object for chaining
     */
    public SourceArgumentsResolver expandSingleSources(boolean expandSingleSources) {
        this.expandSingleSources = expandSingleSources;
        return this;
    }

    public List<String> getSourceModules() {
        return srcModules;
    }

    public List<String> getResourceModules() {
        return resModules;
    }

    public List<File> getSourceFiles() {
        return srcFiles;
    }

    public List<File> getResourceFiles() {
        return resFiles;
    }

    public void expandAndParse(List<String> modulesOrFiles, Backend forBackend) throws IOException {
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        List<String> expandedModulesOrFiles = ModuleWildcardsHelper.expandWildcards(srcs, modulesOrFiles, forBackend);
        parse(expandedModulesOrFiles);
    }
    
    public void parse(List<String> modulesOrFiles) throws IOException {
        HashSet<String> srcMods = new HashSet<String>();
        HashSet<String> resMods = new HashSet<String>();
        HashSet<String> singleFileMods = new HashSet<String>();
        srcFiles = new LinkedList<File>();
        resFiles = new LinkedList<File>();
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        Iterable<File> resrcs = FileUtil.applyCwd(cwd, resourceDirs);
        for (String moduleOrFile : modulesOrFiles) {
            File file = new File(moduleOrFile);
            if (file.isFile()) {
                // It's a single (re)source file instead of a module name, so let's check
                // if it's really located in one of the defined (re)source folders
                File path;
                if (hasAcceptedSuffix(file, sourceSuffixes)) {
                    path = FileUtil.selectPath(srcs, moduleOrFile);
                    if (path == null) {
                        String srcPath = sourceDirs.toString();
                        throw new ToolUsageError(CeylonToolMessages.msg("error.not.in.source.path", moduleOrFile, srcPath));
                    }
                    if (!expandSingleSources) {
                        String relFileName = FileUtil.relativeFile(srcs, file.getPath());
                        srcFiles.add(new File(path, relFileName));
                    } else {
                        // Instead of adding the source file itself we remember
                        // its module name and at the end we expand that and
                        // add all its files
                        singleFileMods.add(moduleName(srcs, path, file));
                    }
                    // Determine the module path from the file path
                    srcMods.add(moduleName(srcs, path, file));
                } else {
                    if (resrcs != null) {
                        path = FileUtil.selectPath(resrcs, moduleOrFile);
                        if (path == null) {
                            String resrcPath = resourceDirs.toString();
                            throw new ToolUsageError(CeylonToolMessages.msg("error.not.in.resource.path", moduleOrFile, resrcPath));
                        }
                        String relFileName = FileUtil.relativeFile(srcs, file.getPath());
                        resFiles.add(new File(path, relFileName));
                        // Determine the module path from the file path
                        resMods.add(moduleName(srcs, path, file));
                    }
                }
            } else {
                visitModuleFiles(srcFiles, srcs, moduleOrFile, sourceSuffixes);
                srcMods.add(moduleOrFile);
                if (resrcs != null) {
                    visitModuleFiles(resFiles, resrcs, moduleOrFile, null);
                    resMods.add(moduleOrFile);
                }
            }
            if (srcFiles.isEmpty() && resFiles.isEmpty()) {
                throw new ToolUsageError(CeylonToolMessages.msg("error.no.files", moduleOrFile));
            }
        }
        // Now expand the sources of any single source modules we encountered
        for (String modName : singleFileMods) {
            visitModuleFiles(srcFiles, srcs, modName, sourceSuffixes);
        }
        srcModules = new ArrayList<String>(srcMods);
        resModules = new ArrayList<String>(resMods);
    }

    private void visitModuleFiles(List<File> files, Iterable<File> paths, String modName, String[] suffixes) throws IOException {
        if (ModuleUtil.isDefaultModule(modName)) {
            visitFiles(paths, null, new RootedFileVisitor(files, true, suffixes));
        } else {
            File modPath = ModuleUtil.moduleToPath(modName);
            if (isModuleFolder(modPath)) {
                visitFiles(paths, modPath, new RootedFileVisitor(files, false, suffixes));
            } else {
                File dir = searchModulePath(modPath);
                if (dir == null || !dir.isDirectory()) {
                    if (modName.contains("/")) {
                        throw new ToolUsageError(CeylonToolMessages.msg("error.invalid.module.or.file", modName));
                    }
                    for (String suffix : suffixes) {
                        if (modName.endsWith(suffix)) {
                            throw new ToolUsageError(CeylonToolMessages.msg("error.file.not.found", modName));
                        }
                    }
                    String ps = sourceDirs.toString();
                    ps = ps.substring(1, ps.length() - 1);
                    throw new ToolUsageError(CeylonToolMessages.msg("error.module.not.found", modName, ps));
                } else {
                    throw new ToolUsageError(CeylonToolMessages.msg("error.not.module", modName));
                }
            }
        }
    }
    
    private static void visitFiles(Iterable<File> dirs, File modPath, RootedFileVisitor visitor) throws IOException {
        for (File dir : dirs) {
            visitFiles(dir, modPath, visitor);
        }
    }
    
    private static void visitFiles(File dir, File modPath, RootedFileVisitor visitor) throws IOException {
        File moduleDir = dir;
        if (modPath != null) {
            moduleDir = new File(dir, modPath.getPath());
        }
        visitor.rootPath = dir;
        if (moduleDir.isDirectory()) {
            Files.walkFileTree(moduleDir.toPath(), visitor);
        }
    }
    
    private class RootedFileVisitor extends SimpleFileVisitor<Path> {
        private final List<File> result;
        private final boolean excludeModules;
        private final String[] acceptedSuffixes;
        
        public File rootPath;
        
        public RootedFileVisitor(List<File> result, boolean excludeModules, String[] acceptedFiles) {
            this.result = result;
            this.excludeModules = excludeModules;
            this.acceptedSuffixes = acceptedFiles;
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (excludeModules && isModuleFolder(rootPath, dir.toFile())) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return super.preVisitDirectory(dir, attrs);
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            boolean add = true;
            if (acceptedSuffixes != null) {
                add = hasAcceptedSuffix(file.toFile(), acceptedSuffixes);
            }
            if (add) {
                result.add(file.toFile());
            }
            return super.visitFile(file, attrs);
        }
    }
    
    private static boolean hasAcceptedSuffix(File file, String... suffixes) {
        for (String ext : suffixes) {
            if (file.toString().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private boolean isModuleFolder(File rootPath, File modPath) {
        File relPath = FileUtil.relativeFile(rootPath, modPath);
        return isModuleFolder(relPath);
    }

    private boolean isModuleFolder(File modPath) {
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        return ModuleUtil.isModuleFolder(srcs, modPath);
    }

    private File searchModulePath(File modPath) {
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        return FileUtil.searchPaths(srcs, modPath.getPath());
    }
    
    private String moduleName(Iterable<File> srcs, File rootPath, File file) {
        File relFile = FileUtil.relativeFile(rootPath, file);
        return ModuleUtil.moduleName(srcs, relFile);
    }
}
