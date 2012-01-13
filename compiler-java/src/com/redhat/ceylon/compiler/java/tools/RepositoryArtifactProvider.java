package com.redhat.ceylon.compiler.java.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VFS;

public class RepositoryArtifactProvider implements ArtifactProvider {

    private Repository repository;
    private VFS vfs;

    public RepositoryArtifactProvider(Repository repository, VFS vfs) {
        this.repository = repository;
        this.vfs = vfs;
    }

    @Override
    public ClosableVirtualFile getArtifact(List<String> moduleName, String version, Iterable<String> extensions) {
        ArtifactContext context = new ArtifactContext();
        for(String ext : extensions){
            context.setName(Util.getName(moduleName));
            context.setVersion(version);
            context.setSuffix("."+ext);
            File artifact;
            try {
                artifact = repository.getArtifact(context);
            } catch (IOException e) {
                e.printStackTrace();
                // try next extension
                continue;
            }
            // we found it
            if(artifact != null)
                return vfs.getFromZipFile(artifact);
        }
        // not found
        return null;
    }

}
