package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.model.typechecker.model.Modules;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {

    private Modules modules;
    private VFS vfs;
    private RepositoryManager repositoryManager;
    private String distVersion;

    public Context(RepositoryManager repositoryManager, VFS vfs) {
        this.vfs = vfs;
        this.repositoryManager = repositoryManager;
    }
    
    public Context(RepositoryManager repositoryManager, VFS vfs, String distVersion) {
        this.vfs = vfs;
        this.repositoryManager = repositoryManager;
        this.distVersion = distVersion;
    }

    public String getDistVersion() {
        return distVersion;
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
