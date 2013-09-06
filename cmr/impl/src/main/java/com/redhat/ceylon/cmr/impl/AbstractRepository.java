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
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.BytecodeUtils.ModuleInfoCallback;
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

    protected static String getArtifactName(String name, String version, String suffix) {
        if (ArtifactContext.DOCS.equals(suffix))
            return ArtifactContext.DOCS;
        else if (ArtifactContext.DOCS_ZIPPED.equals(suffix))
            return ArtifactContext.DOCS_ZIPPED;
        else if (ArtifactContext.MODULE_PROPERTIES.equals(suffix))
            return ArtifactContext.MODULE_PROPERTIES;
        else if (ArtifactContext.MODULE_XML.equals(suffix))
            return ArtifactContext.MODULE_XML;
        else if (RepositoryManager.DEFAULT_MODULE.equals(name))
            return name + suffix;
        else
            return buildArtifactName(name, version, suffix);
    }

    protected static String buildArtifactName(String name, String version, String suffix) {
        return name + "-" + version + suffix;
    }

    public OpenNode getRoot() {
        return root;
    }

    public Node findParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        return NodeUtils.getNode(root, tokens);
    }

    public String getArtifactName(ArtifactContext context) {
        return getArtifactName(context.getName(), context.getVersion(), context.getSuffix());
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
                    addSearchResult(result, moduleName, node, lookup.getType());
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
        for (Node child : node.getChildren()) {
            String name = child.getLabel();
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            if (isFolder && !name.equals(ArtifactContext.DOCS) && containsArtifact(node, child, lookup, ret))
                return true;
        }
        // could not find any
        return false;
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
            case JS:
                return getArtifactName(module, version, ArtifactContext.JS).equals(name);
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

            int[] versions = BytecodeUtils.getBinaryVersions(module, file);
            if (versions == null)
                return false; // can't verify
            if (lookup.getBinaryMajor() != null && versions[0] != lookup.getBinaryMajor())
                return false;
            if (lookup.getBinaryMinor() != null && versions[1] != lookup.getBinaryMinor())
                return false;
            return true;
        } catch (Exception x) {
            // can't verify
            return false;
        }
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
        // this is easy for cars
        if (query.getType() == Type.JVM) {
            return matchFromCar(artifact, moduleName, query.getName());
        }
        // for now, for SRC and JS we will fall back to the JVM archive if present, though in the
        // future we might try to load the metadata from the SRC or JS archives
        String carName;
        switch (query.getType()) {
            case JS:
                carName = name.substring(0, name.length() - ArtifactContext.JS.length()) + ArtifactContext.CAR;
                break;
            case SRC:
                carName = name.substring(0, name.length() - ArtifactContext.SRC.length()) + ArtifactContext.CAR;
                break;
            default:
                // shouldn't happen
                return false;
        }
        Node carArtifact = versionNode.getChild(carName);
        // did we find it?
        if (carArtifact == null)
            return false;
        // try to match from the car
        return matchFromCar(carArtifact, moduleName, query.getName());
    }

    private boolean matchFromCar(Node artifact, String moduleName, String query) {
        try {
            File file = artifact.getContent(File.class);
            return file != null && BytecodeUtils.matchesModuleInfo(moduleName, file, query);
        } catch (Exception e) {
            return false;
        }
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
            for (String suffix : suffixes) {
                String artifactName = getArtifactName(name, version, suffix);
                Node artifact = child.getChild(artifactName);
                if (artifact == null)
                    continue;
                // is it the right version?
                if (suffix.equals(ArtifactContext.CAR) && !checkBinaryVersion(name, artifact, lookup))
                    continue;
                // we found the artifact: let's notify
                final ModuleVersionDetails newVersion = result.addVersion(version);
                if (newVersion != null) {
                    try {
                        File file = artifact.getContent(File.class);
                        if (file != null)
                            BytecodeUtils.readModuleInfo(name, file, new ModuleInfoCallback() {
                                @Override
                                public void storeInfo(String doc, String license, String[] authors, ModuleInfo[] dependencies) {
                                    newVersion.setDoc(doc);
                                    newVersion.setLicense(license);
                                    if (authors != null)
                                        newVersion.getAuthors().addAll(Arrays.asList(authors));
                                    if (dependencies != null)
                                        newVersion.getDependencies().addAll(Arrays.asList(dependencies));
                                }
                            });
                    } catch (Exception e) {
                        // bah
                    }
                }
                break;
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
                        addSearchResult(result, moduleName, child, query.getType());
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
                if (!child.getLabel().equals(ArtifactContext.DOCS)) { // safety check
                    searchModules(child, query, result, ret);
                }
            }
        }
    }

    private void addSearchResult(ModuleSearchResult result, String moduleName, Node namePart, Type type) {
        SortedSet<String> versions = new TreeSet<String>();
        String[] suffixes = type.getSuffixes();
        for (Node child : namePart.getChildren()) {
            // Winner of the less aptly-named method
            boolean isFolder = !child.hasBinaries();
            // ignore non-folders
            if (!isFolder)
                continue;
            // now make sure we can find the artifact we're looking for in there
            String version = child.getLabel();
            // try every known suffix
            for (String suffix : suffixes) {
                String artifactName = getArtifactName(moduleName, version, suffix);
                Node artifact = child.getChild(artifactName);
                if (artifact == null)
                    continue;
                // we found the artifact: store it
                versions.add(version);
                break;
            }
        }
        // sanity check
        if (versions.isEmpty())
            throw new RuntimeException("Assertion failed: we didn't find any version of the proper type for " + moduleName);
        // find the latest version
        String latestVersion = versions.last();
        Node versionChild = namePart.getChild(latestVersion);
        if (versionChild == null)
            throw new RuntimeException("Assertion failed: we didn't find the version child for " + moduleName + "/" + latestVersion);
        String artifactName = getArtifactName(moduleName, latestVersion, ArtifactContext.CAR);
        Node artifact = versionChild.getChild(artifactName);

        // we don't really have mutable captures yet :(
        final String[] doc = new String[1];
        final String[] license = new String[1];
        final SortedSet<String> authors = new TreeSet<String>();
        final SortedSet<ModuleInfo> dependencies = new TreeSet<ModuleInfo>();

        if (artifact != null) {
            try {
                File file = artifact.getContent(File.class);
                if (file != null) {
                    BytecodeUtils.readModuleInfo(moduleName, file, new ModuleInfoCallback() {
                        @Override
                        public void storeInfo(String doc2, String license2, String[] authors2, ModuleInfo[] dependencies2) {
                            doc[0] = doc2;
                            license[0] = license2;
                            if (authors2 != null)
                                authors.addAll(Arrays.asList(authors2));
                            if (dependencies2 != null)
                                dependencies.addAll(Arrays.asList(dependencies2));
                        }
                    });
                }
            } catch (Exception e) {
                // bah
            }
        }

        result.addResult(moduleName, doc[0], license[0], authors, versions, dependencies);
    }
}
