/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.ceylondoc.test;

import static com.redhat.ceylon.compiler.typechecker.TypeChecker.LANGUAGE_MODULE_VERSION;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.redhat.ceylon.ceylondoc.CeylonDocTool;
import com.redhat.ceylon.ceylondoc.Util;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.file.JavacFileManager;

public class CeylonDocToolTest {

    @Rule 
    public TestName name = new TestName();
    
    private CeylonDocTool tool(List<File> sourceFolders, List<File> docFolders, List<String> moduleName, 
            boolean haltOnError, boolean deleteDestDir, String... repositories)
            throws Exception {
        
        CeylonDocTool tool = new CeylonDocTool();
        tool.setSourceFolders(sourceFolders);
        tool.setRepositoryAsStrings(Arrays.asList(repositories));
        tool.setModuleSpecs(moduleName);
        tool.setDocFolders(docFolders);
        tool.setHaltOnError(haltOnError);
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        if (deleteDestDir && dir.exists()) {
            Util.delete(dir);
        }
        tool.setOut(dir.getAbsolutePath());
        tool.initialize();
        return tool;
    }

    private CeylonDocTool tool(String pathname, String moduleName, 
            boolean throwOnError, String... repositories)
            throws Exception {
        return tool(pathname, "doc", moduleName, throwOnError, repositories);
    }

    private CeylonDocTool tool(String pathname, String docPath, String moduleName, 
            boolean throwOnError, String... repositories)
            throws Exception {
        return tool(Arrays.asList(new File(pathname)),
                Arrays.asList(new File(docPath)),
                Arrays.asList(moduleName),
                throwOnError, true, repositories);
    }

