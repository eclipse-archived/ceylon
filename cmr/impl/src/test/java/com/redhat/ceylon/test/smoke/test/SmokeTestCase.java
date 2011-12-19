/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.redhat.ceylon.test.smoke.test;

import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.RemoteContentStore;
import com.redhat.ceylon.cmr.impl.RepositoryBuilder;
import com.redhat.ceylon.cmr.impl.RootRepository;
import com.redhat.ceylon.test.smoke.support.InMemoryContentStore;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SmokeTestCase {

    protected File getRepositoryRoot() throws URISyntaxException {
        URL url = getClass().getResource("/repo");
        Assert.assertNotNull(url);
        return new File(url.toURI());
    }

    protected Repository getRepository() throws URISyntaxException {
        File root = getRepositoryRoot();
        return new RootRepository(root);
    }

    @Test
    public void testNavigation() throws Exception {
        Repository repo = getRepository();
        
        File acme = repo.getArtifact("org.jboss.acme", "1.0.0.Final");
        Assert.assertNotNull(acme);
    }

    @Test
    public void testNoVersion() throws Exception {
        Repository repo = getRepository();

        File def = repo.getArtifact("default", Repository.NO_VERSION);
        Assert.assertNotNull(def);
    }

    @Test
    public void testPut() throws Exception {
        Repository repo = getRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.foobar";
        String version = "1.0.0.Alpha1";
        try {
            repo.putArtifact(name, version, baos);
            
            File file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            repo.removeArtifact(name, version);
        }
    }

    @Test
    public void testExternalNodes() throws Exception {
        RepositoryBuilder builder = new RepositoryBuilder(getRepositoryRoot());

        InMemoryContentStore imcs = new InMemoryContentStore();
        Repository repo = builder.addExternalRoot(imcs.createRoot()).buildRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.fizbiz";
        String version = "1.0.0.Beta1";
        try {
            repo.putArtifact(name, version, baos);

            File file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            repo.removeArtifact(name, version);
        }
    }

    @Test
    public void testRemoteContent() throws Exception {
        String repoURL = "http://jboss-as7-modules-repository.googlecode.com/svn/trunk/ceylon";
        try {
            new URL(repoURL).openStream();
        } catch (Exception ignore) {
            return; // probably not on the internet?
        }
        
        RepositoryBuilder builder = new RepositoryBuilder(getRepositoryRoot());
        RemoteContentStore rcs = new RemoteContentStore(repoURL);
        Repository repo = builder.addExternalRoot(rcs.createRoot()).buildRepository();

        String name = "com.redhat.fizbiz";
        String version = "1.0.0.Beta1";
        try {
            File file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            repo.removeArtifact(name, version);
        }
    }
}
