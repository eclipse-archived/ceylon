package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.common.Backend;
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
    private long javacTarget;

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

    public long getJavacTarget() {
        return javacTarget;
    }

    public void setJavacTarget(long javacTarget) {
        this.javacTarget = javacTarget;
    }

    @Override
    public void mapOptions(CeylonConfig config) {
        super.mapOptions(config);
        setModules(DefaultToolOptions.getCompilerModules(config, Backend.Java));
        setFlatClasspath(DefaultToolOptions.getDefaultFlatClasspath(config));
        setAutoExportMavenDependencies(DefaultToolOptions.getDefaultAutoExportMavenDependencies(config));
        setJdkProvider(DefaultToolOptions.getCompilerJdkProvider(config));
        setJavacOptions(DefaultToolOptions.getCompilerJavac(config));
        setJavacTarget(DefaultToolOptions.getCompilerTargetVersion());
        String[] aptModules = DefaultToolOptions.getCompilerAptModules(config);
        if (aptModules != null) {
            setAptModules(Arrays.asList(aptModules));
        }
    }

    /**
     * Create a new <code>JavaCompilerOptions</code> object initialized with the
     * settings read from the default Ceylon configuration
     * @return An initialized <code>JavaCompilerOptions</code> object
     */
    public static JavaCompilerOptions fromConfig() {
        return fromConfig(CeylonConfig.get());
    }

    /**
     * Create a new <code>JavaCompilerOptions</code> object initialized with the
     * settings read from the given configuration
     * @param config The <code>CeylonConfig</code> to take the settings from
     * @return An initialized <code>JavaCompilerOptions</code> object
     */
    public static JavaCompilerOptions fromConfig(CeylonConfig config) {
        JavaCompilerOptions options = new JavaCompilerOptions();
        options.mapOptions(config);
        return options;
    }
}
