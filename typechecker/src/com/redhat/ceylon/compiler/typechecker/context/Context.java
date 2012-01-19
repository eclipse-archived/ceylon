package com.redhat.ceylon.compiler.typechecker.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.io.VFSArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {

    private List<ArtifactProvider> artifactProviders;
    private Modules modules;
    private VFS vfs;

    public Context(List<ArtifactProvider> artifactProviders, VFS vfs) {
        this.vfs = vfs;
        this.artifactProviders = new LinkedList<ArtifactProvider>();
        this.artifactProviders.addAll(artifactProviders);
        if(this.artifactProviders.isEmpty())
            this.artifactProviders.add(new VFSArtifactProvider(vfs));
    }

    public Modules getModules() {
        return modules;
    }

    public void setModules(Modules modules) {
        this.modules = modules;
    }

    public List<ArtifactProvider> getArtifactProviders() {
        return artifactProviders;
    }

    public VFS getVfs() {
        return vfs;
    }
}
