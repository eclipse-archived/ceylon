/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.new_;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.tool.Tool;
import org.eclipse.ceylon.common.tools.CeylonTool;

public abstract class NewSubTool implements Tool {

    private File directory;
    
    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    void mkBaseDir(File cwd) throws IOException {
        if (directory != null) {
            File actualDir = FileUtil.applyCwd(cwd, directory);
            if (actualDir.exists() && !actualDir.isDirectory()) {
                throw new IOException(Messages.msg("path.exists.and.not.dir", directory));
            } else if (!actualDir.exists()) {
                if (!FileUtil.mkdirs(actualDir)) {
                    throw new IOException(Messages.msg("could.not.mkdir", directory));
                }
            }
        }
    }
    
    public abstract List<Variable> getVariables();
    
    public abstract List<Copy> getResources(Environment env);
    
    @Override
    public void initialize(CeylonTool mainTool) {
    }
    
    @Override
    public final void run() throws Exception {
        // Projects are never run as tools
        throw new RuntimeException();
    }
    
}

