/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package com.redhat.ceylon.cmr.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.Repository;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * Artifact lookup context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SuppressWarnings("serial")
public class ArtifactContext implements Serializable, ContentOptions {
    public static final String CAR = ".car";
    public static final String JAR = ".jar";
    public static final String JS_MODEL = "-model.js";
    public static final String JS = ".js";
    public static final String DART = ".dart";
    public static final String DART_MODEL = "-dartmodel.json";
    public static final String RESOURCES = "module-resources";
    public static final String SRC = ".src";
    public static final String MAVEN_SRC = "-sources.jar";
    public static final String DOCS = "module-doc";
    public static final String MODULE_PROPERTIES = "module.properties";
    public static final String MODULE_XML = "module.xml";
    public static final String SHA1 = ".sha1";
    public static final String INFO = ".info";
    public static final String SCRIPTS_ZIPPED = ".scripts.zip";
    public static final String ZIP = ".zip";

    // IMPORTANT: Makes sure the elements in this array are ordered in such
    // a way that no ambiguities can occur when matching them one at a time.
    // NB: SHA1 and ZIP are not part of this list because they are supposed
    // to be "composed" with other suffixes
    private static final String fileSuffixes[] = {
        CAR, JAR, JS_MODEL, JS, DART, DART_MODEL, RESOURCES, SRC, MAVEN_SRC,
        DOCS, SCRIPTS_ZIPPED
    };
    
    private static final String composedSuffixes[] = {
        ZIP, SHA1
    };
    
    private static final String fileNames[] = {
        MODULE_PROPERTIES, MODULE_XML
    };
    
    private static final String directoryNames[] = {
        RESOURCES, DOCS
    };

    public static final String[] allSuffixes() {
        ArrayList<String> all = new ArrayList<>(fileSuffixes.length + fileNames.length + directoryNames.length);
        all.addAll(Arrays.asList(fileSuffixes));
        all.addAll(Arrays.asList(fileNames));
        all.addAll(Arrays.asList(directoryNames));
        String[] tmp = new String[all.size()];
        return all.toArray(tmp);
    }
    
    private String name;
    private String version;
    private String[] suffixes = {CAR};
    private boolean localOnly;
    private boolean ignoreSHA;
    private boolean ignoreCache;
    private boolean throwErrorIfMissing;
    private boolean forceOperation;
    private boolean forceDescriptorCheck;
    private boolean ignoreDependencies;
    private ArtifactCallback callback;
    private Repository repository;

