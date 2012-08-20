package com.redhat.ceylon.cmr.api;

import java.util.SortedMap;
import java.util.TreeMap;

public class ArtifactLookupResult {
    private String name;
    private SortedMap<String, ArtifactLookupVersion> versions = new TreeMap<String, ArtifactLookupVersion>();

    public ArtifactLookupResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArtifactLookupVersion addVersion(String version) {
        if(versions.containsKey(version))
            return null;
        ArtifactLookupVersion newVersion = new ArtifactLookupVersion(version);
        versions.put(version, newVersion);
        return newVersion;
    }

    public SortedMap<String, ArtifactLookupVersion> getVersions() {
        return versions;
    }

    public boolean hasVersion(String version) {
        return versions.containsKey(version);
    }
}
