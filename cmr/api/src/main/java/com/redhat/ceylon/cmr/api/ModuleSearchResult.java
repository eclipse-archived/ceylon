package com.redhat.ceylon.cmr.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ModuleSearchResult {
    
    public static class ModuleDetails {
        private String name, license, doc, lastVersion;
        private Set<String> authors = new TreeSet<String>();
        private Set<String> versions = new TreeSet<String>();

        public ModuleDetails(String name, String doc, String license, SortedSet<String> authors, SortedSet<String> versions) {
            this.name = name;
            this.doc = toNull(doc);
            this.license = toNull(license);
            this.authors.addAll(authors);
            this.versions.addAll(versions);
        }

        private String toNull(String str) {
            if(str != null && str.isEmpty())
                return null;
            return str;
        }

        public String getName() {
            return name;
        }

        public String getLicense() {
            return license;
        }

        public String getDoc() {
            return doc;
        }

        public String getLastVersion() {
            return lastVersion;
        }

        public Set<String> getAuthors() {
            return authors;
        }

        public Set<String> getVersions() {
            return versions;
        }
    }

    private Map<String,ModuleDetails> results = new TreeMap<String,ModuleDetails>();

    public void addResult(String moduleName, String doc, String license, SortedSet<String> authors, SortedSet<String> versions) {
        results.put(moduleName, new ModuleDetails(moduleName, doc, license, authors, versions));
    }

    public Collection<ModuleDetails> getResults() {
        return results.values();
    }
}
