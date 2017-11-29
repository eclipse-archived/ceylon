/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.test.loader.impl.reflect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.Exclusion;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.eclipse.ceylon.model.cmr.VisibilityType;
import org.eclipse.ceylon.model.loader.impl.reflect.CachedTOCJars;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * Test for bug <a href="https://github.com/ceylon/ceylon/issues/4597">#4597</a>
 * </p>
 * <p>
 * {@code java.net.URISyntaxException} was being thrown from {@code CachedTOCJars.getContentUri} when classpath contained spaces.
 * </p>
 * 
 * @author Tomasz Krakowiak
 */
public class CachedTOCJarsTest {
    private static final String ALSO_INSIDE_JAR = "also inside jar";

    @Test
    public void spacesOnPathTest() throws IOException {
        CachedTOCJars cachedTOCJars = new CachedTOCJars();
        Module module = new Module();
        module.setName(Collections.singletonList("testModule"));
        module.setVersion("testVersion");
        final File artifactFile = File.createTempFile("path with spaces", ".jar");
        try {
            artifactFile.deleteOnExit();
            ZipOutputStream artifactOut = new ZipOutputStream(new FileOutputStream(artifactFile));
            try {
                artifactOut.putNextEntry(new ZipEntry(ALSO_INSIDE_JAR));
            } finally {
                artifactOut.close();
            }
            ArtifactResult artifact = new TestArtifactResult(artifactFile, module);
            cachedTOCJars.addJar(artifact, module);
            
            String uriPart = cachedTOCJars.getContentUri(module, ALSO_INSIDE_JAR).getSchemeSpecificPart();
            
            // I'm not comparing URI part for equality to avoid symbolic links resolution issues. 
            Assert.assertTrue(uriPart, uriPart.endsWith(artifactFile.getName() + "!" + ALSO_INSIDE_JAR));
        } finally {
            artifactFile.delete();
        }
    }

    private final class TestArtifactResult implements ArtifactResult {
        private final File artifactFile;
        private final Module module;

        private TestArtifactResult(File artifactFile, Module module) {
            this.artifactFile = artifactFile;
            this.module = module;
        }

        @Override
        public VisibilityType visibilityType() {
            return null;
        }

        @Override
        public String version() {
            return module.getVersion();
        }

        @Override
        public ArtifactResultType type() {
            return null;
        }

        @Override
        public String repositoryDisplayString() {
            return null;
        }

        @Override
        public Repository repository() {
            return null;
        }

        @Override
        public String namespace() {
            return null;
        }

        @Override
        public String name() {
            return module.getNameAsString();
        }

        @Override
        public boolean optional() {
            return false;
        }

        @Override
        public boolean exported() {
            return false;
        }

        @Override
        public PathFilter filter() {
            return null;
        }

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            return null;
        }

        @Override
        public File artifact() throws RepositoryException {
            return artifactFile;
        }

        @Override
        public ModuleScope moduleScope() {
            return ModuleScope.COMPILE;
        }

        @Override
        public List<Exclusion> getExclusions() {
            return null;
        }
        
        @Override
        public String artifactId() {
            return null;
        }
        
        @Override
        public String groupId() {
            return null;
        }
        
        @Override
        public String classifier() {
            return null;
        }
    }
}
