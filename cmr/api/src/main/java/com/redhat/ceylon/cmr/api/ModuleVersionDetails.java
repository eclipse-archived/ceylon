package com.redhat.ceylon.cmr.api;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class ModuleVersionDetails {
    private String version;
    private String doc;
    private String license;
    private boolean remote;
    private String origin;
    private NavigableSet<String> authors = new TreeSet<String>();
    private NavigableSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();
    private NavigableSet<ModuleVersionArtifact> artifactTypes = new TreeSet<ModuleVersionArtifact>();

    public ModuleVersionDetails(String version) {
        this.version = version;
    }

    // THis constructor is only used by the unit tests
    public ModuleVersionDetails(String version, String doc, String license, String... by) {
        this(version);
        this.doc = doc;
        this.license = license;
        this.authors.addAll(Arrays.asList(by));
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public NavigableSet<String> getAuthors() {
        return authors;
    }

    public void setAuthors(SortedSet<String> authors) {
        this.authors.clear();
        this.authors.addAll(authors);
    }

    public NavigableSet<ModuleInfo> getDependencies() {
        return dependencies;
    }

    public void setDependencies(SortedSet<ModuleInfo> dependencies) {
        this.dependencies.clear();
        this.dependencies.addAll(dependencies);
    }
    
    public NavigableSet<ModuleVersionArtifact> getArtifactTypes() {
        return artifactTypes;
    }

    public void setArtifactTypes(SortedSet<ModuleVersionArtifact> types) {
        this.artifactTypes.clear();
        this.artifactTypes.addAll(types);
    }

}
