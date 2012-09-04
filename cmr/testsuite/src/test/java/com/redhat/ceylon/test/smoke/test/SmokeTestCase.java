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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.RemoteContentStore;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.test.smoke.support.InMemoryContentStore;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SmokeTestCase extends AbstractTest {

    @Test
    public void testNavigation() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        File acme = manager.getArtifact("org.jboss.acme", "1.0.0.Final");
        Assert.assertNotNull("Module 'org.jboss.acme-1.0.0.Final' not found", acme);
    }

    @Test
    public void testNoVersion() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        File def = manager.getArtifact(RepositoryManager.DEFAULT_MODULE, null);
        Assert.assertNotNull("Default module not found", def);
    }

    @Test
    public void testPut() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.foobar1";
        String version = "1.0.0.Alpha1";
        try {
            manager.putArtifact(name, version, baos);

            File file = manager.getArtifact(name, version);
            Assert.assertNotNull("Failed to put or retrieve after put", file);
        } finally {
            manager.removeArtifact(name, version);
        }
    }

    @Test
    public void testForcedPut() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.foobar2";
        String version = "1.0.0.Alpha1";
        try {
            ArtifactContext context = new ArtifactContext();
            context.setName(name);
            context.setVersion(version);
            context.setForceOperation(true);

            manager.putArtifact(context, baos);

            File file = manager.getArtifact(name, version);
            Assert.assertNotNull("Failed to retrieve after put", file);

            baos = new ByteArrayInputStream("ytrewq".getBytes());
            manager.putArtifact(context, baos);

            file = manager.getArtifact(name, version);
            Assert.assertNotNull("Failed to retrieve after forced put", file);
        } finally {
            manager.removeArtifact(name, version);
        }
    }

    @Test
    public void testRemove() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "org.jboss.qwerty";
        String version = "1.0.0.Alpha2";
        File file = null;
        try {
            manager.putArtifact(name, version, baos);

            file = manager.getArtifact(name, version);
            Assert.assertNotNull("Failed to retrieve after put", file);
        } finally {
            manager.removeArtifact(name, version);
            if (file != null)
                Assert.assertFalse("Failed to remove module", file.exists());
        }
    }

    @Test
    public void testExternalNodes() throws Exception {
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(getRepositoryRoot(), log);

        InMemoryContentStore imcs = new InMemoryContentStore();
        Repository repo = new DefaultRepository(imcs.createRoot());
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        ByteArrayInputStream baos = new ByteArrayInputStream("qwerty".getBytes());
        String name = "com.redhat.acme";
        String version = "1.0.0.CR1";
        try {
            manager.putArtifact(name, version, baos);

            File file = manager.getArtifact(name, version);
            Assert.assertNotNull("Failed to retrieve after put", file);
        } finally {
            manager.removeArtifact(name, version);
        }
    }

    @Test
    public void testFolderPut() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        File docs = new File(getFolders(), "docs");
        String name = "com.redhat.docs";
        String version = "1.0.0.CR3";
        ArtifactContext template = new ArtifactContext();
        template.setName(name);
        template.setVersion(version);
        ArtifactContext context = template.getDocsContext();
        manager.putArtifact(context, docs);
        try {
            File copy = manager.getArtifact(context);
            File x = new File(copy, "x.txt");
            Assert.assertTrue(x.exists());
            File y = new File(copy, "sub/y.txt");
            Assert.assertTrue(y.exists());
        } finally {
            manager.removeArtifact(context);
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

        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(getRepositoryRoot(), log);
        RemoteContentStore rcs = new RemoteContentStore(repoURL, log);
        Repository repo = new DefaultRepository(rcs.createRoot());
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        String name = "com.redhat.fizbiz";
        String version = "1.0.0.Beta1";
        try {
            File file = manager.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            manager.removeArtifact(name, version);
        }
    }

    @Test
    @Ignore // this test should work, if you have org.slf4j.slf4j-api 1.5.10 present
    public void testMavenLocal() throws Exception {
        RepositoryManager manager = new SimpleRepositoryManager(MavenRepositoryHelper.getMavenRepository(), log);
        ArtifactContext ac = new ArtifactContext("org.slf4j.slf4j-api", "1.5.10");
        File file = manager.getArtifact(ac);
        Assert.assertNotNull(file);
        // No remove, as we don't wanna delete from mvn manager
    }

    @Test
    public void testMavenRemote() throws Exception {
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(getRepositoryRoot(), log);
        Repository externalRepo = MavenRepositoryHelper.getMavenRepository("https://repository.jboss.org/nexus/content/groups/public", log);
        builder.prependRepository(externalRepo);
        RepositoryManager manager = builder.buildRepository();
        ArtifactContext ac = new ArtifactContext("org.jboss.jboss-vfs", "3.0.1.GA");
        File file = null;
        try {
            file = manager.getArtifact(ac);
            Assert.assertNotNull(file);
            Assert.assertEquals("jboss-vfs-3.0.1.GA.jar", file.getName());
        } finally {
            // delete the jar, not the car
            ac.setSuffix(ArtifactContext.JAR);
            manager.removeArtifact(ac);
            // temporary workaround, because the jar is not stored at the right place
            if(file != null)
                file.delete();
        }
    }

    @Test
    public void testResolver() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        File[] files = manager.resolve("moduletest", "0.1");
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
    }

    @Test
    public void test2ndTry() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        ArtifactContext ac = new ArtifactContext("test-jar", "0.1");
        ArtifactResult result = manager.getArtifactResult(ac);
        Assert.assertNull(result);
        ac.setSuffix(ArtifactContext.JAR);
        result = manager.getArtifactResult(ac);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteEmpty() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{
            "com.acme.helloworld",
            "hello",
            "moduletest",
            "org.jboss.acme",
            "test-jar",
        };
        testComplete("", expected, manager);
    }

    @Test
    public void testCompleteEmptyJS() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{};
        testComplete("", expected, manager, ModuleQuery.Type.JS);
    }

    @Test
    public void testCompleteHe() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{
            "hello",
        };
        testComplete("he", expected, manager);
    }

    @Test
    public void testCompleteOrg() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{
            "org.jboss.acme",
        };
        testComplete("org", expected, manager);
    }

    @Test
    public void testCompleteOrgDot() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{
            "org.jboss.acme",
        };
        testComplete("org.", expected, manager);
    }

    @Test
    public void testCompleteStopAtVersion() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        String[] expected = new String[]{};
        testComplete("org.jboss.acme.", expected, manager);
    }

    @Test
    public void testListVersion() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
            new ModuleVersionDetails("1.0.0", "The classic Hello World module", "Public domain", "Stef Epardaud"),
        };
        testListVersions("com.acme.helloworld", null, expected);
    }

    @Test
    public void testListVersionFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("com.acme.helloworld", "9", expected);
    }

    @Test
    public void testSearchModules() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0")),
                new ModuleDetails("hello", null, null, set(), set("1.0.0")),
                new ModuleDetails("moduletest", null, null, set(), set("0.1")),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final")),
                new ModuleDetails("test-jar", null, null, set(), set("0.1")),
        };
        
        testSearchResults("", Type.JVM, expected);
    }

    @Test
    public void testSearchModulesPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("moduletest", null, null, set(), set("0.1")),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final")),
        };
        
        testSearchResults("", Type.JVM, expected, 2l, 2l);
    }

    @Test
    public void testSearchModulesFilteredByName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0")),
                new ModuleDetails("hello", null, null, set(), set("1.0.0")),
        };
        
        testSearchResults("hello", Type.JVM, expected);
    }
    
    @Test
    public void testSearchModulesFilteredByDocLicenseAndAuthor() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0")),
        };
        
        testSearchResults("classic", Type.JVM, expected);
        testSearchResults("domain", Type.JVM, expected);
        testSearchResults("epardaud", Type.JVM, expected);
    }

    @Test
    public void testSearchModulesFilteredByDocLicenseAndAuthorSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0")),
        };
        
        testSearchResults("classic", Type.SRC, expected);
        testSearchResults("domain", Type.SRC, expected);
        testSearchResults("epardaud", Type.SRC, expected);
    }

    @Test
    public void testSearchModulesFilteredByDocLicenseAndAuthorJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        };
        
        testSearchResults("classic", Type.JS, expected);
        testSearchResults("domain", Type.JS, expected);
        testSearchResults("epardaud", Type.JS, expected);
    }

    @Test
    public void versionComparisonTests() {
        assertEquals(0, VersionComparator.compareVersions("", ""));
        
        assertEquals(-1, VersionComparator.compareVersions("", "a"));
        assertEquals(1, VersionComparator.compareVersions("a", ""));
        assertEquals(0, VersionComparator.compareVersions("a", "a"));
        
        assertEquals(-1, VersionComparator.compareVersions("a", "b"));
        assertEquals(1, VersionComparator.compareVersions("b", "a"));
        assertEquals(-1, VersionComparator.compareVersions("a", "-"));
        assertEquals(1, VersionComparator.compareVersions("-", "a"));

        assertEquals(-1, VersionComparator.compareVersions("a", "aa"));
        assertEquals(1, VersionComparator.compareVersions("aa", "a"));

        assertEquals(0, VersionComparator.compareVersions("a1", "a1"));
        assertEquals(-1, VersionComparator.compareVersions("a1", "a2"));
        assertEquals(0, VersionComparator.compareVersions("a001", "a1"));

        assertEquals(0, VersionComparator.compareVersions("1.0.2", "1.0.2"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "1.0.2.4"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "1.0.2b"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "1.0.2RC"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "1.0.2-RC"));

        assertEquals(-1, VersionComparator.compareVersions("0.3", "2.2.4"));

        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "1.2"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "2"));
        assertEquals(-1, VersionComparator.compareVersions("1.0.2", "2.2.4"));
        
        assertEquals(-1, VersionComparator.compareVersions("1.0", "1.0.2"));
    }
}
