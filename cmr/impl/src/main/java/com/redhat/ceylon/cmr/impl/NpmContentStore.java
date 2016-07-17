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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.impl.AbstractContentStore;
import com.redhat.ceylon.cmr.impl.RootNode;
import com.redhat.ceylon.cmr.resolver.javascript.JavaScriptResolver;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * NPM content store.
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class NpmContentStore extends AbstractContentStore {
    private final File out;
    private final FileContentStore[] stores;
    private final FileContentStore outstore;
    
    public NpmContentStore(File[] roots, File out, Logger log, boolean offline) {
        super(log, offline, -1);
        assert(roots.length > 0);
        this.stores = new FileContentStore[roots.length];
        int i = 0;
        for (File root : roots) {
            stores[i++] = new FileContentStore(root);
        }
        this.out = out;
        if (out != null) {
            outstore = new FileContentStore(out);
        } else {
            outstore = null;
        }
    }

    public Iterable<File> getBaseDirectories() {
        ArrayList<File> baseDirectories = new ArrayList<>(stores.length);
        for (FileContentStore store : stores) {
            for (File baseDir : store.getBaseDirectories()) {
                baseDirectories.add(baseDir);
            }
        }
        return baseDirectories;
    }
    
    public OpenNode createRoot() {
        return new RootNode(this, this);
    }

    public OpenNode find(Node parent, String child) {
        DefaultNode node = null;
        if (hasContent(child) == false) {
            node = new DefaultNode(child);
            node.setContentMarker();
            return node;
        } else {
            if (ArtifactContext.getSuffixFromFilename(child).equals(ArtifactContext.JS)) {
                child = getTrueArtifactName(parent);
            }
            for (FileContentStore store : stores) {
                OpenNode result = store.find(parent, child);
                if (result != null) {
                    return result;
                }
            }
            installNpmModule(parent);
            return outstore.find(parent, child);
        }
    }

    public ContentHandle peekContent(Node node) {
        for (FileContentStore store : stores) {
            ContentHandle result = store.peekContent(node);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private String getTrueArtifactName(Node parent) {
        Node node = parent.getChild("package.json");
        try {
            File json = node.getContent(File.class);
            if (json.exists() && json.isFile() && json.canRead()) {
                //Parse json, get "main", that's the file we need
                try (FileReader reader = new FileReader(json)){
                    Map<String,Object> descriptor = JavaScriptResolver.readNpmDescriptor(json);
                    Object main = descriptor.get("main");
                    if (main instanceof String) {
                        return (String)main;
                    } else if (main == null) {
                        return "index.js";
                    } else {
                        throw new RepositoryException("Unexpected value for 'main' in NPM descriptor: " + json);
                    }
                }
            } else {
                throw new RepositoryException("NPM descriptor not found: " + json);
            }
        } catch (IOException ex) {
            throw new RepositoryException("Error reading NPM descriptor: " + out + "/package.json", ex);
        }
    }
    
    public ContentHandle getContent(Node node) throws IOException {
        ContentHandle result = peekContent(node);
        if (result != null) {
            return result;
        }
        
        if (outstore != null) {
            installNpmModule(node);
            return outstore.getContent(node);
        } else {
            // Until now we used peekContent() which doesn't throw
            // so we re-try with any store to get the error we want
            return stores[0].getContent(node);
        }
    }

    /**
     * 
     */
    public void installNpmModule(Node node) {
        try {
            if (!out.exists()) {
                out.mkdirs();
            }
            ArtifactContext ac = ArtifactContext.fromNode(node);
            String name = ac.getName();
            String version = ac.getVersion();
            if (log != null) log.debug("Installing NPM module " + name + "@" + version + " in " + out);
            ProcessBuilder pb = new ProcessBuilder()
                    .command("npm", "install", "-q", name + "@" + version)
                    .directory(out.getParentFile())
                    .inheritIO();
            
            Process p = pb.start();
            p.waitFor();
            if (p.exitValue() != 0) {
                throw new RepositoryException("NPM installer failed with exit code: " + p.exitValue());
            }
        } catch (InterruptedException | IOException ex) {
            throw new RepositoryException("Error running NPM installer (make sure 'npm' is installed and available in your PATH)", ex);
        }
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        if (outstore != null) {
            return outstore.putContent(node, stream, options);
        } else {
            return null;  // no output specified
        }
    }

    public OpenNode create(Node parent, String child) {
        if (outstore != null) {
            return outstore.create(parent, child);
        } else {
            return null;  // no output specified
        }
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
    }
    
    public String getDisplayString() {
        String name = "NPM";
        if (offline) {
            name += " (offline)";
        }
        return name;
    }

    @Override
    public boolean isHerd() {
        return false;
    }

    @Override
    public boolean canHandleFolders() {
        return false;
    }
}
