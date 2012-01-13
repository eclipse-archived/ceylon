package com.redhat.ceylon.compiler.typechecker.context;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Context(VFS vfs) {
        this.vfs = vfs;
        this.artifactProviders = Arrays.<ArtifactProvider>asList(new VFSArtifactProvider(vfs));
    }

    public Context(Iterable<VirtualFile> repos, VFS vfs) {
        this.vfs = vfs;
        this.artifactProviders = new ArrayList<ArtifactProvider>();
        for (VirtualFile repo : repos) {
            artifactProviders.add(new VFSArtifactProvider(repo, vfs));
        }
    }

    public Context(ArtifactProvider artifactProvider, VFS vfs) {
        this.vfs = vfs;
        this.artifactProviders = Arrays.<ArtifactProvider>asList(artifactProvider);
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
