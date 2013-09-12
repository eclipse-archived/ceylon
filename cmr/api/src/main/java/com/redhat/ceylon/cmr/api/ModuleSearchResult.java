package com.redhat.ceylon.cmr.api;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ModuleSearchResult {
    
    public static class ModuleDetails {
        private String name, license, doc;
        private NavigableSet<String> authors = new TreeSet<String>();
        private NavigableSet<String> versions = new TreeSet<String>(VersionComparator.INSTANCE);
        private NavigableSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();
        private NavigableSet<String> artifactTypes = new TreeSet<String>();
        private Integer majorBinaryVersion;
        private Integer minorBinaryVersion;
        private boolean remote;
        private String origin;

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
            this.doc = toNull(doc);
            this.license = toNull(license);
            this.authors.addAll(authors);
            this.versions.addAll(versions);
            this.dependencies.addAll(dependencies);
            this.artifactTypes.addAll(types);
            this.majorBinaryVersion = majorBinaryVersion;
            this.minorBinaryVersion = minorBinaryVersion;
            this.remote = remote;
            this.origin = origin;
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
            return versions.last();
        }

        public Integer getMajorBinaryVersion() {
            return majorBinaryVersion;
        }

        public Integer getMinorBinaryVersion() {
            return minorBinaryVersion;
        }

        public boolean isRemote() {
            return remote;
        }

        public String getOrigin() {
            return origin;
        }

        public NavigableSet<String> getAuthors() {
            return authors;
        }

        public NavigableSet<String> getVersions() {
            return versions;
        }

        public NavigableSet<ModuleInfo> getDependencies() {
            return dependencies;
        }

        public NavigableSet<String> getArtifactTypes() {
            return artifactTypes;
        }
        
        @Override
        public String toString() {
            return "ModuleSearchResult ["
                    +"name: "+name
                    +", license: "+license
                    +", doc: "+doc
                    +", authors: "+authors
                    +", versions: "+versions
                    +", deps: "+dependencies
                    +", bin version: "
                        +((majorBinaryVersion != null) ? majorBinaryVersion : "")
                        +((minorBinaryVersion != null) ? "." + minorBinaryVersion : "")
                    +", remote: "+remote
                    +", origin: "+origin
                    +"]";
        }
    }

    private NavigableMap<String,ModuleDetails> results = new TreeMap<String,ModuleDetails>();
    private long[] nextPagingInfo;
    private long start;
    private boolean hasMoreResults;

    public void addResult(String moduleName,
            String doc,
            String license,
            SortedSet<String> authors,
            SortedSet<String> versions,
            SortedSet<ModuleInfo> dependencies,
            SortedSet<String> artifactTypes,
            Integer majorBinaryVersion,
            Integer minorBinaryVersion,
            boolean remote,
            String origin) {
        if(versions.isEmpty())
            throw new RuntimeException("Empty versions");
        if(results.containsKey(moduleName)){
            // needs merge
            ModuleDetails details = results.get(moduleName);
            String newLastVersion = versions.last();
            String oldLastVersion = details.getLastVersion();
            // only update doc/license if the newest version is newer than the previous newest
            if(VersionComparator.compareVersions(oldLastVersion, newLastVersion) == -1){
                details.doc = doc;
                details.license = license;
                details.dependencies.clear();
                details.dependencies.addAll(dependencies);
                details.artifactTypes.clear();
                details.artifactTypes.addAll(artifactTypes);
                details.majorBinaryVersion = majorBinaryVersion;
                details.minorBinaryVersion = minorBinaryVersion;
                details.remote = remote;
                details.origin = origin;
            }
            details.authors.addAll(authors);
            details.versions.addAll(versions);
        }else{
            // new module
            results.put(moduleName, new ModuleDetails(
                    moduleName,
                    doc,
                    license,
                    authors,
                    versions,
                    dependencies,
                    artifactTypes,
                    majorBinaryVersion,
                    minorBinaryVersion,
                    remote,
                    origin));
        }
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
