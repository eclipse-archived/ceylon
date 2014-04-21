/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ContentFinder;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Abstract repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepository implements Repository {

    private static final Comparator<? super Node> AlphabeticalNodeComparator = new Comparator<Node>() {
        @Override
        public int compare(Node a, Node b) {
            return a.getLabel().compareToIgnoreCase(b.getLabel());
        }
    };

    private OpenNode root;

    public AbstractRepository(OpenNode root) {
        this.root = root;
    }

    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        final String name = context.getName();
        final List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        final String version = context.getVersion();
        if (RepositoryManager.DEFAULT_MODULE.equals(name) == false && version != null)
            tokens.add(version); // add version
        return tokens;
    }

    protected List<String> getDefaultParentPath(ArtifactContext context) {
        List<String> tokens = LookupCaching.getTokens(getClass());
        if (tokens == null) {
            tokens = getDefaultParentPathInternal(context);
            if (LookupCaching.isEnabled()) {
                LookupCaching.setTokens(getClass(), tokens);
            }
        }
        return tokens;
    }

    protected static String[] getArtifactNames(String name, String version, String[] suffixes) {
        String[] names = new String[suffixes.length];
        for (int i = 0; i < suffixes.length; i++) {
            names[i] = getArtifactName(name, version, suffixes[i]);
        }
        return names;
    }

    private static String getArtifactName(String name, String version, String suffix) {
        if (ArtifactContext.DOCS.equals(suffix))
            return ArtifactContext.DOCS;
        else if (ArtifactContext.MODULE_PROPERTIES.equals(suffix))
            return ArtifactContext.MODULE_PROPERTIES;
        else if (ArtifactContext.MODULE_XML.equals(suffix))
            return ArtifactContext.MODULE_XML;
        else if (RepositoryManager.DEFAULT_MODULE.equals(name))
            return name + suffix;
        else
            return buildArtifactNames(name, version, suffix);
    }

    protected static String buildArtifactNames(String name, String version, String suffix) {
        return name + "-" + version + suffix;
    }

    public OpenNode getRoot() {
        return root;
    }

    public Node findParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        return NodeUtils.getNode(root, tokens);
    }

    public String[] getArtifactNames(ArtifactContext context) {
        return getArtifactNames(context.getName(), context.getVersion(), context.getSuffixes());
    }

    public OpenNode createParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        OpenNode current = root;
        for (String token : tokens) {
            current = current.addNode(token);
        }
        return current;
    }

    protected abstract ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node);

    public ArtifactResult getArtifactResult(RepositoryManager manager, Node node) {
        return (node != null) ? getArtifactResultInternal(manager, node) : null;
    }

    @Override
    public int hashCode() {
        return root.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Repository == false)
            return false;

        final Repository daca = (Repository) obj;
        return root.equals(daca.getRoot());
    }

    @Override
    public String toString() {
        return "Repository (" + getClass().getName() + ") for root: " + root;
    }

    @Override
    public String getDisplayString() {
        return root.getDisplayString();
    }

    @Override
    public void refresh(boolean recurse) {
        root.refresh(recurse);
    }

    @Override
    public void completeModules(ModuleQuery query, ModuleSearchResult result) {
        // check for delegate
        ContentFinder delegate = root.getService(ContentFinder.class);
        if (delegate != null) {
            delegate.completeModules(query, result);
            return;
        }
        // we NEED the -1 limit here to get empty tokens
        String[] paths = query.getName().split("\\.", -1);
        // find the right parent
        Node parent = root;
        for (int i = 0; i < paths.length - 1; i++) {
            parent = parent.getChild(paths[i]);
            // no completion from here
            if (parent == null)
                return;
        }
        String lastPart = paths[paths.length - 1];
        // now find a matching child
        for (Node child : parent.getChildren()) {
            if (child.getLabel().startsWith(lastPart)) {
                collectArtifacts(child, query, result);
            }
        }
    }

    private void collectArtifacts(Node node, ModuleQuery lookup, ModuleSearchResult result) {
        // Winner of the less aptly-named method
        boolean isFolder = !node.hasBinaries();
        if (isFolder) {
            if (node.getLabel().equals(ArtifactContext.DOCS))
                return;
            Ret ret = new Ret();
            if (hasChildrenContainingArtifact(node, lookup, ret)) {
                // we have artifact children, are they of the right type?
                if (ret.foundRightType) {
                    // collect them
                    String moduleName = toModuleName(node);
                    addSearchResult(result, moduleName, node, lookup);
                }
            } else {
                // collect in the children
                List<Node> sortedChildren = new ArrayList<Node>();
                for (Node child : node.getChildren())
                    sortedChildren.add(child);
                Collections.sort(sortedChildren, AlphabeticalNodeComparator);
                for (Node child : sortedChildren) {
                    collectArtifacts(child, lookup, result);
                }
            }
        }
    }

    private boolean hasChildrenContainingArtifact(Node node, ModuleQuery lookup, Ret ret) {
        // We don't look directly at our children, we want the children's children, because if there's
        // nothing in those children it means either this is an empty folder, or its children contain
        // artifacts (in which case we don't want to match it since its name must be a version component),
        // or we could only find artifacts of the wrong type.

        // This allows us to never match the default module, since it's at "/default/default.car" which
        // cannot match this rule. Normal modules always have at least one "/name/version/bla.car".
        boolean found = false;
        for (Node child : node.getChildren()) {
            String name = child.getLabel();
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            if (isFolder && !name.equals(ArtifactContext.DOCS) && containsArtifact(node, child, lookup, ret)){
                // stop if we found the right type
                if(ret.foundRightType)
                    return true;
                // keep looking for the other versions, perhaps we will find the right type
                found = true;
            }
        }
        return found;
    }

    private boolean containsArtifact(Node moduleNode, Node versionNode, ModuleQuery lookup, Ret ret) {
        String module = toModuleName(moduleNode);
        String version = versionNode.getLabel();
        boolean foundArtifact = false;
        for (Node child : versionNode.getChildren()) {
            String name = child.getLabel();
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            if (isFolder) {
                // do not recurse
            } else if (isArtifactOfType(name, child, module, version, lookup)) {
                // we found what we were looking for
                ret.foundRightType = true;
                return true;
            } else if (isArtifact(name, module, version)) {
                // we found something, but not the type we wanted
                foundArtifact = true;
            }
        }
        return foundArtifact;
    }

    private boolean isArtifactOfType(String name, Node node, String module, String version, ModuleQuery lookup) {
        switch (lookup.getType()) {
            case ALL:
                return true;
            case CODE:
                return ((getArtifactName(module, version, ArtifactContext.CAR).equals(name) || getArtifactName(module, version, ArtifactContext.JS).equals(name))
                        && checkBinaryVersion(module, node, lookup))
                        || getArtifactName(module, version, ArtifactContext.JAR).equals(name);
            case JS:
                return getArtifactName(module, version, ArtifactContext.JS).equals(name)
                        && checkBinaryVersion(module, node, lookup);
            case JVM:
                return (getArtifactName(module, version, ArtifactContext.CAR).equals(name)
                        && checkBinaryVersion(module, node, lookup))
                        || getArtifactName(module, version, ArtifactContext.JAR).equals(name);
            case SRC:
                return getArtifactName(module, version, ArtifactContext.SRC).equals(name);
        }
        return false;
    }

    private boolean checkBinaryVersion(String module, Node node, ModuleQuery lookup) {
        if (lookup.getBinaryMajor() == null && lookup.getBinaryMinor() == null)
            return true;
        try {
            File file = node.getContent(File.class);
            if (file == null)
                return false; // can't verify

            String suffix = ArtifactContext.getSuffixFromNode(node);
            ModuleInfoReader reader = getModuleInfoReader(suffix);
            if (reader != null) {
                int[] versions = reader.getBinaryVersions(module, file);
                if (versions == null)
                    return false; // can't verify
                if (lookup.getBinaryMajor() != null && versions[0] != lookup.getBinaryMajor())
                    return false;
                if (lookup.getBinaryMinor() != null && versions[1] != lookup.getBinaryMinor())
                    return false;
                return true;
            }
        } catch (Exception x) {
            // can't verify
        }
        return false;
    }

    /*
     * This method is almost like hasChildrenContainingArtifact but it scans for any type of artifact and records
     * the specific one we want in an out param
     */
    private boolean hasChildrenContainingAnyArtifact(Node moduleNode, ModuleQuery query, Ret ret) {
        // We don't look directly at our children, we want the children's children, because if there's
        // nothing in those children it means either this is an empty folder, or its children contain
        // artifacts (in which case we don't want to match it since its name must be a version component),
        // or we could only find artifacts of the wrong type.

        // This allows us to never match the default module, since it's at "/default/default.car" which
        // cannot match this rule. Normal modules always have at least one "/name/version/bla.car".
        for (Node versionNode : moduleNode.getChildren()) {
            String name = versionNode.getLabel();
            // Winner of the less aptly-named method
            boolean isFolder = !versionNode.hasBinaries();
            if (isFolder
                    && !name.equals(ArtifactContext.DOCS)
                    && containsAnyArtifact(moduleNode, versionNode, query, ret))
                return true;
        }
        // could not find any
        return false;
    }

    /*
     * This method is almost like containsArtifact but it scans for any type of artifact and records
     * the specific one we want in an out param. It's also not recursive so it only scans the current children.
     */
    private boolean containsAnyArtifact(Node moduleNode, Node versionNode, ModuleQuery query, Ret ret) {
        boolean foundArtifact = false;
        String version = versionNode.getLabel();
        String module = toModuleName(moduleNode);
        for (Node child : versionNode.getChildren()) {
            String name = child.getLabel();
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            if (isFolder) {
                // we don't recurse
            } else if (isArtifactOfType(name, child, module, version, query)
                    && matchesSearch(name, child, versionNode, query)) {
                // we found what we were looking for
                ret.foundRightType = true;
                return true;
            } else if (isArtifact(name, module, version)) {
                // we found something, but not the type we wanted
                foundArtifact = true;
            }
        }
        return foundArtifact;
    }

    private boolean matchesSearch(String name, Node artifact, Node versionNode, ModuleQuery query) {
        // match on the module name first
        Node moduleNode = NodeUtils.firstParent(versionNode);
        // can't happen but hey
        if (moduleNode == null)
            return false;
        String moduleName = toModuleName(moduleNode);
        if (moduleName.toLowerCase().contains(query.getName()))
            return true;
        // now search on the metadata
        Node infoArtifact = getBestInfoArtifact(versionNode);
        return matchFromCar(infoArtifact, moduleName, query.getName());
    }

    private boolean matchFromCar(Node artifact, String moduleName, String query) {
        try {
            File file = artifact.getContent(File.class);
            if (file != null) {
                ModuleInfoReader reader = getModuleInfoReader(artifact);
                if (reader != null) {
                    return reader.matchesModuleInfo(moduleName, file, query);
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }

    private boolean isArtifact(String name, String module, String version) {
        return getArtifactName(module, version, ArtifactContext.JS).equals(name)
                || getArtifactName(module, version, ArtifactContext.CAR).equals(name)
                || getArtifactName(module, version, ArtifactContext.JAR).equals(name)
                || getArtifactName(module, version, ArtifactContext.SRC).equals(name);
    }

    protected String toModuleName(Node node) {
        String path = NodeUtils.getFullPath(node, ".");
        // That's sort of an invariant, but let's be safe
        if (path.startsWith("."))
            path = path.substring(1);
        return path;
    }

    @Override
    public void completeVersions(ModuleVersionQuery lookup, ModuleVersionResult result) {
        // check for delegate
        ContentFinder delegate = root.getService(ContentFinder.class);
        if (delegate != null) {
            delegate.completeVersions(lookup, result);
            return;
        }
        // FIXME: handle default module
        // FIXME: we should really get this splitting done somewhere in common
        String name = lookup.getName();
        Node namePart = NodeUtils.getNode(root, Arrays.asList(name.split("\\.")));
        if (namePart == null)
            return;
        String[] suffixes = lookup.getType().getSuffixes();
        String memberName = lookup.getMemberName();
        // now each child is supposed to be a version part, let's verify that
        for (Node child : namePart.getChildren()) {
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            // ignore non-folders
            if (!isFolder)
                continue;
            // now make sure we can find the artifact we're looking for in there
            String version = child.getLabel();
            // optional filter on version
            if (lookup.getVersion() != null && !version.startsWith(lookup.getVersion()))
                continue;
            // avoid duplicates
            if (result.hasVersion(version))
                continue;
            // try every known suffix
            boolean found = false;
            boolean foundInfo = false;
            ModuleVersionDetails mvd = new ModuleVersionDetails(version);
            for (String suffix : suffixes) {
                String artifactName = getArtifactName(name, version, suffix);
                Node artifact = child.getChild(artifactName);
                if (artifact == null)
                    continue;
                // is it the right version?
                if ((suffix.equals(ArtifactContext.CAR) || suffix.equals(ArtifactContext.JS)) && !checkBinaryVersion(name, artifact, lookup))
                    continue;
                // we found the artifact: let's notify
                found = true;
                // let's see if we can extract some information
                try {
                    File file = artifact.getContent(File.class);
                    if (file != null) {
                        ModuleInfoReader reader = getModuleInfoReader(suffix);
                        if (reader != null) {
                            ModuleVersionDetails mvd2 = reader.readModuleInfo(name, file, memberName != null);
                            SortedSet<String> matchingMembers = null;
                            if (memberName != null) {
                                matchingMembers = matchMembers(mvd2, lookup);
                                if (matchingMembers.isEmpty()) {
                                    // We haven't found a matching member in the module so we
                                    // just continue to the next suffix/artifact if any
                                    continue;
                                }
                                mvd.getMembers().addAll(matchingMembers);
                            }
                            foundInfo = true;
                            if (mvd2.getDoc() != null) {
                                mvd.setDoc(mvd2.getDoc());
                            }
                            if (mvd2.getLicense() != null) {
                                mvd.setLicense(mvd2.getLicense());
                            }
                            mvd.getAuthors().addAll(mvd2.getAuthors());
                            mvd.getDependencies().addAll(mvd2.getDependencies());
                            mvd.getArtifactTypes().addAll(mvd2.getArtifactTypes());
                        } else {
                            if (memberName == null) {
                                // We didn't get any information but we'll at least add the artifact type to the result
                                mvd.getArtifactTypes().add(new ModuleVersionArtifact(suffix, null, null));
                            }
                        }
                    }
                } catch (Exception e) {
                    // bah
                }
            }
            // NB: When searching for members it's not enough to have found
            // just any artifact, we need to make sure we were able to
            // read the artifac's information
            if ((found && memberName == null) || foundInfo) {
                mvd.setRemote(root.isRemote());
                mvd.setOrigin(getDisplayString());
                result.addVersion(mvd);
            }
        }
    }

    private static class Ret {
        public boolean foundRightType;
        public long found;
        public boolean stopSearching;
    }

    private static class GetOut extends Exception {
    }

    @Override
    public boolean isSearchable() {
        // check for delegate
        ContentFinder delegate = root.getService(ContentFinder.class);
        if (delegate != null) {
            return delegate.isSearchable();
        }
        return true;
    }
    
    @Override
    public void searchModules(ModuleQuery query, ModuleSearchResult result) {
        // check for delegate
        ContentFinder delegate = root.getService(ContentFinder.class);
        if (delegate != null) {
            delegate.searchModules(query, result);
            return;
        }
        // do the searching the hard way
        try {
            searchModules(root, query, result, new Ret());
        } catch (GetOut e) {
            // easy out
        }
    }

    private void searchModules(Node parent, ModuleQuery query, ModuleSearchResult result, Ret ret) throws GetOut {
        List<Node> sortedChildren = new ArrayList<Node>();
        for (Node child : parent.getChildren())
            sortedChildren.add(child);
        Collections.sort(sortedChildren, AlphabeticalNodeComparator);
        for (Node child : sortedChildren) {
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            // ignore non-folders
            if (!isFolder)
                continue;
            // is it a module? does it contains artifacts?
            ret.foundRightType = false;
            if (!child.getLabel().equals(ArtifactContext.DOCS)) { // safety check
                if (hasChildrenContainingAnyArtifact(child, query, ret)) {
                    // does it contain an artifact of the type we're looking for?
                    if (ret.foundRightType) {
                        // check if we were already done but were checking for a next results
                        if (ret.stopSearching) {
                            // we already found enough results but were checking if there
                            // were more results to be found for paging, so record that
                            // and stop
                            result.setHasMoreResults(true);
                            throw new GetOut();
                        }
                        if (query.getStart() == null || ret.found++ >= query.getStart()) {
                            // are we interested in this result or did we need to skip it?
                            String moduleName = toModuleName(child);
                            addSearchResult(result, moduleName, child, query);
                            // stop if we're done searching
                            if (query.getStart() != null
                                    && query.getCount() != null
                                    && ret.found >= query.getStart() + query.getCount()) {
                                // we're done, but we want to see if there's at least one more result
                                // to be found so we can tell clients there's a next page
                                ret.stopSearching = true;
                            }
                        }
                    }
                } else {
                    // it doesn't contain artifacts, it's probably leading to modules
                    searchModules(child, query, result, ret);
                }
            }
        }
    }

    private void addSearchResult(ModuleSearchResult result, String moduleName, Node namePart, ModuleQuery query) {
        SortedSet<String> versions = new TreeSet<String>();
        String[] suffixes = query.getType().getSuffixes();
        for (Node child : namePart.getChildren()) {
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            // ignore non-folders
            if (!isFolder)
                continue;
            // now make sure at least one of the artifacts we're looking for is in there
            String version = child.getLabel();
            // try every known suffix
            for (String suffix : suffixes) {
                String artifactName = getArtifactName(moduleName, version, suffix);
                Node artifact = child.getChild(artifactName);
                if (artifact != null) {
                    // we found the artifact: store it
                    versions.add(version);
                    break;
                }
            }
        }
        // sanity check
        if (versions.isEmpty()) {
            // We didn't  find any versions so we silently skip this result
            return;
        }
        // find the latest version
        String latestVersion = versions.last();
        Node versionChild = namePart.getChild(latestVersion);
        if (versionChild == null)
            throw new RuntimeException("Assertion failed: we didn't find the version child for " + moduleName + "/" + latestVersion);
        
        Node artifact = getBestInfoArtifact(versionChild);

        String memberName = query.getMemberName();
        ModuleVersionDetails mvd = null;
        if (artifact != null) {
            try {
                File file = artifact.getContent(File.class);
                if (file != null) {
                    ModuleInfoReader reader = getModuleInfoReader(artifact);
                    if (reader != null) {
                        mvd = reader.readModuleInfo(moduleName, file, memberName != null);
                        if (memberName != null) {
                            SortedSet<String> matchingMembers = matchMembers(mvd, query);
                            if (matchingMembers.isEmpty()) {
                                // We haven't found a matching member in the module so we
                                // just continue to the next suffix/artifact if any
                                return;
                            }
                            mvd.setMembers(matchingMembers);
                        }
                    }
                }
            } catch (Exception e) {
                // bah
                if (memberName != null) {
                    // We couldn't check the artifact for its members so we
                    // just exit without adding anything to the search result
                    return;
                }
            }
        } else {
            if (memberName != null) {
                // We haven't found an artifact to check for members so we
                // just exit without adding anything to the search result
                return;
            }
        }
        if (mvd == null) {
            // We didn't get any useful information, so we'll just create a dummy
            mvd = new ModuleVersionDetails(latestVersion);
        }
        mvd.setRemote(root.isRemote());
        mvd.setOrigin(getDisplayString());

        result.addResult(moduleName, mvd);
    }

    private SortedSet<String> matchMembers(ModuleVersionDetails mvd, ModuleQuery query) {
        // We're actually looking for a module containing a specific member
        SortedSet<String> found = new TreeSet<String>();
        String name = query.getMemberName();
        boolean matchPackage = query.isMemberSearchPackageOnly();
        if (query.isMemberSearchExact()) {
            for (String member : mvd.getMembers()) {
                if (matchPackage) {
                    member = packageName(member);
                }
                if (member.equals(name)) {
                    found.add(member);
                }
            }
        } else {
            name = name.toLowerCase();
            for (String member : mvd.getMembers()) {
                if (matchPackage) {
                    member = packageName(member);
                }
                if (member.toLowerCase().contains(name)) {
                    found.add(member);
                }
            }
        }
        return found;
    }

    // Given a fully qualified member name return it's package
    // (or an empty string if it's not part of any package)
    private String packageName(String memberName) {
        int p = memberName.lastIndexOf('.');
        if (p >= 0) {
            return memberName.substring(0, p);
        } else {
            return "";
        }
    }
    
    private Node getBestInfoArtifact(Node versionNode) {
        String moduleName = toModuleName(NodeUtils.firstParent(versionNode));
        String version = versionNode.getLabel();
        String artifactName = getArtifactName(moduleName, version, ArtifactContext.CAR);
        Node artifact = versionNode.getChild(artifactName);
        if (artifact == null) {
            artifactName = getArtifactName(moduleName, version, ArtifactContext.JS);
            artifact = versionNode.getChild(artifactName);
            if (artifact == null) {
                artifactName = getArtifactName(moduleName, version, ArtifactContext.JAR);
                artifact = versionNode.getChild(artifactName);
            }
        }
        return artifact;
    }
    
    private ModuleInfoReader getModuleInfoReader(Node infoNode) {
        String suffix = ArtifactContext.getSuffixFromNode(infoNode);
        return getModuleInfoReader(suffix);
    }
    
    private ModuleInfoReader getModuleInfoReader(String suffix) {
        if (ArtifactContext.CAR.equalsIgnoreCase(suffix)) {
            return BytecodeUtils.INSTANCE;
        } else if (ArtifactContext.JAR.equalsIgnoreCase(suffix)) {
            return JarUtils.INSTANCE;
        } else if (ArtifactContext.JS.equalsIgnoreCase(suffix)) {
            return JSUtils.INSTANCE;
        } else {
            return null;
        }
    }
}
