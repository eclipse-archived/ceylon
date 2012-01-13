package com.redhat.ceylon.compiler.typechecker.io;

import java.util.List;

public interface ArtifactProvider {

    /**
     * Return the artifact or null if not present
     * Must be closed when done with it
     */
    ClosableVirtualFile getArtifact(List<String> moduleName, String version, Iterable<String> extensions);

    public abstract String getArtifactName(List<String> moduleName, String version, String extension);

}