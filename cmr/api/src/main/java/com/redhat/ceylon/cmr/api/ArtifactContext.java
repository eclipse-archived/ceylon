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

import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Artifact lookup context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ArtifactContext implements Serializable, ContentOptions {
    public static final String CAR = ".car";
    public static final String JAR = ".jar";
    public static final String JS = ".js";
    public static final String ZIP = ".zip";
    public static final String SRC = ".src";
    public static final String MAVEN_SRC = "-sources.jar";
    public static final String DOCS = "module-doc";
    public static final String DOCS_ZIPPED = "module-doc.zip";
    public static final String MODULE_PROPERTIES = "module.properties";
    public static final String MODULE_XML = "module.xml";
    public static final String SHA1 = ".sha1";
    public static final String INFO = ".info";

    private String name;
    private String version;
    private String[] suffixes = { CAR };
    private boolean localOnly;
    private boolean ignoreSHA;
    private boolean ignoreCache;
    private boolean throwErrorIfMissing;
    private boolean forceOperation;
    private boolean forceDescriptorCheck;
    private boolean fetchSingleArtifact;

    public ArtifactContext(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public ArtifactContext(String name, String version, String... suffixes) {
        this(name, version);
        this.suffixes = suffixes;
    }

    public ArtifactContext() {
    }

    public ArtifactContext getSha1Context() {
        String[] sha1Suffixes = new String[suffixes.length];
        for (int i = 0; i < sha1Suffixes.length; i++) {
            sha1Suffixes[i] = suffixes[i] + SHA1;
        }
        return new ArtifactContext(name, version, sha1Suffixes);
    }

    public ArtifactContext getDocsContext() {
        return new ArtifactContext(name, version, new String[] { DOCS });
    }

    public ArtifactContext getModuleProperties() {
        return new ArtifactContext(name, version, new String[] { MODULE_PROPERTIES });
    }

    public ArtifactContext getModuleXml() {
        return new ArtifactContext(name, version, new String[] { MODULE_XML });
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

    public String getName() {
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

    public static String getSuffixFromFilename(String fileName) {
        if (fileName.endsWith(CAR)) {
            return CAR;
        } else if (fileName.endsWith(JAR)) {
            return JAR;
        } else if (fileName.endsWith(JS)) {
            return JS;
        } else if (fileName.endsWith(ZIP)) {
            return ZIP;
        } else if (fileName.endsWith(SRC)) {
            return SRC;
        } else {
            throw new RepositoryException("Unknown suffix in " + fileName);
        }
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

    public boolean isFetchSingleArtifact() {
        return fetchSingleArtifact;
    }

    public void setFetchSingleArtifact(boolean fetchSingleArtifact) {
        this.fetchSingleArtifact = fetchSingleArtifact;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getName()).append("-").append(getVersion());
        if (suffixes.length == 1) {
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
}
