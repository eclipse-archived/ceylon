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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleQuery.Retrieval;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;

//
// HOW TO SET UP HERD FOR TESTING
//
//  1. Follow the instructions in the ceylon-herd/README.md relevant to downloading Play
//  2. You can skip the part about setting up a database and a user name
//  3. Start it with `play test`
//
// You should now have a completely initialized Herd ready to be tested!
//

/**
 * @author Stef Epardaud
 */
//@Ignore("Requires Herd running locally")
public class HerdTestCase extends AbstractTest {

    private static final ModuleVersionArtifact ART_SRC = art(".SRC");
    private static final ModuleVersionArtifact ART_JAR = art(".JAR");
    private static final ModuleVersionArtifact ART_CAR = art(".CAR", 8, 0);
    private static final ModuleVersionArtifact ART_JS = art(".JS", 9, 0);
    
    static final SortedSet<ModuleVersionArtifact> carJsSrc = types(ART_CAR, ART_JS, ART_SRC);
    
	private static final String TOM = "Tom Bentley";
	private static final String STEF = "Stéphane Épardaud";
	private static final String ASL = "Apache Software License";
	private static final String SUPER_DOC = "Super documentation";
	private final static String HERD_URL = "http://localhost:9000";
	private static final String HERD_ORIGIN = "The Herd (" + HERD_URL + ")";
    private final static String JDK_VERSION = "8"; // The Java version Herd is running with

    private final static String CEYLON_RELEASE = "6.6.6";
	private static final ModuleDependencyInfo ceylonCollectionDep = new ModuleDependencyInfo("ceylon.collection", CEYLON_RELEASE, false, true);
    
	@BeforeClass
	public static void setup() {
	    boolean running = false;
	    try {
	        URL url = new URL(HERD_URL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.connect();
            con.disconnect();
            running = true;
        } catch (IOException e) {
        }
	    Assume.assumeTrue("Herd server is not running", running);
	}
	
    @Test
    public void testDummy() {
    }

    private RepositoryManager getRepositoryManager(File root, boolean offline, int timeout, String apiVersion) throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(root, offline, timeout, java.net.Proxy.NO_PROXY);
        WebDAVContentStore rcs = new WebDAVContentStore(HERD_URL , log, false, 60000, java.net.Proxy.NO_PROXY, apiVersion);
        CmrRepository repo = new DefaultRepository(rcs.createRoot());
        return builder.addRepository(repo).buildRepository();
    }

    protected RepositoryManager getRepositoryManagerApi1() throws Exception {
        // only Herd
        return getRepositoryManager(getFolders(), false, 20000, "1");
    }

    protected RepositoryManager getRepositoryManager() throws Exception {
        // only Herd
        return getRepositoryManager(getFolders(), false, 20000, null);
    }

    protected RepositoryManager getDualRepositoryManager() throws Exception {
        // Herd + /repo
        return getRepositoryManager(getRepositoryRoot(), false, 20000, null);
    }


