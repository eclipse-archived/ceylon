/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.tool.ToolUsageError;

public class SourceArgumentsResolver {
    private final Iterable<File> sourceDirs;
    private final Iterable<File> resourceDirs;
    private final String[] sourceSuffixes;

    private File cwd;
    private boolean expandSingleSources;
    
    private List<String> srcModules;
    private List<String> resModules;
    private Collection<String> allModules;
    private List<File> srcFiles;
    private List<File> resFiles;
    private Collection<File> allFiles;
    private Map<String,List<File>> srcModuleFiles;
    private Map<String,List<File>> resModuleFiles;
    private Map<String,List<File>> allModuleFiles;
    
    public SourceArgumentsResolver(Iterable<File> sourceDirs, Iterable<File> resourceDirs, String... sourceSuffixes) {
        this.sourceDirs = sourceDirs;
        this.resourceDirs = resourceDirs;
        this.sourceSuffixes = sourceSuffixes;
        this.srcModules = new LinkedList<String>();
        this.resModules = new LinkedList<String>();
        this.srcFiles = new LinkedList<File>();
        this.resFiles = new LinkedList<File>();
        this.srcModuleFiles = new HashMap<String,List<File>>();
        this.resModuleFiles = new HashMap<String,List<File>>();
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

    public Collection<String> getModules() {
        if (allModules == null) {
            allModules = new LinkedHashSet<String>(srcModules.size() + resModules.size());
            allModules.addAll(srcModules);
            allModules.addAll(resModules);
        }
        return allModules;
    }

    public List<File> getSourceFiles() {
        return srcFiles;
    }

    public List<File> getResourceFiles() {
        return resFiles;
    }

    public Collection<File> getFiles() {
        if (allFiles == null) {
            allFiles = new ArrayList<File>(srcFiles.size() + resFiles.size());
            allFiles.addAll(srcFiles);
            allFiles.addAll(resFiles);
        }
        return allFiles;
    }

    public Map<String,List<File>> getSourceFilesByModule() {
        return srcModuleFiles;
    }

    public Map<String,List<File>> getResourceFilesByModule() {
        return resModuleFiles;
    }

    public Map<String,List<File>> getFilesByModule() {
        if (allModuleFiles == null) {
            allModuleFiles = new HashMap<String,List<File>>();
            allModuleFiles.putAll(srcModuleFiles);
            for (Map.Entry<String,List<File>> entry : resModuleFiles.entrySet()) {
                List<File> files = allModuleFiles.get(entry.getKey());
                if (files == null) {
                    allModuleFiles.put(entry.getKey(), entry.getValue());
                } else {
                    List<File> newFiles = new ArrayList<File>(files.size() + entry.getValue().size());
                    newFiles.addAll(files);
                    newFiles.addAll(entry.getValue());
                    allModuleFiles.put(entry.getKey(), newFiles);
                }
            }
        }
        return allModuleFiles;
    }

    public void expandAndParse(Collection<String> modulesOrFiles, Backend forBackend) throws IOException {
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        List<String> expandedModulesOrFiles = ModuleWildcardsHelper.expandWildcards(srcs, modulesOrFiles, forBackend);
        parse(expandedModulesOrFiles);
    }
    
    public void parse(Collection<String> modulesOrFiles) throws IOException {
        HashSet<String> srcMods = new HashSet<String>();
        HashSet<String> resMods = new HashSet<String>();
        HashSet<String> singleFileMods = new HashSet<String>();
        srcFiles = new LinkedList<File>();
        resFiles = new LinkedList<File>();
        allFiles = null;
        srcModuleFiles = new HashMap<String,List<File>>();
        resModuleFiles = new HashMap<String,List<File>>();
        allModuleFiles = null;
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        Iterable<File> resrcs = FileUtil.applyCwd(cwd, resourceDirs);
        for (String moduleOrFile : modulesOrFiles) {
            File file = FileUtil.applyCwd(cwd, new File(moduleOrFile));
            if (file.isFile()) {
                // It's a single (re)source file instead of a module name, so let's check
                // if it's really located in one of the defined (re)source folders
                File path;
                if (hasAcceptedSuffix(file, sourceSuffixes)) {
                    path = FileUtil.selectPath(srcs, file.getPath());
                    if (path == null) {
                        String srcPath = sourceDirs.toString();
                        throw new ToolUsageError(CeylonToolMessages.msg("error.not.in.source.path", moduleOrFile, srcPath));
                    }
                    String module = moduleName(srcs, path, file);
                    if (!expandSingleSources) {
                        String relFileName = FileUtil.relativeFile(srcs, file.getPath());
                        addFile(srcFiles, srcModuleFiles, module, new File(path, relFileName));
                    } else {
                        // Instead of adding the source file itself we remember
                        // its module name and at the end we expand that and
                        // add all its files
                        singleFileMods.add(module);
                    }
                    // Determine the module path from the file path
                    srcMods.add(module);
                } else {
                    if (resrcs != null) {
                        path = FileUtil.selectPath(resrcs, moduleOrFile);
                        if (path == null) {
                            String resrcPath = resourceDirs.toString();
                            throw new ToolUsageError(CeylonToolMessages.msg("error.not.in.resource.path", moduleOrFile, resrcPath));
                        }
                        String module = moduleName(srcs, path, file);
                        String relFileName = FileUtil.relativeFile(resrcs, file.getPath());
                        addFile(resFiles, resModuleFiles, module, new File(path, relFileName));
                        // Determine the module path from the file path
                        resMods.add(module);
                    }
                }
            } else {
                visitModuleFiles(srcFiles, srcModuleFiles, srcs, moduleOrFile, sourceSuffixes);
                srcMods.add(moduleOrFile);
                if (resrcs != null) {
                    visitModuleFiles(resFiles, resModuleFiles, resrcs, moduleOrFile, null);
                    resMods.add(moduleOrFile);
                }
            }
            if (srcFiles.isEmpty() && resFiles.isEmpty()) {
                throw new ToolUsageError(CeylonToolMessages.msg("error.no.files", moduleOrFile));
            }
        }
        // Now expand the sources of any single source modules we encountered
        for (String modName : singleFileMods) {
            visitModuleFiles(srcFiles, srcModuleFiles, srcs, modName, sourceSuffixes);
        }
        srcModules = new ArrayList<String>(srcMods);
        resModules = new ArrayList<String>(resMods);
        allModules = null;
    }

    private void addFile(List<File> files, Map<String, List<File>> moduleFiles, String module, File file) {
        files.add(file);
        List<File> mfiles = moduleFiles.get(module);
        if (mfiles == null) {
            mfiles = new ArrayList<File>();
            moduleFiles.put(module, mfiles);
        }
        mfiles.add(file);
    }
    
    private void visitModuleFiles(List<File> files, Map<String, List<File>> moduleFiles, Iterable<File> paths, String modName, String[] suffixes) throws IOException {
        if (ModuleUtil.isDefaultModule(modName)) {
            visitFiles(paths, null, new RootedFileVisitor(files, moduleFiles, true, suffixes));
        } else {
            File modPath = ModuleUtil.moduleToPath(modName);
            if (isModuleFolder(modPath)) {
                visitFiles(paths, modPath, new RootedFileVisitor(files, moduleFiles, false, suffixes));
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
        private final List<File> files;
        private final Map<String, List<File>> moduleFiles;
        private final boolean excludeModules;
        private final String[] acceptedSuffixes;
        
        private String module;
        
        public File rootPath;
        
        public RootedFileVisitor(List<File> files, Map<String, List<File>> moduleFiles, boolean excludeModules, String[] acceptedFiles) {
            this.files = files;
            this.moduleFiles = moduleFiles;
            this.excludeModules = excludeModules;
            this.acceptedSuffixes = acceptedFiles;
        }
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (isModuleFolder(rootPath, dir.toFile())) {
                if (excludeModules) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                module = moduleName(rootPath, dir.toFile());
            }
            return super.preVisitDirectory(dir, attrs);
        }
        
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
            if (isModuleFolder(rootPath, dir.toFile())) {
                module = "default";
            }
            return super.postVisitDirectory(dir, ex);
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            boolean add = true;
            if (acceptedSuffixes != null) {
                add = hasAcceptedSuffix(file.toFile(), acceptedSuffixes);
            }
            if (add) {
                addFile(files, moduleFiles, module, file.toFile());
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
    
    private String moduleName(File rootPath, File file) {
        Iterable<File> srcs = FileUtil.applyCwd(cwd, sourceDirs);
        return moduleName(srcs, rootPath, file);
    }
    
    private String moduleName(Iterable<File> srcs, File rootPath, File file) {
        File relFile = FileUtil.relativeFile(rootPath, file);
        return ModuleUtil.moduleName(srcs, relFile);
    }
}
