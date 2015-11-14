package com.redhat.ceylon.compiler.java.runtime.tools;

public class JavaCompilerOptions extends CompilerOptions {
    private boolean flatClasspath;
    private boolean autoExportMavenDependencies;

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
}
