/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.DefaultToolOptions;

public class JavaCompilerOptions extends CompilerOptions {
    private boolean flatClasspath;
    private boolean autoExportMavenDependencies;
    private boolean fullyExportMavenDependencies;
    private String jdkProvider;
    private List<String> aptModules = new LinkedList<>();
    private List<String> javacOptions = new LinkedList<>();
    private long javacTarget;
    private boolean noOsgi;
    private String osgiProvidedBundles;
    private boolean noPom;
    private boolean pack200;
    private boolean jigsaw;
    private boolean ee;
    private List<String> eeImport;
    private List<String> eeAnnotation;

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

    public boolean isFullyExportMavenDependencies() {
        return fullyExportMavenDependencies;
    }

    public void setFullyExportMavenDependencies(boolean fullyExportMavenDependencies) {
        this.fullyExportMavenDependencies = fullyExportMavenDependencies;
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

    public boolean isNoOsgi() {
        return noOsgi;
    }

    public void setNoOsgi(boolean noOsgi) {
        this.noOsgi = noOsgi;
    }

    public String getOsgiProvidedBundles() {
        return osgiProvidedBundles;
    }

    public void setOsgiProvidedBundles(String osgiProvidedBundles) {
        this.osgiProvidedBundles = osgiProvidedBundles;
    }

    public boolean isNoPom() {
        return noPom;
    }

    public void setNoPom(boolean noPom) {
        this.noPom = noPom;
    }

    public boolean isPack200() {
        return pack200;
    }

    public void setPack200(boolean pack200) {
        this.pack200 = pack200;
    }

    public boolean isGenerateModuleInfo() {
        return jigsaw;
    }

    public void setGenerateModuleInfo(boolean jigsaw) {
        this.jigsaw = jigsaw;
    }

    public boolean isEe() {
        return ee;
    }

    public void setEe(boolean ee) {
        this.ee = ee;
    }

    public List<String> getEeImport() {
        return eeImport;
    }

    public void setEeImport(List<String> eeImport) {
        this.eeImport = eeImport;
    }

    public List<String> getEeAnnotation() {
        return eeAnnotation;
    }

    public void setEeAnnotation(List<String> eeAnnotation) {
        this.eeAnnotation = eeAnnotation;
    }

    @Override
    public void mapOptions(CeylonConfig config) {
        super.mapOptions(config);
        setModules(DefaultToolOptions.getCompilerModules(config, Backend.Java));
        setFlatClasspath(DefaultToolOptions.getDefaultFlatClasspath(config));
        setAutoExportMavenDependencies(DefaultToolOptions.getDefaultAutoExportMavenDependencies(config));
        setFullyExportMavenDependencies(DefaultToolOptions.getDefaultFullyExportMavenDependencies(config));
        setNoOsgi(DefaultToolOptions.getCompilerNoOsgi(config));
        setOsgiProvidedBundles(DefaultToolOptions.getCompilerOsgiProvidedBundles(config));
        setNoPom(DefaultToolOptions.getCompilerNoPom(config));
        setGenerateModuleInfo(DefaultToolOptions.getCompilerGenerateModuleInfo(config));
        setPack200(DefaultToolOptions.getCompilerPack200(config));
        setJdkProvider(DefaultToolOptions.getCompilerJdkProvider(config));
        String[] aptModules = DefaultToolOptions.getCompilerAptModules(config);
        if (aptModules != null) {
            setAptModules(Arrays.asList(aptModules));
        }
        setJavacTarget(DefaultToolOptions.getCompilerTargetVersion());
        setJavacOptions(DefaultToolOptions.getCompilerJavac(config));
        setEe(DefaultToolOptions.getCompilerEe(config));
        setEeImport(DefaultToolOptions.getCompilerEeImport(config));
        setEeAnnotation(DefaultToolOptions.getCompilerEeAnnotation(config));
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
