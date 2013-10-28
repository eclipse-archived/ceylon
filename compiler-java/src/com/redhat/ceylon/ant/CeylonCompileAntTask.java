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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;


public class CeylonCompileAntTask extends LazyCeylonAntTask  {

    static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";

    public static class JavacOption {
        String javacOption;
        public void addText(String javacOption) {
            this.javacOption = javacOption;
        }
        
    }
    
    private static final FileFilter ARTIFACT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String name = pathname.getName();
            return name.endsWith(".car")
                    || name.endsWith(".src")
                    || name.endsWith(".car.sha1")
                    || name.endsWith(".src.sha1");
        }
    };
    
    private List<File> compileList = new ArrayList<File>(2);
    private Path classpath;
    private final ModuleSet moduleSet = new ModuleSet();
    private FileSet files;
    private String verbose;
    private List<String> javacOptions = new ArrayList<String>(0);

    public CeylonCompileAntTask() {
        super("compile");
    }

    public void setVerbose(String verbose){
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

    /** Adds an option to be passed to javac via a {@code --javac=...} option */
    public void addConfiguredJavacOption(JavacOption javacOption) {
        this.javacOptions.add(javacOption.javacOption);
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
            addToCompileList(getSrc());
            addToCompileList(getResource());
        }
        
        if (compileList.size() == 0 && moduleSet.getModules().size() == 0){
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
                long mtime = Long.MAX_VALUE;
                String name = file.getPath().toLowerCase();
                if (name.endsWith(".car") || name.endsWith(".src")) {
                    JarFile jarFile = null;
                    try {
                        jarFile = new JarFile(file);
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while(entries.hasMoreElements()){
                            JarEntry entry = entries.nextElement();
                            if (entry.getTime() < mtime) {
                                mtime = entry.getTime();
                            }
                        }
                    } catch (IOException ex) {
                        // Maybe something's wrong with the CAR so let's return MIN_VALUE
                        mtime = Long.MIN_VALUE;
                    } finally {
                        if (jarFile != null) {
                            try {
                                jarFile.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                } else {
                    mtime = file.lastModified();
                }
                return mtime;
            }
            
            @Override
            protected long getArtifactFileTime(Module module, File file) {
                File moduleDir = getArtifactDir(module);
                String name = module.getName() + ((module.getVersion() != null) ? "-" + module.getVersion() : "") ;
                File carFile = new File(moduleDir, name + ".car");
                File srcFile = new File(moduleDir, name + ".src");
                long carTime = getCarEntryTime(carFile, file);
                long srcTime = getZipEntryTime(srcFile, file);
                return Math.min(carTime, srcTime);
            }

            private long getCarEntryTime(File carFile, File entryFile) {
                long mtime = Long.MAX_VALUE;
                String name = entryFile.getPath().replace('\\', '/');
                Properties mapping = readMappingFromCar(carFile);
                if (mapping != null) {
                    JarFile jarFile = null;
                    try {
                        jarFile = new JarFile(carFile);
                        for (String className : mapping.stringPropertyNames()) {
                            String srcName = mapping.getProperty(className);
                            if (name.equals(srcName) || name.endsWith("/" + srcName)) {
                                ZipEntry entry = jarFile.getEntry(className);
                                if (entry != null) {
                                    mtime = Math.min(mtime, entry.getTime());
                                }
                            }
                        }
                    } catch (IOException ex) {
                        // Ignore
                    } finally {
                        if (jarFile != null) {
                            try {
                                jarFile.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
                return mtime;
            }
            
            private long getZipEntryTime(File zipFile, File entryFile) {
                if (zipFile.isFile()) {
                    String name = entryFile.getPath().replace('\\', '/');
                    JarFile jarFile = null;
                    try {
                        jarFile = new JarFile(zipFile);
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while(entries.hasMoreElements()){
                            JarEntry entry = entries.nextElement();
                            if (name.equals(entry.getName()) || name.endsWith("/" + entry.getName())) {
                                return entry.getTime();
                            }
                        }
                    } catch (IOException ex) {
                        // Ignore
                    } finally {
                        if (jarFile != null) {
                            try {
                                jarFile.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
                return Long.MAX_VALUE;
            }
            
            private Properties readMappingFromCar(File carFile) {
                if (carFile.isFile()) {
                    JarFile jarFile = null;
                    try {
                        jarFile = new JarFile(carFile);
                        ZipEntry entry = jarFile.getEntry("META-INF/mapping.txt");
                        if (entry != null) {
                            InputStream inputStream = jarFile.getInputStream(entry);
                            try {
                                Properties mapping = new Properties();
                                mapping.load(inputStream);
                                return mapping;
                            } finally {
                                inputStream.close();
                            }
                        }
                    } catch (IOException ex) {
                        // Ignore
                    } finally {
                        if (jarFile != null) {
                            try {
                                jarFile.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
                return null;
            }
        };
        
        if (lazyTask.filterFiles(compileList) 
                && lazyTask.filterModules(moduleSet.getModules())) {
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
    
    /**
     * Perform the compilation.
     */
    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        appendVerboseOption(cmd, verbose);
        for (String javacOption : javacOptions) {
            appendOptionArgument(cmd, "--javac", javacOption);
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
