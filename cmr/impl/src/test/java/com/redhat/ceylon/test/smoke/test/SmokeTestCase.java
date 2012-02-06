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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.impl.*;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.test.smoke.support.InMemoryContentStore;
import org.junit.Assert;
import org.junit.Ignore;
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
        Assert.assertNotNull("Repository root '/repo' not found", url);
        return new File(url.toURI());
    }

    protected File getFolders() throws URISyntaxException {
        URL url = getClass().getResource("/folders");
        Assert.assertNotNull("Repository folder '/folders' not found", url);
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
        Assert.assertNotNull("Module 'org.jboss.acme-1.0.0.Final' not found", acme);
    }

    @Test
    public void testNoVersion() throws Exception {
        Repository repo = getRepository();

        File def = repo.getArtifact(Repository.DEFAULT_MODULE, null);
        Assert.assertNotNull("Default module not found", def);
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
            Assert.assertNotNull("Failed to put or retrieve after put", file);
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
            Assert.assertNotNull("Failed to retrieve after put", file);

            baos = new ByteArrayInputStream("ytrewq".getBytes());
            repo.putArtifact(context, baos);

            file = repo.getArtifact(name, version);
            Assert.assertNotNull("Failed to retrieve after forced put", file);
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
            Assert.assertNotNull("Failed to retrieve after put", file);
        } finally {
            repo.removeArtifact(name, version);
            if (file != null)
                Assert.assertFalse("Failed to remove module", file.exists());
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
            Assert.assertNotNull("Failed to retrieve after put", file);
        } finally {
            repo.removeArtifact(name, version);
        }
    }

    @Test
    public void testFolderPut() throws Exception {
        Repository repository = getRepository();
        File docs = new File(getFolders(), "docs");
        String name = "com.redhat.docs";
        String version = "1.0.0.CR3";
        ArtifactContext template = new ArtifactContext();
        template.setName(name);
        template.setVersion(version);
        ArtifactContext context = template.getDocsContext();
        repository.putArtifact(context, docs);
        try {
            File copy = repository.getArtifact(context);
            File x = new File(copy, "x.txt");
            Assert.assertTrue(x.exists());
            File y = new File(copy, "sub/y.txt");
            Assert.assertTrue(y.exists());
        } finally {
            repository.removeArtifact(context);
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

    @Test
    @Ignore // this test should work, if you have org.slf4j.slf4j-api 1.5.10 present
    public void testMavenLocal() throws Exception {
        Repository repository = new SimpleRepository(new MavenLocalContentStore(), log);
        ArtifactContext ac = new ArtifactContext("org.slf4j.slf4j-api", "1.5.10");
        File file = repository.getArtifact(ac);
        Assert.assertNotNull(file);
        // No remove, as we don't wanna delete from mvn repo
    }

    @Test
    @Ignore // this test should work, but we don't want the tests to take forever
    public void testMavenRemote() throws Exception {
        RepositoryBuilder builder = new RepositoryBuilder(getRepositoryRoot(), log);
        OpenNode externalRoot = new MavenRemoteContentStore("https://repository.jboss.org/nexus/content/groups/public/", log).createRoot();
        builder.prependExternalRoot(externalRoot);
        Repository repository = builder.buildRepository();
        ArtifactContext ac = new ArtifactContext("org.jboss.jboss-vfs", "3.0.1.GA");
        try {
            File file = repository.getArtifact(ac);
            Assert.assertNotNull(file);
        } finally {
            repository.removeArtifact(ac);
        }
    }
}
