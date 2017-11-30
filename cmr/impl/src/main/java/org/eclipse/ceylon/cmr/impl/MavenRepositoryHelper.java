/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.net.Proxy;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.StructureBuilder;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Maven repository helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenRepositoryHelper {

    static File getMavenHome() {
        File mvnHome = new File(System.getProperty("user.home"), ".m2/repository");
        if (mvnHome.exists())
            return mvnHome;

        final String property = System.getProperty("maven.home");
        if (property != null) {
            mvnHome = new File(property, "repository");
            if (mvnHome.exists())
                return mvnHome;
        }

        throw new IllegalArgumentException("No Maven repository found!");
    }

    public static CmrRepository getMavenRepository() {
        return new MavenRepository(new MavenContentStore().createRoot());
    }

    public static CmrRepository getMavenRepository(File mvnRepository) {
        return new MavenRepository(new MavenContentStore(mvnRepository).createRoot());
    }

    public static CmrRepository getMavenRepository(String repositoryURL, Logger log, boolean offline, int timeout, Proxy proxy) {
        return new MavenRepository(new RemoteContentStore(repositoryURL, log, offline, timeout, proxy).createRoot());
    }

    public static CmrRepository getMavenRepository(StructureBuilder structureBuilder) {
        return new MavenRepository(structureBuilder.createRoot());
    }

    private static class MavenContentStore extends FileContentStore {
        private MavenContentStore() {
            this(getMavenHome());
        }

        private MavenContentStore(File root) {
            super(root);
        }

        @Override
        protected void delete(File file, Node node) {
            // cannot delete from Maven repo
        }

        @Override
        public boolean canHandleFolders() {
            return false;
        }
    }
}
