package com.redhat.ceylon.cmr.api;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ModuleSearchResult {
    
    public static class ModuleDetails {
        private String name;
        private NavigableSet<ModuleVersionDetails> versions = new TreeSet<ModuleVersionDetails>();

        public ModuleDetails(String name) {
            this.name = name;
        }

        // This should only be used by the tests!
        public ModuleDetails(String name,
                String doc,
                String license,
                SortedSet<String> authors,
                SortedSet<String> versions,
                SortedSet<ModuleInfo> dependencies,
                SortedSet<String> types,
                Integer majorBinaryVersion,
                Integer minorBinaryVersion,
                boolean remote,
                String origin) {
            this.name = name;
            for (String v : versions) {
                ModuleVersionDetails mvd = new ModuleVersionDetails(v);
                mvd.setDoc(toNull(doc));
                mvd.setLicense(toNull(license));
                mvd.getAuthors().addAll(authors);
                mvd.getDependencies().addAll(dependencies);
                for (String t : types) {
                    ModuleVersionArtifact mva = new ModuleVersionArtifact(t, majorBinaryVersion, minorBinaryVersion);
                    mvd.getArtifactTypes().add(mva);
                }
                mvd.setRemote(remote);
                mvd.setOrigin(origin);
                this.versions.add(mvd);
            }
        }

        private String toNull(String str) {
            if(str != null && str.isEmpty())
                return null;
            return str;
        }
        public String getName() {
            return name;
        }

        public NavigableSet<ModuleVersionDetails> getVersions() {
            return versions;
        }

        public ModuleVersionDetails getLastVersion() {
            if (versions.isEmpty()) {
                return null;
            } else {
                return versions.last();
            }
        }

        public String getLicense() {
            return (getLastVersion() != null) ? getLastVersion().getLicense() : null;
        }

        public String getDoc() {
            return (getLastVersion() != null) ? getLastVersion().getDoc() : null;
        }

        public Integer getMajorBinaryVersion() {
            if (getLastVersion() != null && !getLastVersion().getArtifactTypes().isEmpty()) {
                return getLastVersion().getArtifactTypes().first().getMajorBinaryVersion();
            } else {
                return null;
            }
        }

        public Integer getMinorBinaryVersion() {
            if (getLastVersion() != null && !getLastVersion().getArtifactTypes().isEmpty()) {
                return getLastVersion().getArtifactTypes().first().getMinorBinaryVersion();
            } else {
                return null;
            }
        }

        public boolean isRemote() {
            return (getLastVersion() != null) ? getLastVersion().isRemote() : false;
        }

        public String getOrigin() {
            return (getLastVersion() != null) ? getLastVersion().getOrigin() : null;
        }

        public NavigableSet<String> getAuthors() {
            return (getLastVersion() != null) ? getLastVersion().getAuthors() : null;
        }

        public NavigableSet<ModuleInfo> getDependencies() {
            return (getLastVersion() != null) ? getLastVersion().getDependencies() : null;
        }

        public NavigableSet<ModuleVersionArtifact> getArtifactTypes() {
            return (getLastVersion() != null) ? getLastVersion().getArtifactTypes() : null;
        }
        
        @Override
        public String toString() {
            return "ModuleSearchResult ["
                    +"name: "+name
                    +", license: "+getLicense()
                    +", doc: "+getDoc()
                    +", authors: "+getAuthors()
                    +", versions: "+getVersions()
                    +", deps: "+getDependencies()
                    +", bin version: "
                        +((getMajorBinaryVersion() != null) ? getMajorBinaryVersion() : "")
                        +((getMinorBinaryVersion() != null) ? "." + getMinorBinaryVersion() : "")
                    +", remote: "+isRemote()
                    +", origin: "+getOrigin()
                    +"]";
        }
    }

    private NavigableMap<String,ModuleDetails> results = new TreeMap<String,ModuleDetails>();
    private long[] nextPagingInfo;
    private long start;
    private boolean hasMoreResults;

    public void addResult(String moduleName, ModuleDetails otherDetails) {
        ModuleDetails details;
        if (results.containsKey(moduleName)){
            // needs merge
            details = results.get(moduleName);
        } else {
            // new module
            details = new ModuleDetails(moduleName);
            results.put(moduleName, details);
        }
        details.getVersions().addAll(otherDetails.getVersions());
    }

    public void addResult(String moduleName, ModuleVersionDetails mvd) {
        ModuleDetails details;
        if (results.containsKey(moduleName)){
            // needs merge
            details = results.get(moduleName);
        } else {
            // new module
            details = new ModuleDetails(moduleName);
            results.put(moduleName, details);
        }
        details.getVersions().add(mvd);
    }

    public Collection<ModuleDetails> getResults() {
        return results.values();
    }

    public NavigableSet<String> getModuleNames() {
        return results.navigableKeySet();
    }

    public ModuleDetails getResult(String module) {
        return results.get(module);
    }

    public long[] getNextPagingInfo() {
        return nextPagingInfo;
    }

    public void setNextPagingInfo(long[] nextPagingInfo) {
        this.nextPagingInfo = nextPagingInfo;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public boolean getHasMoreResults() {
        return hasMoreResults;
    }

    public void setHasMoreResults(boolean hasMoreResults) {
        this.hasMoreResults = hasMoreResults;
    }

    public long getCount() {
        return results.size();
    }
}
