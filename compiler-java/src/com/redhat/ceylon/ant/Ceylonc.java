/* Based on the javac task from apache-ant-1.7.1.
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;


public class Ceylonc extends LazyTask {

    private static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";

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
    private File executable;
    private List<Module> modules = new LinkedList<Module>();
    private FileSet files;
    private Boolean verbose;
    private String user;
    private String pass;
    private ExitHandler exitHandler = new ExitHandler();

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

    public void setVerbose(Boolean verbose){
        this.verbose = verbose;
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

    public boolean getFailOnError() {
        return exitHandler.isFailOnError();
    }

    public void setFailOnError(boolean failOnError) {
        this.exitHandler.setFailOnError(failOnError);
    }
    
    /**
     * Sets the classpath by a path reference
     * @param classpathReference
     */
	public void setClasspathref(Reference classpathReference) {
		createClasspath().setRefid(classpathReference);
	}

	/**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addModule(Module module){
        modules.add(module);
    }
    
    public void addFiles(FileSet fileset) {
        if (this.files != null) {
            throw new BuildException("<ceylonc> only supports a single <files> element");
        }
        this.files = fileset;
    }

    public String getErrorProperty() {
        return exitHandler.getErrorProperty();
    }

    public void setErrorProperty(String errorProperty) {
        this.exitHandler.setErrorProperty(errorProperty);
    }

    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        
        checkParameters();
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

        compile();
    }

    /**
     * Set compiler executable depending on the OS.
     */
    public void setExecutable(String executable) {
        this.executable = new File(Util.getScriptName(executable));
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
        if (this.modules.isEmpty()
                && this.files == null) {
            throw new BuildException("You must specify a <module> and/or <files>");
        }
        // this will check that we have one
        getCompiler();
    }
    
    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.size() == 0 && modules.size() == 0){
            log("Nothing to compile");
            return;
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
                && lazyTask.filterModules(modules)) {
            log("Everything's up to date");
            return;
        }
        
        Commandline cmd = new Commandline();
        cmd.setExecutable(getCompiler());
        cmd.createArgument().setValue("compile");
        if(verbose != null && verbose.booleanValue()){
            cmd.createArgument().setValue("--verbose");
        }
        if(user != null){
            cmd.createArgument().setValue("--user=" + user);
        }
        if(pass != null){
            cmd.createArgument().setValue("--pass=" + pass);
        }
        
        cmd.createArgument().setValue("--out=" + getOut());
        
        
        for (File src : getSrc()) {
            cmd.createArgument().setValue("--src=" + src.getAbsolutePath());
        }
        
        if (getSystemRepository() != null) {
            cmd.createArgument().setValue("--sysrep=" + Util.quoteParameter(getSystemRepository().url));
        }
        for(Rep rep : getRepositories()){
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
        for (Module module : modules) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }

        try {
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            log("Command line " + Arrays.toString(cmd.getCommandline()), Project.MSG_VERBOSE);
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0) {
                exitHandler.handleExit(this, exe.getExitValue(), FAIL_MSG);
            }
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
        
        
    }

    /**
     * Tries to find a ceylonc compiler either user-specified or detected
     */
    private String getCompiler() {
        return Util.findCeylonScript(this.executable, getProject());
    }
}
