package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.common.config.CeylonConfig;

import java.io.File;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public class CompilerOptions extends Options {
    private List<String> modules = new LinkedList<>();
    private List<File> files = new LinkedList<>();
    private List<File> sourcePath = new LinkedList<>();
    private List<File> resourcePath = new LinkedList<>();
    private String outputRepository;
    private Writer outWriter;

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

    public String getOutputRepository() {
        return outputRepository;
    }
    public void setOutputRepository(String outputRepository) {
        this.outputRepository = outputRepository;
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

    public static CompilerOptions fromConfig(CeylonConfig config) {
        CompilerOptions options = new CompilerOptions();
        Options.mapOptions(config, options);
        return options;
    }
}
