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

import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleSearchResult;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
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

//
// HOW TO SET UP HERD FOR TESTING
//
// 1. Follow the instructions in the ceylon-herd/README.md
// 2. Run "ant publish-herd" to publish the SDK to the new installation
// 3. Make sure you can log in (named YOUR_USER and YOUR_PASSWD from now on)
// 4. While logged in create a new upload and note its URL (named UPLOAD_URL from now on)
// 5. Run "ceylon copy --verbose=files --with-dependencies --all --user=YOUR_USER --pass=YOUR_PASSWD --out=UPLOAD_URL `ceylon info --show-versions --show-incompatible ceylon.json`"
// 6. Go back to the upload page and refresh
// 7. Fix any fixable problems (like claiming projects etc), delete the rest (only a couple of very old modules and the ones that already exist because of step #5)
// 8. Publish the upload
//
// You should now have a completely initialized Herd ready to be tested!
//

/**
 * @author Stef Epardaud
 */
public class HerdTestCase extends AbstractTest {

    @Test
    public void testDummy() {
    }

    private RepositoryManager getRepositoryManager(File root, boolean offline, int timeout, String apiVersion) throws Exception {
        RepositoryManagerBuilder builder = getRepositoryManagerBuilder(root, offline, timeout);
        WebDAVContentStore rcs = new WebDAVContentStore("http://localhost:9000/test", log, false, 60000, apiVersion);
        Repository repo = new DefaultRepository(rcs.createRoot());
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
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testComplete("", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.runtime", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, null, set(), set("5.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, null, set(), set("0.11"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.common", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.compiler.java", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.compiler.js", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.maven-support", null, null, set(), set("2.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.module-resolver", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.typechecker", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("io.undertow.core", null, null, set(), set("1.0.0.Beta20"), deps(), types(), true, "The Herd"),
                new ModuleDetails("net.minidev.json-smart", null, null, set(), set("1.1.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.antlr", null, null, set(), set("2.7.7"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.runtime", null, null, set(), set("3.4"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.stringtemplate", null, null, set(), set("3.2.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.commons.codec", null, null, set(), set("1.8"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.commons.logging", null, null, set(), set("1.1.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.httpcomponents.httpclient", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.httpcomponents.httpcore", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.jboss.jandex", null, null, set(), set("1.0.3.Final"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.jboss.logging", null, null, set(), set("3.1.3.GA", "3.1.2.GA"), deps(), types(), true, "The Herd"),
        };
        testComplete("", expected, getRepositoryManager(), Type.JVM, 1234, 0);
    }

    static final ModuleDependencyInfo ceylang110 = new ModuleDependencyInfo("ceylon.language", "1.1.0", false, false);
    static final ModuleDependencyInfo ceycommon110_sh = new ModuleDependencyInfo("com.redhat.ceylon.common", "1.1.0", false, true);
    static final ModuleDependencyInfo ceymaven110_opt = new ModuleDependencyInfo("com.redhat.ceylon.maven-support", "2.0", true, false);
    static final ModuleDependencyInfo ceycmr110 = new ModuleDependencyInfo("com.redhat.ceylon.module-resolver", "1.1.0", false, false);
    static final ModuleDependencyInfo ceycoll100_sh = new ModuleDependencyInfo("ceylon.collection", "1.0.0", false, true);
    static final ModuleDependencyInfo ceycoll110_sh = new ModuleDependencyInfo("ceylon.collection", "1.1.0", false, true);
    static final ModuleDependencyInfo ceycoll110 = new ModuleDependencyInfo("ceylon.collection", "1.1.0", false, false);
    static final ModuleDependencyInfo ceytime110_sh = new ModuleDependencyInfo("ceylon.time", "1.1.0", false, true);
    static final ModuleDependencyInfo ceyinterop110 = new ModuleDependencyInfo("ceylon.interop.java", "1.1.0", false, false);
    static final ModuleDependencyInfo ceymath110_sh = new ModuleDependencyInfo("ceylon.math", "1.1.0", false, true);
    static final ModuleDependencyInfo ceytime110 = new ModuleDependencyInfo("ceylon.time", "1.1.0", false, false);
    static final ModuleDependencyInfo ceyfile110 = new ModuleDependencyInfo("ceylon.file", "1.1.0", false, false);
    static final ModuleDependencyInfo ceyfile110_sh = new ModuleDependencyInfo("ceylon.file", "1.1.0", false, true);
    static final ModuleDependencyInfo ceyio110_sh = new ModuleDependencyInfo("ceylon.io", "1.1.0", false, true);
    static final ModuleDependencyInfo ceyunicode110 = new ModuleDependencyInfo("ceylon.unicode", "1.1.0", false, false);
    static final ModuleDependencyInfo javajdbc7 = new ModuleDependencyInfo("java.jdbc", "7", false, true);
    static final ModuleDependencyInfo javatls7 = new ModuleDependencyInfo("java.tls", "7", false, false);
    static final ModuleDependencyInfo javabase7 = new ModuleDependencyInfo("java.base", "7", false, false);
    static final ModuleDependencyInfo javabase7_sh = new ModuleDependencyInfo("java.base", "7", false, true);
    static final ModuleDependencyInfo undertow = new ModuleDependencyInfo("io.undertow.core", "1.0.0.Beta20", false, false);
    static final ModuleDependencyInfo xnioapi = new ModuleDependencyInfo("org.jboss.xnio.api", "3.1.0.CR7", false, false);
    static final ModuleDependencyInfo xnionio = new ModuleDependencyInfo("org.jboss.xnio.nio", "3.1.0.CR7", false, false);
    static final ModuleDependencyInfo logman = new ModuleDependencyInfo("org.jboss.logmanager", "1.4.0.Final", false, false);
    static final ModuleDependencyInfo jbossmods = new ModuleDependencyInfo("org.jboss.modules", "1.3.3.Final", false, false);
    static final ModuleDependencyInfo httpclient = new ModuleDependencyInfo("org.apache.httpcomponents.httpclient", "4.3.2", false, false);
    static final ModuleDependencyInfo slf4japi = new ModuleDependencyInfo("org.slf4j.api", "1.6.1", false, false);
    static final ModuleDependencyInfo hello110 = new ModuleDependencyInfo("hello", "1.0.0", false, false);
    static final ModuleDependencyInfo moduletest01_shopt = new ModuleDependencyInfo("moduletest", "0.1", true, true);
    
    static final SortedSet<ModuleVersionArtifact> car6Js6Src = types(art(".CAR", 6, 0), art(".JS", 6, 0), art(".SRC"));
    static final SortedSet<ModuleVersionArtifact> car7Js7 = types(art(".CAR", 7, 0), art(".JS", 7, 0));
    static final SortedSet<ModuleVersionArtifact> car7Src = types(art(".CAR", 7, 0), art(".SRC"));
    static final SortedSet<ModuleVersionArtifact> car7Js7Src = types(art(".CAR", 7, 0), art(".JS", 7, 0), art(".SRC"));
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeyl() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
        };
        testComplete("ceylon.l", expected, getRepositoryManager());
    }

    public final static String jsonDoc0_4 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is Object parsedJson){\n            if(is String author = parsedJson.get(\"author\")){\n                return author;\n            }\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object{\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array{\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n\n";
    public final static String jsonDoc0_5 = "Contains everything required to parse and serialise JSON data.\n\nSample usage for parsing and accessing JSON:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        if(is String author = parsedJson.get(\"author\")){\n            return author;\n        }\n        throw Exception(\"Invalid JSON data\");\n    }\n\nOr if you're really sure that you should have a String value:\n\n    String getAuthor(String json){\n        value parsedJson = parse(json);\n        return parsedJson.getString(\"author\")){\n    }\n\nYou can iterate Json objects too::\n\n    {String*} getModules(String json){\n        value parsedJson = parse(json);\n        if(is Array modules = parsedJson.get(\"modules\")){\n            return { for (mod in modules) \n                       if(is Object mod, is String name = mod.get(\"name\")) \n                         name \n                   };\n        }\n        throw Exception(\"Invalid JSON data\");\n    }     \nSample usage for generating JSON data:\n\n    String getJSON(){\n        value json = Object {\n            \"name\" -> \"Introduction to Ceylon\",\n            \"authors\" -> Array {\n                \"Stef Epardaud\",\n                \"Emmanuel Bernard\"\n            }\n        };\n        return json.string;\n    }\n";
    
    public final static ModuleDetails jsonModuleDetailsAll_jvm =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0"), 
                              deps(new ModuleDependencyInfo("ceylon.collection", "1.1.0", false, true)),
                              car7Js7Src, true, "The Herd");
    public final static ModuleDetails jsonModuleDetails0_5_Api1 =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.3", "0.4", "0.5"), 
                              deps(), types(), true, "The Herd");
    public final static ModuleDetails jsonModuleDetailsAll_js =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0"), 
                              deps(new ModuleDependencyInfo("ceylon.collection", "1.1.0", false, true)),
                              car7Js7Src, true, "The Herd");
    public final static ModuleDetails jsonModuleDetailsAll_Api1 =
            new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0"), 
                              deps(), types(), true, "The Herd");

    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteName() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_jvm,
        };
        testComplete("ceylon.json", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCompleteNameApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_Api1,
        };
        testComplete("ceylon.json", expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_js,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.JS);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForSrc() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_jvm,
        };
        testComplete("ceylon.json", expected, getRepositoryManager(), Type.SRC);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CODE);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CAR);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.JAR);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonForCeylonCode() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.json", null, "Apache Software License", set("Stéphane Épardaud"), set("0.5", "0.6", "0.6.1", "1.0.0", "1.0.1", "1.1.0"), deps(ceycoll110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager(), Type.CEYLON_CODE);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteForAll() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_jvm,
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
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
        };
        testComplete("ceylon", expected, getRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteCeylonDot() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
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
        };
        testListVersions("ceylon.json", null, expected);
    }

    public static final ModuleVersionDetails jsonVersionDetail0_3_1 =                 
            new ModuleVersionDetails("0.3.1", "A JSON parser / serialiser", "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.3.1", false, false)), 
                    types(new ModuleVersionArtifact(".car", 2, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_4 =                 
            new ModuleVersionDetails("0.4", jsonDoc0_4, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.4", false, false)), 
                    types(new ModuleVersionArtifact(".car", 3, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_5 =                 
            new ModuleVersionDetails("0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.5", false, true)), 
                    types(new ModuleVersionArtifact(".car", 4, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_5_Api1 =                 
            new ModuleVersionDetails("0.5", jsonDoc0_5, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(), 
                    types(),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_6 = 
            new ModuleVersionDetails("0.6", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.6", false, true)), 
                    types(new ModuleVersionArtifact(".car", 5, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail0_6_1 = 
            new ModuleVersionDetails("0.6.1", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(new ModuleDependencyInfo("ceylon.collection", "0.6.1", false, true)), 
                    types(new ModuleVersionArtifact(".car", 5, 0),
                            new ModuleVersionArtifact(".js", 0, 0),
                            new ModuleVersionArtifact(".src", null, null)),
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail1_0_0 =                 
            new ModuleVersionDetails("1.0.0", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(ceycoll100_sh), 
                    car6Js6Src,
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail1_0_1 =                 
            new ModuleVersionDetails("1.0.1", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(ceycoll100_sh), 
                    car6Js6Src,
                    true, "The Herd (http://localhost:9000/test)");
    public static final ModuleVersionDetails jsonVersionDetail1_1_0 =                 
            new ModuleVersionDetails("1.1.0", null, "Apache Software License", set("Stéphane Épardaud"), 
                    deps(ceycoll110_sh), 
                    car7Js7Src,
                    true, "The Herd (http://localhost:9000/test)");

    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdCompleteVersionsComplete() throws Exception {
        ModuleVersionDetails[] expected = new ModuleVersionDetails[]{
                jsonVersionDetail1_1_0,
        };
        testListVersions("ceylon.json", "1.1.0", expected);
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
    public void testHerdSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchBinaryIncompatible() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.runtime", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, null, set(), set("5.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, null, set(), set("0.11"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.common", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.compiler.java", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.compiler.js", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.maven-support", null, null, set(), set("2.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.module-resolver", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("com.redhat.ceylon.typechecker", null, null, set(), set("1.1.0"), deps(), types(), true, "The Herd"),
                new ModuleDetails("io.undertow.core", null, null, set(), set("1.0.0.Beta20"), deps(), types(), true, "The Herd"),
                new ModuleDetails("net.minidev.json-smart", null, null, set(), set("1.1.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.antlr", null, null, set(), set("2.7.7"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.runtime", null, null, set(), set("3.4"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.antlr.stringtemplate", null, null, set(), set("3.2.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.commons.codec", null, null, set(), set("1.8"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.commons.logging", null, null, set(), set("1.1.1"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.httpcomponents.httpclient", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.apache.httpcomponents.httpcore", null, null, set(), set("4.3.2"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.jboss.jandex", null, null, set(), set("1.0.3.Final"), deps(), types(), true, "The Herd"),
                new ModuleDetails("org.jboss.logging", null, null, set(), set("3.1.3.GA", "3.1.2.GA"), deps(), types(), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, null, null, getRepositoryManager(), null, 1234, 0);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJvm() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
        };
        testSearchResults("ceylon.lo", Type.JVM, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredCar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
        };
        testSearchResults("ceylon.lo", Type.CAR, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJar() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("ceylon.r", Type.JAR, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredJs() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
        };
        testSearchResults("ceylon.l", Type.JS, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredComplete() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_jvm,
        };
        testSearchResults("ceylon.json", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredLicense() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
        };
        testSearchResults("Apache Software License 2.0", Type.ALL, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredAuthor() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
        };
        testSearchResults("Enrique", Type.ALL, expected);
    }
    
    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredDependencies() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
        };
        testSearchResults("ceylon.file", Type.JVM, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchFilteredCompleteApi1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                jsonModuleDetailsAll_Api1,
        };
        testSearchResults("ceylon.json", Type.JVM, expected, getRepositoryManagerApi1());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchPaged() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, 1l, 2l);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearch() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.locale", "", "", set(), set("1.1.0"), deps(ceycoll110_sh, ceytime110_sh), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.logging", null, "", set(), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.math", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.net", null, "Apache Software License", set("Stéphane Épardaud, Matej Lazar"), set("1.1.0"), deps(ceycoll110_sh, ceyfile110, ceyinterop110, ceyio110_sh, undertow, javabase7, xnioapi, xnionio), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.promise", null, "Apache Software License", set("Julien Viet"), set("1.1.0"), deps(javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.runtime", null, "", set(), set("1.1.0"), deps(ceylang110, ceycommon110_sh, ceymaven110_opt, ceycmr110, logman, jbossmods), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("ceylon.test", null, "Apache Software License", set("Tom Bentley", "Tomáš Hradec"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.time", null, "", set("Diego Coronel", "Roland Tepp"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.unicode", null, "", set("Tom Bentley"), set("1.1.0"), deps(javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("com.acme.helloworld", "The classic Hello World module", "Public domain", set("Stef Epardaud"), set("1.0.0"), deps(), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("com.github.lookfirst.sardine", null, "", set(), set("5.1"), deps(httpclient, slf4japi), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("com.github.rjeschke.txtmark", null, "", set(), set("0.11"), deps(), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("hello", null, "Apache Software License", set("The Ceylon Team"), set("1.0.0"), deps(), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("moduletest", null, "GPLv2", set("The Ceylon Team"), set("0.1"), deps(hello110), types(art(".CAR", 3, null)), true, "The Herd"),
                new ModuleDetails("old-jar", null, null, set(), set("1.2.CR1"), deps(moduletest01_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("older-jar", null, null, set(), set("12-b3"), deps(moduletest01_shopt), types(art(".JAR")), true, "The Herd"),
                new ModuleDetails("org.jboss.acme", null, null, set(), set("1.0.0.Final"), deps(), types(), true, "The Herd"),
                new ModuleDetails("test-jar", null, null, set(), set("0.1"), deps(), types(art(".JAR")), true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged1() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
        };
        testSearchResults("", Type.JVM, expected, 0L, 3L, getDualRepositoryManager());
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdAndRepoSearchPaged2() throws Exception {
        // first page
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
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
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
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
                new ModuleDetails("ceylon.collection", null, "Apache Software License", set("Stéphane Épardaud"), set("0.3.1", "0.4", "0.5", "0.6", "0.6.1", "1.0.0", "1.1.0"), deps(), car7Js7Src, true, "The Herd"),
                new ModuleDetails("ceylon.dbc", null, "Apache Software License 2.0", set("Enrique Zamudio"), set("1.1.0"), deps(ceycoll110, ceyinterop110, ceymath110_sh, ceytime110, javabase7, javajdbc7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.file", null, "", set("Gavin King"), set("1.1.0"), deps(ceycoll110, javabase7), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.html", null, "", set("Daniel Rochetti"), set("1.1.0"), deps(ceycoll110), car7Js7Src, true, "The Herd"),
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
                new ModuleDetails("ceylon.interop.java", null, "", set("The Ceylon Team"), set("1.1.0"), deps(ceycoll110_sh, javabase7_sh), car7Src, true, "The Herd"),
                new ModuleDetails("ceylon.io", null, "Apache Software License", set("Stéphane Épardaud"), set("1.1.0"), deps(ceycoll110, ceyfile110_sh, ceyinterop110, javabase7, javatls7), car7Src, true, "The Herd"),
                jsonModuleDetailsAll_jvm,
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
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
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
        };

        testSearchResultsMember("", Type.JVM, "process", false, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByNameAndMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.process", null, "", set("Gavin King"), set("1.1.0"), deps(ceyfile110_sh, javabase7), car7Src, true, "The Herd"),
        };

        testSearchResultsMember("process", Type.JVM, "process", false, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByExactMember() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
        };

        testSearchResultsMember("", Type.JVM, "ceylon.language::process", true, false, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
        };

        testSearchResultsMember("", Type.JVM, "ceylon.languag", false, true, expected);
    }

    @Test
    @Ignore("Required Herd running locally")
    public void testHerdSearchModulesFilteredByExactPackage() throws Exception {
        ModuleDetails[] expected = new ModuleDetails[]{
                new ModuleDetails("ceylon.language", null, "http://www.apache.org/licenses/LICENSE-2.0.html", set("Enrique Zamudio", "Gavin King", "Stephane Epardaud", "Tako Schotanus", "Tom Bentley"), set("1.1.0"), deps(), car7Js7, true, "The Herd"),
        };

        testSearchResultsMember("", Type.JVM, "ceylon.language", true, true, expected);
    }
}
