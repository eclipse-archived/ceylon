/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.context;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.compiler.typechecker.io.VFS;
import org.eclipse.ceylon.model.typechecker.model.Modules;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {

    private Modules modules;
    private VFS vfs;
    private RepositoryManager repositoryManager;

    public Context(RepositoryManager repositoryManager, VFS vfs) {
        this.vfs = vfs;
        this.repositoryManager = repositoryManager;
    }

    public Modules getModules() {
        return modules;
    }

    public void setModules(Modules modules) {
        this.modules = modules;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public VFS getVfs() {
        return vfs;
    }
}
