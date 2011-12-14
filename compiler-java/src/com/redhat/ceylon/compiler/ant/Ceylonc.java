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
    private File destDir;
    private File[] compileList;
    private Reference classpathReference;
    private List<Rep> repositories = new LinkedList<Rep>();

	public void setClasspathReference(Reference classpathReference) {
		this.classpathReference = classpathReference;
	}

	// TODO: There must be a better way to get the path for the Ceylon
    // compiler into the compile() method. Once that better way is
    // found, remove all references to compilerExecutable in this file.
    private File compilerExecutable;
    
	/**
     * Set the source directories to find the source Java and Ceylon files.
     * @param srcDir the source directories as a path
     */
    public void setSrcdir(Path srcDir) {
        if (src == null) {
            src = srcDir;
        } else {
            src.append(srcDir);
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
     * @param destDir the destination director
     */
    public void setDestdir(File destDir) {
        this.destDir = destDir;
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
                throw new BuildException("srcdir \"" + srcDir.getPath() + "\" does not exist!", getLocation());
            }

            DirectoryScanner ds = getDirectoryScanner(srcDir);
            String[] files = ds.getIncludedFiles();

            scanDir(srcDir, destDir != null ? destDir : srcDir, files);
        }

        compile();
    }

    /**
     * Set compiler executable depending on the OS.
     */
    public void setCompiler(String compilerPath) {
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			compilerExecutable = new File(compilerPath + ".bat");
		} else {
			compilerExecutable = new File(compilerPath);
		}		
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
        if (src == null) {
            throw new BuildException("srcdir attribute must be set!", getLocation());
        }
        if (src.size() == 0) {
            throw new BuildException("srcdir attribute must be set!", getLocation());
        }

        if (destDir != null && !destDir.isDirectory()) {
            throw new BuildException("destination directory \"" + destDir + "\" does not exist " + "or is not a directory", getLocation());
        }

        if (compilerExecutable == null) {
            throw new BuildException("compiler attribute must be set!", getLocation());
        }
    }

    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.length == 0)
            return;

        Commandline cmd = new Commandline();
        cmd.setExecutable(compilerExecutable.getAbsolutePath());
        cmd.createArgument().setValue("-d");
        cmd.createArgument().setValue(destDir.getAbsolutePath());
        cmd.createArgument().setValue("-sourcepath");
        cmd.createArgument().setValue(src.toString());
        if(classpathReference != null){
        	String path = classpathReference.getReferencedObject().toString();
        	if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
        		path = "\"" + path + "\"";
        	}
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setValue(path);
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
}
