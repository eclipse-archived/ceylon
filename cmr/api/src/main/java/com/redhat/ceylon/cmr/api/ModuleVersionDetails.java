package com.redhat.ceylon.cmr.api;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.common.MiscUtil;

public class ModuleVersionDetails implements Comparable<ModuleVersionDetails> {
    private String namespace;
    private String module;
    private String label;
    private String version;
    private String doc;
    private String license;
    private boolean remote;
    private String origin;
    private NavigableSet<String> authors = new TreeSet<String>();
    private NavigableSet<ModuleDependencyInfo> dependencies = new TreeSet<ModuleDependencyInfo>();
    private NavigableSet<ModuleVersionArtifact> artifactTypes = new TreeSet<ModuleVersionArtifact>();
    private NavigableSet<String> members = new TreeSet<>();
    private String groupId;
    private String artifactId;

    public ModuleVersionDetails(String module, String version, String groupId, String artifactId) {
        this(null, module, version, groupId, artifactId);
    }

    public ModuleVersionDetails(String namespace, String module, String version, String groupId, String artifactId) {
        assert(version != null);
        this.namespace = namespace;
        this.module = module;
        this.version = version;
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    // THis constructor is only used by the unit tests
//    public ModuleVersionDetails(String namespace, String module, String version,
//            String doc, String license, String... by) {
//        this(namespace, module, version, null, null);
//        this.doc = doc;
//        this.license = license;
//        this.authors.addAll(Arrays.asList(by));
//    }

    public ModuleVersionDetails(String namespace, String module, String version, 
            String groupId, String artifactId, 
            String label, String doc, String license, Set<String> authors, 
            Set<ModuleDependencyInfo> dependencies, Set<ModuleVersionArtifact> artifactTypes,
            boolean remote, String origin) {
        this(namespace, module, version, groupId, artifactId);
        this.label = label;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getArtifactId() {
        return artifactId;
    }
    
    public String getGroupId() {
        return groupId;
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
        hash = 37 * hash + (namespace != null ? namespace.hashCode() : 0);
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
        int result = MiscUtil.compare(namespace, o.namespace);
        if (result == 0) {
            result = MiscUtil.compare(module, o.module);
            if (result == 0) {
                result = VersionComparator.compareVersions(version, o.version);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ModuleVersionDetails[ "
                + module + "/" + version
                + ", label: " + label
                + ", doc: " + ((doc != null) ? ((doc.length() > 10) ? doc.substring(0, 10) + "..." : doc) : null)
                + ", license: " + license
                + ", by: " + authors
                + ", deps: " + dependencies
                + ", artifacts: " + artifactTypes
                + ", remote: " + remote
                + ", origin: " + origin
                + "]";
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

}
