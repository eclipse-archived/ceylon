package com.redhat.ceylon.cmr.api;

public class ArtifactLookupVersion {
    private String version;
    private String license;
    private String[] by;
    private String doc;

    public ArtifactLookupVersion(String version) {
        this.version = version;
    }

    public ArtifactLookupVersion(String version, String doc, String license, String... by) {
        this(version);
        this.doc = doc;
        this.license = license;
        this.by = by;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public void setBy(String[] by) {
        this.by = by;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVersion() {
        return version;
    }

    public String getLicense() {
        return license;
    }

    public String[] getBy() {
        return by;
    }

    public String getDoc() {
        return doc;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
