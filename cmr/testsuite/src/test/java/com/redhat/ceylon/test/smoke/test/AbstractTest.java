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

package com.redhat.ceylon.test.smoke.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.impl.FileContentStore;
import com.redhat.ceylon.cmr.impl.JULLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AbstractTest {

    protected Logger log = new JULLogger();

    private Path temp;

    @Before
    public void setUp() throws Exception {
        temp = Files.createTempDirectory("temp-");
    }

    @After
    public void tearDown() throws Exception {
        Files.walkFileTree(temp, new FileVisitor<Path>() {
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    protected File getRepositoryRoot() throws URISyntaxException {
        URL url = getClass().getResource("/repo");
        Assert.assertNotNull("RepositoryManager root '/repo' not found", url);
        return new File(url.toURI());
    }

    protected File getFolders() throws URISyntaxException {
        URL url = getClass().getResource("/folders");
        Assert.assertNotNull("RepositoryManager folder '/folders' not found", url);
        return new File(url.toURI());
    }

    protected RepositoryManagerBuilder getRepositoryManagerBuilder(boolean offline) throws Exception {
        return getRepositoryManagerBuilder(getRepositoryRoot(), offline);
    }

    protected RepositoryManagerBuilder getRepositoryManagerBuilder(File root, boolean offline) throws Exception {
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(temp.toFile(), log, offline);
        builder.appendRepository(new DefaultRepository(new FileContentStore(root).createRoot()));
        return builder;
    }

    protected RepositoryManager getRepositoryManager(boolean offline) throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(offline);
        return builder.buildRepository();
    }

    protected RepositoryManager getRepositoryManager() throws Exception {
        return getRepositoryManager(false);
    }

    protected void testComplete(String query, ModuleDetails[] expected, RepositoryManager manager) {
        testComplete(query, expected, manager, ModuleQuery.Type.JVM);
    }

    protected void testComplete(String query, ModuleDetails[] expected, RepositoryManager manager, ModuleQuery.Type type) {
        testComplete(query, expected, manager, type, null, null);
    }

    protected void testComplete(String query, ModuleDetails[] expected, RepositoryManager manager,
                                ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        ModuleQuery lookup = new ModuleQuery(query, type);
        lookup.setBinaryMajor(binaryMajor);
        lookup.setBinaryMinor(binaryMinor);
        ModuleSearchResult result = manager.completeModules(lookup);
        compareSearchResults(expected, result);
    }

    protected void testListVersions(String query, String versionQuery, ModuleVersionDetails[] expected) throws Exception {
        RepositoryManager manager = getRepositoryManager();
        testListVersions(query, versionQuery, expected, manager);
    }

    protected void testListVersions(String query, String versionQuery, ModuleVersionDetails[] expected,
                                    RepositoryManager manager) throws Exception {
        testListVersions(query, versionQuery, expected, manager, null, null);
    }

    protected void testListVersions(String query, String versionQuery, ModuleVersionDetails[] expected,
                                    RepositoryManager manager, Integer binaryMajor, Integer binaryMinor) throws Exception {
        ModuleVersionQuery lookup = new ModuleVersionQuery(query, versionQuery, ModuleQuery.Type.JVM);
        lookup.setBinaryMajor(binaryMajor);
        lookup.setBinaryMinor(binaryMinor);
        ModuleVersionResult result = manager.completeVersions(lookup);
        int i = 0;
        Assert.assertEquals(expected.length, result.getVersions().size());
        for (Entry<String, ModuleVersionDetails> entry : result.getVersions().entrySet()) {
            ModuleVersionDetails expectedVersion = expected[i++];
            ModuleVersionDetails version = entry.getValue();
            Assert.assertEquals(expectedVersion.getVersion(), entry.getKey());
            Assert.assertEquals(expectedVersion.getVersion(), version.getVersion());
            Assert.assertEquals(expectedVersion.getDoc(), version.getDoc());
            Assert.assertEquals(expectedVersion.getLicense(), version.getLicense());
            Assert.assertEquals(expectedVersion.getAuthors(), version.getAuthors());
            Assert.assertEquals(expectedVersion.getDependencies(), version.getDependencies());
            Assert.assertEquals(expectedVersion.getArtifactTypes(), version.getArtifactTypes());
            Assert.assertEquals(expectedVersion.isRemote(), version.isRemote());
            Assert.assertEquals(expectedVersion.getOrigin(), version.getOrigin());
        }
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected) throws Exception {
        return testSearchResults(q, type, expected, null, null);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, RepositoryManager manager) throws Exception {
        return testSearchResults(q, type, expected, null, null, manager);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, Long start, Long count) throws Exception {
        RepositoryManager manager = getRepositoryManager();
        return testSearchResults(q, type, expected, start, count, manager);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected,
                                                   Long start, Long count, RepositoryManager manager) throws Exception {
        return testSearchResults(q, type, expected, start, count, manager, null);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected,
                                                   Long start, Long count, RepositoryManager manager, long[] pagingInfo) throws Exception {
        return testSearchResults(q, type, expected, start, count, manager, pagingInfo, null, null);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected,
                                                   Long start, Long count, RepositoryManager manager, long[] pagingInfo,
                                                   Integer binaryMajor, Integer binaryMinor) throws Exception {

        ModuleQuery query = new ModuleQuery(q, type);
        query.setStart(start);
        query.setCount(count);
        query.setPagingInfo(pagingInfo);
        query.setBinaryMajor(binaryMajor);
        query.setBinaryMinor(binaryMinor);
        ModuleSearchResult results = manager.searchModules(query);

        compareSearchResults(expected, results);
        return results;
    }

    private void compareSearchResults(ModuleDetails[] expected, ModuleSearchResult results) {
        int i = 0;
        Collection<ModuleDetails> resultsList = results.getResults();
        Assert.assertEquals(expected.length, resultsList.size());
        for (ModuleDetails result : resultsList) {
            ModuleDetails expectedResult = expected[i++];
            System.err.println("Testing " + result.getName());
            Assert.assertEquals(expectedResult.getName(), result.getName());
            Assert.assertEquals(expectedResult.getDoc(), result.getDoc());
            Assert.assertEquals(expectedResult.getLicense(), result.getLicense());
            Assert.assertEquals(expectedResult.getAuthors(), result.getAuthors());
            Assert.assertEquals(expectedResult.getVersions(), result.getVersions());
            Assert.assertEquals(expectedResult.getDependencies(), result.getDependencies());
            Assert.assertEquals(expectedResult.getArtifactTypes(), result.getArtifactTypes());
            Assert.assertEquals(expectedResult.getMajorBinaryVersion(), result.getMajorBinaryVersion());
            Assert.assertEquals(expectedResult.getMinorBinaryVersion(), result.getMinorBinaryVersion());
        }
    }

    protected static SortedSet<String> set(String... values) {
        SortedSet<String> ret = new TreeSet<String>();
        Collections.addAll(ret, values);
        return ret;
    }

    protected static SortedSet<ModuleInfo> deps(ModuleInfo... values) {
        SortedSet<ModuleInfo> ret = new TreeSet<ModuleInfo>();
        Collections.addAll(ret, values);
        return ret;
    }

    protected static SortedSet<ModuleVersionArtifact> types(ModuleVersionArtifact... values) {
        SortedSet<ModuleVersionArtifact> ret = new TreeSet<ModuleVersionArtifact>();
        Collections.addAll(ret, values);
        return ret;
    }
}
