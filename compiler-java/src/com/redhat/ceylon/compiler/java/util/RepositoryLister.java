package com.redhat.ceylon.compiler.java.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RepositoryLister {
    private List<String> extensions;
    
    public RepositoryLister(List<String> extensions) {
        this.extensions = extensions;
    }
    
    public RepositoryLister() {
        this.extensions = Arrays.asList(".jar", ".car");
    }
    
    public static abstract class Actions {
        public abstract void doWithFile(File path);
        public void enterDirectory(File path) {}
        public void exitDirectory(File path) {}
    };
    
    public void list(File path, RepositoryLister.Actions actions) {
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                actions.enterDirectory(path);
                list(f, actions);
                actions.exitDirectory(path);
            }
        }
        else if (path.isFile()) {
            String fileName = path.getName();
            for (String extension : extensions) {
                if (fileName.endsWith(extension)) {
                    actions.doWithFile(path);
                    return;
                }
            }
        }
    }
}