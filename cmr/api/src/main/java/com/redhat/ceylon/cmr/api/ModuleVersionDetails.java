package com.redhat.ceylon.cmr.api;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ModuleVersionDetails implements Comparable<ModuleVersionDetails> {
    private String namespace;
    private String module;
    private String version;
    private String doc;
    private String license;
    private boolean remote;
    private String origin;
    private NavigableSet<String> authors = new TreeSet<String>();
    private NavigableSet<ModuleDependencyInfo> dependencies = new TreeSet<ModuleDependencyInfo>();
    private NavigableSet<ModuleVersionArtifact> artifactTypes = new TreeSet<ModuleVersionArtifact>();
    private NavigableSet<String> members = new TreeSet<>();

    public ModuleVersionDetails(String module, String version) {
        this(null, module, version);
    }

    public ModuleVersionDetails(String namespace, String module, String version) {
        assert(version != null);
        this.namespace = namespace;
        this.module = module;
        this.version = version;
    }

    // THis constructor is only used by the unit tests
    public ModuleVersionDetails(String namespace, String module, String version, String doc, String license, String... by) {
        this(namespace, module, version);
        this.doc = doc;
        this.license = license;
        this.authors.addAll(Arrays.asList(by));
    }

    public ModuleVersionDetails(String namespace, String module, String version, String doc, String license, Set<String> authors, 
            Set<ModuleDependencyInfo> dependencies, Set<ModuleVersionArtifact> artifactTypes,
            boolean remote, String origin) {
        this(namespace, module, version);
        this.doc = doc;
        this.license = license;
        this.authors.addAll(authors);
        this.dependencies.addAll(dependencies);
        this.artifactTypes.addAll(artifactTypes);
        this.remote = remote;
        this.origin = origin;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getModule() {
        return module;
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

    public NavigableSet<ModuleDependencyInfo> getDependencies() {
        return dependencies;
    }

    public void setDependencies(SortedSet<ModuleDependencyInfo> dependencies) {
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

    public NavigableSet<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members.clear();
        this.members.addAll(members);
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + (module != null ? module.hashCode() : 0);
        hash = 37 * hash + (version != null ? version.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if (obj instanceof ModuleVersionDetails == false) {
            return false;
        }
        ModuleVersionDetails other = (ModuleVersionDetails)obj;
        return Objects.equals(namespace, other.namespace)
                && Objects.equals(module, other.module)
                && Objects.equals(version, other.version);
    }

    @Override
    public int compareTo(ModuleVersionDetails o) {
        int result = compare(namespace, o.namespace);
        if (result == 0) {
            result = compare(module, o.module);
            if (result == 0) {
                result = VersionComparator.compareVersions(version, o.version);
            }
        }
        return result;
    }

    private int compare(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return 0;
        }
        if (str1 == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        return str1.compareTo(str2);
    }
    
    @Override
    public String toString() {
        return "ModuleVersionDetails[ "
                + module + "/" + version
                + ", doc: " + ((doc != null) ? ((doc.length() > 10) ? doc.substring(0, 10) + "..." : doc) : null)
                + ", license: " + license
                + ", by: " + authors
                + ", deps: " + dependencies
                + ", artifacts: " + artifactTypes
                + ", remote: " + remote
                + ", origin: " + origin
                + "]";
    }

}
