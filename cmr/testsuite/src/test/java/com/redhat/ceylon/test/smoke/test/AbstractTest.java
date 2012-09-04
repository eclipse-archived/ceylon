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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.RootRepositoryManager;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AbstractTest {

    protected Logger log = new JULLogger();

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

    protected RepositoryManager getRepositoryManager() throws URISyntaxException {
        File root = getRepositoryRoot();
        return new RootRepositoryManager(root, log);
    }

    protected void testComplete(String query, String[] expected, RepositoryManager manager){
        testComplete(query, expected, manager, ModuleQuery.Type.JVM);
    }
    
    protected void testComplete(String query, String[] expected, RepositoryManager manager,
            ModuleQuery.Type type){
        ModuleQuery lookup = new ModuleQuery(query, type);
        ModuleResult result = manager.completeModules(lookup);
        int i=0;
        Assert.assertEquals(expected.length, result.getResults().size());
        for(String name : result.getResults()){
            Assert.assertEquals(expected[i++], name);
        }
    }

    protected void testListVersions(String query, String versionQuery, ModuleVersionDetails[] expected) throws Exception{
        RepositoryManager manager = getRepositoryManager();
        ModuleVersionQuery lookup = new ModuleVersionQuery(query, versionQuery, ModuleQuery.Type.JVM);
        ModuleVersionResult result = manager.completeVersions(lookup);
        int i=0;
        Assert.assertEquals(expected.length, result.getVersions().size());
        for(Entry<String, ModuleVersionDetails> entry : result.getVersions().entrySet()){
            ModuleVersionDetails expectedVersion = expected[i++];
            ModuleVersionDetails version = entry.getValue();
            Assert.assertEquals(expectedVersion.getVersion(), entry.getKey());
            Assert.assertEquals(expectedVersion.getVersion(), version.getVersion());
            Assert.assertEquals(expectedVersion.getDoc(), version.getDoc());
            Assert.assertEquals(expectedVersion.getLicense(), version.getLicense());
            Assert.assertEquals(expectedVersion.getAuthors(), version.getAuthors());
        }
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected) throws Exception{
        return testSearchResults(q, type, expected, null, null);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, RepositoryManager manager) throws Exception{
        return testSearchResults(q, type, expected, null, null, manager);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, Long start, Long count) throws Exception{
        RepositoryManager manager = getRepositoryManager();
        return testSearchResults(q, type, expected, start, count, manager);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, 
            Long start, Long count, RepositoryManager manager) throws Exception{
        return testSearchResults(q, type, expected, start, count, manager, null);
    }

    protected ModuleSearchResult testSearchResults(String q, Type type, ModuleDetails[] expected, 
            Long start, Long count, RepositoryManager manager, long[] pagingInfo) throws Exception{
        
        ModuleQuery query = new ModuleQuery(q, type);
        query.setStart(start);
        query.setCount(count);
        query.setPagingInfo(pagingInfo);
        ModuleSearchResult results = manager.searchModules(query);
        
        int i=0;
        Collection<ModuleDetails> resultsList = results.getResults();
        Assert.assertEquals(expected.length, resultsList.size());
        for(ModuleDetails result : resultsList){
            ModuleDetails expectedResult = expected[i++];
            System.err.println("Testing " + result.getName());
            Assert.assertEquals(expectedResult.getName(), result.getName());
            Assert.assertEquals(expectedResult.getDoc(), result.getDoc());
            Assert.assertEquals(expectedResult.getLicense(), result.getLicense());
            Assert.assertEquals(expectedResult.getAuthors(), result.getAuthors());
            Assert.assertEquals(expectedResult.getVersions(), result.getVersions());
        }
        return results;
    }

    protected SortedSet<String> set(String... values){
        SortedSet<String> ret = new TreeSet<String>();
        for(String v : values){
            ret.add(v);
        }
        return ret;
    }
    
}
