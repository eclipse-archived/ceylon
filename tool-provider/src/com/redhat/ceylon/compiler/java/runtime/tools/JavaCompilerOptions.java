package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JavaCompilerOptions extends CompilerOptions {
    private boolean flatClasspath;
    private boolean autoExportMavenDependencies;
    private String jdkProvider;
    private List<String> aptModules = new LinkedList<>();
    private List<String> javacOptions = new LinkedList<>();

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

    public List<String> getJavacOptions() {
        return javacOptions;
    }

    public void setJavacOptions(List<String> javacOptions) {
        this.javacOptions = javacOptions;
    }

    public static JavaCompilerOptions fromConfig(CeylonConfig config) {
        JavaCompilerOptions options = new JavaCompilerOptions();
        mapOptions(config, options);
        return options;
    }

    static void mapOptions(CeylonConfig config, JavaCompilerOptions options) {
        CompilerOptions.mapOptions(config, options);
        options.setFlatClasspath(DefaultToolOptions.getDefaultFlatClasspath(config));
        options.setAutoExportMavenDependencies(DefaultToolOptions.getDefaultAutoExportMavenDependencies(config));
        options.setJdkProvider(DefaultToolOptions.getCompilerJdkProvider(config));
        options.setJavacOptions(DefaultToolOptions.getCompilerJavac(config));
        String[] aptModules = DefaultToolOptions.getCompilerAptModules(config);
        if (aptModules != null) {
            options.setAptModules(Arrays.asList(aptModules));
        }
    }
}
