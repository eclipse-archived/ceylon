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
import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery.Retrieval;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.impl.DefaultRepository;
import com.redhat.ceylon.cmr.webdav.WebDAVContentStore;

//
// HOW TO SET UP HERD FOR TESTING
//
//  1. Follow the instructions in the ceylon-herd/README.md
//  2. Make sure you can log in (named YOUR_USER and YOUR_PASSWD from now on)
//  3. While logged in create a new upload and note its URL (named UPLOAD_URL from now on)
//  4. Make sure you have an up-to-date distribution by running "ant clean publish" in ceylon-dist
//  5. Run "ant -Dherd.repo=UPLOAD_URL -Dherd.user=YOUR_USER -Dherd.pass=YOUR_PASSWD copy-herd" in ceylon-dist
//  6. Run "ant -Dherd.repo=UPLOAD_URL -Dherd.user=YOUR_USER -Dherd.pass=YOUR_PASSWD publish-herd" in ceylon-sdk
//  7. Run "ceylon copy --verbose=files --with-dependencies --all --user=YOUR_USER --pass=YOUR_PASSWD --out=UPLOAD_URL `ceylon info --show-versions --show-incompatible ceylon.json`"
//  8. Go back to the upload page and refresh
//  9. Fix any fixable problems (like claiming projects etc), delete the rest (only a couple of very old modules)
// 10. Publish the upload
//
// You should now have a completely initialized Herd ready to be tested!
//

/**
 * @author Stef Epardaud
 */
public class HerdTestCase extends AbstractTest {

