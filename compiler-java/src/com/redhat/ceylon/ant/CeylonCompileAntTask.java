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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;


public class CeylonCompileAntTask extends LazyCeylonAntTask  {

    static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";

    private static final FileFilter ARTIFACT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name.endsWith(".car")
                    || name.endsWith(".src")
                    || name.endsWith(".sha1");
        }
    };
    
    private List<File> compileList = new ArrayList<File>(2);
    private Path classpath;
    private final ModuleSet moduleSet = new ModuleSet();
    private FileSet files;
    private String verbose;
    private Boolean bootstrap;
    private String user;
    private String pass;

    public CeylonCompileAntTask() {
        super("compile");
    }
    
    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setVerbose(String verbose){
        this.verbose = verbose;
    }

    public void setBootstrap(Boolean bootstrap){
        this.bootstrap = bootstrap;
    }

    /**
     * Sets the classpath
     * @param path
     */
    public void setClasspath(Path path){
        if(this.classpath == null)
            this.classpath = path;
        else
            this.classpath.add(path);
    }
    
    /**
     * Adds a &lt;classpath&gt; nested param
     */
    public Path createClasspath(){
        if(this.classpath == null)
            return this.classpath = new Path(getProject());
        else
            return this.classpath.createPath(); 
    }
    
    /**
     * Sets the classpath by a path reference
     * @param classpathReference
     */
	public void setClasspathref(Reference classpathReference) {
		createClasspath().setRefid(classpathReference);
	}

	public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.moduleSet.addConfiguredModuleSet(moduleset);
    }
    
	/**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addConfiguredModule(Module module) {
        this.moduleSet.addConfiguredModule(module);
    }
    
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.moduleSet.addConfiguredSourceModules(sourceModules);
    }
    
    public void addFiles(FileSet fileset) {
        if (this.files != null) {
            throw new BuildException("<ceylonc> only supports a single <files> element");
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
        if (this.moduleSet.getModules().isEmpty()
                && this.files == null) {
            throw new BuildException("You must specify a <module>, <moduleset> and/or <files>");
        }
    }
    
    @Override
    protected Commandline buildCommandline() {
        resetFileLists();
        if (files != null) {
            
            for (File srcDir : getSrc()) {
                FileSet fs = (FileSet)this.files.clone();
                fs.setDir(srcDir);
                if (!srcDir.exists()) {
                    throw new BuildException("source path \"" + srcDir.getPath() + "\" does not exist!", getLocation());
                }
    
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] files = ds.getIncludedFiles();
    
                for(String fileName : files)
                    compileList.add(new File(srcDir, fileName));
            }
        }
        
        if (compileList.size() == 0 && moduleSet.getModules().size() == 0){
            log("Nothing to compile");
            return null;
        }
     
        LazyHelper lazyTask = new LazyHelper(this) {
            @Override
            protected File getArtifactDir(String version, Module module) {
                File outModuleDir = new File(getOut(), module.toDir().getPath()+"/"+version);
                return outModuleDir;
            }
            
            @Override
            protected FileFilter getArtifactFilter() {
                return ARTIFACT_FILTER;
            }
        };
        
        if (lazyTask.filterFiles(compileList) 
                && lazyTask.filterModules(moduleSet.getModules())) {
            log("Everything's up to date");
            return null;
        }
        return super.buildCommandline();
    }
    
    /**
     * Perform the compilation.
     */
    @Override
    protected void completeCommandline(Commandline cmd) {
        
        appendVerboseOption(cmd, verbose);
        if(bootstrap != null && bootstrap.booleanValue()){
            cmd.createArgument().setValue("--javac=-Xbootstrapceylon");
        }
        
        appendUserOption(cmd, user);
        appendPassOption(cmd, pass);
        
        cmd.createArgument().setValue("--out=" + getOut());
        
        
        for (File src : getSrc()) {
            cmd.createArgument().setValue("--src=" + src.getAbsolutePath());
        }
        
        if (getSystemRepository() != null) {
            // This argument is separated on purpose! It has to be parsed
            // by the "ceylon" scripts themselves which are not able to
            // handle "="-joined arguments!
            cmd.createArgument().setValue("--sysrep");
            cmd.createArgument().setValue(Util.quoteParameter(getSystemRepository()));
        }
        for(Repo rep : getReposet()){
            // skip empty entries
            if(rep.url == null || rep.url.isEmpty())
                continue;
            log("Adding repository: "+rep, Project.MSG_VERBOSE);
            cmd.createArgument().setValue("--rep=" + Util.quoteParameter(rep.url));
        }
        if(classpath != null){
            throw new RuntimeException("-classpath not longer supported");
        	/*String path = classpath.toString();
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setValue(Util.quoteParameter(path));*/
        }
        // files to compile
        for (File file : compileList) {
            log("Adding source file: "+file.getAbsolutePath(), Project.MSG_VERBOSE);
            cmd.createArgument().setValue(file.getAbsolutePath());
        }
        // modules to compile
        for (Module module : moduleSet.getModules()) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toVersionlessSpec());
        }
    }

    @Override
    protected String getFailMessage() {
        return FAIL_MSG;
    }
}