    protected void assertFileExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertTrue(file + " doesn't exist", file.exists());
        Assert.assertTrue(file + " exists but is not a file", file.isFile());
    }
    
    protected void assertFileNotExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertFalse(file + " does exist", file.exists());
    }
    
    protected void assertDirectoryExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertTrue(file + " doesn't exist", file.exists());
        Assert.assertTrue(file + " exist but isn't a directory", file.isDirectory());
    }
    
    static interface GrepAsserter {

        void makeAssertions(Matcher matcher);

    }
    
    static GrepAsserter AT_LEAST_ONE_MATCH = new GrepAsserter() {

        @Override
        public void makeAssertions(Matcher matcher) {
            Assert.assertTrue("Zero matches for " + matcher.pattern().pattern(), matcher.find());
        }
        
    };
    
    static GrepAsserter NO_MATCHES = new GrepAsserter() {

        @Override
        public void makeAssertions(Matcher matcher) {
            boolean found = matcher.find();
            if (found) {
                Assert.fail("Unexpected match for " + matcher.pattern().pattern() + ": " + matcher.group(0));
            }
        }
        
    };
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern, GrepAsserter asserter) throws Exception {
        assertFileExists(destDir, path);
        Charset charset = Charset.forName("UTF-8");
        
        File file = new File(destDir, path);
        FileInputStream stream = new FileInputStream(file);
        try  {
            FileChannel channel = stream.getChannel();
            try {
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                CharBuffer chars = charset.decode(map);
                Matcher matcher = pattern.matcher(chars);
                asserter.makeAssertions(matcher);
            } finally {
                channel.close();
            }
        } finally {
            stream.close();
        }
    }
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern) throws Exception {
        assertMatchInFile(destDir, path, pattern, AT_LEAST_ONE_MATCH);
    }
    
    protected void assertNoMatchInFile(File destDir, String path, Pattern pattern) throws Exception {
        assertMatchInFile(destDir, path, pattern, NO_MATCHES);
    }
    
    @Test
    public void moduleA() throws Exception {
        moduleA(false);
    }
    
    @Test
    public void moduleAWithPrivate() throws Exception {
        moduleA(true);
    }

    private void moduleA(boolean includeNonShared) throws Exception {
        String pathname = "test/ceylondoc";
        String docname = "test/ceylondoc-doc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.single";

        CeylonDocTool tool = tool(pathname, docname, moduleName, true);
        tool.setIncludeNonShared(includeNonShared);
        tool.setIncludeSourceCode(true);
        tool.setHeader("<div class='navbar-inverse navbar-static-top'>" +
        		           "<div class='navbar-inner' style='color:white; font-style: italic; text-align: center'>" +
        		               "documentation under construction" +
        		           "</div>" +
        		       "</div>");
        tool.setFooter("<p style='text-align: right;'>" +
        		       "Copyright © 2010-2013, Red Hat, Inc. or third-party contributors" +
        		       "</p>");
        tool.run();
        
        Module module = new Module();
        module.setName(Arrays.asList(moduleName));
        module.setVersion("3.1.4");
        
        File destDir = getOutputDir(tool, module);
        
        assertFileExists(destDir, includeNonShared);
        assertBasicContent(destDir, includeNonShared);
        assertZipContent(destDir);
        assertBy(destDir);
        assertLicense(destDir);
        assertParametersDocumentation(destDir);
        assertParametersAssertions(destDir);
        assertThrows(destDir);
        assertSee(destDir);
        assertIcons(destDir);
        assertInnerTypesDoc(destDir);
        assertDeprecated(destDir);
        assertTagged(destDir);
        assertDocumentationOfRefinedMember(destDir);
        assertSequencedParameter(destDir);
        assertCallableParameter(destDir);
        assertTupleParameter(destDir);
        assertDefaultedParametres(destDir);
        assertAnythingReturnType(destDir);
        assertFencedCodeBlockWithSyntaxHighlighter(destDir);
        assertWikiStyleLinkSyntax(destDir);
        assertConstants(destDir);
        assertLinksToRefinedDeclaration(destDir);
        assertGenericTypeParams(destDir);
        assertObjectPageDifferences(destDir);
        assertSharedParameterOfClass(destDir);
        assertAliases(destDir);
        assertPackageNavigation(destDir);
        assertSubpackages(destDir);
        assertAnnotations(destDir);
        assertAbstractClassModifier(destDir);
        assertFinalClassModifier(destDir);
        assertHeaderAndFooter(destDir);
        assertBug659ShowInheritedMembers(destDir);
        assertBug691AbbreviatedOptionalType(destDir);
        assertBug839(destDir);
        assertBug927LoadingAndSortingInheritedMembers(destDir);
        assertBug968(destDir);
        assertBug1619BrokenLinkFromInheritedDoc(destDir);
        assertBug1619BrokenLinkWithNewLine(destDir);
    }

    @Test
    public void externalLinksToLocalRepoUrlWithModuleNamePattern() throws Exception {
        String repoUrl = "file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        externalLinks(repoUrl, "com.redhat=" + repoUrl);
    }
    
    @Test
    public void externalLinksToLocalRepoPathWithModuleNamePattern() throws Exception {
        String repoUrl = new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        // note that even though we pass a path, the links are created as URIs, which must include the file: scheme
        // but the // for authority is not required and will in fact not be generated, as per URI RFC
        externalLinks("file:"+repoUrl, "com.redhat=" + repoUrl);
    }

    @Test
    public void externalLinksToRemoteRepoWithModuleNamePattern() throws Exception {
        String repoUrl = "http://acme.com/repo";
        externalLinks(repoUrl, "com.redhat=" + repoUrl);
    }
    
    @Test
    public void externalLinksToLocalRepoUrlWithoutModuleNamePattern() throws Exception {
        String repoUrl = "file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        externalLinks(repoUrl, "file://not-existing-dir", "https://not-existing-url", repoUrl);
    }
    
    @Test
    public void externalLinksToRemoteRepoWithoutModuleNamePattern() throws Exception {
        HttpServer stubServer = HttpServer.create(new InetSocketAddress(0), 1);
        stubServer.createContext("/repo", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if (httpExchange.getRequestMethod().equals("HEAD")) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_IMPLEMENTED, -1);
                }
                httpExchange.close();
            }
        });
        stubServer.start();
        
        try {
            String repoUrl = "http://localhost:" + stubServer.getAddress().getPort() + "/repo";
            externalLinks(repoUrl, "file://not-existing-dir", "https://not-existing-url", repoUrl);
        } finally {
            stubServer.stop(0);
        }
    }    
    
    private File externalLinks(String repoUrl, String... linkArgs) throws Exception {
        compile("test/ceylondoc", "com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        compile("test/ceylondoc", "com.redhat.ceylon.ceylondoc.test.modules.dependency.c");

        List<String> modules = new ArrayList<String>();
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.dependency.c");
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.externallinks");

        CeylonDocTool tool = tool(Arrays.asList(new File("test/ceylondoc")), Collections.<File>emptyList(), modules, true, true, "build/ceylon-cars");
        tool.setLinks(Arrays.asList(linkArgs));
        tool.run();

        Module module = new Module();
        module.setName(Arrays.asList("com.redhat.ceylon.ceylondoc.test.modules.externallinks"));
        module.setVersion("1.0");        

        File destDir = getOutputDir(tool, module);
        assertExternalLinks(destDir, repoUrl);
        return destDir;
    }
    
    @Test
    public void moduleDependencies() throws Exception {
        String repoUrl = "http://acme.com/repo";
        File destDir = externalLinks(repoUrl, "com.redhat=" + repoUrl);
        assertModuleDependencies(destDir);
    }

    @Test
    public void dependentOnBinaryModule() throws Exception {
        String pathname = "test/ceylondoc";
        
        // compile the b module
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        
        CeylonDocTool tool = tool(pathname, "com.redhat.ceylon.ceylondoc.test.modules.dependency.c", true, "build/ceylon-cars");
        tool.run();
    }

    @Test
    public void classLoading() throws Exception {
        String pathname = "test/ceylondoc";
        
        // compile the a and b modules
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.a");
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.b");
        
        // now run docs on c, which uses b, which uses a
        CeylonDocTool tool = tool(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.c", true, "build/ceylon-cars");
        tool.run();
    }

    @Test
    public void containsJavaCode() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.mixed";
        
        // compile the java code first
        compileJavaModule(pathname, "com/redhat/ceylon/ceylondoc/test/modules/mixed/Java.java");
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.run();
    }

    @Test
    public void documentSingleModule() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.multi.a";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.run();

        Module a = makeModule("com.redhat.ceylon.ceylondoc.test.modules.multi.a", "1");
        File destDirA = getOutputDir(tool, a);
        Module b = makeModule("com.redhat.ceylon.ceylondoc.test.modules.multi.b", "1");
        File destDirB = getOutputDir(tool, b);
        Module def = makeDefaultModule();
        File destDirDef = getOutputDir(tool, def);
        
        assertFileExists(destDirA, "index.html");
        assertFileNotExists(destDirB, "index.html");
        assertFileNotExists(destDirDef, "index.html");
    }

    @Test
    public void documentPackage() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.multi.a.sub";
        
        try{
            CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
            tool.run();
        }catch(RuntimeException x){
            Assert.assertEquals("Can't find module: com.redhat.ceylon.ceylondoc.test.modules.multi.a.sub", x.getMessage());
            return;
        }
        Assert.fail("Expected exception");
    }

    @Test
    public void documentDefaultModule() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "default";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.setIncludeNonShared(true);
        tool.run();

        Module a = makeModule("com.redhat.ceylon.ceylondoc.test.modules.multi.a", "1");
        File destDirA = getOutputDir(tool, a);
        Module b = makeModule("com.redhat.ceylon.ceylondoc.test.modules.multi.b", "1");
        File destDirB = getOutputDir(tool, b);
        Module def = makeDefaultModule();
        File destDirDef = getOutputDir(tool, def);
        
        assertFileNotExists(destDirA, "index.html");
        assertFileNotExists(destDirB, "index.html");
        assertFileExists(destDirDef, "index.html");
        assertFileExists(destDirDef, "com/redhat/ceylon/ceylondoc/test/modules/multi/goes/into/bar.object.html");
        assertFileExists(destDirDef, "com/redhat/ceylon/ceylondoc/test/modules/multi/goes/into/defaultmodule/foo.object.html");
        
        assertFileExists(destDirDef, "../default.doc.zip");
        assertFileExists(destDirDef, "../default.doc.zip.sha1");
    }

    @Test
    public void ceylonLanguage() throws Exception {
        String pathname = "../ceylon.language/src";
        String moduleName = AbstractModelLoader.CEYLON_LANGUAGE;
        CeylonDocTool tool = tool(pathname, moduleName, true);
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.run();
        
        Module module = makeModule(AbstractModelLoader.CEYLON_LANGUAGE, LANGUAGE_MODULE_VERSION);
        File destDir = getOutputDir(tool, module);
        
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "Nothing.type.html");
        assertFileExists(destDir, "../ceylon.language-"+ LANGUAGE_MODULE_VERSION +".doc.zip");
        assertFileExists(destDir, "../ceylon.language-"+ LANGUAGE_MODULE_VERSION +".doc.zip.sha1");
    }

    @Test
    public void ceylonSdk() throws Exception {
        File sdkDir = new File("../ceylon-sdk");
        if (!sdkDir.exists()
                || !sdkDir.isDirectory()) {
            Assert.fail("You don't have ceylon-sdk checked out at " + sdkDir.getAbsolutePath() + " so this test doesn't apply");
        }
        String[] fullModuleNames = {
                "ceylon.collection", 
                "ceylon.dbc",
                "ceylon.file",
                "ceylon.html",
                "ceylon.interop.java", 
                "ceylon.io",
                "ceylon.json", 
                "ceylon.math",
                "ceylon.net",
                "ceylon.process",
                "ceylon.test",
                "ceylon.time",
                "ceylon.unicode"
        };
        
        compileSdkJavaFiles();

        CeylonDocTool tool = tool(Arrays.asList(new File("../ceylon-sdk/source")),
                Collections.<File>emptyList(),
                Arrays.asList(fullModuleNames), true, false);
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.run();
        Map<String,String> nameToVersion = new HashMap<String,String>();
        for(Module module : tool.getDocumentedModules()){
            nameToVersion.put(module.getNameAsString(), module.getVersion());
        }
        
        for(String moduleName : fullModuleNames){
            Module module = makeModule(moduleName, nameToVersion.get(moduleName));
            File destDir = getOutputDir(tool, module);

            assertFileExists(destDir, "index.html");
        }
    }

    /**
     * This is disgusting, but the current CeylonDoc doesn't handle source files, so we need to compile them first,
     * and we do it using javac to avoid compiling the whole SDK for one java file.
     */
    private void compileSdkJavaFiles() throws FileNotFoundException, IOException {
        // put it all in a special folder
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        if (dir.exists()) {
            Util.delete(dir);
        }
        dir.mkdirs();

        // download a required jar
        RepositoryManager repoManager = CeylonUtils.repoManager().buildManager();
        File undertowCoreModule = repoManager.getArtifact(new ArtifactContext("io.undertow.core", "1.0.0.Beta20", ".jar"));
        File languageModule = repoManager.getArtifact(new ArtifactContext(AbstractModelLoader.CEYLON_LANGUAGE, TypeChecker.LANGUAGE_MODULE_VERSION, ".car"));

        // fire up the java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("Missing Java compiler, this test is probably being run with a JRE instead of a JDK!", compiler);
        List<String> options = Arrays.asList("-sourcepath", "../ceylon-sdk/source", "-d", dir.getAbsolutePath(), 
                "-classpath", undertowCoreModule.getAbsolutePath()+File.pathSeparator+languageModule.getAbsolutePath());
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        String[] fileNames = new String[]{
                "ceylon/net/http/server/internal/JavaHelper.java",
                "ceylon/interop/java/javaBooleanArray_.java",
                "ceylon/interop/java/javaByteArray_.java",
                "ceylon/interop/java/javaShortArray_.java",
                "ceylon/interop/java/javaIntArray_.java",
                "ceylon/interop/java/javaLongArray_.java",
                "ceylon/interop/java/javaFloatArray_.java",
                "ceylon/interop/java/javaDoubleArray_.java",
                "ceylon/interop/java/javaStringArray_.java",
                "ceylon/interop/java/javaObjectArray_.java",
                "ceylon/interop/java/javaString_.java",
        };
        List<String> qualifiedNames = new ArrayList<String>(fileNames.length);
        for(String name : fileNames){
            qualifiedNames.add("../ceylon-sdk/source/" + name);
        }
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromStrings(qualifiedNames);
        CompilationTask task = compiler.getTask(null, null, null, options, null, fileObjects);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
        
        // now we need to zip it up
        makeCarFromClassFiles(dir, fileNames, "ceylon.net", "1.1.0");
        makeCarFromClassFiles(dir, fileNames, "ceylon.interop.java", "1.1.0");
    }

    private void makeCarFromClassFiles(File dir, String[] fileNames, String module, String version) throws IOException {
        String modulePath = module.replace('.', '/') + "/";
        File jarFolder = new File(dir, modulePath+version);
        jarFolder.mkdirs();
        File jarFile = new File(jarFolder, module+"-"+version+".car");
        // now jar it up
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        for(String name : fileNames){
            if(!name.startsWith(modulePath))
                continue;
            String classFile = name.substring(0, name.length()-5) + ".class";
            ZipEntry entry = new ZipEntry(classFile);
            outputStream.putNextEntry(entry);

            File javaFile = new File(dir, classFile);
            FileInputStream inputStream = new FileInputStream(javaFile);
            com.redhat.ceylon.compiler.java.util.Util.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.flush();
        }
        outputStream.close();
    }

    private Module makeDefaultModule() {
        Module module = new Module();
        module.setName(Arrays.asList(Module.DEFAULT_MODULE_NAME));
        module.setDefault(true);
        return module;
    }

    private Module makeModule(String name, String version) {
        Module module = new Module();
        module.setName(Arrays.asList(name.split("\\.")));
        module.setVersion(version);
        return module;
    }

    @Test
    public void bug1622() throws Exception{
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.bug1622";
        
        compile(pathname, dir.getPath(), moduleName);

        CeylonDocTool tool = 
                tool(Arrays.asList(new File(pathname)),
                        Arrays.asList(new File("doc")),
                        Arrays.asList(moduleName),
                        true, false);
        tool.setIncludeNonShared(true);
        tool.run();
    }
    
    private void assertFileExists(File destDir, boolean includeNonShared) {
        assertFileExists(destDir, "../com.redhat.ceylon.ceylondoc.test.modules.single-3.1.4.doc.zip");
        assertFileExists(destDir, "../com.redhat.ceylon.ceylondoc.test.modules.single-3.1.4.doc.zip.sha1");
        
        assertDirectoryExists(destDir, ".resources");
        assertFileExists(destDir, ".resources/index.js");
        assertFileExists(destDir, ".resources/ceylondoc.css");
        assertFileExists(destDir, ".resources/ceylondoc.js");
        assertFileExists(destDir, ".resources/bootstrap.min.css");
        assertFileExists(destDir, ".resources/bootstrap.min.js");
        assertFileExists(destDir, ".resources/jquery-1.8.2.min.js");
        assertFileExists(destDir, ".resources/shCore.css");
        assertFileExists(destDir, ".resources/shThemeDefault.css");
        assertFileExists(destDir, ".resources/shCore.js");
        assertFileExists(destDir, ".resources/shBrushCeylon.js");
        assertFileExists(destDir, ".resources/ceylondoc-logo.png");
        assertFileExists(destDir, ".resources/ceylondoc-icons.png");
        assertFileExists(destDir, ".resources/favicon.ico");
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "search.html");
        assertFileExists(destDir, "Types.type.html");
        assertFileExists(destDir, "SharedClass.type.html");
        assertFileExists(destDir, "CaseSensitive.type.html");
        assertFileExists(destDir, "caseSensitive.object.html");
        
        if( includeNonShared ) {
            assertFileExists(destDir, "PrivateClass.type.html");
            assertFileExists(destDir, "privatepackage/index.html");
        }
        else {
            assertFileNotExists(destDir, "PrivateClass.type.html");
            assertFileNotExists(destDir, "privatepackage");
        }
    }

    private void assertBasicContent(File destDir, boolean includeNonShared) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> module"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> package"));
        
        assertMatchInFile(destDir, "SharedClass.type.html", 
                Pattern.compile("<.*? id='sharedAttribute'.*?>"));
        assertMatchInFile(destDir, "SharedClass.type.html", 
                Pattern.compile("<.*? id='sharedGetter'.*?>"));
        assertMatchInFile(destDir, "SharedClass.type.html", 
                Pattern.compile("<.*? id='sharedMethod'.*?>"));
        
        if( includeNonShared ) {
            assertMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateAttribute'.*?>"));
            assertMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateMethod'.*?>"));
            assertMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateGetter'.*?>"));
        }
        else {
            assertNoMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateAttribute'.*?>"));
            assertNoMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateMethod'.*?>"));
            assertNoMatchInFile(destDir, "SharedClass.type.html", 
                    Pattern.compile("<.*? id='privateGetter'.*?>"));
        }
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<a class='link-one-self' title='Link to this declaration' href='index.html#StubClass'><i class='icon-link'></i></a>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<a class='link-source-code' href='StubClass.ceylon.html'><i class='icon-source-code'></i>Source Code</a>"));
    }
    
    private void assertZipContent(File destDir) throws Exception {
        Set<String> zipContent = new HashSet<String>();
        ZipFile zipFile = new ZipFile(new File(destDir, "../com.redhat.ceylon.ceylondoc.test.modules.single-3.1.4.doc.zip"));
        try {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                zipContent.add(zipEntry.getName());
            }
        } finally {
            zipFile.close();
        }
        
        assertTrue(zipContent.contains("api/index.html"));
        assertTrue(zipContent.contains("api/search.html"));
        assertTrue(zipContent.contains("api/StubClass.type.html"));
        assertTrue(zipContent.contains("api/.resources/ceylondoc.css"));
        assertTrue(zipContent.contains("api/.resources/ceylondoc-icons.png"));
        assertTrue(zipContent.contains("doc/Docs.md"));
        assertTrue(zipContent.contains("doc/page.html"));
    }

    private void assertBy(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span class='title'>By: </span><span class='value'>Tom Bentley</span>"));
        assertMatchInFile(destDir, "Types.type.html", 
                Pattern.compile("<span class='title'>By: </span><span class='value'>Tom Bentley</span>"));
    }
    
    private void assertLicense(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span class='title'>License: </span><span class='value'>http://www.gnu.org/licenses/gpl.html</span>"));
    }

    private void assertParametersDocumentation(File destDir) throws Exception {
    	assertMatchInFile(destDir, "index.html", 
    			Pattern.compile("<div class='parameters section'><span class='title'>Parameters: </span><ul><li><span class='parameter' id='stubTopLevelMethod-numbers'>numbers</span><div class='doc section'><p>Sequenced parameters <code>numbers</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='parameters section'><span class='title'>Parameters:"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter' id='StubClass-a'>a</span><div class='doc section'><p>Initializer parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter' id='StubClass-b'>b</span><div class='doc section'><p>Initializer parameter <code>b</code></p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter' id='methodWithParametersDocumentation-a'>a</span><div class='doc section'><p>Method parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter' id='methodWithParametersDocumentation-b'>b</span><div class='doc section'><p>Method parameter <code>b</code></p>"));
	}
    
    private void assertParametersAssertions(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<li><span class='parameter' id='stubFunctionWithAssertion2-n'>n</span><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists n</code></li><li><i class='icon-assertion'></i><code>n \\&gt;= 0 \\&amp;\\&amp; n \\&lt;=255</code></li>" +
                		"<li><i class='icon-assertion'></i><code>s.size \\&lt; n</code></li>" +
                		"</ul>" +
                		"</div></li><li><span class='parameter' id='stubFunctionWithAssertion2-s'>s</span><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists s</code></li><li><i class='icon-assertion'></i><code>s.size != 0</code></li>" +
                		"<li><i class='icon-assertion'></i><code>s.size \\&lt; n</code></li>" +
                		"</ul></div></li></ul>"));
        
        assertMatchInFile(destDir, "StubClassWithAssertions.type.html",
                Pattern.compile("<li><span class='parameter' id='StubClassWithAssertions-n'>n</span><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists n</code></li>" +
                		"<li><i class='icon-assertion'></i><code>0 \\&lt; n \\&lt; 123k</code></li>" +
                		"</ul>"));
        
        assertMatchInFile(destDir, "StubClassWithAssertions.type.html",
                Pattern.compile("<span class='parameter' id='StubClassWithAssertions-s'>s</span><div class='assertions' title='Parameter assertions'>" +
                        "<ul>" +
                        "<li><i class='icon-assertion'></i><code>exists s</code></li>" +
                        "<li><i class='icon-assertion'></i><code>s.any\\(\\(Character c\\) =\\&gt; c.digit\\)</code></li></ul>"));
    }

	private void assertThrows(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='throws section'><span class='title'>Throws: </span><ul><li>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span title='ceylon.language::OverflowException'>OverflowException</span><p>if the number is too large to be represented as an integer</p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubException.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubException'>StubException</a><p><code>when</code> with <strong>WIKI</strong> syntax</p>"));
    }

    private void assertSee(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>, <a class='link' href='index.html#stubTopLevelMethod' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'>stubTopLevelMethod</a></span></div>"));
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>, <a class='link' href='index.html#stubTopLevelAttribute' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'>stubTopLevelAttribute</a></span></div>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a>, <a class='link' href='index.html#stubTopLevelAttribute' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'>stubTopLevelAttribute</a>, <a class='link' href='index.html#stubTopLevelMethod' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'>stubTopLevelMethod</a>"));
        // FIXME: enable back when we can have metamodel refs to object members
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html#methodWithSee' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.methodWithSee'>methodWithSee</a>"/*, <a class='link' href='stubObject.object.html#foo' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubObject.foo'>stubObject.foo</a>"*/));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html#attributeWithSee' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.attributeWithSee'>attributeWithSee</a>, <a class='link' href='StubException.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubException'>StubException</a>, <a class='link' href='a/A1.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single.a::A1'>A1</a>, <a class='link' href='a/A2.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single.a::A2'>com.redhat.ceylon.ceylondoc.test.modules.single.a::A2</a>"));
    }
    
    private void assertIcons(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<i class='icon-interface'></i><span class='sub-navbar-name'>StubInterface</span>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-formal'></i></i>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'></i></i>"));

        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-interface'></i><a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='StubInnerClass' nowrap><i class='icon-class'></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-class'></i><span class='sub-navbar-name'>StubClass</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-impl'></i></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'><i class='icon-decoration-over'></i></i></i>"));
        
        assertMatchInFile(destDir, "index.html", Pattern.compile("<td id='stubTopLevelAttribute' nowrap><i class='icon-shared-member'><i class='icon-decoration-variable'></i></i>stubTopLevelAttribute</td>"));
    }
    
    private void assertInnerTypesDoc(File destDir) throws Exception {
        assertFileExists(destDir, "StubClass.StubInnerInterface.type.html");
        assertFileExists(destDir, "StubClass.StubInnerClass.type.html");
        assertFileExists(destDir, "StubClass.StubInnerException.type.html");
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Interfaces"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerInterface'>StubInnerInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Classes"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass'>StubInnerClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Exceptions"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerException.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerException'>StubInnerException</a>"));
        
        assertMatchInFile(destDir, "StubClass.StubInnerInterface.type.html", 
                Pattern.compile("<div class='enclosingType section'><span class='title'>Enclosing class: </span><i class='icon-class'></i><a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html", 
                Pattern.compile("<div class='enclosingType section'><span class='title'>Enclosing class: </span><i class='icon-class'></i><a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html", 
                Pattern.compile("<div class='satisfied section'><span class='title'>Satisfied Interfaces: </span><a class='link' href='StubClass.StubInnerInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerInterface'>StubInnerInterface</a>"));                
    }
    
    private void assertDeprecated(File destDir) throws Exception {
        assertFileExists(destDir, "DeprecatedClass.type.html");
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<td id='DeprecatedClass' nowrap><i class='icon-decoration-deprecated'><i class='icon-class'></i></i><a class='link-discreet' href='DeprecatedClass.type.html'>DeprecatedClass</a></td>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <a class='link' href='DeprecatedClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::DeprecatedClass'>DeprecatedClass</a></div><div class='description'><div class='deprecated section'><p><span class='title'>Deprecated: </span>This is <code>DeprecatedClass</code></p>"));
        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this attribute!"));
        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this method"));
    }
    
    private void assertTagged(File destDir) throws Exception {
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("var tagIndex = \\[\\n'stubInnerMethodTag1',"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass', 'type': 'class', 'url': 'StubClass.type.html', 'doc': '<p>This is <code>StubClass</code></p>\\\\n', 'tags': \\['stubTag1', 'stubTag2'\\]"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass.attributeWithTagged', 'type': 'value', 'url': 'StubClass.type.html#attributeWithTagged', 'doc': '<p>The stub attribute with <code>tagged</code>.</p>\\\\n', 'tags': \\['stubTag1'\\]"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass.methodWithTagged', 'type': 'function', 'url': 'StubClass.type.html#methodWithTagged', 'doc': '<p>The stub method with <code>tagged</code> .*?</p>\\\\n', 'tags': \\['stubTag2'\\]"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='search.html\\?q=stubTag1'>stubTag1</a><a class='tag label' name='stubTag2' href='search.html\\?q=stubTag2'>stubTag2</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='search.html\\?q=stubTag1'>stubTag1</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag2' href='search.html\\?q=stubTag2'>stubTag2</a>"));
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1a' href='search.html\\?q=stubTag1a'>stubTag1a</a><a class='tag label' name='stubTag1b' href='search.html\\?q=stubTag1b'>stubTag1b</a><a class='tag label' name='stubTagWithVeryLongName ... !!!' href='search.html\\?q=stubTagWithVeryLongName ... !!!'>stubTagWithVeryLongName ... !!!</a>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='search.html\\?q=stubTag1'>stubTag1</a><a class='tag label' name='stubTag2' href='search.html\\?q=stubTag2'>stubTag2</a>"));
    }
    
    private void assertDocumentationOfRefinedMember(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Description of StubInterface.formalMethodFromStubInterface"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Description of StubInterface.defaultDeprecatedMethodFromStubInterface"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Deprecated in StubInterface.defaultDeprecatedMethodFromStubInterface"));
    }
    
	private void assertSequencedParameter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='void'>void</span> methodWithSequencedParameter\\(<span title='ceylon.language::Integer'>Integer</span>\\[\\] numbers\\)"));
	}
    
    private void assertCallableParameter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithCallableParameter1\\(<span class='void'>void</span> onClick\\(\\)\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                  Pattern.compile("methodWithCallableParameter2<span class='type-parameter'>&lt;Element&gt;</span>\\(<span title='ceylon.language::Boolean'>Boolean</span> selecting\\(<span class='type-parameter'>Element</span> element\\)\\)"));

        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithCallableParameter3\\(<span class='void'>void</span> fce1\\(<span class='void'>void</span> fce2\\(<span class='void'>void</span> fce3\\(\\)\\)\\)\\)"));
    }
    
    private void assertTupleParameter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithTouple1\\(\\[<span title='ceylon.language::Integer'>Integer</span>, <span title='ceylon.language::Float'>Float</span>\\] t\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithTouple2<span class='type-parameter'>&lt;T&gt;</span>\\(\\[<span title='ceylon.language::String'>String</span>|<span class='type-parameter'>T</span>, <span title='ceylon.language::Integer'>Integer=</span>, <span title='ceylon.language::Float'>Float</span>\\*\\] t\\)"));
    }
    
    private void assertDefaultedParametres(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithDefaultedParameter1\\(" +
                                "<span title='ceylon.language::Boolean'>Boolean</span> b1<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>true</span>, " +
                                "<span title='ceylon.language::Boolean'>Boolean</span> b2<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>false</span>, " +
                                "<span title='ceylon.language::Integer'>Integer</span> i1<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>0</span>, " +
                                "<span title='ceylon.language::Integer'>Integer</span> i2<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>1</span>, " +
                                "<span title='ceylon.language::Integer'>Integer</span> i3<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>-1</span>, " +
                                "<span title='ceylon.language::Float'>Float</span> f1<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>0.0</span>, " +
                                "<span title='ceylon.language::Float'>Float</span> f2<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>1.0</span>, " +
                                "<span title='ceylon.language::Float'>Float</span> f3<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>-1.0</span>, " +
                                "<span title='ceylon.language::String'>String</span> s1<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>\"\"</span>, " +
                                "<span title='ceylon.language::String'>String</span>\\? s2<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>null</span>, " +
                                "<span title='ceylon.language::String'>String</span>\\[\\] s3<span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>\\[\\]</span>\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithDefaultedParameter2\\(" +
                        "<span title='ceylon.language::String'>String</span> a<span class='parameter-default-value'> = </span><a class='parameter-default-value' href='#methodWithDefaultedParameter2-a' title='Go to parameter default value'>...</a>, " +
                        "<span title='ceylon.language::String'>String</span> b<span class='parameter-default-value'> = </span><a class='parameter-default-value' href='#methodWithDefaultedParameter2-b' title='Go to parameter default value'>...</a>\\)"));

        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<li><span class='parameter' id='methodWithDefaultedParameter4-separator'>separator</span><span class='parameter-default-value' title='Parameter default value'> = \\(Character ch\\) =\\&gt; ch.whitespace</span></li>"));
    }
    
    private void assertAnythingReturnType(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span title='ceylon.language::Anything'>Anything</span> methodWithAnything\\(\\)"));
    }

    private void assertFencedCodeBlockWithSyntaxHighlighter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<link href='.resources/shCore.css' rel='stylesheet' type='text/css'/>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<link href='.resources/shThemeDefault.css' rel='stylesheet' type='text/css'/>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<script type='text/javascript' src='.resources/shCore.js'>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<script type='text/javascript' src='.resources/shBrushCeylon.js'>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<pre class=\"brush: ceylon\">shared default Boolean subset\\(Set set\\) \\{"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<pre class=\"brush: ceylon\">shared actual default Integer hash \\{"));
    }
    
    private void assertWikiStyleLinkSyntax(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass = <a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("class StubClass = <a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface = <a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("interface StubInterface = <a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInnerException = <a class='link' href='StubClass.StubInnerException.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerException'>StubInnerException</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelMethod = <a class='link' href='index.html#stubTopLevelMethod' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'>stubTopLevelMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("function stubTopLevelMethod = <a class='link' href='index.html#stubTopLevelMethod' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'>stubTopLevelMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelAttribute = <a class='link' href='index.html#stubTopLevelAttribute' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'>stubTopLevelAttribute</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("value stubTopLevelAttribute = <a class='link' href='index.html#stubTopLevelAttribute' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'>stubTopLevelAttribute</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'>StubInterface.formalMethodFromStubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass = <a class='link' href='StubClass.StubInnerClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass'>StubClass.StubInnerClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass.innerMethod = <a class='link' href='StubClass.StubInnerClass.type.html#innerMethod' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass.innerMethod'>StubClass.StubInnerClass.innerMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface with custom name = <a class='link-custom-text' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("unresolvable1 = <span class='link-unresolvable'>\\[unresolvable\\]</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("unresolvable2 = <span class='link-unresolvable'>\\[unresolvable with custom name|unresolvable\\]</span>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject = <a class='link' href='index.html#stubObject' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubObject'>stubObject</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.foo = <a class='link' href='stubObject.object.html#foo' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubObject.foo'>stubObject.foo</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject = <a class='link' href='stubObject.object.html#stubInnerObject' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubObject.stubInnerObject'>stubObject.stubInnerObject</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject.fooInner = <a class='link' href='stubObject.stubInnerObject.object.html#fooInner' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubObject.stubInnerObject.fooInner'>stubObject.stubInnerObject.fooInner</a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported A1 = <a class='link' href='a/A1.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single.a::A1'>A1</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported AliasA2 = <a class='link' href='a/A2.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single.a::A2'>com.redhat.ceylon.ceylondoc.test.modules.single.a::A2</a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClassWithGenericTypeParams = <a class='link' href='StubClassWithGenericTypeParams.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClassWithGenericTypeParams'>StubClassWithGenericTypeParams</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClassWithGenericTypeParams with custom name = <a class='link-custom-text' href='StubClassWithGenericTypeParams.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClassWithGenericTypeParams'>custom with type params</a>"));
                
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("module = <a class='link' href='index.html' title='Go to module'>com.redhat.ceylon.ceylondoc.test.modules.single</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("package = <a class='link' href='index.html#section-package' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single'>com.redhat.ceylon.ceylondoc.test.modules.single</a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface = <a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'>com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface with custom name = <a class='link-custom-text' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>full custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullUnresolvable1 = <span class='link-unresolvable'>\\[unresolvable::Bar\\]</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullUnresolvable2 = <span class='link-unresolvable'>\\[unresolvable.bar::Bar.foo\\]</span>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter s = <a class='link' href='StubClass.type.html#methodWithLinksInDoc-s' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.methodWithLinksInDoc.s'>s</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter methodWithParametersDocumentation.a = <a class='link' href='StubClass.type.html#methodWithParametersDocumentation-a' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.methodWithParametersDocumentation.a'>methodWithParametersDocumentation.a</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter stubTopLevelMethod.numbers = <a class='link' href='index.html#stubTopLevelMethod-numbers' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod.numbers'>stubTopLevelMethod.numbers</a>"));
    }
    
    private void assertConstants(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'>String</span> constAbc<span class='specifier'>= \"abcdef\"</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'>String</span> constLoremIpsumMultiLine<span class='specifier'>= \"Lorem ipsum dolor sit amet, consectetur adipisicing elit, </span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>                                          sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'>String</span>\\[\\] constAbcArray<span class='specifier'>= \\[</span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>    \"abc\","));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Character'>Character</span> constCharA<span class='specifier'>= 'A'</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Integer'>Integer</span> constNumTwo<span class='specifier'>= constNumZero \\+ 1 \\+ 1</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Float'>Float</span> constNumPI<span class='specifier'>= 3.14</span>"));
    }
    
    private void assertLinksToRefinedDeclaration(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.defaultDeprecatedMethodFromStubInterface'>StubInterface.defaultDeprecatedMethodFromStubInterface</a></div>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'>StubInterface.formalMethodFromStubInterface</a></div>"));
        assertMatchInFile(destDir, "a/StubClassExtended.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='../StubClass.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass.formalMethodFromStubInterface'>StubClass.formalMethodFromStubInterface</a><span class='title'> ultimately refines </span><a class='link-custom-text' href='../StubInterface.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'>StubInterface.formalMethodFromStubInterface</a></div>"));
    }
    
    private void assertGenericTypeParams(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<span class='sub-navbar-name'>StubClassWithGenericTypeParams<span class='type-parameter'>&lt;<span class='type-parameter-keyword'>in </span>ContravariantType, T1, T2, T3, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter'> = </span><span class='type-parameter-value'>\\{<a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>\\*\\}</span>&gt;</span></span>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T1</span><span class='type-parameter-keyword'> satisfies </span><span title='ceylon.language::Obtainable'>Obtainable</span> &amp; <span title='ceylon.language::Destroyable'>Destroyable</span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T2</span><span class='type-parameter-keyword'> of </span><span title='ceylon.language::Obtainable'>Obtainable</span> | <span title='ceylon.language::String'>String</span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T3</span>\\(<span title='ceylon.language::String'>String</span> s\\)</div></div>"));
        
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> methodWithGenericTypeParams<span class='type-parameter'>&lt;<span class='type-parameter-keyword'>in </span>ContravariantType, X1, X2, X3, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter'> = </span><span class='type-parameter-value'>\\{<a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>\\*\\}</span>&gt;</span>\\(\\)"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>X1</span><span class='type-parameter-keyword'> satisfies </span><span title='ceylon.language::Obtainable'>Obtainable</span> &amp; <span title='ceylon.language::Destroyable'>Destroyable</span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>X2</span><span class='type-parameter-keyword'> of </span><span title='ceylon.language::Obtainable'>Obtainable</span> | <span title='ceylon.language::String'>String</span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T3</span>\\(<span title='ceylon.language::String'>String</span> s\\)</div>"));
    }
    
    private void assertObjectPageDifferences(File destDir) throws Exception {
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<title>Object stubObject</title>"));
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<span class='sub-navbar-label'>object</span><i class='icon-object'></i><span class='sub-navbar-name'>stubObject</span>"));
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<a href='index.html#stubObject'><span title='Jump to singleton object declaration'>Singleton object declaration</span></a>"));
        assertNoMatchInFile(destDir, "stubObject.object.html", 
                Pattern.compile("<table id='section-initializer'"));
        assertMatchInFile(destDir, "stubObject.stubInnerObject.object.html",
                Pattern.compile("<a href='stubObject.object.html#stubInnerObject'><span title='Jump to singleton object declaration'>Singleton object declaration</span></a>"));
    }
    
    private void assertExternalLinks(File destDir, String repoUrl) throws Exception {
        String linkStart = "<a class='link' href='" + repoUrl + "/com/redhat/ceylon/ceylondoc/test/modules/dependency";
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/b/1.0/module-doc/B.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B'>B</a> fceB")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/c/1.0/module-doc/C.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.c::C'>C</a> fceC")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("b = " + linkStart + "/b/1.0/module-doc/index.html#b' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::b'>b</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B = " + linkStart + "/b/1.0/module-doc/B.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B'>B</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B.b = " + linkStart + "/b/1.0/module-doc/B.type.html#b' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B.b'>B.b</a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("b2 = " + linkStart + "/b/1.0/module-doc/bb/index.html#b2' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::b2'>b2</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B2 = " + linkStart + "/b/1.0/module-doc/bb/B2.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2'>B2</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B2.b2 = " + linkStart + "/b/1.0/module-doc/bb/B2.type.html#b2' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2'>B2.b2</a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b::b = " + linkStart + "/b/1.0/module-doc/index.html#b' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::b'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b::b</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B = " + linkStart + "/b/1.0/module-doc/B.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B.b = " + linkStart + "/b/1.0/module-doc/B.type.html#b' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B.b'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b::B.b</a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::b2 = " + linkStart + "/b/1.0/module-doc/bb/index.html#b2' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::b2'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::b2</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2 = " + linkStart + "/b/1.0/module-doc/bb/B2.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2</a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2 = " + linkStart + "/b/1.0/module-doc/bb/B2.type.html#b2' title='Go to com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2</a>")));
    }
    
    private void assertSharedParameterOfClass(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile(Pattern.quote("<td id='printHello' nowrap><i class='icon-shared-member'></i>printHello</td>")));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> printHello(<span title='ceylon.language::String'>String</span> name)</div>")));
    }
    
    private void assertAliases(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("Aliases")));        
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<td id='AliasEntry' nowrap><i class='icon-type-alias'></i>AliasEntry</td>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> AliasEntry<span class='type-parameter'>&lt;T&gt;</span><div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T</span><span class='type-parameter-keyword'> satisfies </span><span title='ceylon.language::Object'>Object</span></div><div class='type-alias-specifier'><span class='specifier'>=> </span><span class='type-parameter'>T</span>-&gt;<span class='type-parameter'>T</span></div>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<td id='AliasStubs' nowrap><i class='icon-decoration-deprecated'><i class='icon-type-alias'></i></i>AliasStubs</td>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> AliasStubs<span class='specifier-operator'> => </span><a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a>") + "|" + Pattern.quote("<a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>") + "|" + Pattern.quote("<a class='link' href='StubException.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubException'>StubException</a>") + "|" + Pattern.quote("<span title='ceylon.language::Null'>Null</span></div>"))); 
        
        assertMatchInFile(destDir, "StubClassWithAlias.type.html",
                Pattern.compile(Pattern.quote("Nested Aliases")));
        assertMatchInFile(destDir, "StubClassWithAlias.type.html",
                Pattern.compile(Pattern.quote("<td id='InnerAliasNumber' nowrap><i class='icon-type-alias'></i>InnerAliasNumber</td><td>")));
    }
    
    private void assertPackageNavigation(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>package</span><i class='icon-package'></i><span class='sub-navbar-name'><a class='link' href='../index.html#section-package' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single'>com.redhat.ceylon.ceylondoc.test.modules.single</a>.<a class='link-custom-text' href='../a/index.html' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single.a'>a</a></span>")));
        assertMatchInFile(destDir, "a/aa/index.html",
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>package</span><i class='icon-package'></i><span class='sub-navbar-name'><a class='link' href='../../index.html#section-package' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single'>com.redhat.ceylon.ceylondoc.test.modules.single</a>.<a class='link-custom-text' href='../../a/index.html' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single.a'>a</a>.<a class='link-custom-text' href='../../a/aa/index.html' title='Go to package com.redhat.ceylon.ceylondoc.test.modules.single.a.aa'>aa</a></span>")));
    }
    
    private void assertSubpackages(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<tr class='table-header' title='Click for expand/collapse'><td colspan='2'><i class='icon-expand'></i>Subpackages</td></tr>")));
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<tr><td><i class='icon-package'></i><a class='link' href='../a/aa/index.html'>com.redhat.ceylon.ceylondoc.test.modules.single.a.aa</a></td><td></td></tr>")));
    }
    
    private void assertAnnotations(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<table id='section-annotations'")));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<td id='stubAnnotationBar' nowrap><i class='icon-shared-member'><i class='icon-decoration-annotation'></i></i>stubAnnotationBar</td>")));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<td id='StubAnnotationBar' nowrap><i class='icon-class'><i class='icon-decoration-annotation'></i></i><a class='link-discreet' href='StubAnnotationBar.type.html'>StubAnnotationBar</a></td>")));
        
        assertMatchInFile(destDir, "StubAnnotationBar.type.html", 
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>annotation</span>")));
        assertMatchInFile(destDir, "StubAnnotationBar.type.html", 
                Pattern.compile(Pattern.quote("<div class='annotationConstructors section'><span class='title'>Annotation Constructors: </span><a class='link' href='index.html#stubAnnotationBar' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'>stubAnnotationBar</a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<div class='doc section'><p>The stub annotated function.</p>\n</div>" +
                		        "<div class='annotations section'><span class='title'>Annotations: </span><ul>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationFoo' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationFoo'>stubAnnotationFoo</a>" +
                		"(" +
                		"<span class='literal'>&quot;abc&quot;</span>, " +
                		"<span class='literal'>'a'</span>, " +
                		"<span class='literal'>123</span>, " +
                		"<span class='literal'>987.654</span>, " +
                		"true, " +
                		"`<span class='keyword'>class </span><a class='link' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>`, " +
                		"`<span class='keyword'>package </span>ceylon.language.meta`, " +
                		"[], " +
                		"[<span class='literal'>0</span>, <span class='literal'>1</span>], " +
                		"{}, " +
                		"{<span class='literal'>0.0</span>, <span class='literal'>1.1</span>}" +
                		")</li>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationBar' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'>stubAnnotationBar</a>" +
                		"(<a class='link' href='index.html#stubAnnotationBaz' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationBaz'>stubAnnotationBaz</a>(<span class='literal'>&quot;baz&quot;</span>))</li>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationBar' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'>stubAnnotationBar</a>" +
                		"{baz=<a class='link' href='index.html#stubAnnotationBaz' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::stubAnnotationBaz'>stubAnnotationBaz</a>{s=<span class='literal'>&quot;baz&quot;</span>;};}</li>")));
    }
    
    private void assertAbstractClassModifier(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td id='StubAbstractClass' nowrap><i class='icon-class'><i class='icon-decoration-abstract'></i></i>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<span class='modifiers'>shared abstract</span> <a class='link' href='StubAbstractClass.type.html'")));         
    }
    
    private void assertFinalClassModifier(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td id='StubFinalClass' nowrap><i class='icon-class'><i class='icon-decoration-final'></i></i>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<span class='modifiers'>shared final</span> <a class='link' href='StubFinalClass.type.html'")));         
    }
    
    private void assertHeaderAndFooter(File destDir) throws Exception {
        Pattern headerPattern = Pattern.compile(Pattern.quote("<header><div class='navbar-inverse navbar-static-top'><div class='navbar-inner' style='color:white; font-style: italic; text-align: center'>documentation under construction</div></div></header>"));
        assertMatchInFile(destDir, "index.html", headerPattern);
        assertMatchInFile(destDir, "search.html", headerPattern);
        assertMatchInFile(destDir, "StubClass.type.html", headerPattern);
        
        Pattern footerPattern = Pattern.compile(Pattern.quote("<footer><p style='text-align: right;'>Copyright © 2010-2013, Red Hat, Inc. or third-party contributors</p></footer>"));
        assertMatchInFile(destDir, "index.html", footerPattern);
        assertMatchInFile(destDir, "search.html", footerPattern);
        assertMatchInFile(destDir, "StubClass.type.html", footerPattern);
    }
    
    private void assertModuleDependencies(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<table id='section-dependencies'")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td class='shrink'><span title='shared import of module com.redhat.ceylon.ceylondoc.test.modules.dependency.b 1.0'><i class='icon-module'><i class='icon-module-exported-decoration'></i></i></span><a class='link' href='http://acme.com/repo/com/redhat/ceylon/ceylondoc/test/modules/dependency/b/1.0/module-doc/index.html' title='Go to module'>com.redhat.ceylon.ceylondoc.test.modules.dependency.b</a></td><td class='shrink'>1.0</td><td><div class='description import-description'></div></td></tr>")));        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td class='shrink'><span title='import of module com.redhat.ceylon.ceylondoc.test.modules.dependency.c 1.0'><i class='icon-module'></i></span><a class='link' href='http://acme.com/repo/com/redhat/ceylon/ceylondoc/test/modules/dependency/c/1.0/module-doc/index.html' title='Go to module'>com.redhat.ceylon.ceylondoc.test.modules.dependency.c</a></td><td class='shrink'>1.0</td><td><div class='description import-description'></div></td></tr>")));        
    }

    private void assertBug659ShowInheritedMembers(File destDir) throws Exception {
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile(Pattern.quote("Inherited Methods")));
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile(Pattern.quote("<td>Methods inherited from: <i class='icon-interface'></i><a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a><div class='inherited-members'><a class='link' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.defaultDeprecatedMethodFromStubInterface'>defaultDeprecatedMethodFromStubInterface</a>, <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'>formalMethodFromStubInterface</a>")));
    }

    private void assertBug691AbbreviatedOptionalType(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> <span title='ceylon.language::String'>String</span>? bug691AbbreviatedOptionalType1()</div>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> <span class='type-parameter'>Element</span>? bug691AbbreviatedOptionalType2<span class='type-parameter'>&lt;Element&gt;</span>()</div>")));
    }
    
    private void assertBug839(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> \\{&lt;<span title='ceylon.language::Integer'>Integer</span>-&gt;<span class='type-parameter'>Element</span>&amp;<span title='ceylon.language::Object'>Object</span>&gt;\\*\\} bug839<span class='type-parameter'>&lt;Element&gt;</span>\\(\\)</div>"));
    }

    private void assertBug968(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> {<span title='ceylon.language::Integer'>Integer</span>*} bug968_1()</div>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<div class='signature'><span class='modifiers'>shared</span> {<span title='ceylon.language::Integer'>Integer</span>+} bug968_2()</div>")));
    }

    private void assertBug927LoadingAndSortingInheritedMembers(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("Inherited Attributes"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td>Attributes inherited from: <i class='icon-class'><i class='icon-decoration-abstract'></i></i><span title='ceylon.language::Object'>Object</span><div class='inherited-members'>hash, string</div></td>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td>Attributes inherited from: <i class='icon-interface'></i><a class='link' href='StubInterface.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface'>StubInterface</a><div class='inherited-members'><a class='link' href='StubInterface.type.html#string' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.string'>string</a></div></td>")));
    }
    
    private void assertBug1619BrokenLinkFromInheritedDoc(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/StubClassExtended.type.html", 
                Pattern.compile("StubClass = <a class='link' href='../StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>StubClass</a>"));
        assertMatchInFile(destDir, "a/StubClassExtended.type.html", 
                Pattern.compile("imported A1 = <a class='link' href='../a/A1.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single.a::A1'>A1</a>"));
    }
    
    private void assertBug1619BrokenLinkWithNewLine(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("bug1619BrokenLinkWithNewLine\\(\\)</div><div class='description'><div class='doc section'><p><a class='link-custom-text' href='StubClass.type.html' title='Go to com.redhat.ceylon.ceylondoc.test.modules.single::StubClass'>foo\nbar"));
    }
    
    private File getOutputDir(CeylonDocTool tool, Module module) {
        String outputRepo = tool.getOut();
        return new File(com.redhat.ceylon.compiler.java.util.Util.getModulePath(new File(outputRepo), module),
                "module-doc");
    }

        
    private void compile(String pathname, String moduleName) throws Exception {
        compile(pathname, "build/ceylon-cars", moduleName);
    }
    
    private void compile(String pathname, String destDir, String moduleName) throws Exception {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", destDir, "-cp", CompilerTest.getClassPathAsPath());
        JavacTask task = compiler.getTask(null, null, null, options, Arrays.asList(moduleName), null);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
    }

    private void compileJavaModule(String pathname, String... fileNames) throws Exception {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", "build/ceylon-cars", "-cp", CompilerTest.getClassPathAsPath());
        JavacFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        List<String> qualifiedNames = new ArrayList<String>(fileNames.length);
        for(String name : fileNames){
            qualifiedNames.add(pathname + File.separator + name);
        }
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromStrings(qualifiedNames);
        JavacTask task = compiler.getTask(null, null, null, options, null, fileObjects);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
    }
    
}