    private final static String HERD_URL = "http://localhost:9000";
    private final static String CEYLON_RELEASE = "1.1.1";
    private final static ModuleVersionArtifact ART_CAR = art(".CAR", 8, 0);
    private final static ModuleVersionArtifact ART_JS = art(".JS", 8, 0);
    private final static String JDK_VERSION = "8"; // The Java version Herd is running with
    
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

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteEmptyJvm() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testComplete("", expected, getRepositoryManager());
    }

    static final ModuleDependencyInfo ceylang = new ModuleDependencyInfo("ceylon.language", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceycommon_sh = new ModuleDependencyInfo("com.redhat.ceylon.common", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceymaven_opt = new ModuleDependencyInfo("com.redhat.ceylon.maven-support", "2.0", true, false);
    static final ModuleDependencyInfo ceycmr = new ModuleDependencyInfo("com.redhat.ceylon.module-resolver", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceycoll100_sh = new ModuleDependencyInfo("ceylon.collection", "1.0.0", false, true);
    static final ModuleDependencyInfo ceycoll110_sh = new ModuleDependencyInfo("ceylon.collection", "1.1.0", false, true);
    static final ModuleDependencyInfo ceycoll_sh = new ModuleDependencyInfo("ceylon.collection", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceycoll = new ModuleDependencyInfo("ceylon.collection", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceytime_sh = new ModuleDependencyInfo("ceylon.time", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceyinterop = new ModuleDependencyInfo("ceylon.interop.java", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceymath_sh = new ModuleDependencyInfo("ceylon.math", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceytime = new ModuleDependencyInfo("ceylon.time", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceyfile = new ModuleDependencyInfo("ceylon.file", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceyfile_sh = new ModuleDependencyInfo("ceylon.file", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceyio_sh = new ModuleDependencyInfo("ceylon.io", CEYLON_RELEASE, false, true);
    static final ModuleDependencyInfo ceyruntime = new ModuleDependencyInfo("ceylon.runtime", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceytest = new ModuleDependencyInfo("com.redhat.ceylon.test", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo ceyunicode = new ModuleDependencyInfo("ceylon.unicode", CEYLON_RELEASE, false, false);
    static final ModuleDependencyInfo javajdbc = new ModuleDependencyInfo("java.jdbc", JDK_VERSION, false, true);
    static final ModuleDependencyInfo javatls = new ModuleDependencyInfo("java.tls", JDK_VERSION, false, false);
    static final ModuleDependencyInfo javabase = new ModuleDependencyInfo("java.base", JDK_VERSION, false, false);
    static final ModuleDependencyInfo javabase_sh = new ModuleDependencyInfo("java.base", JDK_VERSION, false, true);
    static final ModuleDependencyInfo undertow = new ModuleDependencyInfo("io.undertow.core", "1.0.0.Beta20", false, false);
    static final ModuleDependencyInfo xnioapi = new ModuleDependencyInfo("org.jboss.xnio.api", "3.1.0.CR7", false, false);
    static final ModuleDependencyInfo xnionio = new ModuleDependencyInfo("org.jboss.xnio.nio", "3.1.0.CR7", false, false);
    static final ModuleDependencyInfo logman = new ModuleDependencyInfo("org.jboss.logmanager", "1.4.0.Final", false, false);
    static final ModuleDependencyInfo jbossmods = new ModuleDependencyInfo("org.jboss.modules", "1.3.3.Final", false, false);
    static final ModuleDependencyInfo httpclient = new ModuleDependencyInfo("org.apache.httpcomponents.httpclient", "4.3.2", false, false);
    static final ModuleDependencyInfo slf4japi = new ModuleDependencyInfo("org.slf4j.api", "1.6.1", false, false);
    static final ModuleDependencyInfo hello = new ModuleDependencyInfo("hello", "1.0.0", false, false);
    static final ModuleDependencyInfo moduletest_shopt = new ModuleDependencyInfo("moduletest", "0.1", true, true);
    
    static final SortedSet<ModuleVersionArtifact> car6Js6Src = types(art(".CAR", 6, 0), art(".JS", 6, 0), art(".SRC"));
    static final SortedSet<ModuleVersionArtifact> car7Js7Src = types(art(".CAR", 7, 0), art(".JS", 7, 0), art(".SRC"));
    static final SortedSet<ModuleVersionArtifact> carJs = types(ART_CAR, ART_JS);
    static final SortedSet<ModuleVersionArtifact> carSrc = types(ART_CAR, art(".SRC"));
    static final SortedSet<ModuleVersionArtifact> carJsSrc = types(ART_CAR, ART_JS, art(".SRC"));
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeyl() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
                new ModuleDetails("ceylon.locale", null, "", set(), set(CEYLON_RELEASE), deps(ceycoll_sh, ceytime_sh), carJsSrc, true, "The Herd"),
                cloggingDetails,
        };
        testComplete("ceylon.l", expected, getRepositoryManager());
    }

    public final static String jsonDoc0_4 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is Object parsedJson){\n            if(is String author = parsedJson.get(\"author\")){\n                return author;\n            }\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object{\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array{\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n\n";
    public final static String jsonDoc0_5 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is String author = parsedJson.get(\"author\")){\n            return author;\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nOr if you're really sure that you should have a String value:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        return parsedJson.getString(\"author\")){\n    }\n\nYou can iterate Json objects too::\n\n    {String*} getModules(String json){\n        value parsedJson = parse(json);\n        if(is Array modules = parsedJson.get(\"modules\")){\n            return { for (mod in modules) \n                       if(is Object mod, is String name = mod.get(\"name\")) \n                         name \n                   };\n        }\n        throw Exception(\"Invalid JSON data\");\n    }     \nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object {\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array {\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n";
    
    public final static ModuleDetails jsonDetailsAll_jvm =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud", "Tom Bentley"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0", CEYLON_RELEASE), 
                              deps(new ModuleDependencyInfo("ceylon.collection", CEYLON_RELEASE, false, true)),
                              carJsSrc, true, "The Herd");
    public final static ModuleDetails jsonDetails0_5_Api1 =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.3", "0.4", "0.5"), 
                              deps(), types(), true, "The Herd");
    public final static ModuleDetails jsonDetailsAll_js =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud", "Tom Bentley"), set("0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0", CEYLON_RELEASE), 
                              deps(new ModuleDependencyInfo("ceylon.collection", CEYLON_RELEASE, false, true)),
                              carJsSrc, true, "The Herd");
    public final static ModuleDetails jsonDetailsAll_Api1 =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud", "Tom Bentley"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0", CEYLON_RELEASE), 
                              deps(), types(), true, "The Herd");
    public final static ModuleDetails clangDetails =
            new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set(CEYLON_RELEASE), deps(IGNORE_DEPS), carJs, true, "The Herd");
    public final static ModuleDetails clocaleDetails =
            new ModuleDetails("ceylon.locale", null, "", set(), set(CEYLON_RELEASE), deps(ceycoll_sh, ceytime_sh), carJsSrc, true, "The Herd");
    public final static ModuleDetails cloggingDetails =
            new ModuleDetails("ceylon.logging", null, "", set(), set(CEYLON_RELEASE), deps(ceycoll), carJsSrc, true, "The Herd");
    public final static ModuleDetails cmathDetails =
            new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set(CEYLON_RELEASE), deps(javabase), carSrc, true, "The Herd");
    public final static ModuleDetails cnetDetails =
            new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud", "Matej Lazar"), set(CEYLON_RELEASE), deps(ceycoll_sh, ceyfile, ceyinterop, ceyio_sh, undertow, javabase, xnioapi, xnionio), carSrc, true, "The Herd");
    public final static ModuleDetails cprocessDetails =
            new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set(CEYLON_RELEASE), deps(ceyfile_sh, javabase), carSrc, true, "The Herd");
    public final static ModuleDetails cpromiseDetails =
            new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set(CEYLON_RELEASE), deps(javabase), carSrc, true, "The Herd");
    public final static ModuleDetails cruntimeDetails =
            new ModuleDetails("ceylon.runtime", null, "", set(), set(CEYLON_RELEASE), deps(ceylang, ceycommon_sh, ceymaven_opt, ceycmr, logman, jbossmods), types(art(".JAR")), true, "The Herd");
    public final static ModuleDetails ctestDetails =
            new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set(CEYLON_RELEASE), deps(ceycoll), carJsSrc, true, "The Herd");
    public final static ModuleDetails ctimeDetails =
            new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set(CEYLON_RELEASE), deps(ceycoll), carJsSrc, true, "The Herd");
    public final static ModuleDetails cunicodeDetails =
            new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set(CEYLON_RELEASE), deps(ceyinterop, javabase_sh), carSrc, true, "The Herd");
    public final static ModuleDetails ccollectionDetails =
            new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0", CEYLON_RELEASE), deps(), carJsSrc, true, "The Herd");
    public final static ModuleDetails cdbcDetails =
            new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set(CEYLON_RELEASE), deps(ceycoll, ceyinterop, ceymath_sh, ceytime, javabase, javajdbc), carSrc, true, "The Herd");
    public final static ModuleDetails cfileDetails =
            new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set(CEYLON_RELEASE), deps(ceycoll, javabase), carSrc, true, "The Herd");
    public final static ModuleDetails chtmlDetails =
            new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set(CEYLON_RELEASE), deps(ceycoll), carJsSrc, true, "The Herd");
    public final static ModuleDetails cinteropJavaDetails =
            new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set(CEYLON_RELEASE), deps(ceycoll_sh, javabase_sh), carSrc, true, "The Herd");
    public final static ModuleDetails cioDetails =
            new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set(CEYLON_RELEASE), deps(ceycoll, ceyfile_sh, ceyinterop, javabase, javatls), carSrc, true, "The Herd");
    public final static ModuleDetails testJvmDetails =
            new ModuleDetails("com.redhat.ceylon.testjvm", null, "", set(), set(CEYLON_RELEASE), deps(ceycoll, ceyfile, ceyruntime, ceytest, javabase, jbossmods), carSrc, true, "The Herd");
    
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_jvm,
        };
        testComplete("ceylon.json", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteNameApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_Api1,
        };
        testComplete("ceylon.json", expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_js,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.JS);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_jvm,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.SRC);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteFilteredMembers() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_jvm,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.JVM, Retrieval.ANY, null, null, "ParseException");
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CODE);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CAR);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cruntimeDetails,
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.JAR);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCeylonCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0", CEYLON_RELEASE), deps(), carJsSrc, true, "The Herd"),
                chtmlDetails,
                new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud", "Tom Bentley"), set("0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0", CEYLON_RELEASE), deps(ceycoll_sh), carJsSrc, true, "The Herd"),
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                ctestDetails,
                ctimeDetails,
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CEYLON_CODE, Retrieval.ALL, null, null);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForAll() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_jvm,
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
    public void testHerdCompleteCeylon() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
        };
        testComplete("ceylon", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonDot() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
        };
        testComplete("ceylon.", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersions() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail0_3_1,
                jsonVersionDetail0_4,
                jsonVersionDetail0_5,
                jsonVersionDetail0_6,
                jsonVersionDetail0_6_1,
                jsonVersionDetail1_0_0,
                jsonVersionDetail1_0_1,
                jsonVersionDetail1_1_0,
                jsonVersionDetail1_1_1,
        };
        testListVersions("ceylon.json", null, expected);
    }

    public static final ModuleVersionDetails jsonVersionDetail0_3_1 =                 
            new ModuleVersionDetails("", "0.3.1", "A JSON parser / serialiser", "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.3.1", false, false)), 
                    types(new ModuleVersionArtifact(".car", 2, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail0_4 =                 
            new ModuleVersionDetails("", "0.4", jsonDoc0_4, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.4", false, false)), 
                    types(new ModuleVersionArtifact(".car", 3, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail0_5 =                 
            new ModuleVersionDetails("", "0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.5", false, true)), 
                    types(new ModuleVersionArtifact(".car", 4, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail0_5_Api1 =                 
            new ModuleVersionDetails("", "0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(), 
                    types(),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail0_6 = 
            new ModuleVersionDetails("", "0.6", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.6", false, true)), 
                    types(new ModuleVersionArtifact(".car", 5, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail0_6_1 = 
            new ModuleVersionDetails("", "0.6.1", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.6.1", false, true)), 
                    types(new ModuleVersionArtifact(".car", 5, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail1_0_0 =                 
            new ModuleVersionDetails("", "1.0.0", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(ceycoll100_sh), 
                    car6Js6Src,
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail1_0_1 =                 
            new ModuleVersionDetails("", "1.0.1", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(ceycoll100_sh), 
                    car6Js6Src,
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail1_1_0 =                 
            new ModuleVersionDetails("", "1.1.0", null, "Apache Software License", set("Stéphane Épardaud"),
                    deps(ceycoll110_sh), 
                    car7Js7Src,
                    true, "The Herd (" + HERD_URL + ")");
    public static final ModuleVersionDetails jsonVersionDetail1_1_1 =                 
            new ModuleVersionDetails("", CEYLON_RELEASE, null, "Apache Software License", set("Stéphane Épardaud", "Tom Bentley"),
                    deps(ceycoll_sh), 
                    carJsSrc,
                    true, "The Herd (" + HERD_URL + ")");

    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsComplete() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail1_1_1,
        };
        testListVersions("ceylon.json", CEYLON_RELEASE, expected);
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
                jsonVersionDetail1_0_0,
                jsonVersionDetail1_0_1,
        };
        testListVersions("ceylon.json", "1.0", expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsMembersFiltered() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail1_0_1,
        };
        testListVersions("ceylon.json", "1.0", expected, getRepositoryManager(), null, null, "ParseException");
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected);
    }

    public final static ModuleDetails[] binIncompatDetails = new ModuleDetails[]{
            new ModuleDetails("ceylon.runtime", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.github.lookfirst.sardine", null, null, set(), set("5.1"), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.github.rjeschke.txtmark", null, null, set(), set("0.11"), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.common", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.compiler.java", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.compiler.js", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.maven-support", null, null, set(), set("2.0"), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.module-resolver", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("com.redhat.ceylon.typechecker", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("io.undertow.core", null, null, set(), set("1.0.0.Beta20"), deps(), types(), true, "The Herd"),
            new ModuleDetails("javax.servlet", null, null, set(), set("3.1.0"), deps(), types(), true, "The Herd"),
            new ModuleDetails("net.minidev.json-smart", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.antlr.antlr", null, null, set(), set("2.7.7"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.antlr.runtime", null, null, set(), set("3.4"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.antlr.stringtemplate", null, null, set(), set("3.2.1"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.apache.commons.codec", null, null, set(), set("1.8"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.apache.commons.logging", null, null, set(), set(CEYLON_RELEASE), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.apache.httpcomponents.httpclient", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.apache.httpcomponents.httpcore", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
            new ModuleDetails("org.jboss.jandex", null, null, set(), set("1.0.3.Final"), deps(), types(), true, "The Herd"),
    };

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteBinaryIncompatible() throws Exception {
        testComplete("", binIncompatDetails, getRepositoryManager(), Type.JVM, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchBinaryIncompatible() throws Exception {
        testSearchResults("", Type.JVM, binIncompatDetails, null, null, getRepositoryManager(), null, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJvm() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clocaleDetails,
                cloggingDetails,
        };
        testSearchResults("ceylon.lo", Type.JVM, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clocaleDetails,
                cloggingDetails,
        };
        testSearchResults("ceylon.lo", Type.CAR, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cruntimeDetails,
        };
        testSearchResults("ceylon.r", Type.JAR, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
                clocaleDetails,
                cloggingDetails,
        };
        testSearchResults("ceylon.l", Type.JS, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredComplete() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_jvm,
        };
        testSearchResults("ceylon.json", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredLicense() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cdbcDetails,
        };
        testSearchResults("Apache Software License 2.0", Type.ALL, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredAuthor() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cdbcDetails,
                clangDetails,
        };
        testSearchResults("Enrique", Type.ALL, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredDependencies() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cfileDetails,
                cioDetails,
                cnetDetails,
                cprocessDetails,
                testJvmDetails,
        };
        testSearchResults("ceylon.file", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredCompleteApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonDetailsAll_Api1,
        };
        testSearchResults("ceylon.json", Type.JVM, expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cdbcDetails,
                cfileDetails,
        };
        testSearchResults("", Type.JVM, expected, 1l, 2l);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
                clocaleDetails,
                cloggingDetails,
                cmathDetails,
                cnetDetails,
                cprocessDetails,
                cpromiseDetails,
                cruntimeDetails,
                ctestDetails,
                ctimeDetails,
                cunicodeDetails,
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0"), deps(), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("hello", null, "Apache Software License", set("The Ceylon Team"), set("1.0.0"), deps(), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("moduletest", null, "GPLv2", set("The Ceylon Team"), set("0.1"), deps(hello), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("old-jar", null, null, set(), set("1.2.CR1"), deps(moduletest_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("older-jar", null, null, set(), set("12-b3"), deps(moduletest_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final"), deps(), types(), true, "The Herd"),
                new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
        };
        testSearchResults("", Type.JVM, expected, 0L, 3L, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged2() throws Exception {
        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
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
                cfileDetails,
                chtmlDetails,
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
        };
        results = testSearchResults("ceylon", Type.JVM, expected, 0L, 6L, getDualRepositoryManager(), pagingInfo);

        // check end indices
        pagingInfo = results.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo);
        Assert.assertEquals(3, pagingInfo.length);
        Assert.assertEquals(0, pagingInfo[0]);
        Assert.assertEquals(0, pagingInfo[1]);
        Assert.assertEquals(8, pagingInfo[2]);
        
        // again first page
        ModuleDetails[] expected2 = new ModuleDetails[]{
                ccollectionDetails,
                cdbcDetails,
                cfileDetails,
                chtmlDetails,
        };
        ModuleSearchResult results2 = testSearchResults("ceylon", Type.JVM, expected2, 0L, 4L, getDualRepositoryManager());

        // check end indices
        long[] pagingInfo2 = results2.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo2);
        Assert.assertEquals(3, pagingInfo2.length);
        Assert.assertEquals(0, pagingInfo2[0]);
        Assert.assertEquals(0, pagingInfo2[1]);
        Assert.assertEquals(4, pagingInfo2[2]);

        // second page
        expected2 = new ModuleDetails[]{
                cinteropJavaDetails,
                cioDetails,
                jsonDetailsAll_jvm,
                clangDetails,
        };
        results2 = testSearchResults("ceylon", Type.JVM, expected2, 0L, 4L, getDualRepositoryManager(), pagingInfo2);

        // check end indices
        pagingInfo2 = results2.getNextPagingInfo();
        Assert.assertNotNull(pagingInfo2);
        Assert.assertEquals(3, pagingInfo2.length);
        Assert.assertEquals(0, pagingInfo2[0]);
        Assert.assertEquals(0, pagingInfo2[1]);
        Assert.assertEquals(8, pagingInfo2[2]);

    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
                cprocessDetails,
        };

        testSearchResultsMember("", Type.JVM, "process", false, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByNameAndMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cprocessDetails,
        };

        testSearchResultsMember("process", Type.JVM, "process", false, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByExactMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
        };

        testSearchResultsMember("", Type.JVM, "ceylon.language::process", true, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByExactMemberKeyword() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cmathDetails,
        };

        testSearchResultsMember("", Type.JVM, "ceylon.math.float::pi", true, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByMemberKeyword() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                cmathDetails,
        };

        testSearchResultsMember("", Type.JVM, "ceylon.math.fl", false, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
        };

        testSearchResultsMember("", Type.JVM, "ceylon.languag", false, true, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByExactPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                clangDetails,
        };

        testSearchResultsMember("", Type.JVM, "ceylon.language", true, true, expected);
    }
}
