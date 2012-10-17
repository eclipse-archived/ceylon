package com.redhat.ceylon.tools.new_;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Tool;

public abstract class Project implements Tool {

    private File directory = new File(".");
    
    public abstract List<Variable> getVariables();
    
    public abstract List<Copy> getResources(Environment env);
    
    @Argument(argumentName="dir", multiplicity="?", order=1)
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    File mkBaseDir() throws IOException {
        if (directory.exists() && !directory.isDirectory()) {
            throw new IOException(Messages.msg("path.exists.and.not.dir", directory));
        } else if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException(Messages.msg("could.not.mkdir", directory));
            }
        }
        return directory;
    }
    
    @Override
    public final void run() throws Exception {
        // Projects are never run as tools
        throw new RuntimeException();
    }
    
}

