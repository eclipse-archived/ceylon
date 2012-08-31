package com.redhat.ceylon.cmr.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ModuleSearchResult {
    
    public static class ModuleDetails {
        private String name, license, doc, lastVersion;
        private Set<String> authors;
        private int versions;

        public ModuleDetails(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private Map<String,ModuleDetails> results = new TreeMap<String,ModuleDetails>();

    public void addResult(String moduleName) {
        results.put(moduleName, new ModuleDetails(moduleName));
    }

    public Collection<ModuleDetails> getResults() {
        return results.values();
    }
}
