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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryBuilder;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.impl.JDKRepository;
import com.redhat.ceylon.cmr.impl.MavenRepositoryHelper;
import com.redhat.ceylon.cmr.impl.RemoteContentStore;
import com.redhat.ceylon.cmr.impl.SimpleRepositoryManager;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.test.smoke.support.InMemoryContentStore;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

        String name = "org.jboss.qwerty";
        String version = "1.0.0.Alpha2";
        File file = null;
        try {
            File artifact = manager.getArtifact(name, version);
            Assert.assertNull(artifact);

            manager.putArtifact(name, version, new ByteArrayInputStream("qwerty".getBytes()));

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
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);

        InMemoryContentStore imcs = new InMemoryContentStore();
        OpenNode root = imcs.createRoot();
        Repository repo = new DefaultRepository(root);
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        // a few impl details, feel free to remove/ignore this test

        String name = "com.redhat.acme";
        String version = "1.0.0.CR1";
        ArtifactContext context = new ArtifactContext(name, version);
        context.setIgnoreSHA(true); // ignore with in-memory

        OpenNode parent = repo.createParent(context);
        parent.addContent(name + "-" + version + ArtifactContext.CAR, new ByteArrayInputStream("qwerty".getBytes()), context);

        try {
            File file = manager.getArtifact(context);
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

        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
        RemoteContentStore rcs = new RemoteContentStore(repoURL, log, false);
        Repository repo = new DefaultRepository(rcs.createRoot());
        RepositoryManager manager = builder.appendRepository(repo).buildRepository();

        String name = "com.redhat.fizbiz";
        String version = "1.0.0.Beta1";
        try {
            File file = manager.getArtifact(name, version);
            Assert.assertNotNull(file);
        } finally {
            manager.removeArtifact(name, version);
            // test if remove really works
            testSearchResults("com.redhat.fizbiz", Type.JVM, new ModuleDetails[0]);
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
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
        Repository externalRepo = MavenRepositoryHelper.getMavenRepository("https://repository.jboss.org/nexus/content/groups/public", log, false);
        builder.prependRepository(externalRepo);
        RepositoryManager manager = builder.buildRepository();
        ArtifactContext ac = new ArtifactContext("org.jboss.jboss-vfs", "3.0.1.GA", ArtifactContext.JAR);
        File file = null;
        try {
            file = manager.getArtifact(ac);
            Assert.assertNotNull(file);
            Assert.assertEquals("jboss-vfs-3.0.1.GA.jar", file.getName());
        } finally {
            // delete the jar, not the car
            ac.setSuffixes(ArtifactContext.JAR);
            manager.removeArtifact(ac);
            // temporary workaround, because the jar is not stored at the right place
            if (file != null)
                Assert.assertTrue(file.delete());
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
    public void testPropertiesResolver() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        ArtifactContext context = new ArtifactContext("old-jar", "1.2.CR1", ArtifactContext.JAR);
        File[] files = manager.resolve(context);
        Assert.assertNotNull(files);
        Assert.assertEquals(3, files.length);
    }

    @Test
    public void testPropertiesPut() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        ArtifactContext context = new ArtifactContext("org.acme.props", "1.0", ArtifactContext.JAR);
        try {
            manager.putArtifact(context, new ByteArrayInputStream("dummy_jar".getBytes()));
            manager.putArtifact(context.getModuleProperties(), new ByteArrayInputStream("moduletest=0.1\n".getBytes()));
            File[] files = manager.resolve(context);
            Assert.assertNotNull(files);
            Assert.assertEquals(3, files.length);
        } finally {
            manager.removeArtifact(context);
        }
    }

    @Test
    public void testPropertiesGet() throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(false);
        RepositoryBuilder rb = builder.repositoryBuilder();
        Repository repository = rb.buildRepository("http://modules.ceylon-lang.org/test");
        builder.appendRepository(repository);
        RepositoryManager manager = builder.buildRepository();

        ArtifactContext context = new ArtifactContext("io.undertow.core", "1.0.0.Alpha1-9fdfd5f766", ArtifactContext.JAR);
        try {
            File artifact = manager.getArtifact(context);
            Assert.assertNotNull(artifact);
            File mp = new File(artifact.getParent(), ArtifactContext.MODULE_PROPERTIES);
            Assert.assertNotNull(mp.exists());
        } finally {
            manager.removeArtifact(context);
        }
    }

    @Test
    public void testXmlResolver() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        ArtifactContext context = new ArtifactContext("older-jar", "12-b3", ArtifactContext.JAR);
        File[] files = manager.resolve(context);
        Assert.assertNotNull(files);
        Assert.assertEquals(3, files.length);
    }

    @Test
    public void test2ndTry() throws Exception {
        RepositoryManager manager = getRepositoryManager();
        ArtifactContext ac = new ArtifactContext("test-jar", "0.1");
        ArtifactResult result = manager.getArtifactResult(ac);
        Assert.assertNull(result);
        ac.setSuffixes(ArtifactContext.JAR);
        result = manager.getArtifactResult(ac);
        Assert.assertNotNull(result);
    }

    public final static ModuleDetails com_acme_helloworld = new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0"), deps(), set(".car"), 3, null, false, null);
    public final static ModuleDetails hello = new ModuleDetails("hello", "A test", "Apache Software License", set("The Ceylon Team"), set("1.0.0"), deps(), set(".car"), 3, null, false, null);
    public final static ModuleDetails moduletest = new ModuleDetails("moduletest", "A test", "GPLv2", set("The Ceylon Team"), set("0.1"), deps(new ModuleInfo("hello", "1.0.0", false, false)), set(".car"), 3, null, false, null);
    public final static ModuleDetails old_jar = new ModuleDetails("old-jar", null, null, set(), set("1.2.CR1"), deps(), set(), null, null, false, null);
    public final static ModuleDetails older_jar = new ModuleDetails("older-jar", null, null, set(), set("12-b3"), deps(), set(), null, null, false, null);
    public final static ModuleDetails org_jboss_acme = new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final"), deps(), set(), null, null, false, null);
    public final static ModuleDetails test_jar = new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), set(), null, null, false, null);
    public final static ModuleDetails jsonly = new ModuleDetails("jsonly", null, null, set(), set("1.0.0"), deps(), set(".js"), null, null, false, null);

    @Test
    public void testCompleteEmpty() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                com_acme_helloworld,
                hello,
                moduletest,
                old_jar,
                older_jar,
                org_jboss_acme,
                test_jar,
        };
        testComplete("", expected, manager);
    }

    @Test
    public void testCompleteEmptyJS() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                hello,
                jsonly,
                moduletest
        };
        testComplete("", expected, manager, ModuleQuery.Type.JS);
    }

    @Test
    public void testCompleteHe() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                hello,
        };
        testComplete("he", expected, manager);
    }

    @Test
    public void testCompleteOrg() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                org_jboss_acme,
        };
        testComplete("org", expected, manager);
    }

    @Test
    public void testCompleteOrgBinaryIncompatible() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
        };
        testComplete("org", expected, manager, Type.JVM, 1234, 0);
    }

    @Test
    public void testCompleteOrgDot() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                org_jboss_acme,
        };
        testComplete("org.", expected, manager);
    }

    @Test
    public void testCompleteStopAtVersion() throws Exception {
        RepositoryManager manager = getRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
        };
        testComplete("org.jboss.acme.", expected, manager);
    }

    @Test
    public void testListVersion() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                new ModuleVersionDetails("1.0.0", "The classic Hello World module", "Public domain", set("Stef Epardaud"), 
                        deps(), types(new ModuleVersionArtifact(".car", 3, null)), false, new File("build/classes/repo").getAbsolutePath()),
        };
        testListVersions("com.acme.helloworld", null, expected);
    }

    @Test
    public void testListVersionBinaryIncompatible() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("com.acme.helloworld", null, expected, getRepositoryManager(), 1234, 0);
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
                com_acme_helloworld,
                hello,
                moduletest,
                old_jar,
                older_jar,
                org_jboss_acme,
                test_jar,
        };

        testSearchResults("", Type.JVM, expected);
    }

    @Test
    public void testSearchModulesPaged() throws Exception {
        RepositoryManager repoManager = getRepositoryManager();

        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                com_acme_helloworld,
                hello,
        };
        ModuleSearchResult results = testSearchResults("", Type.JVM, expected, 0l, 2l, repoManager);
        Assert.assertEquals(2, results.getCount());
        Assert.assertEquals(true, results.getHasMoreResults());
        Assert.assertEquals(0, results.getStart());

        // second page
        expected = new ModuleDetails[]{
                moduletest,
                old_jar,
                older_jar,
        };

        results = testSearchResults("", Type.JVM, expected, results.getStart() + results.getCount(), 3l, repoManager, results.getNextPagingInfo());
        Assert.assertEquals(3, results.getCount());
        Assert.assertEquals(true, results.getHasMoreResults());
        Assert.assertEquals(2, results.getStart());

        // third page
        expected = new ModuleDetails[]{
                org_jboss_acme,
                test_jar,
        };
        results = testSearchResults("", Type.JVM, expected, results.getStart() + results.getCount(), 2l, repoManager, results.getNextPagingInfo());
        Assert.assertEquals(2, results.getCount());
        Assert.assertEquals(false, results.getHasMoreResults());
        Assert.assertEquals(5, results.getStart());
    }

    @Test
    public void testSearchModulesFilteredByName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                com_acme_helloworld,
                hello,
                moduletest,
        };

        testSearchResults("hello", Type.JVM, expected);
    }

    @Test
    public void testSearchModulesFilteredByDocLicenseAndAuthor() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                com_acme_helloworld,
        };

        testSearchResults("classic", Type.JVM, expected);
        testSearchResults("domain", Type.JVM, expected);
        testSearchResults("epardaud", Type.JVM, expected);
    }

    @Test
    public void testSearchModulesFilteredByDocLicenseAndAuthorSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                com_acme_helloworld,
        };

        testSearchResults("classic", Type.SRC, expected);
        testSearchResults("domain", Type.SRC, expected);
        testSearchResults("epardaud", Type.SRC, expected);
    }

    @Test
    public void testSearchModulesIncompatibleBinaryVersion() throws Exception {
        // we only get jars since those have no binary version
        ModuleDetails[] expected = new ModuleDetails[]{
                old_jar,
                older_jar,
                test_jar,
        };
        testSearchResults("", Type.JVM, expected, null, null, getRepositoryManager(), null, 1234, 0);
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

        assertEquals(-1, VersionComparator.compareVersions("0.3", "0.3.1"));
        assertEquals(1, VersionComparator.compareVersions("0.3.1", "0.3"));
        assertEquals(-1, VersionComparator.compareVersions("0.3.1", "0.3.2"));
        assertEquals(1, VersionComparator.compareVersions("0.3.2", "0.3.1"));
    }

    private RepositoryManager getJDKRepositoryManager() {
        return new SimpleRepositoryManager(new JDKRepository(), log);
    }

    @Test
    public void testCompleteJDK() throws Exception {
        RepositoryManager manager = getJDKRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("java.auth", "JDK module java.auth", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.auth.kerberos", "JDK module java.auth.kerberos", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.base", "JDK module java.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.compiler", "JDK module java.compiler", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.corba", "JDK module java.corba", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.desktop", "JDK module java.desktop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.instrument", "JDK module java.instrument", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc", "JDK module java.jdbc", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc.rowset", "JDK module java.jdbc.rowset", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.logging", "JDK module java.logging", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.management", "JDK module java.management", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.prefs", "JDK module java.prefs", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.rmi", "JDK module java.rmi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.security.acl", "JDK module java.security.acl", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.tls", "JDK module java.tls", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.annotation", "JDK module javax.annotation", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.jaxws", "JDK module javax.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.naming", "JDK module javax.naming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.script", "JDK module javax.script", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.transaction", "JDK module javax.transaction", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xml", "JDK module javax.xml", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xmldsig", "JDK module javax.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.auth", "JDK module oracle.jdk.auth", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.base", "JDK module oracle.jdk.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.compat", "JDK module oracle.jdk.compat", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.corba", "JDK module oracle.jdk.corba", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.cosnaming", "JDK module oracle.jdk.cosnaming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.deploy", "JDK module oracle.jdk.deploy", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.desktop", "JDK module oracle.jdk.desktop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.httpserver", "JDK module oracle.jdk.httpserver", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.instrument", "JDK module oracle.jdk.instrument", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jaxp", "JDK module oracle.jdk.jaxp", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jaxws", "JDK module oracle.jdk.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jdbc.rowset", "JDK module oracle.jdk.jdbc.rowset", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jndi", "JDK module oracle.jdk.jndi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.logging", "JDK module oracle.jdk.logging", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.management", "JDK module oracle.jdk.management", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.management.iiop", "JDK module oracle.jdk.management.iiop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.rmi", "JDK module oracle.jdk.rmi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.scripting", "JDK module oracle.jdk.scripting", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.sctp", "JDK module oracle.jdk.sctp", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.security.acl", "JDK module oracle.jdk.security.acl", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.smartcardio", "JDK module oracle.jdk.smartcardio", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.base", "JDK module oracle.jdk.tools.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.jaxws", "JDK module oracle.jdk.tools.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.jre", "JDK module oracle.jdk.tools.jre", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.xmldsig", "JDK module oracle.jdk.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.sun.charsets", "JDK module oracle.sun.charsets", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };
        testComplete("", expected, manager);
    }

    @Test
    public void testCompleteJDKOnJS() throws Exception {
        RepositoryManager manager = getJDKRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
        };
        testComplete("", expected, manager, ModuleQuery.Type.JS);
    }

    @Test
    public void testCompleteJDKWithPrefix() throws Exception {
        RepositoryManager manager = getJDKRepositoryManager();

        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("java.auth", "JDK module java.auth", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.auth.kerberos", "JDK module java.auth.kerberos", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.base", "JDK module java.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.compiler", "JDK module java.compiler", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.corba", "JDK module java.corba", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.desktop", "JDK module java.desktop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.instrument", "JDK module java.instrument", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc", "JDK module java.jdbc", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc.rowset", "JDK module java.jdbc.rowset", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.logging", "JDK module java.logging", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.management", "JDK module java.management", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.prefs", "JDK module java.prefs", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.rmi", "JDK module java.rmi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.security.acl", "JDK module java.security.acl", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.tls", "JDK module java.tls", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.annotation", "JDK module javax.annotation", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.jaxws", "JDK module javax.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.naming", "JDK module javax.naming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.script", "JDK module javax.script", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.transaction", "JDK module javax.transaction", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xml", "JDK module javax.xml", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xmldsig", "JDK module javax.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };
        testComplete("java", expected, manager);

        expected = new ModuleDetails[]{
                new ModuleDetails("javax.annotation", "JDK module javax.annotation", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.jaxws", "JDK module javax.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.naming", "JDK module javax.naming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.script", "JDK module javax.script", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.transaction", "JDK module javax.transaction", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xml", "JDK module javax.xml", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xmldsig", "JDK module javax.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };
        testComplete("javax", expected, manager);
    }

    @Test
    public void testListVersionJDK() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                new ModuleVersionDetails("7", "JDK module java.base", null, set(), deps(), types(new ModuleVersionArtifact(".jar", null, null)), false, "Java Runtime"),
        };
        testListVersions("java.base", null, expected, getJDKRepositoryManager());
    }

    @Test
    public void testListVersionJDKFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("java.base", "8", expected, getJDKRepositoryManager());
    }

    @Test
    public void testSearchJDKModulesFilteredByName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("java.base", "JDK module java.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.base", "JDK module oracle.jdk.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.base", "JDK module oracle.jdk.tools.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };

        testSearchResults("base", Type.JVM, expected, getJDKRepositoryManager());
    }

    @Test
    public void testSearchJDKModulesPaged() throws Exception {
        RepositoryManager repoManager = getJDKRepositoryManager();

        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("java.auth", "JDK module java.auth", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.auth.kerberos", "JDK module java.auth.kerberos", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.base", "JDK module java.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.compiler", "JDK module java.compiler", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.corba", "JDK module java.corba", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.desktop", "JDK module java.desktop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.instrument", "JDK module java.instrument", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc", "JDK module java.jdbc", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.jdbc.rowset", "JDK module java.jdbc.rowset", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.logging", "JDK module java.logging", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.management", "JDK module java.management", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.prefs", "JDK module java.prefs", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.rmi", "JDK module java.rmi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.security.acl", "JDK module java.security.acl", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("java.tls", "JDK module java.tls", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.annotation", "JDK module javax.annotation", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.jaxws", "JDK module javax.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.naming", "JDK module javax.naming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.script", "JDK module javax.script", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.transaction", "JDK module javax.transaction", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };
        ModuleSearchResult results = testSearchResults("", Type.JVM, expected, 0l, 20l, repoManager);
        Assert.assertEquals(20, results.getCount());
        Assert.assertEquals(true, results.getHasMoreResults());
        Assert.assertEquals(0, results.getStart());

        // second page
        expected = new ModuleDetails[]{
                new ModuleDetails("javax.xml", "JDK module javax.xml", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("javax.xmldsig", "JDK module javax.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.auth", "JDK module oracle.jdk.auth", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.base", "JDK module oracle.jdk.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.compat", "JDK module oracle.jdk.compat", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.corba", "JDK module oracle.jdk.corba", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.cosnaming", "JDK module oracle.jdk.cosnaming", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.deploy", "JDK module oracle.jdk.deploy", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.desktop", "JDK module oracle.jdk.desktop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.httpserver", "JDK module oracle.jdk.httpserver", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.instrument", "JDK module oracle.jdk.instrument", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jaxp", "JDK module oracle.jdk.jaxp", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jaxws", "JDK module oracle.jdk.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jdbc.rowset", "JDK module oracle.jdk.jdbc.rowset", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.jndi", "JDK module oracle.jdk.jndi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.logging", "JDK module oracle.jdk.logging", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.management", "JDK module oracle.jdk.management", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.management.iiop", "JDK module oracle.jdk.management.iiop", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.rmi", "JDK module oracle.jdk.rmi", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.scripting", "JDK module oracle.jdk.scripting", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };

        results = testSearchResults("", Type.JVM, expected, results.getStart() + results.getCount(), 20l, repoManager, results.getNextPagingInfo());
        Assert.assertEquals(20, results.getCount());
        Assert.assertEquals(true, results.getHasMoreResults());
        Assert.assertEquals(20, results.getStart());

        // third page
        expected = new ModuleDetails[]{
                new ModuleDetails("oracle.jdk.sctp", "JDK module oracle.jdk.sctp", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.security.acl", "JDK module oracle.jdk.security.acl", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.smartcardio", "JDK module oracle.jdk.smartcardio", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.base", "JDK module oracle.jdk.tools.base", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.jaxws", "JDK module oracle.jdk.tools.jaxws", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.tools.jre", "JDK module oracle.jdk.tools.jre", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.jdk.xmldsig", "JDK module oracle.jdk.xmldsig", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
                new ModuleDetails("oracle.sun.charsets", "JDK module oracle.sun.charsets", null, set(), set("7"), deps(), set(".jar"), null, null, false, null),
        };
        results = testSearchResults("", Type.JVM, expected, results.getStart() + results.getCount(), 20l, repoManager, results.getNextPagingInfo());
        Assert.assertEquals(8, results.getCount());
        Assert.assertEquals(false, results.getHasMoreResults());
        Assert.assertEquals(40, results.getStart());
    }

}
