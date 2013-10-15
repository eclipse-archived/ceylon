package com.redhat.ceylon.cmr.api;

public class ModuleVersionQuery extends ModuleQuery {

    private String version;

    public ModuleVersionQuery(String name, String version, Type type) {
        super(name, type);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ModuleVersionQuery[name=" + name + ",version=" + version + ",type=" + type + "]";
    }
}
