package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {

    private ArtifactProvider artifactProvider;
    private Modules modules;
    private VFS vfs;

    public Context(VFS vfs) {
        this.vfs = vfs;
        this.artifactProvider = new ArtifactProvider(vfs);
    }

    public Modules getModules() {
        return modules;
    }

    public void setModules(Modules modules) {
        this.modules = modules;
    }

    public ArtifactProvider getArtifactProvider() {
        return artifactProvider;
    }

    public VFS getVfs() {
        return vfs;
    }
}