    public ArtifactContext(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public ArtifactContext(String name, String version, String... suffixes) {
        this(name, version);
        this.suffixes = suffixes;
    }

    private ArtifactContext(String name, String version, Repository repository, String... suffixes) {
        this(name, version);
        this.suffixes = suffixes;
        this.repository = repository;
    }

    public ArtifactContext() {
    }

    // Returns true if the suffix that was passed can have
    // an associated .sha1 suffix
    private static boolean isShaAllowed(String suffix) {
        if (MODULE_PROPERTIES.equals(suffix) || MODULE_XML.equals(suffix)
                || suffix.endsWith(SHA1) || isDirectoryName(suffix)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the SHA1 version of the current context or null
     * if the current context is already an SHA1 artifact or
     * if the artifact doesn't allow or need signing
     * @return A new SHA1 context
     */
    public ArtifactContext getSha1Context() {
        if (suffixes.length == 1) {
            if (isShaAllowed(suffixes[0])) {
                String[] sha1Suffixes = { suffixes[0] + SHA1 };
                return new ArtifactContext(name, version, sha1Suffixes);
            }
        }
        return null;
    }

    /**
     * Returns the ZIP version of the current context or null
     * if the current context is already a ZIP artifact or
     * if the artifact doesn't allow or need zipping
     * @return A new ZIP context
     */
    public ArtifactContext getZipContext() {
        if (suffixes.length == 1) {
            if (!suffixes[0].endsWith(ZIP) && isDirectoryName(suffixes[0])) {
                String[] sha1Suffixes = { suffixes[0] + ZIP };
                return new ArtifactContext(name, version, sha1Suffixes);
            }
        }
        return null;
    }

    public ArtifactContext getSuffixContext(String... suffixes) {
        ArtifactContext ac = copy();
        ac.setSuffixes(suffixes);
        return ac;
    }

    public ArtifactContext getDocsContext() {
        return getSuffixContext(DOCS);
    }

    public ArtifactContext getResourcesContext() {
        return getSuffixContext(RESOURCES);
    }

    public ArtifactContext getModuleProperties() {
        return getSuffixContext(MODULE_PROPERTIES);
    }

    public ArtifactContext getModuleXml() {
        return getSuffixContext(MODULE_XML);
    }

    public ArtifactContext getSibling(ArtifactResult result, String... suffixes) {
        ArtifactContext sibling = new ArtifactContext(result.name(), result.version(), result.repository(), suffixes);
        sibling.copySettingsFrom(this);
        return sibling;
    }

    public void toNode(Node node) {
        if (node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            on.addNode(INFO, this);
        }
    }

    public static ArtifactContext fromNode(Node node) {
        final Node ac = (node instanceof OpenNode) ? ((OpenNode) node).peekChild(INFO) : node.getChild(INFO);
        return ac != null ? ac.getValue(ArtifactContext.class) : null;
    }

    public static void removeNode(Node node) {
        if (node instanceof OpenNode) {
            final OpenNode on = (OpenNode) node;
            if (on.peekChild(INFO) != null) {
                on.removeNode(INFO);
            }
        }
    }

    public boolean isMaven(){
        return name != null
                && (name.startsWith("maven:")
                        || name.indexOf(':') != -1);
    }
    
    public String getName() {
        if(name != null && name.startsWith("maven:"))
            return name.substring("maven:".length());
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(String... suffixes) {
        this.suffixes = suffixes;
    }

    public String getSingleSuffix() {
        if (suffixes.length != 1) {
            throw new RepositoryException("ArtifactContext should have a single suffix");
        }
        return suffixes[0];
    }

    public static String getSuffixFromNode(Node node) {
        String fileName = node.getLabel();
        return getSuffixFromFilename(fileName);
    }

    /**
     * Returns the artifact "suffix" of the file name that gets passed.
     * The word "suffix" is in quotes because it can also return one
     * of the "full name" results like "module-docs" or "module.properties"
     */
    public static String getSuffixFromFilename(String fileName) {
        String suffix = getSuffixFromFilename_(fileName);
        if (SHA1.equals(suffix)) {
            fileName = fileName.substring(0, fileName.length() - 5);
            return (fileName.isEmpty()) ? SHA1 : getSuffixFromFilename(fileName) + SHA1;
        } else if (ZIP.equals(suffix)) {
            fileName = fileName.substring(0, fileName.length() - 4);
            return (fileName.isEmpty()) ? ZIP : getSuffixFromFilename(fileName) + ZIP;
        }
        return suffix;
    }

    private static String getSuffixFromFilename_(String fileName) {
        for (String suffix : fileNames) {
            if (fileName.equals(suffix)) {
                return suffix;
            }
        }
        for (String suffix : directoryNames) {
            if (fileName.equals(suffix)) {
                return suffix;
            }
        }
        for (String suffix : fileSuffixes) {
            if (fileName.endsWith(suffix)) {
                return suffix;
            }
        }
        for (String suffix : composedSuffixes) {
            if (fileName.endsWith(suffix)) {
                return suffix;
            }
        }
        throw new RepositoryException("Unknown suffix in " + fileName);
    }

    public static String getArtifactName(String name, String version, String suffix) {
        if (suffix.endsWith(SHA1)) {
            return getArtifactName(name, version, suffix.substring(0, suffix.length() - 5)) + SHA1;
        } else {
            if (isDirectoryName(suffix) || isFullName(suffix))
                return suffix;
            else if (RepositoryManager.DEFAULT_MODULE.equals(name))
                return name + suffix;
            else
                return name + "-" + version + suffix;
        }
    }

    /**
     * Returns true if the filename that was passed is one of the
     * directory artifact names. Eg. "module-docs"
     */
    public static boolean isDirectoryName(String fileName) {
        for (String suffix : directoryNames) {
            if (fileName.equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the filename that was passed is one of the
     * non-suffix artifact names. Eg. "module.properties"
     */
    private static boolean isFullName(String fileName) {
        for (String suffix : fileNames) {
            if (fileName.equals(suffix)) {
                return true;
            }
        }
        for (String suffix : directoryNames) {
            if (fileName.equals(suffix + ZIP) || fileName.equals(suffix + ZIP + SHA1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocalOnly() {
        return localOnly;
    }

    public void setLocalOnly(boolean localOnly) {
        this.localOnly = localOnly;
    }

    public boolean isIgnoreSHA() {
        return ignoreSHA;
    }

    public void setIgnoreSHA(boolean ignoreSHA) {
        this.ignoreSHA = ignoreSHA;
    }

    public boolean isIgnoreCache() {
        return ignoreCache;
    }

    public void setIgnoreCache(boolean ignoreCache) {
        this.ignoreCache = ignoreCache;
    }

    public boolean isThrowErrorIfMissing() {
        return throwErrorIfMissing;
    }

    public void setThrowErrorIfMissing(boolean throwErrorIfMissing) {
        this.throwErrorIfMissing = throwErrorIfMissing;
    }

    public boolean isForceOperation() {
        return forceOperation;
    }

    public void setForceOperation(boolean forceOperation) {
        this.forceOperation = forceOperation;
    }

    public boolean isForceDescriptorCheck() {
        return forceDescriptorCheck;
    }

    public void setForceDescriptorCheck(boolean forceDescriptorCheck) {
        this.forceDescriptorCheck = forceDescriptorCheck;
    }

    public boolean isIgnoreDependencies() {
        return ignoreDependencies;
    }

    public void setIgnoreDependencies(boolean ignoreDependencies) {
        this.ignoreDependencies = ignoreDependencies;
    }

    public ArtifactCallback getCallback() {
        return callback;
    }

    public void setCallback(ArtifactCallback callback) {
        this.callback = callback;
    }

    public Repository getSearchRepository() {
        return repository;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getName()).append("-").append(getVersion());
        if (suffixes.length == 1 && !isDirectoryName(suffixes[0]) && !isFullName(suffixes[0])) {
            str.append(suffixes[0]);
        } else {
            str.append("(");
            boolean first = true;
            for (String s : suffixes) {
                if (!first) {
                    str.append("|");
                }
                str.append(s);
                first = false;
            }
            str.append(")");
        }
        return str.toString();
    }

    public boolean forceOperation() {
        return isForceOperation();
    }

    public boolean forceDescriptorCheck() {
        return isForceDescriptorCheck();
    }
    
    public ArtifactContext copy() {
        ArtifactContext ac = new ArtifactContext(name, version, suffixes);
        ac.copySettingsFrom(this);
        ac.repository = repository;
        return ac;
    }

    public ArtifactContext copySettingsFrom(ArtifactContext ac) {
        localOnly = ac.localOnly;
        ignoreSHA = ac.ignoreSHA;
        ignoreCache = ac.ignoreCache;
        throwErrorIfMissing = ac.throwErrorIfMissing;
        forceOperation = ac.forceOperation;
        forceDescriptorCheck = ac.forceDescriptorCheck;
        ignoreDependencies = ac.ignoreDependencies;
        callback = ac.callback;
        return ac;
    }

    @Override
    public boolean equals(Object obj) {
        // WARNING: this is only about equality of coordinates, as used in Overrides
        if(obj == this)
            return true;
        if(obj instanceof ArtifactContext == false)
            return false;
        ArtifactContext other = (ArtifactContext) obj;
        return Objects.equals(getName(), other.getName())
                && Objects.equals(version, other.version);
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + (getName() != null ? getName().hashCode() : 0);
        hash = 37 * hash + (version != null ? version.hashCode() : 0);
        return hash;
    }
}
