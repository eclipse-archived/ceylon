package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;

import java.io.File;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public class CompilerOptions extends Options {
    private List<String> modules = new LinkedList<>();
    private List<File> files = new LinkedList<>();
    private List<File> sourcePath = new LinkedList<>();
    private List<File> resourcePath = new LinkedList<>();
    private String resourceRootName;
    private String outputRepository;
    private Writer outWriter;
    private String encoding;

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
        setModules(DefaultToolOptions.getCompilerModules(config, null));
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
