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

import java.io.File;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.DefaultToolOptions;

public class CompilerOptions extends Options {
    private List<String> modules = new LinkedList<>();
    private List<File> files = new LinkedList<>();
    private List<File> sourcePath = new LinkedList<>();
    private List<File> resourcePath = new LinkedList<>();
    private List<String> suppressWarnings;
    private String resourceRootName;
    private String outputRepository;
    private Writer outWriter;
    private String encoding;
    private String includeDependencies;
    private boolean progress;

    public List<String> getModules() {
        return modules;
    }
    public void setModules(List<String> modules) {
        this.modules = modules;
    }
    public void addModule(String module) {
        modules.add(module);
    }
    
    public List<File> getFiles() {
        return files;
    }
    public void setFiles(List<File> files) {
        this.files = files;
    }
    public void addFile(File file){
        files.add(file);
    }

    public List<File> getSourcePath() {
        return sourcePath;
    }
    public void setSourcePath(List<File> sourcePath) {
        this.sourcePath = sourcePath;
    }
    public void addSourcePath(File sourcePath){
        this.sourcePath.add(sourcePath);
    }

    public List<File> getResourcePath() {
        return resourcePath;
    }
    public void setResourcePath(List<File> resourcePath) {
        this.resourcePath = resourcePath;
    }
    public void addResourcePath(File resourcePath){
        this.resourcePath.add(resourcePath);
    }

    public String getResourceRootName() {
        return resourceRootName;
    }
    public void setResourceRootName(String resourceRootName) {
        this.resourceRootName = resourceRootName;
    }
    
    public String getOutputRepository() {
        return outputRepository;
    }
    public void setOutputRepository(String outputRepository) {
        this.outputRepository = outputRepository;
    }

    public List<String> getSuppressWarnings() {
        return suppressWarnings;
    }
    
    public void setSuppressWarnings(List<String> suppressWarnings) {
        this.suppressWarnings = suppressWarnings;
    }
    
    public boolean getProgress() {
        return progress;
    }
    
    public void setProgress(boolean progress) {
        this.progress = progress;
    }
    
    public String getIncludeDependencies() {
        return includeDependencies;
    }
    
    public void setIncludeDependencies(String includeDependencies) {
        this.includeDependencies = includeDependencies;
    }
    
    public Writer getOutWriter() {
        return outWriter;
    }
    /**
     * Specifies a {@link Writer} where messages will be written when {@link #setVerbose(boolean)} is set
     * to <code>true</code>.
     * @param outWriter the output writer
     */
    public void setOutWriter(Writer outWriter) {
        this.outWriter = outWriter;
    }
    
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void mapOptions(CeylonConfig config) {
        super.mapOptions(config);
        setEncoding(DefaultToolOptions.getDefaultEncoding(config));
        setOutputRepository(DefaultToolOptions.getCompilerOutputRepo(config));
        setSourcePath(DefaultToolOptions.getCompilerSourceDirs(config));
        setResourcePath(DefaultToolOptions.getCompilerResourceDirs(config));
        setResourceRootName(DefaultToolOptions.getCompilerResourceRootName(config));
        setSuppressWarnings(DefaultToolOptions.getCompilerSuppressWarnings(config));
        setProgress(DefaultToolOptions.getCompilerProgress(config));
        setIncludeDependencies(DefaultToolOptions.getCompilerIncludeDependencies(config));
    }

    /**
     * Create a new <code>CompilerOptions</code> object initialized with the
     * settings read from the default Ceylon configuration
     * @return An initialized <code>CompilerOptions</code> object
     */
    public static CompilerOptions fromConfig() {
        return fromConfig(CeylonConfig.get());
    }

    /**
     * Create a new <code>CompilerOptions</code> object initialized with the
     * settings read from the given configuration
     * @param config The <code>CeylonConfig</code> to take the settings from
     * @return An initialized <code>CompilerOptions</code> object
     */
    public static CompilerOptions fromConfig(CeylonConfig config) {
        CompilerOptions options = new CompilerOptions();
        options.mapOptions(config);
        return options;
    }
}
