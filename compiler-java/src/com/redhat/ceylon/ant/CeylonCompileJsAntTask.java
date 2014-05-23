/* Originally based on the javac task from apache-ant-1.7.1.
 * The license in that file is as follows:
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or
 *   more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information regarding
 *   copyright ownership.  The ASF licenses this file to You under
 *   the Apache License, Version 2.0 (the "License"); you may not
 *   use this file except in compliance with the License.  You may
 *   obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS
 *   IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied.  See the License for the specific language
 *   governing permissions and limitations under the License.
 *
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */
package com.redhat.ceylon.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;

public class CeylonCompileJsAntTask extends LazyCeylonAntTask {

// TODO Resources not implemented yet for the JS compiler
//    private Path res;
    
    private ModuleSet moduleset = new ModuleSet();
    private FileSet files;
    private boolean optimize = true;
    private boolean modulify = true;
    private boolean gensrc = true;

    private List<File> compileList = new ArrayList<File>(2);
    private Set<Module> modules = null;
    
    private static final FileFilter ARTIFACT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name.endsWith(".js") || name.endsWith(".js.sha1");
        }
    };

    public CeylonCompileJsAntTask() {
        super("compile-js");
    }

    /**
     * Set the resource directories to find the resource files.
     * @param res the resource directories as a path
     */
//    public void setResource(Path res) {
//        if (this.res == null) {
//            this.res = res;
//        } else {
//            this.res.append(res);
//        }
//    }
//
//    public List<File> getResource() {
//        if (this.res == null) {
//            return Collections.singletonList(getProject().resolveFile(Constants.DEFAULT_RESOURCE_DIR));
//        }
//        String[] paths = this.res.list();
//        ArrayList<File> result = new ArrayList<File>(paths.length);
//        for (String path : paths) {
//            result.add(getProject().resolveFile(path));
//        }
//        return result;
//    }
    
    /** Tells the JS compiler whether to wrap the generated code in CommonJS module format. */
    public void setWrapModule(boolean flag){
        modulify = flag;
    }
    /** Tells the JS compiler whether to use lexical scope style or not. */
    public void setLexicalScopeStyle(boolean flag){
        this.optimize = !flag;
    }
    /** Tells the JS compiler whether to generate the .src archive; default is true, but can be turned off
     * to save some time when doing joint jvm/js compilation. */
    public void setSrcArchive(boolean flag) {
        gensrc = flag;
    }

    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addConfiguredModule(Module module){
        this.moduleset.addConfiguredModule(module);
    }
    
    public void addConfiguredModuleset(ModuleSet moduleset){
        this.moduleset.addConfiguredModuleSet(moduleset);
    }

    public void addFiles(FileSet fileset) {
        if (this.files != null) {
            throw new BuildException("<ceyloncjs> only supports a single <files> element");
        }
        this.files = fileset;
    }

    /**
     * Clear the list of files to be compiled and copied..
     */
    protected void resetFileLists() {
        compileList.clear();
    }

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     *
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if (this.moduleset.getModules().isEmpty()
                && this.files == null) {
            throw new BuildException("You must specify a <module> and/or <files>");
        }
    }

    @Override
    protected Commandline buildCommandline() {
        resetFileLists();
        if (files != null) {
            addToCompileList(getSrc());
            addToCompileList(getResource());
        }
        modules = moduleset.getModules();
        
        if (compileList.size() == 0 && modules.size() == 0){
            log("Nothing to compile");
            return null;
        }
        
        LazyHelper lazyTask = new LazyHelper(this) {
            @Override
            protected File getArtifactDir(Module module) {
                File outModuleDir = new File(getOut(), module.toVersionedDir().getPath());
                return outModuleDir;
            }
            
            @Override
            protected FileFilter getArtifactFilter() {
                return ARTIFACT_FILTER;
            }

            @Override
            protected long getOldestArtifactTime(File file) {
                return file.lastModified();
            }
            
            @Override
            protected long getArtifactFileTime(Module module, File file) {
                File moduleDir = getArtifactDir(module);
                String name = module.getName() + ((module.getVersion() != null) ? "-" + module.getVersion() : "") + ".js";
                File jsFile = new File(moduleDir, name);
                if (jsFile.isFile()) {
                    return jsFile.lastModified();
                } else {
                    return Long.MAX_VALUE;
                }
            }
        };

        if (lazyTask.filterFiles(compileList) 
                && lazyTask.filterModules(modules)) {
            log("Everything's up to date");
            return null;
        }
        return super.buildCommandline();
    }
    
    private void addToCompileList(List<File> dirs) {
        for (File srcDir : dirs) {
            if (srcDir.isDirectory()) {
                FileSet fs = (FileSet)this.files.clone();
                fs.setDir(srcDir);
                
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] files = ds.getIncludedFiles();

                for(String fileName : files)
                    compileList.add(new File(srcDir, fileName));
            }
        }
    }
    
    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (!optimize) {
            cmd.createArgument().setValue("--lexical-scope-style");
        }
        if (!modulify) {
            appendOption(cmd, "--no-module");
        }
        if (!gensrc) {
            appendOption(cmd, "--skip-src-archive");
        }

        for (File res : getResource()) {
            appendOptionArgument(cmd, "--resource", res.getAbsolutePath());
        }
        
        for (File file : compileList) {
            log("Adding source file: "+file.getAbsolutePath(), Project.MSG_VERBOSE);
            cmd.createArgument().setValue(file.getAbsolutePath());
        }
        
        // modules to compile
        for (Module module : modules) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }
    }
    
    @Override
    protected String getFailMessage() {
        return CeylonCompileAntTask.FAIL_MSG;
    }
    

}
