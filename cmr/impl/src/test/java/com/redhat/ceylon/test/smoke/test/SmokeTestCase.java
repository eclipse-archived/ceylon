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

package com.redhat.ceylon.test.smoke.test;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.JULLogger;
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

    private Logger log = new JULLogger();
    
    protected File getRepositoryRoot() throws URISyntaxException {
        URL url = getClass().getResource("/repo");
        Assert.assertNotNull(url);
        return new File(url.toURI());
    }

    protected Repository getRepository() throws URISyntaxException {
        File root = getRepositoryRoot();
        return new RootRepository(root, log);
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

        File def = repo.getArtifact(Repository.DEFAULT_MODULE, null);
        Assert.assertNotNull(def);
    }

    @Test
    public void testPut() throws Exception {
        Repository repo = getRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.foobar1";
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
    public void testForcedPut() throws Exception {
        Repository repo = getRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.foobar2";
        String version = "1.0.0.Alpha1";
        try {
            ArtifactContext context = new ArtifactContext();
            context.setName(name);
            context.setVersion(version);
            context.setForceOperation(true);

            repo.putArtifact(context, baos);

            File file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);

            baos = new ByteArrayInputStream("ytrewq".getBytes());
            repo.putArtifact(context, baos);

            file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            repo.removeArtifact(name, version);
        }
    }

    @Test
    public void testRemove() throws Exception {
        Repository repo = getRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "org.jboss.qwerty";
        String version = "1.0.0.Alpha2";
        File file = null;
        try {
            repo.putArtifact(name, version, baos);

            file = repo.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            repo.removeArtifact(name, version);
            if (file != null)
                Assert.assertFalse(file.exists());
        }
    }

    @Test
    public void testExternalNodes() throws Exception {
        RepositoryBuilder builder = new RepositoryBuilder(getRepositoryRoot(), log);

        InMemoryContentStore imcs = new InMemoryContentStore();
        Repository repo = builder.appendExternalRoot(imcs.createRoot()).buildRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.acme";
        String version = "1.0.0.CR1";
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

        RepositoryBuilder builder = new RepositoryBuilder(getRepositoryRoot(), log);
        RemoteContentStore rcs = new RemoteContentStore(repoURL, log);
        Repository repo = builder.appendExternalRoot(rcs.createRoot()).buildRepository();

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
