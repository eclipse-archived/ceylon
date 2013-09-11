package com.redhat.ceylon.cmr.api;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class ModuleVersionDetails {
    private String version;
    private String doc;
    private String license;
    private NavigableSet<String> authors = new TreeSet<String>();
    private NavigableSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();

    public ModuleVersionDetails(String version) {
        this.version = version;
    }

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
}
