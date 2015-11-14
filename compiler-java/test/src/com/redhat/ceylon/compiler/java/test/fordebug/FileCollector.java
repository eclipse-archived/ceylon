package com.redhat.ceylon.compiler.java.test.fordebug;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileCollector {

    private List<String> sources = new ArrayList<String>();
    
    public static final FileFilter JAVA_SOURCE_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".java");
        }
    };

    public static final FileFilter JAR_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar");
        }
    };
    
    public static final FileFilter JAR_OR_CAR_FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar")
                    || pathname.getName().endsWith(".car");
        }
    };
    
    public FileCollector() {
        
    }
    
    public FileCollector addFiles(String file, FileFilter filter) {
        return addFiles(new File(file), filter);
    }
    
    public FileCollector addFiles(File file, FileFilter filter) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                addFiles(child, filter);
            }
        } else if (filter.accept(file)) {
            sources.add(file.getPath());
        }
        return this;
    }
    
    public List<String> getFiles() {
        return sources;
    }
    
}
