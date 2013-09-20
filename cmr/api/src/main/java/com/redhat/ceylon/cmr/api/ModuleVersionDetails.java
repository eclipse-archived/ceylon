package com.redhat.ceylon.cmr.api;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ModuleVersionDetails implements Comparable<ModuleVersionDetails> {
    private String version;
    private String doc;
    private String license;
    private boolean remote;
    private String origin;
    private NavigableSet<String> authors = new TreeSet<String>();
    private NavigableSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();
    private NavigableSet<ModuleVersionArtifact> artifactTypes = new TreeSet<ModuleVersionArtifact>();

    public ModuleVersionDetails(String version) {
        assert(version != null);
        this.version = version;
    }

    // THis constructor is only used by the unit tests
    public ModuleVersionDetails(String version, String doc, String license, String... by) {
        this(version);
        this.doc = doc;
        this.license = license;
        this.authors.addAll(Arrays.asList(by));
    }

    public ModuleVersionDetails(String version, String doc, String license, Set<String> authors, 
            Set<ModuleInfo> dependencies, Set<ModuleVersionArtifact> artifactTypes,
            boolean remote, String origin) {
        this(version);
        this.doc = doc;
        this.license = license;
        this.authors.addAll(authors);
        this.dependencies.addAll(dependencies);
        this.artifactTypes.addAll(artifactTypes);
        this.remote = remote;
        this.origin = origin;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        assert(version != null);
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

    
    @Override
    public int hashCode() {
        // This only work well for versions within the same module!
        return version.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // This only work well for versions within the same module!
        if (obj instanceof ModuleVersionDetails) {
            return version.equals(((ModuleVersionDetails) obj).version);
        }
        return false;
    }

    @Override
    public int compareTo(ModuleVersionDetails o) {
        return VersionComparator.compareVersions(version, o.version);
    }

    @Override
    public String toString() {
        return "ModuleVersionDetails[ "
                + "version: " + version
                + ", doc: " + doc
                + ", license: " + license
                + ", by: " + authors
                + ", deps: " + dependencies
                + ", artifacts: " + artifactTypes
                + ", remote: " + remote
                + ", origin: " + origin
                + "]";
    }

}