    public final static ModuleDetails ceylonCollectionDetails =
            new ModuleDetails("ceylon.collection", 
            		"Collection documentation", 
            		"Apache Software License 2.0", 
            		set(STEF, TOM, "Enrique Zamudio"), 
            		set(CEYLON_RELEASE), 
            		deps(),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTestDetails =
            new ModuleDetails("fr.epardaud.test", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "2.3"), 
            		deps(ceylonCollectionDep),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudJavaDetails =
            new ModuleDetails("fr.epardaud.java", 
            		"", 
            		"", 
            		set(), 
            		set("1.0"), 
            		deps(),
            		types(ART_JAR), 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTest2Details =
            new ModuleDetails("fr.epardaud.test2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("0.3", "0.4", "1.0", "1.2"), 
            		deps(ceylonCollectionDep),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTest2DetailsAll =
            new ModuleDetails("fr.epardaud.test2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("1.0", "1.2"), 
            		deps(ceylonCollectionDep),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTest2DetailsJs =
            new ModuleDetails("fr.epardaud.test2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("0.4", "1.0", "1.2"), 
            		deps(ceylonCollectionDep),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTest2DetailsJvm =
            new ModuleDetails("fr.epardaud.test2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("0.3", "1.0", "1.2"), 
            		deps(ceylonCollectionDep),
            		carJsSrc, 
            		true, 
            		"The Herd");
    public final static ModuleDetails frEpardaudTest2DetailsJvmApi1 =
            new ModuleDetails("fr.epardaud.test2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
            		set("0.3", "1.0", "1.2"), 
            		deps(),// no deps in API1
            		types(), // no types in API1
            		true, 
            		"The Herd");

    static final ModuleDependencyInfo langmod = new ModuleDependencyInfo("ceylon.language", "1.2.2", false, false);
    static final ModuleDependencyInfo hello = new ModuleDependencyInfo("hello", "1.0.0", false, false);
    static final ModuleDependencyInfo moduletest_shopt = new ModuleDependencyInfo("moduletest", "0.1", true, true);
    
    @Test
    public void testHerdCompleteEmptyJvm() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
        		frEpardaudJavaDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("", expected, getRepositoryManager());
    }

    @Test
    public void testHerdCompleteFrEpardaudT() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud.t", expected, getRepositoryManager());
    }

    @Test
    public void testHerdCompleteCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManager());
    }

    @Test
    public void testHerdCompleteCompleteNameApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvmApi1,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManagerApi1());
    }

    @Test
    public void testHerdCompleteForJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJs,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManager(), Type.JS);
    }

    @Test
    public void testHerdCompleteForSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2Details,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManager(), Type.SRC);
    }

    @Test
    public void testHerdCompleteFilteredMembers() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManager(), Type.JVM, Retrieval.ANY, 
        		null, null, null, null, "ParseException");
    }

    @Test
    public void testHerdCompleteCeylonForCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2Details,
        };
        testComplete("fr.epardaud", expected, getRepositoryManager(), Type.CODE);
    }

    @Test
    public void testHerdCompleteCeylonForCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud", expected, getRepositoryManager(), Type.CAR);
    }

    @Test
    public void testHerdCompleteCeylonForJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        };
        testComplete("fr.epardaud", expected, getRepositoryManager(), Type.JAR);
    }

    @Test
    public void testHerdCompleteCeylonForCeylonCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsAll,
        };
        testComplete("fr.epardaud", expected, getRepositoryManager(), Type.CEYLON_CODE, Retrieval.ALL, 
        		null, null, null, null);
    }

    @Test
    public void testHerdCompleteForAll() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2Details,
        };
        testComplete("fr.epardaud.test2", expected, getRepositoryManager(), Type.ALL);
    }

    @Test
    public void testHerdCompleteStopsAfterCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        };
        testComplete("fr.epardaud.test2.", expected, getRepositoryManager());
    }

    @Test
    public void testHerdCompleteCeylon() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud", expected, getRepositoryManager());
    }

    @Test
    public void testHerdCompleteCeylonDot() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testComplete("fr.epardaud.", expected, getRepositoryManager());
    }

    public static final ModuleVersionDetails frEpardaudTest2_03 =                 
            new ModuleVersionDetails("fr.epardaud.test2", "0.3", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest2_04 =                 
            new ModuleVersionDetails("fr.epardaud.test2", "0.4", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest2_10 =                 
            new ModuleVersionDetails("fr.epardaud.test2", "1.0", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                    		new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest2_12 =                 
            new ModuleVersionDetails("fr.epardaud.test2", "1.2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                    		new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest2_12Api1 =                 
            new ModuleVersionDetails("fr.epardaud.test2", "1.2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest_12 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.2", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                    		new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest_13 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.3", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(new ModuleVersionArtifact(".car", 7, 0),
                    		new ModuleVersionArtifact(".js", 7, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);
    public static final ModuleVersionDetails frEpardaudTest_14 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.4", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                    		new ModuleVersionArtifact(".js", 8, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);
    public static final ModuleVersionDetails frEpardaudTest_15 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.5", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(new ModuleVersionArtifact(".car", 9, 0),
                    		new ModuleVersionArtifact(".js", 8, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);
    public static final ModuleVersionDetails frEpardaudTest_16 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.6", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(new ModuleVersionArtifact(".car", 9, 0),
                    		new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);
    public static final ModuleVersionDetails frEpardaudTest_17 =                 
            new ModuleVersionDetails("fr.epardaud.test", "1.7", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(), 
                    types(new ModuleVersionArtifact(".car", 10, 0),
                    		new ModuleVersionArtifact(".js", 10, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    public static final ModuleVersionDetails frEpardaudTest_23 =                 
            new ModuleVersionDetails("fr.epardaud.test", "2.3", 
            		SUPER_DOC, 
            		ASL, 
            		set(STEF, TOM), 
                    deps(ceylonCollectionDep), 
                    types(new ModuleVersionArtifact(".car", 8, 0),
                    		new ModuleVersionArtifact(".js", 9, 0),
                            ART_SRC),
                    true, HERD_ORIGIN);

    @Test
    public void testHerdCompleteVersions() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        		frEpardaudTest2_03,
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", null, expected);
    }

    @Test
    public void testHerdCompleteVersionsComplete() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", "1.2", expected);
    }

    @Test
    public void testHerdCompleteVersionsCompleteApi1() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        		frEpardaudTest2_12Api1,
        };
        testListVersions("fr.epardaud.test2", "1.2", expected, getRepositoryManagerApi1());
    }

    @Test
    public void testHerdCompleteVersionsBinaryIncompatible() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        };
        testListVersions("fr.epardaud.test2", null, expected, getRepositoryManager(), 1234, 0, 1234, 0);
    }

    @Test
    public void testHerdCompleteVersionsBinaryFilter() throws Exception {
        ModuleVersionDetails[] expected70 = new ModuleVersionDetails[]{
        		frEpardaudTest_13,
        };
        testListVersions("fr.epardaud.test", null, expected70, getRepositoryManager(), 7, 0, 7, 0);
        testListVersions("fr.epardaud.test", null, expected70, getRepositoryManager(), 7, 0, null, null);
        testListVersions("fr.epardaud.test", null, expected70, getRepositoryManager(), null, null, 7, 0);

        ModuleVersionDetails[] expected88Both = new ModuleVersionDetails[]{
        		frEpardaudTest_14,
        };
        testListVersions("fr.epardaud.test", null, expected88Both, getRepositoryManager(), 8, 0, 8, 0);

        ModuleVersionDetails[] expected8Jvm = new ModuleVersionDetails[]{
        		frEpardaudTest_12,
        		frEpardaudTest_14,
        		frEpardaudTest_23,
        };
        testListVersions("fr.epardaud.test", null, expected8Jvm, getRepositoryManager(), 8, 0, null, null);

        ModuleVersionDetails[] expected8Js = new ModuleVersionDetails[]{
        		frEpardaudTest_14,
        		frEpardaudTest_15,
        };
        testListVersions("fr.epardaud.test", null, expected8Js, getRepositoryManager(), null, null, 8, 0);

        ModuleVersionDetails[] expected89Both = new ModuleVersionDetails[]{
        		frEpardaudTest_12,
        		frEpardaudTest_23,
        };
        testListVersions("fr.epardaud.test", null, expected89Both, getRepositoryManager(), 8, 0, 9, 0);

        ModuleVersionDetails[] expected98Both = new ModuleVersionDetails[]{
        		frEpardaudTest_15,
        };
        testListVersions("fr.epardaud.test", null, expected98Both, getRepositoryManager(), 9, 0, 8, 0);

        ModuleVersionDetails[] expected9Jvm = new ModuleVersionDetails[]{
        		frEpardaudTest_15,
        		frEpardaudTest_16,
        };
        testListVersions("fr.epardaud.test", null, expected9Jvm, getRepositoryManager(), 9, 0, null, null);

        ModuleVersionDetails[] expected9Js = new ModuleVersionDetails[]{
        		frEpardaudTest_12,
        		frEpardaudTest_16,
        		frEpardaudTest_23,
        };
        testListVersions("fr.epardaud.test", null, expected9Js, getRepositoryManager(), null, null, 9, 0);

        ModuleVersionDetails[] expected10 = new ModuleVersionDetails[]{
        		frEpardaudTest_17,
        };
        testListVersions("fr.epardaud.test", null, expected10, getRepositoryManager(), 10, 0, 10, 0);
        testListVersions("fr.epardaud.test", null, expected10, getRepositoryManager(), 10, 0, null, null);
        testListVersions("fr.epardaud.test", null, expected10, getRepositoryManager(), null, null, 10, 0);
        
        // now artifacts that are only JS/only JVM

        ModuleVersionDetails[] expectedOnlyJs = new ModuleVersionDetails[]{
        		frEpardaudTest2_04,
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", null, expectedOnlyJs, getRepositoryManager(), 8, 0, 9, 0, null, 
        		ModuleQuery.Type.JS, ModuleQuery.Retrieval.ANY);

        ModuleVersionDetails[] expectedOnlyJvm = new ModuleVersionDetails[]{
        		frEpardaudTest2_03,
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", null, expectedOnlyJvm, getRepositoryManager(), 8, 0, 9, 0, null, 
        		ModuleQuery.Type.JVM, ModuleQuery.Retrieval.ANY);

        ModuleVersionDetails[] expectedAny = new ModuleVersionDetails[]{
        		frEpardaudTest2_03,
        		frEpardaudTest2_04,
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", null, expectedAny, getRepositoryManager(), 8, 0, 9, 0, null, 
        		ModuleQuery.Type.CODE, ModuleQuery.Retrieval.ANY);

        ModuleVersionDetails[] expectedBoth = new ModuleVersionDetails[]{
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", null, expectedBoth, getRepositoryManager(), 8, 0, 9, 0, null, 
        		ModuleQuery.Type.CEYLON_CODE, ModuleQuery.Retrieval.ALL);
    }

    @Test
    public void testHerdCompleteVersionsFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        		frEpardaudTest2_10,
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", "1", expected);
    }

    @Test
    public void testHerdCompleteVersionsMembersFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
        		frEpardaudTest2_12,
        };
        testListVersions("fr.epardaud.test2", "1", expected, getRepositoryManager(), null, null, null, null, "ParseException");
    }

    @Test
    public void testHerdSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
        		frEpardaudJavaDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testSearchResults("", Type.JVM, expected);
    }

    @Test
    public void testHerdCompleteBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        };
        testComplete("", expected, getRepositoryManager(), Type.JVM, 1234, 0, 1234, 0);
    }

    @Test
    public void testHerdSearchBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        };
        testSearchResults("", Type.JVM, expected, null, null, getRepositoryManager(), null, 1234, 0, 1234, 0);
    }

    @Test
    public void testHerdSearchFilteredJvm() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testSearchResults("fr.epardaud.te", Type.JVM, expected);
    }
    
    @Test
    public void testHerdSearchFilteredCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testSearchResults("fr.epardaud.te", Type.CAR, expected);
    }
    
    @Test
    public void testHerdSearchFilteredJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudJavaDetails,
        };
        testSearchResults("fr.epar", Type.JAR, expected);
    }
    
    @Test
    public void testHerdSearchFilteredJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJs,
        };
        testSearchResults("fr.epar", Type.JS, expected);
    }
    
    @Test
    public void testHerdSearchFilteredComplete() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };
        testSearchResults("fr.epardaud.test2", Type.JVM, expected);
    }

    @Test
    public void testHerdSearchFilteredLicense() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
        };
        testSearchResults("Apache Software License 2.0", Type.ALL, expected);
    }

    @Test
    public void testHerdSearchFilteredAuthor() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
        };
        testSearchResults("Enrique", Type.ALL, expected);
    }
    
    @Test
    public void testHerdSearchFilteredDependencies() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };
        testSearchResults("ceylon.collection", Type.JVM, expected);
    }

    @Test
    public void testHerdSearchFilteredCompleteApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                frEpardaudTest2DetailsJvmApi1,
        };
        testSearchResults("fr.epardaud.test2", Type.JVM, expected, getRepositoryManagerApi1());
    }

    @Test
    public void testHerdSearchPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                frEpardaudJavaDetails,
                frEpardaudTestDetails,
        };
        testSearchResults("", Type.JVM, expected, 1l, 2l);
    }

    @Test
    public void testHerdAndRepoSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0"), deps(), types(art(".CAR", 3, 0)), true, "The Herd"),
                frEpardaudJavaDetails,
                frEpardaudTestDetails,
                frEpardaudTest2DetailsJvm,
                new ModuleDetails("hello", null, ASL, set("The Ceylon Team"), set("1.2.1"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
                new ModuleDetails("hello2", "A test", ASL, set("The Ceylon Team"), set("1.0.0"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
                new ModuleDetails("moduletest", null, "GPLv2", set("The Ceylon Team"), set("0.1"), deps(hello), types(art(".CAR", 3, 0)), true, "The Herd"),
                new ModuleDetails("old-jar", null, null, set(), set("1.2.CR1"), deps(moduletest_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("older-jar", null, null, set(), set("12-b3"), deps(moduletest_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, getDualRepositoryManager());
    }

    @Test
    public void testHerdAndRepoSearchPaged1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0"), deps(), types(art(".CAR", 3, 0)), true, "The Herd"),
                frEpardaudJavaDetails,
        };
        testSearchResults("", Type.JVM, expected, 0L, 3L, getDualRepositoryManager());
    }

    @Test
    public void testHerdAndRepoSearchPaged2() throws Exception {
        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
        		ceylonCollectionDetails,
                frEpardaudTestDetails,
        };
        ModuleSearchResult results = testSearchResults("ceylon", Type.JVM, expected, 0L, 2L, getDualRepositoryManager());

        // check end indices
        long[] pagingInfo = results.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo);
        Assert.assertEquals(3, pagingInfo.length);
        Assert.assertEquals(0, pagingInfo[0]);
        Assert.assertEquals(0, pagingInfo[1]);
        Assert.assertEquals(2, pagingInfo[2]);

        // second page
        expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
                new ModuleDetails("hello", null, ASL, set("The Ceylon Team"), set("1.2.1"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
                new ModuleDetails("hello2", "A test", ASL, set("The Ceylon Team"), set("1.0.0"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
                new ModuleDetails("moduletest", null, "GPLv2", set("The Ceylon Team"), set("0.1"), deps(hello), types(art(".CAR", 3, 0)), true, "The Herd"),
        };
        results = testSearchResults("ceylon", Type.JVM, expected, 0L, 6L, getDualRepositoryManager(), pagingInfo);

        // check end indices
        pagingInfo = results.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo);
        Assert.assertEquals(3, pagingInfo.length);
        Assert.assertEquals(3, pagingInfo[0]);
        Assert.assertEquals(0, pagingInfo[1]);
        Assert.assertEquals(3, pagingInfo[2]);
        
        // again first page
        ModuleDetails[] expected2 = new ModuleDetails[]{
        		ceylonCollectionDetails,
                frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
                new ModuleDetails("hello", null, ASL, set("The Ceylon Team"), set("1.2.1"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
        };
        ModuleSearchResult results2 = testSearchResults("ceylon", Type.JVM, expected2, 0L, 4L, getDualRepositoryManager());

        // check end indices
        long[] pagingInfo2 = results2.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo2);
        Assert.assertEquals(3, pagingInfo2.length);
        Assert.assertEquals(1, pagingInfo2[0]);
        Assert.assertEquals(0, pagingInfo2[1]);
        Assert.assertEquals(3, pagingInfo2[2]);

        // second page
        expected2 = new ModuleDetails[]{
                new ModuleDetails("hello2", "A test", ASL, set("The Ceylon Team"), set("1.0.0"), deps(langmod), types(art(".CAR", 8, 0)), true, "The Herd"),
                new ModuleDetails("moduletest", null, "GPLv2", set("The Ceylon Team"), set("0.1"), deps(hello), types(art(".CAR", 3, 0)), true, "The Herd"),
        };
        results2 = testSearchResults("ceylon", Type.JVM, expected2, 0L, 4L, getDualRepositoryManager(), pagingInfo2);

        // check end indices
        pagingInfo2 = results2.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo2);
        Assert.assertEquals(3, pagingInfo2.length);
        Assert.assertEquals(3, pagingInfo2[0]);
        Assert.assertEquals(0, pagingInfo2[1]);
        Assert.assertEquals(3, pagingInfo2[2]);

    }

    @Test
    public void testHerdSearchModulesFilteredByMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("", Type.JVM, "ParseException", false, false, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByNameAndMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("test2", Type.JVM, "ParseException", false, false, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByExactMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("", Type.JVM, "fr.epardaud.test2::ParseException", true, false, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByExactMemberKeyword() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("", Type.JVM, "fr.epardaud.test2.float::pi", true, false, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByMemberKeyword() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("", Type.JVM, "fr.epardaud.test2.fl", false, false, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        		frEpardaudTest2DetailsJvm,
        };

        testSearchResultsMember("", Type.JVM, "fr.epardaud.te", false, true, expected);
    }

    @Test
    public void testHerdSearchModulesFilteredByExactPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
        		frEpardaudTestDetails,
        };

        testSearchResultsMember("", Type.JVM, "fr.epardaud.test", true, true, expected);
    }
}
