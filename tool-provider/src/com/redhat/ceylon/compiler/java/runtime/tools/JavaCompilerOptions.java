package com.redhat.ceylon.compiler.java.runtime.tools;

import java.util.LinkedList;
import java.util.List;

public class JavaCompilerOptions extends CompilerOptions {
    private boolean flatClasspath;
    private boolean autoExportMavenDependencies;
    private String jdkProvider;
    private List<String> aptModules = new LinkedList<>();

    public boolean isFlatClasspath() {
        return flatClasspath;
    }

    public void setFlatClasspath(boolean flatClasspath) {
        this.flatClasspath = flatClasspath;
    }

    public boolean isAutoExportMavenDependencies() {
        return autoExportMavenDependencies;
    }

    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
    }

    public String getJdkProvider() {
        return jdkProvider;
    }

    public void setJdkProvider(String jdkProvider) {
        this.jdkProvider = jdkProvider;
    }

    public List<String> getAptModules() {
        return aptModules;
    }

    public void setAptModules(List<String> aptModules) {
        this.aptModules = aptModules;
    }
}
