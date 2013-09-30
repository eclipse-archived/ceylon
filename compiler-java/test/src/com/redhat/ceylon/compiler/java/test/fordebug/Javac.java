package com.redhat.ceylon.compiler.java.test.fordebug;

import java.util.ArrayList;
import java.util.List;

public class Javac {

    private List<String> classPath = new ArrayList<String>();
    private List<String> sourcePath = new ArrayList<String>();
    private List<String> sourceFiles = new ArrayList<String>();
    private boolean verbose;
    private String encoding;
    
    public Javac() {
    }
    
    public Javac verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }
    
    public Javac encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    
    
    public Javac appendClassPath(String path) {
        classPath.add(path);
        return this;
    }
    

    public void appendClassPath(FileCollector classPath) {
        this.classPath.addAll(classPath.getFiles());
    }
    
    public Javac appendSourcePath(String path) {
        sourcePath.add(path);
        return this;
    }
    
    public Javac sourceFiles(List<String> sourceFiles) {
        this.sourceFiles = new ArrayList<String>(sourceFiles);
        return this;
    }
    
    public Javac addSourceFile(String sourceFile) {
        this.sourceFiles.add(sourceFile);
        return this;
    }
    
    public Javac addSourceFiles(FileCollector sources) {
        this.sourceFiles.addAll(sources.getFiles());
        return this;
    }
    
    
    private ProcessBuilder build() {
        List<String> args = buildArgs();
        ProcessBuilder pb = new ProcessBuilder(args);
        return pb;
    }
    
    public String toString() {
        return buildArgs().toString();
    }

    private List<String> buildArgs() {
        List<String> args = new ArrayList<String>();
        args.add("javac");
        if (verbose) {
            args.add("-verbose");
        }
        if (classPath != null) {
            args.add("-classpath");
            args.add(Path.path(this.classPath));
        }
        if (sourcePath != null) {
            args.add("-sourcepath");
            args.add(Path.path(this.sourcePath));
        }
        if (encoding != null) {
            args.add("-encoding");
            args.add(encoding); 
        }
        args.addAll(sourceFiles);
        return args;
    }
    
    public int exec() throws Exception {
        return ProcessRunner.exec(build());
    }
}
