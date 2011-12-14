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

package com.redhat.ceylon.compiler.ant;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

public class Ceylonc extends MatchingTask {

    private static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";

    private Path src;   
    private File out;
    private File[] compileList;
    private Path classpath;
    private List<Rep> repositories = new LinkedList<Rep>();
    private File compiler;

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
	public void setClasspathRef(Reference classpathReference) {
		createClasspath().setRefid(classpathReference);
	}

	/**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    /**
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep){
        repositories.add(rep);
    }
    
    /**
     * Set the destination directory into which the Java source files should be
     * compiled.
     * @param out the destination director
     */
    public void setOut(File out) {
        this.out = out;
    }

    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    public void execute() throws BuildException {
        checkParameters();
        resetFileLists();
        
        String[] list = src.list();
        for (int i = 0; i < list.length; i++) {
            File srcDir = getProject().resolveFile(list[i]);
            if (!srcDir.exists()) {
                throw new BuildException("source path \"" + srcDir.getPath() + "\" does not exist!", getLocation());
            }

            DirectoryScanner ds = getDirectoryScanner(srcDir);
            String[] files = ds.getIncludedFiles();

            scanDir(srcDir, out != null ? out : srcDir, files);
        }

        compile();
    }

    /**
     * Set compiler executable depending on the OS.
     */
    public void setCompiler(String compilerPath) {
        compiler = new File(getScriptName(compilerPath));
	}

    /**
     * Clear the list of files to be compiled and copied..
     */
    protected void resetFileLists() {
        compileList = new File[0];
    }

    /**
     * Scans the directory looking for source files to be compiled. The results
     * are returned in the class variable compileList
     * 
     * @param srcDir The source directory
     * @param destDir The destination directory
     * @param files An array of filenames
     */
    private void scanDir(File srcDir, File destDir, String[] files) {
        // FIXME: we can't compile java at the same time in M1
        //scanDir(srcDir, destDir, files, "*.java");
        scanDir(srcDir, destDir, files, "*.ceylon");
    }

    /**
     * Scans the directory looking for source files to be compiled. The results
     * are returned in the class variable compileList
     * 
     * @param srcDir The source directory
     * @param destDir The destination directory
     * @param files An array of filenames
     * @param pattern The pattern to match source files
     */
    private void scanDir(File srcDir, File destDir, String[] files, String pattern) {
        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom(pattern);
        m.setTo("*.class");
        SourceFileScanner sfs = new SourceFileScanner(this);
        File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);

        if (newFiles.length > 0) {
            File[] newCompileList = new File[compileList.length + newFiles.length];
            System.arraycopy(compileList, 0, newCompileList, 0, compileList.length);
            System.arraycopy(newFiles, 0, newCompileList, compileList.length, newFiles.length);
            compileList = newCompileList;
        }
    }

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     * 
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        // this will check that we have one
        getCompiler();
    }

    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.length == 0)
            return;

        Commandline cmd = new Commandline();
        cmd.setExecutable(getCompiler());
        if(out != null){
            cmd.createArgument().setValue("-out");
            cmd.createArgument().setValue(out.getAbsolutePath());
        }
        if(src != null){
            cmd.createArgument().setValue("-src");
            cmd.createArgument().setValue(src.toString());
        }
        if(repositories != null){
            for(Rep rep : repositories){
                // skip empty entries
                if(rep.url == null || rep.url.isEmpty())
                    continue;
                cmd.createArgument().setValue("-rep");
                cmd.createArgument().setValue(quoteParameter(rep.url));
            }
        }
        if(classpath != null){
        	String path = classpath.toString();
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setValue(quoteParameter(path));
        }
        for (int i = 0; i < compileList.length; i++) {
            cmd.createArgument().setValue(compileList[i].getAbsolutePath());
        }

        try {
            Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0)
                throw new BuildException(FAIL_MSG, getLocation());
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
    }

    /**
     * Tries to find a ceylonc compiler either user-specified or detected
     */
    private String getCompiler() {
        if(this.compiler != null){
            if(!this.compiler.exists())
                throw new BuildException("Failed to find compiler executable in "+this.compiler.getPath());
            if(!this.compiler.canExecute())
                throw new BuildException("Cannot execute compiler executable in "+this.compiler.getPath()+" (not executable)");
            return this.compiler.getAbsolutePath();
        }
        // try to guess from the "ceylon.home" project property
        String ceylonHome = getProject().getProperty("ceylon.home");
        if(ceylonHome == null || ceylonHome.isEmpty()){
            // try again from the CEYLON_HOME env var
            ceylonHome = System.getenv("CEYLON_HOME");
        }
        if(ceylonHome == null || ceylonHome.isEmpty())
            throw new BuildException("Failed to find Ceylon home, specify the ceylon.home property or set the CEYLON_HOME environment variable");
        // now try to find the executable
        String compilerPath = ceylonHome + File.separatorChar + "bin" + File.separatorChar + getScriptName("ceylonc");
        File compiler = new File(compilerPath);
        if(!compiler.exists())
            throw new BuildException("Failed to find 'ceylonc' executable in "+ceylonHome);
        if(!compiler.canExecute())
            throw new BuildException("Cannot execute 'ceylonc' executable in "+ceylonHome+" (not executable)");
        return compiler.getAbsolutePath();
    }
}
