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
 * TODO: this file needs an appropriate license header.
 */
package com.redhat.ceylon.compiler.ant;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

public class Ceylonc extends MatchingTask {

    private static final String FAIL_MSG
        = "Compile failed; see the compiler error output for details.";

    private Path src;
    private File destDir;
    private File[] compileList;

    // TODO: There must be a better way to get the classpath for the
    // compiler into the compile() method.  Once that better way is
    // found, remove all references to compilerClasspath in this file.
    private Path compilerClasspath;

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
     * Set the destination directory into which the Java source
     * files should be compiled.
     * @param destDir the destination director
     */
    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Set the classpath of the Ceylon compiler and its associated libraries.
     * @param classpath the classpath
     */
    public void setCompilerclasspath(Path classpath) {
        if (compilerClasspath == null) {
            compilerClasspath = classpath;
        } else {
            compilerClasspath.append(classpath);
        }
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
                throw new BuildException("srcdir \""
                                         + srcDir.getPath()
                                         + "\" does not exist!", getLocation());
            }

            DirectoryScanner ds = getDirectoryScanner(srcDir);
            String[] files = ds.getIncludedFiles();

            scanDir(srcDir, destDir != null ? destDir : srcDir, files);
        }

        compile();
    }

    /**
     * Clear the list of files to be compiled and copied..
     */
    protected void resetFileLists() {
        compileList = new File[0];
    }

    /**
     * Scans the directory looking for source files to be compiled.
     * The results are returned in the class variable compileList
     *
     * @param srcDir   The source directory
     * @param destDir  The destination directory
     * @param files    An array of filenames
     */
    private void scanDir(File srcDir, File destDir, String[] files) {
        scanDir(srcDir, destDir, files, "*.java");
        scanDir(srcDir, destDir, files, "*.ceylon");
    }

    /**
     * Scans the directory looking for source files to be compiled.
     * The results are returned in the class variable compileList
     *
     * @param srcDir   The source directory
     * @param destDir  The destination directory
     * @param files    An array of filenames
     * @param pattern  The pattern to match source files
     */
    private void scanDir(File srcDir,
                         File destDir,
                         String[] files,
                         String pattern) {
        GlobPatternMapper m = new GlobPatternMapper();
        m.setFrom(pattern);
        m.setTo("*.class");
        SourceFileScanner sfs = new SourceFileScanner(this);
        File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);

        if (newFiles.length > 0) {
            File[] newCompileList
                = new File[compileList.length + newFiles.length];
            System.arraycopy(compileList, 0, newCompileList, 0,
                    compileList.length);
            System.arraycopy(newFiles, 0, newCompileList,
                    compileList.length, newFiles.length);
            compileList = newCompileList;
        }
    }

    /**
     * Check that all required attributes have been set and nothing
     * silly has been entered.
     *
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if (src == null) {
            throw new BuildException("srcdir attribute must be set!",
                                     getLocation());
        }
        if (src.size() == 0) {
            throw new BuildException("srcdir attribute must be set!",
                                     getLocation());
        }

        if (destDir != null && !destDir.isDirectory()) {
            throw new BuildException("destination directory \""
                                     + destDir
                                     + "\" does not exist "
                                     + "or is not a directory", getLocation());
        }

        if (compilerClasspath == null) {
            throw new BuildException("compilerclasspath attribute must be set!",
                                     getLocation());
        }
        if (compilerClasspath.size() == 0) {
            throw new BuildException("compilerclasspath attribute must be set!",
                                     getLocation());
        }
    }

    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.length == 0)
            return;

        Commandline cmd = new Commandline();
        cmd.setExecutable("java");
        cmd.createArgument().setValue("-ea");
        cmd.createArgument().setValue("-cp");
        cmd.createArgument().setValue(compilerClasspath.toString());
        cmd.createArgument().setValue("com.redhat.ceylon.compiler.Main");
        cmd.createArgument().setValue("-d");
        cmd.createArgument().setValue(destDir.getAbsolutePath());
        cmd.createArgument().setValue("-src");
        cmd.createArgument().setValue(src.toString());
        for (int i = 0; i < compileList.length; i++) {
            cmd.createArgument().setValue(compileList[i].getAbsolutePath());
        }

        try {
            Execute exe = new Execute(new LogStreamHandler(this,
                                                           Project.MSG_INFO,
                                                           Project.MSG_WARN));
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            exe.execute();
            if (exe.getExitValue() != 0)
                throw new BuildException(FAIL_MSG, getLocation());
        }
        catch (IOException e) {
            throw new BuildException(
                "Error running Ceylon compiler", e, getLocation());
        }
    }
}
