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
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
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

    private RepositoryManager getRepositoryManager(File root, boolean offline, String apiVersion) throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(root, offline);
        WebDAVContentStore rcs = new WebDAVContentStore("http://localhost:9000/test", log, false, apiVersion);
        Repository repo = new DefaultRepository(rcs.createRoot());
        return builder.appendRepository(repo).buildRepository();
    }

    protected RepositoryManager getRepositoryManagerApi1() throws Exception {
        // only Herd
        return getRepositoryManager(getFolders(), false, "1");
    }

    protected RepositoryManager getRepositoryManager() throws Exception {
        // only Herd
        return getRepositoryManager(getFolders(), false, null);
    }

    protected RepositoryManager getDualRepositoryManager() throws Exception {
        // Herd + /repo
        return getRepositoryManager(getRepositoryRoot(), false, null);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteEmpty() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testComplete("", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testComplete("", expected, getRepositoryManager(), Type.JVM, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeyl() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testComplete("ceyl", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylon() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager());
    }

    public final static String jsonDoc0_4 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is Object parsedJson){\n            if(is String author = parsedJson.get(\"author\")){\n                return author;\n            }\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object{\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array{\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n\n";
    public final static String jsonDoc0_5 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is String author = parsedJson.get(\"author\")){\n            return author;\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nOr if you're really sure that you should have a String value:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        return parsedJson.getString(\"author\")){\n    }\n\nYou can iterate Json objects too::\n\n    {String*} getModules(String json){\n        value parsedJson = parse(json);\n        if(is Array modules = parsedJson.get(\"modules\")){\n            return { for (mod in modules) \n                       if(is Object mod, is String name = mod.get(\"name\")) \n                         name \n                   };\n        }\n        throw Exception(\"Invalid JSON data\");\n    }     \nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object {\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array {\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n";
    
    public final static ModuleDetails jsonModuleDetails0_5 =
            new ModuleDetails("ceylon.json", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), set("0.3.3", "0.4", "0.5"), 
                              deps(new ModuleInfo("ceylon.collection", "0.5", false, false)), 
                              set(".car", ".js", ".src"), 4, 0, true, "The Herd");
    public final static ModuleDetails jsonModuleDetails0_5_Api1 =
            new ModuleDetails("ceylon.json", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), set("0.3.3", "0.4", "0.5"), 
                              deps(), set(), null, null, true, "The Herd");
    public final static ModuleDetails jsonModuleDetails0_5_js =
            new ModuleDetails("ceylon.json", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), set("0.5"), 
                              deps(new ModuleInfo("ceylon.collection", "0.5", false, false)), 
                              set(".car", ".js", ".src"), 4, 0, true, "The Herd");

    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5,
        };
        testComplete("ceylon.json", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteNameApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5_Api1,
        };
        testComplete("ceylon.json", expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5_js,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.JS);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.SRC);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForAll() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.ALL);
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
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testComplete("ceylon.", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersions() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail0_3_3,
                jsonVersionDetail0_4,
                jsonVersionDetail0_5,
        };
        testListVersions("ceylon.json", null, expected);
    }

    public static final ModuleVersionDetails jsonVersionDetail0_3_3 =                 
            new ModuleVersionDetails("0.3.3", "A JSON parser / serialiser", "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleInfo("ceylon.collection", "0.3.3", false, false)), 
                    types(new ModuleVersionArtifact(".car", 3, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");

    public static final ModuleVersionDetails jsonVersionDetail0_4 =                 
            new ModuleVersionDetails("0.4", jsonDoc0_4, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleInfo("ceylon.collection", "0.4", false, false)), 
                    types(new ModuleVersionArtifact(".car", 3, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");

    public static final ModuleVersionDetails jsonVersionDetail0_5 =                 
            new ModuleVersionDetails("0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleInfo("ceylon.collection", "0.5", false, false)), 
                    types(new ModuleVersionArtifact(".car", 4, 0),
                            new ModuleVersionArtifact(".js", null, null),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_5_Api1 =                 
            new ModuleVersionDetails("0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(), 
                    types(),
                    true, "The Herd (http://localhost:9000/test)");

    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsComplete() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail0_5,
        };
        testListVersions("ceylon.json", "0.5", expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsCompleteApi1() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail0_5_Api1,
        };
        testListVersions("ceylon.json", "0.5", expected, getRepositoryManagerApi1());
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
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, null, null, getRepositoryManager(), null, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFiltered() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("cey", Type.JVM, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredComplete() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5,
        };
        testSearchResults("ceylon.json", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredCompleteApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetails0_5_Api1,
        };
        testSearchResults("ceylon.json", Type.JVM, expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, 1l, 2l);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("hello", null, null, set(), set("1.0.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("moduletest", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, 0L, 3L, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged2() throws Exception {
        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", "A module for collections \"foo\" `hehe` < 3\n\n    some code `with` \"stuff\" < 𐒅 &lt; &#32; &#x32; 2\n\nboo", "Apache Software License", set("Stéphane Épardaud"), set("0.3.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
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
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0", "1.0.2", "1.0.3"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.collections", null, null, set(), set("0.1", "0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.iop", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.json", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.net", null, null, set(), set("0.2"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("fr.epardaud.test", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
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
                new ModuleDetails("hello", null, null, set(), set("1.0.0"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("moduletest", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.apache.commons.httpclient", null, null, set(), set("3.1"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final"), deps(), set(), null, null, true, "The Herd"),
                new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), set(), null, null, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, 8L, null, getDualRepositoryManager(), pagingInfo);
    }
}
