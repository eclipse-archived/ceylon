package com.redhat.ceylon.model.loader;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import com.redhat.ceylon.model.cmr.ArtifactResult;

public interface ContentAwareArtifactResult extends ArtifactResult {
    Collection<String> getPackages();
    Collection<String> getEntries();
    byte[] getContents(String path);
    URI getContentUri(String path);
    List<String> getFileNames(String path);
}