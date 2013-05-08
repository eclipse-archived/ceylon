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

import java.io.File;

import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Stef Epardaud
 */
public class HerdTestCase extends AbstractTest {

    @Test
    public void testDummy() {
    }

    private RepositoryManager getRepositoryManager(File root, boolean offline) throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(root, offline);
        WebDAVContentStore rcs = new WebDAVContentStore("http://localhost:9000/test", log, false);
        Repository repo = new DefaultRepository(rcs.createRoot());
        return builder.appendRepository(repo).buildRepository();
    }

    protected RepositoryManager getRepositoryManager() throws Exception {
        // only Herd
        return getRepositoryManager(getFolders(), false);
    }

    protected RepositoryManager getDualRepositoryManager() throws Exception {
        // Herd + /repo
        return getRepositoryManager(getRepositoryRoot(), false);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteEmpty() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3")),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2")),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2")),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1")),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
        };
        testComplete("", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
        };
        testComplete("", expected, getRepositoryManager(), Type.JVM, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeyl() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
        };
        testComplete("ceyl", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylon() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
        };
        testComplete("ceylon", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
        };
        testComplete("ceylon.collection", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteStopsAfterCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        };
        testComplete("ceylon.collection.", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonDot() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
        };
        testComplete("ceylon.", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersions() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                new ModuleVersionDetails("0.3.0", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", "Stéphane Épardaud"),
        };
        testListVersions("ceylon.collection", null, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsBinaryIncompatible() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("ceylon.collection", null, expected, getRepositoryManager(), 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("ceylon.collection", "1.0", expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3")),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2")),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2")),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1")),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
        };
        testSearchResults("", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
        };
        testSearchResults("", Type.JVM, expected, null, null, getRepositoryManager(), null, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFiltered() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
        };
        testSearchResults("cey", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3")),
        };
        testSearchResults("", Type.JVM, expected, 1l, 2l);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3")),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2")),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2")),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1")),
                new ModuleDetails("hello", null, null, set(), set("1.0.0")),
                new ModuleDetails("moduletest", null, null, set(), set("0.1")),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final")),
                new ModuleDetails("test-jar", null, null, set(), set("0.1")),
        };
        testSearchResults("", Type.JVM, expected, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3")),
        };
        testSearchResults("", Type.JVM, expected, 0L, 3L, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged2() throws Exception {
        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0")),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1")),
        };
        ModuleSearchResult results = testSearchResults("", Type.JVM, expected, 0L, 2L, getDualRepositoryManager());

        // check end indices
        long[] pagingInfo = results.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo);
        Assert.assertEquals(2, pagingInfo.length);
        Assert.assertEquals(0, pagingInfo[0]);
        Assert.assertEquals(2, pagingInfo[1]);

        // second page
        expected = new ModuleDetails[]{
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3")),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2")),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1")),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2")),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1")),
        };
        results = testSearchResults("", Type.JVM, expected, 2L, 6L, getDualRepositoryManager(), pagingInfo);

        // check end indices
        pagingInfo = results.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo);
        Assert.assertEquals(2, pagingInfo.length);
        Assert.assertEquals(1, pagingInfo[0]);
        Assert.assertEquals(8, pagingInfo[1]);

        // third page
        expected = new ModuleDetails[]{
                new ModuleDetails("hello", null, null, set(), set("1.0.0")),
                new ModuleDetails("moduletest", null, null, set(), set("0.1")),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1")),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final")),
                new ModuleDetails("test-jar", null, null, set(), set("0.1")),
        };
        testSearchResults("", Type.JVM, expected, 8L, null, getDualRepositoryManager(), pagingInfo);
    }
}
