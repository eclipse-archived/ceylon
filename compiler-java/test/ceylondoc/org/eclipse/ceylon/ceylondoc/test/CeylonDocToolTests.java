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
package org.eclipse.ceylon.ceylondoc.test;

import static org.eclipse.ceylon.compiler.typechecker.TypeChecker.LANGUAGE_MODULE_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Ignore;

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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.eclipse.ceylon.ceylondoc.CeylonDocTool;
import org.eclipse.ceylon.ceylondoc.CeylondException;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.tools.CeyloncTool;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.javax.tools.JavaCompiler;
import org.eclipse.ceylon.javax.tools.JavaFileObject;
import org.eclipse.ceylon.javax.tools.StandardJavaFileManager;
import org.eclipse.ceylon.javax.tools.ToolProvider;
import org.eclipse.ceylon.javax.tools.JavaCompiler.CompilationTask;
import org.eclipse.ceylon.langtools.source.util.JavacTask;
import org.eclipse.ceylon.langtools.tools.javac.file.JavacFileManager;
import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class CeylonDocToolTests {

    @Rule 
    public TestName name = new TestName();
    
    private CeylonDocTool tool(List<File> sourceFolders, List<File> docFolders, List<String> moduleName, 
            boolean haltOnError, boolean deleteDestDir, boolean bootstrapCeylon, String... repositories)
            throws Exception {
        return tool(sourceFolders, repositories, null, docFolders, moduleName, haltOnError, deleteDestDir, bootstrapCeylon);
    }
    private CeylonDocTool tool(List<File> sourceFolders, String[] repositories, String overrides, List<File> docFolders, List<String> moduleName, 
            boolean haltOnError, boolean deleteDestDir, boolean bootstrapCeylon)
            throws Exception {
        
        CeylonDocTool tool = new CeylonDocTool();
        tool.setSystemRepository("../dist/dist/repo");
        tool.setSourceFolders(sourceFolders);
        tool.setRepositoryAsStrings(Arrays.asList(repositories));
        tool.setModuleSpecs(moduleName);
        tool.setDocFolders(docFolders);
        tool.setHaltOnError(haltOnError);
        tool.setBootstrapCeylon(bootstrapCeylon);
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        if (deleteDestDir && dir.exists()) {
            FileUtil.delete(dir);
        }
        tool.setOut(dir.getAbsolutePath());
        if (overrides != null) {
            tool.setOverrides(overrides);
        }
        tool.initialize(new CeylonTool());
        return tool;
    }

    private CeylonDocTool tool(String pathname, String moduleName, 
            boolean throwOnError, String... repositories)
            throws Exception {
        return tool(pathname, "doc", moduleName, throwOnError, false, repositories);
    }

    private CeylonDocTool tool(String pathname, String moduleName, 
            boolean throwOnError, boolean bootstrapCeylon, String... repositories)
            throws Exception {
        return tool(pathname, "doc", moduleName, throwOnError, bootstrapCeylon, repositories);
    }

    private CeylonDocTool tool(String pathname, String docPath, String moduleName, 
            boolean throwOnError, boolean bootstrapCeylon, String... repositories)
            throws Exception {
        return tool(Arrays.asList(new File(pathname)),
                Arrays.asList(new File(docPath)),
                Arrays.asList(moduleName),
                throwOnError, true, bootstrapCeylon, repositories);
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
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void moduleA() throws Exception {
        moduleA(false);
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void moduleAWithPrivate() throws Exception {
        moduleA(true);
    }

    private void moduleA(boolean includeNonShared) throws Exception {
        String pathname = "test/ceylondoc";
        String docname = "test/ceylondoc-doc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.single";

        CeylonDocTool tool = tool(pathname, docname, moduleName, true, false);
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
        
        File destDir = getOutputDir(tool, moduleName, "3.1.4");
        
        assertFileExists(destDir, includeNonShared);
        assertResourcesExists(destDir, ".resources");
        assertBasicContent(destDir, includeNonShared);
        assertConstructors(destDir, includeNonShared);
        assertBy(destDir);
        assertLicense(destDir);
        assertParametersDocumentation(destDir);
        assertParametersAssertions(destDir);
        assertParametersLinks(destDir);
        assertThrows(destDir);
        assertSee(destDir);
        assertSince(destDir);
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
        assertWikiStyleLinkSyntax(destDir, includeNonShared);
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
        assertExceptions(destDir);
        assertNameAliases(destDir);
        assertBug659ShowInheritedMembers(destDir);
        assertBug691AbbreviatedOptionalType(destDir);
        assertBug839(destDir);
        assertBug927LoadingAndSortingInheritedMembers(destDir);
        assertBug968(destDir);
        assertBug1619BrokenLinkFromInheritedDoc(destDir);
        assertBug1619BrokenLinkWithNewLine(destDir);
        assertBug2307AliasedName(destDir);
    }

    @Test
    public void externalLinksToLocalRepoUrlWithModuleNamePattern() throws Exception {
        String repoUrl = "file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        externalLinks(repoUrl, "org.eclipse=" + repoUrl);
    }
    
    @Test
    public void externalLinksToLocalRepoPathWithModuleNamePattern() throws Exception {
        String root = new File("").getAbsolutePath().replace('\\', '/');
        if (!root.startsWith("/")) {
            root = "/" + root;
        }
        String repoUrl = root + "/build/CeylonDocToolTest/" + name.getMethodName();
        // note that even though we pass a path, the links are created as URIs, which must include the file: scheme
        // but the // for authority is not required and will in fact not be generated, as per URI RFC
        externalLinks("file:"+repoUrl, "org.eclipse=" + repoUrl);
    }

    @Test
    public void externalLinksToRemoteRepoWithModuleNamePattern() throws Exception {
        String repoUrl = "http://acme.com/repo";
        externalLinks(repoUrl, "org.eclipse=" + repoUrl);
    }
    
    @Test
    public void externalLinksToLocalRepoUrlWithoutModuleNamePattern() throws Exception {
        String repoUrl = "file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        externalLinks(repoUrl, "file://not-existing-dir", "https://not-existing-url", repoUrl);
    }
    
    @Test
    public void externalLinksToRemoteRepoWithoutModuleNamePattern() throws Exception {
        Assume.assumeTrue(CompilerTests.allowNetworkTests());
        
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
        compile("test/ceylondoc", "org.eclipse.ceylon.ceylondoc.test.modules.dependency.b");
        compile("test/ceylondoc", "org.eclipse.ceylon.ceylondoc.test.modules.dependency.c");

        List<String> modules = new ArrayList<String>();
        modules.add("org.eclipse.ceylon.ceylondoc.test.modules.dependency.b");
        modules.add("org.eclipse.ceylon.ceylondoc.test.modules.dependency.c");
        modules.add("org.eclipse.ceylon.ceylondoc.test.modules.externallinks");

        CeylonDocTool tool = tool(Arrays.asList(new File("test/ceylondoc")), Collections.<File>emptyList(), modules, true, true, false, "build/ceylon-cars");
        tool.setLinks(Arrays.asList(linkArgs));
        tool.run();

        File destDir = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.externallinks", "1.0");
        assertExternalLinks(destDir, repoUrl);
        return destDir;
    }
    
    @Test
    public void moduleDependencies() throws Exception {
        String repoUrl = "http://acme.com/repo";
        File destDir = externalLinks(repoUrl, "org.eclipse=" + repoUrl);
        assertModuleDependencies(destDir);
    }

    @Test
    public void dependentOnBinaryModule() throws Exception {
        String pathname = "test/ceylondoc";
        
        // compile the b module
        compile(pathname, "org.eclipse.ceylon.ceylondoc.test.modules.dependency.b");
        
        CeylonDocTool tool = tool(pathname, "org.eclipse.ceylon.ceylondoc.test.modules.dependency.c", true, "build/ceylon-cars");
        tool.run();
    }

    @Test
    public void classLoading() throws Exception {
        String pathname = "test/ceylondoc";
        
        // compile the a and b modules
        compile(pathname, "org.eclipse.ceylon.ceylondoc.test.modules.classloading.a");
        compile(pathname, "org.eclipse.ceylon.ceylondoc.test.modules.classloading.b");
        
        // now run docs on c, which uses b, which uses a
        CeylonDocTool tool = tool(pathname, "org.eclipse.ceylon.ceylondoc.test.modules.classloading.c", true, "build/ceylon-cars");
        tool.run();
    }

    @Test
    @Ignore("Temporarily disabling for work on 1.3.4 (case sensitive fs issue?)")
    public void containsJavaCode() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.mixed";
        
        // compile the java code first
        compileJavaModule(pathname, 
                "org/eclipse/ceylon/ceylondoc/test/modules/mixed/Java.java",
                "org/eclipse/ceylon/ceylondoc/test/modules/mixed/JavaWithCeylonAnnotations.java");
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.run();
        
        File destDir = getOutputDir(tool, moduleName, "1.0.0");
        
        assertOverloadedMethods(destDir);
    }

    private void assertOverloadedMethods(File destDir) throws Exception {
        // see https://github.com/ceylon/ceylon/issues/5748
        assertMatchInFile(destDir, "JavaWithCeylonAnnotations.type.html", 
                Pattern.compile(Pattern.quote("<td id='foo' nowrap><i class='icon-shared-member'></i><code class='decl-label'>foo</code></td>"
                        + "<td><a class='link-one-self' title='Link to this declaration' href='JavaWithCeylonAnnotations.type.html#foo'><i class='icon-link'></i></a>"
                        + "<code class='signature'><span class='modifiers'>shared default</span> <span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> <span class='identifier'>foo</span>()</code>")));
        
        assertMatchInFile(destDir, "JavaWithCeylonAnnotations.type.html", 
                Pattern.compile(Pattern.quote("<td id='foo_2' nowrap><i class='icon-shared-member'></i><code class='decl-label'>foo</code></td>"
                        + "<td><a class='link-one-self' title='Link to this declaration' href='JavaWithCeylonAnnotations.type.html#foo_2'><i class='icon-link'></i></a>"
                        + "<code class='signature'><span class='modifiers'>shared default</span> <span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> <span class='identifier'>foo</span>"
                        + "(<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='parameter'>p1</span>)</code>")));
        
        assertMatchInFile(destDir, "JavaWithCeylonAnnotations.type.html", 
                Pattern.compile(Pattern.quote("<td id='foo_3' nowrap><i class='icon-shared-member'></i><code class='decl-label'>foo</code></td>"
                        + "<td><a class='link-one-self' title='Link to this declaration' href='JavaWithCeylonAnnotations.type.html#foo_3'><i class='icon-link'></i></a>"
                        + "<code class='signature'><span class='modifiers'>shared default</span> <span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> <span class='identifier'>foo</span>"
                        + "(<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='parameter'>p1</span>, "
                        + "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='parameter'>p2</span>)</code>")));
    }
    
    @Test
    public void documentSingleModule() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.multi.a";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.run();

        File destDirA = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.multi.a", "1");
        File destDirB = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.multi.b", "1");
        File destDirDef = getOutputDir(tool, makeDefaultModule());
        
        assertFileExists(destDirA, "index.html");
        assertFileNotExists(destDirB, "index.html");
        assertFileNotExists(destDirDef, "index.html");
    }

    @Test
    public void documentPackage() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.multi.a.sub";
        
        try{
            CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
            tool.run();
        }catch(RuntimeException x){
            Assert.assertEquals("Can't find module: org.eclipse.ceylon.ceylondoc.test.modules.multi.a.sub", x.getMessage());
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

        File destDirA = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.multi.a", "1");
        File destDirB = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.multi.b", "1");
        File destDirDef = getOutputDir(tool, makeDefaultModule());
        
        assertFileNotExists(destDirA, "index.html");
        assertFileNotExists(destDirB, "index.html");
        assertFileExists(destDirDef, "index.html");
        assertFileExists(destDirDef, "org/eclipse/ceylon/ceylondoc/test/modules/multi/goes/into/bar.object.html");
        assertFileExists(destDirDef, "org/eclipse/ceylon/ceylondoc/test/modules/multi/goes/into/defaultmodule/foo.object.html");
    }

    @Test
    public void documentDynamics() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.dynamics";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.run();

        File destDir = getOutputDir(tool, "org.eclipse.ceylon.ceylondoc.test.modules.dynamics", "1.0.0");
        
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "DynaTest.type.html");
    }

    @Test
    public void ceylonLanguage() throws Exception {
        String pathname = "../language/src";
        String moduleName = AbstractModelLoader.CEYLON_LANGUAGE;
        CeylonDocTool tool = tool(pathname, moduleName, true, true);
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.run();
        
        File destDir = getOutputDir(tool, moduleName, LANGUAGE_MODULE_VERSION);
        
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "Nothing.type.html");
    }

    @Test
    public void ceylonLanguageMissingBootstrap() throws Exception {
        String moduleName = AbstractModelLoader.CEYLON_LANGUAGE;
        try{
            tool("source", moduleName, true, false);
            fail();
        }catch(CeylondException x){
            assertEquals("To generate the ceylon.language module documentation you must set the --bootstrap-ceylon option", x.getMessage());
        }
    }

    @Test
    public void ceylonLanguageMissingSource() throws Exception {
        String moduleName = AbstractModelLoader.CEYLON_LANGUAGE;
        try{
            tool("source", moduleName, true, true);
            fail();
        }catch(RuntimeException x){
            assertEquals("Failed to find the language module sources in the specified source paths", x.getMessage());
        }
    }

    @Test
    public void ceylonSdk() throws Exception {
        Assume.assumeTrue(CompilerTests.allowSdkTests());
        
        File sdkDir = new File("../../ceylon-sdk");
        if (!sdkDir.exists()
                || !sdkDir.isDirectory()) {
            Assert.fail("You don't have ceylon-sdk checked out at " + sdkDir.getAbsolutePath() + " so this test doesn't apply");
        }
        String[] fullModuleNames = {
                "ceylon.buffer",
                "ceylon.collection",
                "ceylon.dbc",
                "ceylon.decimal",
                "ceylon.file",
                "ceylon.html",
                "ceylon.http.client",
                "ceylon.http.common",
                "ceylon.http.server",
                "ceylon.interop.browser", 
                "ceylon.interop.java", 
                "ceylon.io",
                "ceylon.json",
                "ceylon.locale", 
                "ceylon.logging", 
                "ceylon.math",
                "ceylon.numeric",
                "ceylon.process",
                "ceylon.promise",
                "ceylon.random",
                "ceylon.regex",
                "ceylon.test",
                "ceylon.time",
                "ceylon.unicode",
                "ceylon.uri",
                "ceylon.whole",
                "org.eclipse.ceylon.war"
        };
        
        compileSdkJavaFiles();

        CeylonDocTool tool = tool(Arrays.asList(new File("../../ceylon-sdk/source")),
                new String[0],
                null,
                Collections.<File>emptyList(),
                Arrays.asList(fullModuleNames), true, false, false);
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.run();
        Map<String,String> nameToVersion = new HashMap<String,String>();
        for(Module module : tool.getDocumentedModules()){
            nameToVersion.put(module.getNameAsString(), module.getVersion());
        }
        
        for(String moduleName : fullModuleNames){
            File destDir = getOutputDir(tool, moduleName, nameToVersion.get(moduleName));

            assertFileExists(destDir, "index.html");
        }
    }
    
    @Test
    public void customResourcesFolder() throws Exception {
        String pathname = "test/ceylondoc";
        String docname = "test/ceylondoc-doc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.single";

        CeylonDocTool tool = tool(pathname, docname, moduleName, true, false);
        tool.setResourceFolder("assets");
        tool.run();
        
        File destDir = getOutputDir(tool, moduleName, "3.1.4");
        assertResourcesExists(destDir, "assets");
    }

    /**
     * This is disgusting, but the current CeylonDoc doesn't handle source files, so we need to compile them first,
     * and we do it using javac to avoid compiling the whole SDK for one java file.
     */
    private void compileSdkJavaFiles() throws FileNotFoundException, IOException {
        // put it all in a special folder
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        if (dir.exists()) {
            FileUtil.delete(dir);
        }
        dir.mkdirs();
        
        // download a required jar
        RepositoryManager repoManager = CeylonUtils.repoManager().systemRepo("../dist/dist/repo").buildManager();
        File jbossModulesModule = repoManager.getArtifact(new ArtifactContext(null, "org.jboss.modules", Versions.DEPENDENCY_JBOSS_MODULES_VERSION, ".jar"));
        File runtimeModule = repoManager.getArtifact(new ArtifactContext(null, "ceylon.runtime", Versions.CEYLON_VERSION_NUMBER, ".jar"));
        File modelModule = repoManager.getArtifact(new ArtifactContext(null, "org.eclipse.ceylon.model", Versions.CEYLON_VERSION_NUMBER, ".jar"));
        File undertowCoreModule = repoManager.getArtifact(new ArtifactContext(null, "io.undertow.core", "1.3.5.Final", ".jar"));
        File languageModule = repoManager.getArtifact(new ArtifactContext(null, AbstractModelLoader.CEYLON_LANGUAGE, TypeChecker.LANGUAGE_MODULE_VERSION, ".car"));

        // fire up the java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("Missing Java compiler, this test is probably being run with a JRE instead of a JDK!", compiler);
        List<String> options = Arrays.asList("-sourcepath", "../../ceylon-sdk/source", "-d", dir.getAbsolutePath(), 
                "-classpath", undertowCoreModule.getAbsolutePath()+File.pathSeparator
                +jbossModulesModule.getAbsolutePath()+File.pathSeparator
                +runtimeModule.getAbsolutePath()+File.pathSeparator
                +modelModule.getAbsolutePath()+File.pathSeparator
                +languageModule.getAbsolutePath());
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        String[] fileNames = new String[]{
                "ceylon/interop/java/internal/javaBooleanArray_.java",
                "ceylon/interop/java/internal/javaByteArray_.java",
                "ceylon/interop/java/internal/javaCharArray_.java",
                "ceylon/interop/java/internal/javaDoubleArray_.java",
                "ceylon/interop/java/internal/javaFloatArray_.java",
                "ceylon/interop/java/internal/javaIntArray_.java",
                "ceylon/interop/java/internal/javaLongArray_.java",
                "ceylon/interop/java/internal/javaObjectArray_.java",
                "ceylon/interop/java/internal/javaShortArray_.java",
                "ceylon/interop/java/internal/javaStringArray_.java",
                "ceylon/interop/java/internal/synchronize_.java",
        };
        List<String> qualifiedNames = new ArrayList<String>(fileNames.length);
        for(String name : fileNames){
            qualifiedNames.add("../../ceylon-sdk/source/" + name);
        }
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromStrings(qualifiedNames);
        CompilationTask task = compiler.getTask(null, null, null, options, null, fileObjects);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
        
        // now we need to zip it up
        makeCarFromClassFiles(dir, fileNames, "ceylon.test", Versions.CEYLON_VERSION_NUMBER);
        makeCarFromClassFiles(dir, fileNames, "ceylon.http.server", Versions.CEYLON_VERSION_NUMBER);
        makeCarFromClassFiles(dir, fileNames, "ceylon.interop.java", Versions.CEYLON_VERSION_NUMBER);
        makeCarFromClassFiles(dir, fileNames, "ceylon.transaction", Versions.CEYLON_VERSION_NUMBER);
    }

    private void makeCarFromClassFiles(File dir, String[] fileNames, String module, String version) throws IOException {
        String modulePath = module.replace('.', '/') + "/";
        final Path dirPath = dir.toPath();
        File classFolder = new File(dir, modulePath);
        File jarFolder = new File(dir, modulePath+version);
        jarFolder.mkdirs();
        File jarFile = new File(jarFolder, module+"-"+version+".car");
        // now jar it up
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        final List<String> files = new LinkedList<String>();
        final Path classPath = classFolder.toPath();
        // collect all class files
        Files.walkFileTree(classPath, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
                if(path.toString().endsWith(".class")){
                    files.add(dirPath.relativize(path).toString());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        for(String classFile : files){
            ZipEntry entry = new ZipEntry(classFile);
            outputStream.putNextEntry(entry);

            File javaFile = new File(dir, classFile);
            FileInputStream inputStream = new FileInputStream(javaFile);
            org.eclipse.ceylon.compiler.java.util.Util.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.flush();
        }
        outputStream.close();
    }

    private Module makeDefaultModule() {
        Module module = new Module();
        module.setName(Arrays.asList(Module.DEFAULT_MODULE_NAME));
//        module.setDefault(true);
        return module;
    }

    @Test
    public void bug1622() throws Exception{
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug1622";
        
        compile(pathname, dir.getPath(), moduleName);

        CeylonDocTool tool = 
                tool(Arrays.asList(new File(pathname)),
                        Arrays.asList(new File("doc")),
                        Arrays.asList(moduleName),
                        true, false, false);
        tool.setIncludeNonShared(true);
        tool.run();
    }

    @Test
    public void bug1975() throws Exception{
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug1975";
        
        compile(pathname, dir.getPath(), moduleName);

        CeylonDocTool tool = 
                tool(Arrays.asList(new File(pathname)),
                        Arrays.asList(new File("doc")),
                        Arrays.asList(moduleName),
                        true, false, false);
        tool.setIncludeNonShared(true);
        tool.run();
    }

    @Test
    public void bug2094() throws Exception{
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug2094";
        
        CeylonDocTool tool = 
                tool(Arrays.asList(new File(pathname)),
                        Arrays.asList(new File("doc")),
                        Arrays.asList(moduleName),
                        true, false, false);
        tool.run();
    }
    
    @Test
    public void bug2101() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug2101";
        
        CeylonDocTool tool = 
                tool(Arrays.asList(new File(pathname)),
                        Arrays.asList(new File("doc")),
                        Arrays.asList(moduleName),
                        true, false, false);
        tool.run();
        
        File destDir = getOutputDir(tool, moduleName, "1");
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span class='identifier'>bug2101</span>\\(\\)</code>"
                        + "<div class='description'><div class='doc section'><p>actual doc</p>"));
        
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile("<span class='identifier'>bug2101_wildcard_import</span>\\(\\)</code>"
                        + "<div class='description'><div class='doc section'></div>"
                        + "<div class='annotations section'><span class='title'>Annotations: </span><ul><li>doc\\(<span class='literal'>&quot;fake doc&quot;</span>\\)"));
    }

    @Test
    public void bug2285() throws Exception {
        String pathname = "test/ceylondoc-doesnotexist";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug2285";
        
        Module module = new Module();
        module.setName(Arrays.asList(moduleName));
        module.setVersion("1");
        
        try{
            CeylonDocTool tool = 
                    tool(Arrays.asList(new File(pathname)),
                            Arrays.asList(new File("doc")),
                            Arrays.asList(moduleName),
                            true, false, false);
            tool.run();
            Assert.fail();
        }catch(RuntimeException x){
            Assert.assertEquals("Can't find module: org.eclipse.ceylon.ceylondoc.test.modules.bug2285", x.getMessage());
        }
    }

    @Test
    public void bug2346() throws Exception {
        String pathname = "test/ceylondoc";
        String moduleName = "org.eclipse.ceylon.ceylondoc.test.modules.bug2346";
        
        Module module = new Module();
        module.setName(Arrays.asList(moduleName));
        module.setVersion("1");
        
        CeylonDocTool tool = 
                    tool(Arrays.asList(new File(pathname)),
                            Arrays.asList(new File("doc")),
                            Arrays.asList(moduleName),
                            true, false, false);
        tool.run();
    }

    private void assertFileExists(File destDir, boolean includeNonShared) {
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "api-index.html");
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
    
    private void assertResourcesExists(File destDir, String resourceFolder) {
        assertDirectoryExists(destDir, resourceFolder);
        assertFileExists(destDir, resourceFolder+"/index.js");
        assertFileExists(destDir, resourceFolder+"/ceylondoc.css");
        assertFileExists(destDir, resourceFolder+"/ceylondoc.js");
        assertFileExists(destDir, resourceFolder+"/bootstrap.min.css");
        assertFileExists(destDir, resourceFolder+"/bootstrap.min.js");
        assertFileExists(destDir, resourceFolder+"/jquery-1.8.2.min.js");
        assertFileExists(destDir, resourceFolder+"/ceylon.css");
        assertFileExists(destDir, resourceFolder+"/ceylon.js");
        assertFileExists(destDir, resourceFolder+"/rainbow.min.js");
        assertFileExists(destDir, resourceFolder+"/ceylondoc-logo.png");
        assertFileExists(destDir, resourceFolder+"/ceylondoc-icons.png");
        assertFileExists(destDir, resourceFolder+"/favicon.ico");
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
    
    private void assertConstructors(File destDir, boolean includeNonShared) throws Exception {
        assertMatchInFile(destDir, "StubClassWithConstructors.type.html", 
                Pattern.compile("<span class='identifier'>StubClassWithConstructors</span>\\(\\)"));
        assertMatchInFile(destDir, "StubClassWithConstructors.type.html", 
                Pattern.compile("ctor1"));
        assertMatchInFile(destDir, "StubClassWithConstructors.type.html", 
                Pattern.compile("ctor2"));
        
        if( includeNonShared ) {
            assertMatchInFile(destDir, "StubClassWithConstructors.type.html", 
                    Pattern.compile("ctorInternal"));
        }
        else {
            assertNoMatchInFile(destDir, "StubClassWithConstructors.type.html", 
                    Pattern.compile("ctorInternal"));
        }
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
    			Pattern.compile("<div class='parameters section'><span class='title'>Parameters: </span><ul><li><code><span class='parameter' id='stubTopLevelMethod-numbers'>numbers</span></code><div class='doc section'><p>Sequenced parameters <code>numbers</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='parameters section'><span class='title'>Parameters:"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><code><span class='parameter' id='StubClass-a'>a</span></code><div class='doc section'><p>Initializer parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><code><span class='parameter' id='StubClass-b'>b</span></code><div class='doc section'><p>Initializer parameter <code>b</code></p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><code><span class='parameter' id='methodWithParametersDocumentation-a'>a</span></code><div class='doc section'><p>Method parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><code><span class='parameter' id='methodWithParametersDocumentation-b'>b</span></code><div class='doc section'><p>Method parameter <code>b</code></p>"));
	}
    
    private void assertParametersAssertions(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<li><code><span class='parameter' id='stubFunctionWithAssertion2-n'>n</span></code><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists n</code></li><li><i class='icon-assertion'></i><code>n \\&gt;= 0 \\&amp;\\&amp; n \\&lt;=255</code></li>" +
                		"<li><i class='icon-assertion'></i><code>s.size \\&lt; n</code></li>" +
                		"</ul>" +
                		"</div></li><li><code><span class='parameter' id='stubFunctionWithAssertion2-s'>s</span></code><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists s</code></li><li><i class='icon-assertion'></i><code>s.size != 0</code></li>" +
                		"<li><i class='icon-assertion'></i><code>s.size \\&lt; n</code></li>" +
                		"</ul></div></li></ul>"));
        
        assertMatchInFile(destDir, "StubClassWithAssertions.type.html",
                Pattern.compile("<li><code><span class='parameter' id='StubClassWithAssertions-n'>n</span></code><div class='assertions' title='Parameter assertions'>" +
                		"<ul>" +
                		"<li><i class='icon-assertion'></i><code>exists n</code></li>" +
                		"<li><i class='icon-assertion'></i><code>0 \\&lt; n \\&lt; 123k</code></li>" +
                		"</ul>"));
        
        assertMatchInFile(destDir, "StubClassWithAssertions.type.html",
                Pattern.compile("<code><span class='parameter' id='StubClassWithAssertions-s'>s</span></code><div class='assertions' title='Parameter assertions'>" +
                        "<ul>" +
                        "<li><i class='icon-assertion'></i><code>exists s</code></li>" +
                        "<li><i class='icon-assertion'></i><code>s.any\\(\\(Character c\\) =\\&gt; c.digit\\)</code></li></ul>"));
    }
    
    private void assertParametersLinks(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='parameter' id='methodWithLinksToParametersOfParameterMethod-fce1'>fce1</span>" +
                		"<a id='methodWithLinksToParametersOfParameterMethod-fce1-fce2'></a>" +
                		"<a id='methodWithLinksToParametersOfParameterMethod-fce1-fce2-s'></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='doc section'><p>" +
                		"<a class='link' href='StubClass.type.html#methodWithLinksToParametersOfParameterMethod-fce1-fce2' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.methodWithLinksToParametersOfParameterMethod.fce1.fce2'><code><span class='identifier'>fce2\\(\\)</span></code></a>, " +
                		"<a class='link' href='StubClass.type.html#methodWithLinksToParametersOfParameterMethod-fce1-fce2-s' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.methodWithLinksToParametersOfParameterMethod.fce1.fce2.s'><code><span class='identifier'>s</span></code></a></p>"));
    }

	private void assertThrows(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='throws section'><span class='title'>Throws </span><ul><li>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<code><span title='ceylon.language::OverflowException'><span class='type-identifier'>OverflowException</span></span></code><p>if the number is too large to be represented as an integer</p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<code><a class='link' href='StubException.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubException'><span class='type-identifier'>StubException</span></a></code><p><code>when</code> with <strong>WIKI</strong> syntax</p>"));
    }

    private void assertSee(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also </span><span class='value'><code><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>, <a class='link' href='index.html#stubTopLevelMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'><code><span class='identifier'>stubTopLevelMethod\\(\\)</span></code></a></span></div>"));
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also </span>"
                + "<span class='value'><code><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>, "
                + "<a class='link' href='index.html#stubTopLevelAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'><code><span class='identifier'>stubTopLevelAttribute</span></code></a>, "
                + "<a class='link' href='index.html' title='Go to module'><code>org.eclipse.ceylon.ceylondoc.test.modules.single</code></a>, "
                + "<a class='link' href='index.html#section-package' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single'><code>org.eclipse.ceylon.ceylondoc.test.modules.single</code></a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also </span>"
                + "<span class='value'>"
                + "<code><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>, "
                + "<code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code>, "
                + "<a class='link' href='index.html#stubTopLevelAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'><code><span class='identifier'>stubTopLevelAttribute</span></code></a>, "
                + "<a class='link' href='index.html#stubTopLevelMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'><code><span class='identifier'>stubTopLevelMethod\\(\\)</span></code></a>, "
                + "<a class='link' href='index.html' title='Go to module'><code>org.eclipse.ceylon.ceylondoc.test.modules.single</code></a>, "
                + "<a class='link' href='a/index.html' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single.a'><code>org.eclipse.ceylon.ceylondoc.test.modules.single.a</code></a>"));
        
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<div class='see section'><span class='title'>See also </span>"
                + "<span class='value'>"
                + "<code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code>"));

        // FIXME: enable back when we can have metamodel refs to object members
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also </span><span class='value'><a class='link' href='StubClass.type.html#methodWithSee' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.methodWithSee'><code><span class='identifier'>methodWithSee\\(\\)</span></code></a>"/*, <a class='link' href='stubObject.object.html#foo' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject.foo'>stubObject.foo</a>"*/));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also </span><span class='value'><a class='link' href='StubClass.type.html#attributeWithSee' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.attributeWithSee'><code><span class='identifier'>attributeWithSee</span></code></a>, <code><a class='link' href='StubException.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubException'><span class='type-identifier'>StubException</span></a></code>, <code><a class='link' href='a/A1.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A1'><span class='type-identifier'>A1</span></a></code>, <code><a class='link' href='a/A2.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A2'><span class='type-identifier'>AliasA2</span></a>"));
    }

    private void assertSince(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='since section'><span class='title'>Since </span><span class='value'>6.6.6-class</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='since section'><span class='title'>Since </span><span class='value'>6.6.6-attribute</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='since section'><span class='title'>Since </span><span class='value'>6.6.6-method</span>"));
    }

    private void assertIcons(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<i class='icon-interface'><i class='icon-decoration-enumerated'></i></i><span class='sub-navbar-name'><span class='type-identifier'>StubInterface</span></span>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-formal'></i></i>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'></i></i>"));

        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-interface'><i class='icon-decoration-enumerated'></i></i><code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='StubInnerClass' nowrap><i class='icon-class'></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-class'></i><span class='sub-navbar-name'><span class='type-identifier'>StubClass</span></span>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-impl'></i></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'><i class='icon-decoration-over'></i></i></i>"));
        
        assertMatchInFile(destDir, "index.html", Pattern.compile("<td id='stubTopLevelAttribute' nowrap><i class='icon-shared-member'><i class='icon-decoration-variable'></i></i><code class='decl-label'>stubTopLevelAttribute</code></td>"));
    }
    
    private void assertInnerTypesDoc(File destDir) throws Exception {
        assertFileExists(destDir, "StubClass.StubInnerInterface.type.html");
        assertFileExists(destDir, "StubClass.StubInnerClass.type.html");
        assertFileExists(destDir, "StubClass.StubInnerException.type.html");
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Interfaces"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerInterface'><span class='type-identifier'>StubInnerInterface</span></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Classes"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass'><span class='type-identifier'>StubInnerClass</span></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Exceptions"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerException.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerException'><span class='type-identifier'>StubInnerException</span></a>"));
        
        assertMatchInFile(destDir, "StubClass.StubInnerInterface.type.html", 
                Pattern.compile("<span class='sub-navbar-label'>interface</span><i class='icon-interface'></i><span class='sub-navbar-name'><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>.<span class='type-identifier'>StubInnerInterface</span></span></div>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html", 
                Pattern.compile("<span class='sub-navbar-name'><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>.<span class='type-identifier'>StubInnerClass</span></span>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html",
                Pattern.compile("<div class='inheritance-of'> <span class='keyword'>satisfies</span> <a class='link' href='StubClass.StubInnerInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerInterface'><span class='type-identifier'>StubClass</span>.<span class='type-identifier'>StubInnerInterface</span></a></div>"));                
    }
    
    private void assertDeprecated(File destDir) throws Exception {
        assertFileExists(destDir, "DeprecatedClass.type.html");
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<td id='DeprecatedClass' nowrap><i class='icon-decoration-deprecated'><i class='icon-class'></i></i><a class='decl-label' href='DeprecatedClass.type.html'><code>DeprecatedClass</code></a></td>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<code class='signature'><span class='modifiers'>shared</span> <a class='link' href='DeprecatedClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::DeprecatedClass'><span class='type-identifier'>DeprecatedClass</span></a></code><div class='description'><div class='deprecated section'><p><span class='title'>Deprecated: </span>This is <code>DeprecatedClass</code></p>"));

        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this attribute!"));
        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this method"));
    }
    
    private void assertTagged(File destDir) throws Exception {
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("var tagIndex = \\[\\n'stubInnerMethodTag1',"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass', 'type': 'class', 'url': 'StubClass.type.html', 'doc': '<p>This is <code>StubClass</code></p>', 'tags': \\['stubTag1', 'stubTag2'\\]"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass.attributeWithTagged', 'type': 'value', 'url': 'StubClass.type.html#attributeWithTagged', 'doc': '<p>The stub attribute with <code>tagged</code>.</p>', 'tags': \\['stubTag1'\\]"));
        assertMatchInFile(destDir, ".resources/index.js", 
                Pattern.compile("\\{'name': 'StubClass.methodWithTagged', 'type': 'function', 'url': 'StubClass.type.html#methodWithTagged', 'doc': '<p>The stub method with <code>tagged</code> .*?</p>', 'tags': \\['stubTag2'\\]"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='javascript:;' title='Enable/disable tag filter'>stubTag1</a><a class='tag label' name='stubTag2' href='javascript:;' title='Enable/disable tag filter'>stubTag2</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='javascript:;' title='Enable/disable tag filter'>stubTag1</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag2' href='javascript:;' title='Enable/disable tag filter'>stubTag2</a>"));
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1a' href='javascript:;' title='Enable/disable tag filter'>stubTag1a</a><a class='tag label' name='stubTag1b' href='javascript:;' title='Enable/disable tag filter'>stubTag1b</a><a class='tag label' name='stubTagWithVeryLongName ... !!!' href='javascript:;' title='Enable/disable tag filter'>stubTagWithVeryLongName ... !!!</a>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<div class='tags section'><a class='tag label' name='stubTag1' href='javascript:;' title='Enable/disable tag filter'>stubTag1</a><a class='tag label' name='stubTag2' href='javascript:;' title='Enable/disable tag filter'>stubTag2</a>"));
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
                Pattern.compile("<span class='void'>void</span> <span class='identifier'>methodWithSequencedParameter</span>\\(<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>\\* <span class='parameter'>numbers</span>\\)"));
	}
    
    private void assertCallableParameter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>methodWithCallableParameter1</span>\\(<span class='void'>void</span> onClick\\(\\)\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                  Pattern.compile("<span class='identifier'>methodWithCallableParameter2</span>&lt;<span class='type-parameter'>Element</span>&gt;\\(<span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> selecting\\(<span class='type-parameter'>Element</span> <span class='parameter'>element</span>\\)\\)"));

        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithCallableParameter3</span>\\(<span class='void'>void</span> fce1\\(<span class='void'>void</span> fce2\\(<span class='void'>void</span> fce3\\(\\)\\)\\)\\)"));
    }
    
    private void assertTupleParameter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>methodWithTouple1</span>\\(\\[<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>, <span title='ceylon.language::Float'><span class='type-identifier'>Float</span></span>\\] <span class='parameter'>t</span>\\)"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>methodWithTouple2</span>&lt;<span class='type-parameter'>T</span>&gt;\\(\\[<span title='ceylon.language::String'><span class='type-identifier'>String</span></span>|<span class='type-parameter'>T</span>, <span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>=, <code title='ceylon.language::Float'><span class='type-identifier'>Float</span></code>\\*\\] <span class='parameter'>t</span>\\)"));
    }
    
    private void assertDefaultedParametres(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>methodWithDefaultedParameter1</span>\\(" +
                                "<span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> <span class='parameter'>b1</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>true</span>, " +
                                "<span title='ceylon.language::Boolean'><span class='type-identifier'>Boolean</span></span> <span class='parameter'>b2</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>false</span>, "+
                                "<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='parameter'>i1</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>0</span>, " +
                                "<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='parameter'>i2</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>1</span>, " +
                                "<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='parameter'>i3</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>-1</span>, " +
                                "<span title='ceylon.language::Float'><span class='type-identifier'>Float</span></span> <span class='parameter'>f1</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>0.0</span>, " +
                                "<span title='ceylon.language::Float'><span class='type-identifier'>Float</span></span> <span class='parameter'>f2</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>1.0</span>, " +
                                "<span title='ceylon.language::Float'><span class='type-identifier'>Float</span></span> <span class='parameter'>f3</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>-1.0</span>, " +
                                "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='parameter'>s1</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>\"\"</span>, " +
                                "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span>\\? <span class='parameter'>s2</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>null</span>, " +
                                "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span>\\[\\] <span class='parameter'>s3</span><span class='parameter-default-value'> = </span><span class='parameter-default-value' title='Parameter default value'>\\[\\]</span>\\)"));
                
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>methodWithDefaultedParameter2</span>\\(" +
                                "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='parameter'>a</span><span class='parameter-default-value'> = </span><a class='parameter-default-value' href='#methodWithDefaultedParameter2-a' title='Go to parameter default value'>...</a>, " +
                                "<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='parameter'>b</span><span class='parameter-default-value'> = </span><a class='parameter-default-value' href='#methodWithDefaultedParameter2-b' title='Go to parameter default value'>...</a>\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<li><code><span class='parameter' id='methodWithDefaultedParameter4-separator'>separator</span><span class='parameter-default-value' title='Parameter default value'> = \\(Character ch\\) =\\&gt; ch.whitespace</span></code></li>"));
    }
    
    private void assertAnythingReturnType(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span title='ceylon.language::Anything'><span class='type-identifier'>Anything</span></span> <span class='identifier'>methodWithAnything</span>\\(\\)"));
    }

    private void assertFencedCodeBlockWithSyntaxHighlighter(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<script type='text/javascript' src='.resources/rainbow.min.js'>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<pre data-language=\"ceylon\">shared default Boolean subset\\(Set set\\) \\{"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<pre data-language=\"ceylon\">shared actual default Integer hash \\{"));
    }
    
    private void assertWikiStyleLinkSyntax(File destDir, boolean includeNonShared) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass = <code><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("class StubClass = <code><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface = <code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("interface StubInterface = <code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInnerException = <code><a class='link' href='StubClass.StubInnerException.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerException'><span class='type-identifier'>StubInnerException</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelMethod = <a class='link' href='index.html#stubTopLevelMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'><code><span class='identifier'>stubTopLevelMethod\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelMethod() = <a class='link' href='index.html#stubTopLevelMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'><code><span class='identifier'>stubTopLevelMethod\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("function stubTopLevelMethod = <a class='link' href='index.html#stubTopLevelMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod'><code><span class='identifier'>stubTopLevelMethod\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelAttribute = <a class='link' href='index.html#stubTopLevelAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'><code><span class='identifier'>stubTopLevelAttribute</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("value stubTopLevelAttribute = <a class='link' href='index.html#stubTopLevelAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelAttribute'><code><span class='identifier'>stubTopLevelAttribute</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'><code><span class='type-identifier'>StubInterface</span>.<span class='identifier'>formalMethodFromStubInterface\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass = <code><a class='link' href='StubClass.StubInnerClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass'><span class='type-identifier'>StubInnerClass</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass.innerMethod = <a class='link' href='StubClass.StubInnerClass.type.html#innerMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass.innerMethod'><code><span class='type-identifier'>StubInnerClass</span>.<span class='identifier'>innerMethod\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface with custom name = <a class='link-custom-text' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'>custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("unresolvable1 = <span class='link-unresolvable'><code>unresolvable</code></span>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("unresolvable2 = <span class='link-unresolvable'>unresolvable with custom name \\(<code>unresolvable</code>\\)</span>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject = <a class='link' href='index.html#stubObject' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject'><code><span class='identifier'>stubObject</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.foo = <a class='link' href='stubObject.object.html#foo' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject.foo'><code><span class='type-identifier'>stubObject</span>.<span class='identifier'>foo\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.foo() = <a class='link' href='stubObject.object.html#foo' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject.foo'><code><span class='type-identifier'>stubObject</span>.<span class='identifier'>foo\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject = <a class='link' href='stubObject.object.html#stubInnerObject' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject.stubInnerObject'><code><span class='type-identifier'>stubObject</span>.<span class='identifier'>stubInnerObject</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject.fooInner = <a class='link' href='stubObject.stubInnerObject.object.html#fooInner' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubObject.stubInnerObject.fooInner'><code><span class='type-identifier'>stubObject</span>.<span class='type-identifier'>stubInnerObject</span>.<span class='identifier'>fooInner\\(\\)</span></code></a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported A1 = <code><a class='link' href='a/A1.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A1'><span class='type-identifier'>A1</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported AliasA2 = <code><a class='link' href='a/A2.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A2'><span class='type-identifier'>AliasA2</span></a></code>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClassWithGenericTypeParams = <code><a class='link' href='StubClassWithGenericTypeParams.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClassWithGenericTypeParams'><span class='type-identifier'>StubClassWithGenericTypeParams</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClassWithGenericTypeParams with custom name = <a class='link-custom-text' href='StubClassWithGenericTypeParams.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClassWithGenericTypeParams'>custom with type params</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("module = <a class='link' href='index.html' title='Go to module'><code>org.eclipse.ceylon.ceylondoc.test.modules.single</code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("package = <a class='link' href='index.html#section-package' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single'><code>org.eclipse.ceylon.ceylondoc.test.modules.single</code></a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface = <code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'><code><span class='type-identifier'>StubInterface</span>.<span class='identifier'>formalMethodFromStubInterface\\(\\)</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface with custom name = <a class='link-custom-text' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'>full custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullUnresolvable1 = <span class='link-unresolvable'><code>unresolvable::Bar</code></span>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullUnresolvable2 = <span class='link-unresolvable'><code>unresolvable.bar::Bar.foo</code></span>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter s = <a class='link' href='StubClass.type.html#methodWithLinksInDoc-s' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.methodWithLinksInDoc.s'><code><span class='identifier'>s</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter methodWithParametersDocumentation.a = <a class='link' href='StubClass.type.html#methodWithParametersDocumentation-a' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.methodWithParametersDocumentation.a'><code><span class='identifier'>a</span></code></a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("parameter stubTopLevelMethod.numbers = <a class='link' href='index.html#stubTopLevelMethod-numbers' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubTopLevelMethod.numbers'><code><span class='identifier'>numbers</span></code></a>"));
        
        if (includeNonShared) {
            assertMatchInFile(destDir, "StubClass.type.html",
                    Pattern.compile("PrivateClass = <code><a class='link' href='PrivateClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::PrivateClass'><span class='type-identifier'>PrivateClass</span></a></code>"));
        } else {
            assertMatchInFile(destDir, "StubClass.type.html",
                    Pattern.compile("PrivateClass = <code><span title='org.eclipse.ceylon.ceylondoc.test.modules.single::PrivateClass'><span class='type-identifier'>PrivateClass</span></span></code>"));
        }
    }
    
    private void assertConstants(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='identifier'>constAbc</span><span class='specifier'>= \"abcdef\"</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='identifier'>constLoremIpsumMultiLine</span><span class='specifier'>= \"Lorem ipsum dolor sit amet, consectetur adipisicing elit, </span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>                                          sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::String'><span class='type-identifier'>String</span></span>\\[\\] <span class='identifier'>constAbcArray</span><span class='specifier'>= \\[</span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>    \"abc\","));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Character'><span class='type-identifier'>Character</span></span> <span class='identifier'>constCharA</span><span class='specifier'>= 'A'</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span> <span class='identifier'>constNumTwo</span><span class='specifier'>= constNumZero \\+ 1 \\+ 1</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span title='ceylon.language::Float'><span class='type-identifier'>Float</span></span> <span class='identifier'>constNumPI</span><span class='specifier'>= 3.14</span>"));
    }
    
    private void assertLinksToRefinedDeclaration(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.defaultDeprecatedMethodFromStubInterface'><code><span class='type-identifier'>StubInterface</span>.<span class='identifier'>defaultDeprecatedMethodFromStubInterface</span></code></a></div>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'><code><span class='type-identifier'>StubInterface</span>.<span class='identifier'>formalMethodFromStubInterface</span></code></a></div>"));
        assertMatchInFile(destDir, "a/StubClassExtended.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refines </span><a class='link-custom-text' href='../StubClass.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.formalMethodFromStubInterface'><code><span class='type-identifier'>StubClass</span>.<span class='identifier'>formalMethodFromStubInterface</span></code></a><span class='title'> ultimately refines </span><a class='link-custom-text' href='../StubInterface.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'><code><span class='type-identifier'>StubInterface</span>.<span class='identifier'>formalMethodFromStubInterface</span></code></a></div>"));
    }
    
    private void assertGenericTypeParams(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<span class='sub-navbar-name'><span class='type-identifier'>StubClassWithGenericTypeParams</span>&lt;<span class='type-parameter'><span class='type-parameter-keyword'>in </span>ContravariantType, T1, T2, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter'> = </span><span class='type-parameter-value'>\\{<a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>\\*\\}</span></span>&gt;</span>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given</span> <span class='type-parameter'>T1</span> <span class='keyword'>satisfies</span> <span title='ceylon.language::Obtainable'><span class='type-identifier'>Obtainable</span></span> &amp; <span title='ceylon.language::Identifiable'><span class='type-identifier'>Identifiable</span></span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given</span> <span class='type-parameter'>T2</span> <span class='keyword'>of</span> <span title='ceylon.language::Obtainable'><span class='type-identifier'>Obtainable</span></span> | <span title='ceylon.language::String'><span class='type-identifier'>String</span></span></div>"));

        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<code class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> <span class='identifier'>methodWithGenericTypeParams</span>&lt;<span class='type-parameter'><span class='type-parameter-keyword'>in </span>ContravariantType, X1, X2, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter'> = </span><span class='type-parameter-value'>\\{<a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>\\*\\}</span></span>&gt;\\(\\)"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given</span> <span class='type-parameter'>X1</span> <span class='keyword'>satisfies</span> <span title='ceylon.language::Obtainable'><span class='type-identifier'>Obtainable</span></span> &amp; <span title='ceylon.language::Identifiable'><span class='type-identifier'>Identifiable</span></span></div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given</span> <span class='type-parameter'>X2</span> <span class='keyword'>of</span> <span title='ceylon.language::Obtainable'><span class='type-identifier'>Obtainable</span></span> | <span title='ceylon.language::String'><span class='type-identifier'>String</span></span></div>"));
    }
    
    private void assertObjectPageDifferences(File destDir) throws Exception {
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<title>Object stubObject</title>"));
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<span class='sub-navbar-label'>object</span><i class='icon-object'></i><span class='sub-navbar-name'><span class='type-identifier'>stubObject</span></span>"));
        assertMatchInFile(destDir, "stubObject.object.html",
                Pattern.compile("<a href='index.html#stubObject'><span title='Jump to singleton object declaration'>Singleton object declaration</span></a>"));
        assertNoMatchInFile(destDir, "stubObject.object.html", 
                Pattern.compile("<table id='section-initializer'"));
        assertMatchInFile(destDir, "stubObject.stubInnerObject.object.html",
                Pattern.compile("<a href='stubObject.object.html#stubInnerObject'><span title='Jump to singleton object declaration'>Singleton object declaration</span></a>"));
    }
    
    private void assertExternalLinks(File destDir, String repoUrl) throws Exception {
        String linkStart = "<a class='link' href='" + repoUrl + "/org/eclipse/ceylon/ceylondoc/test/modules/dependency";
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/b/1.0/module-doc/api/B.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B'><span class='type-identifier'>B</span></a> <span class='identifier'>fceB</span>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/c/1.0/module-doc/api/C.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.c::C'><span class='type-identifier'>C</span></a> <span class='identifier'>fceC</span>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("b = " + linkStart + "/b/1.0/module-doc/api/index.html#b' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::b'><code><span class='identifier'>b()</span></code></a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B = <code>" + linkStart + "/b/1.0/module-doc/api/B.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B'><span class='type-identifier'>B</span></a></code>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B.b = " + linkStart + "/b/1.0/module-doc/api/B.type.html#b' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B.b'><code><span class='type-identifier'>B</span>.<span class='identifier'>b()</span></code></a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("b2 = " + linkStart + "/b/1.0/module-doc/api/bb/index.html#b2' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::b2'><code><span class='identifier'>b2()</span></code></a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B2 = <code>" + linkStart + "/b/1.0/module-doc/api/bb/B2.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2'><span class='type-identifier'>B2</span></a></code>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("B2.b2 = " + linkStart + "/b/1.0/module-doc/api/bb/B2.type.html#b2' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2'><code><span class='type-identifier'>B2</span>.<span class='identifier'>b2()</span></code></a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::b = " + linkStart + "/b/1.0/module-doc/api/index.html#b' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::b'><code><span class='identifier'>b()</span></code></a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/b/1.0/module-doc/api/B.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B'><span class='type-identifier'>B</span></a></code>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B.b = " + linkStart + "/b/1.0/module-doc/api/B.type.html#b' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b::B.b'><code><span class='type-identifier'>B</span>.<span class='identifier'>b()</span></code></a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::b2 = " + linkStart + "/b/1.0/module-doc/api/bb/index.html#b2' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::b2'><code><span class='identifier'>b2()</span></code></a>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote(linkStart + "/b/1.0/module-doc/api/bb/B2.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2'><span class='type-identifier'>B2</span></a></code>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2 = " + linkStart + "/b/1.0/module-doc/api/bb/B2.type.html#b2' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.dependency.b.bb::B2.b2'><code><span class='type-identifier'>B2</span>.<span class='identifier'>b2()</span></code></a>")));
    }
    
    private void assertSharedParameterOfClass(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile(Pattern.quote("<td id='printHello' nowrap><i class='icon-shared-member'></i><code class='decl-label'>printHello</code></td>")));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> <span class='identifier'>printHello</span>(<span title='ceylon.language::String'><span class='type-identifier'>String</span></span> <span class='parameter'>name</span>)</code>")));
    }

    private void assertNameAliases(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("See <code class='signature'><span class='type-identifier'><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></span></code>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<div class='aliased section'><span class='title'>Aliases: </span><span class='value'><code class='signature'><span class='type-identifier'>StubClassAlias</span></code></span></div>")));
        
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td><a class='link-one-self' title='Link to this declaration' href='StubClass.type.html#firstAlias'><i class='icon-link'></i></a>See <code class='signature'><span class='identifier'><a class='link' href='StubClass.type.html#aliasedAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.aliasedAttribute'><span class='identifier'>aliasedAttribute</span></a></span></code></td>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td><a class='link-one-self' title='Link to this declaration' href='StubClass.type.html#methodAlias'><i class='icon-link'></i></a>See <code class='signature'><span class='identifier'><a class='link' href='StubClass.type.html#aliasedMethod' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.aliasedMethod'><span class='identifier'>aliasedMethod()</span></a></span></code></td>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td><a class='link-one-self' title='Link to this declaration' href='StubClass.type.html#StubInnerAlias'><i class='icon-link'></i></a><div class='tags section'><a class='tag label' name='stubTag1' href='javascript:;' title='Enable/disable tag filter'>stubTag1</a></div>See <code class='signature'><span class='type-identifier'><a class='link' href='StubClass.type.html#StubInnerTypeAlias' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerTypeAlias'>StubInnerTypeAlias</a></span></code></td>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td><a class='link-one-self' title='Link to this declaration' href='StubClass.type.html#StubInnerClassAlias'><i class='icon-link'></i></a>See <code class='signature'><span class='type-identifier'><a class='link' href='StubClass.StubInnerClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.StubInnerClass'><span class='type-identifier'>StubInnerClass</span></a></span></code></td>")));
        
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<div class='aliased section'><span class='title'>Aliases: </span><span class='value'><code class='signature'><span class='type-identifier'>StubInnerAlias</span></code></span></div>")));

        assertMatchInFile(destDir, "a/StubClassExtended.type.html",
                Pattern.compile(Pattern.quote(", <a class='link-custom-text' href='../StubClass.type.html#aliasedAttribute' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass.aliasedAttribute'><code><span class='identifier'>firstAlias</span></code></a>, ")));
    }

    private void assertAliases(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("Aliases")));        
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<td id='AliasEntry' nowrap><i class='icon-type-alias'></i><code class='decl-label'>AliasEntry</code></td>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> <span class='type-identifier'>AliasEntry</span>&lt;<span class='type-parameter'>T</span>&gt;<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given</span> <span class='type-parameter'>T</span> <span class='keyword'>satisfies</span> <span title='ceylon.language::Object'><span class='type-identifier'>Object</span></span></div><div class='type-alias-specifier'><span class='specifier'>=> </span><span class='type-parameter'>T</span>-&gt;<span class='type-parameter'>T</span></div>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<td id='AliasStubs' nowrap><i class='icon-decoration-deprecated'><i class='icon-type-alias'></i></i><code class='decl-label'>AliasStubs</code></td>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> <span class='type-identifier'>AliasStubs</span><span class='specifier-operator'> => </span><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a>") + "|" + Pattern.quote("<a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>") + "|" + Pattern.quote("<a class='link' href='StubException.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubException'><span class='type-identifier'>StubException</span></a>") + "|" + Pattern.quote("<span title='ceylon.language::Null'><span class='type-identifier'>Null</span></span></code>"))); 
        
        assertMatchInFile(destDir, "StubClassWithAlias.type.html",
                Pattern.compile(Pattern.quote("Nested Aliases")));
        assertMatchInFile(destDir, "StubClassWithAlias.type.html",
                Pattern.compile(Pattern.quote("<td id='InnerAliasNumber' nowrap><i class='icon-type-alias'></i><code class='decl-label'>InnerAliasNumber</code></td><td>")));
    }
    
    private void assertPackageNavigation(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>package</span><i class='icon-package'></i><span class='sub-navbar-name'><span class='package-identifier'><a class='link' href='../index.html#section-package' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single'>org.eclipse.ceylon.ceylondoc.test.modules.single</a>.<a class='link-custom-text' href='../a/index.html' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single.a'>a</a></span></span>")));
        assertMatchInFile(destDir, "a/aa/index.html",
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>package</span><i class='icon-package'></i><span class='sub-navbar-name'><span class='package-identifier'><a class='link' href='../../index.html#section-package' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single'>org.eclipse.ceylon.ceylondoc.test.modules.single</a>.<a class='link-custom-text' href='../../a/index.html' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single.a'>a</a>.<a class='link-custom-text' href='../../a/aa/index.html' title='Go to package org.eclipse.ceylon.ceylondoc.test.modules.single.a.aa'>aa</a></span></span>")));
    }
    
    private void assertSubpackages(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<tr class='table-header' title='Click for expand/collapse'><td colspan='2'><i class='icon-expand'></i>Subpackages</td></tr>")));
        assertMatchInFile(destDir, "a/index.html",
                Pattern.compile(Pattern.quote("<tr><td><i class='icon-package'></i><a class='link' href='../a/aa/index.html'>org.eclipse.ceylon.ceylondoc.test.modules.single.a.aa</a></td><td></td></tr>")));
    }
    
    private void assertAnnotations(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<table id='section-annotations'")));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<td id='stubAnnotationBar' nowrap><i class='icon-shared-member'><i class='icon-decoration-annotation'></i></i><code class='decl-label'>stubAnnotationBar</code></td>")));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(Pattern.quote("<td id='StubAnnotationBar' nowrap><i class='icon-class'><i class='icon-decoration-annotation'></i></i><a class='decl-label' href='StubAnnotationBar.type.html'><code>StubAnnotationBar</code></a></td>")));
        
        assertMatchInFile(destDir, "StubAnnotationBar.type.html", 
                Pattern.compile(Pattern.quote("<span class='sub-navbar-label'>annotation</span>")));
        assertMatchInFile(destDir, "StubAnnotationBar.type.html", 
                Pattern.compile(Pattern.quote("<div class='annotationConstructors section'><span class='title'>Annotation Constructors: </span><a class='link' href='index.html#stubAnnotationBar' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'><span class='identifier'>stubAnnotationBar()</span></a>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<div class='doc section'><p>The stub annotated function.</p>\n</div>" +
                		        "<div class='annotations section'><span class='title'>Annotations: </span><ul>")));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationFoo' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationFoo'><span class='identifier'>stubAnnotationFoo</span></a>" +
                		"(" +
                		"<span class='literal'>&quot;abc&quot;</span>, " +
                		"<span class='literal'>'a'</span>, " +
                		"<span class='literal'>123</span>, " +
                		"<span class='literal'>987.654</span>, " +
                		"true, " +
                		"`<span class='keyword'>class </span><a class='link' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a>`, " +
                		"`<span class='keyword'>package</span> ceylon.language.meta`, " +
                		"[], " +
                		"[<span class='literal'>0</span>, <span class='literal'>1</span>], " +
                		"{}, " +
                		"{<span class='literal'>0.0</span>, <span class='literal'>1.1</span>}" +
                		")</li>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationBar' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'><span class='identifier'>stubAnnotationBar</span></a>" +
                		"(<a class='link' href='index.html#stubAnnotationBaz' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationBaz'><span class='identifier'>stubAnnotationBaz</span></a>(<span class='literal'>&quot;baz&quot;</span>))</li>")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<li><a class='link' href='index.html#stubAnnotationBar' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationBar'><span class='identifier'>stubAnnotationBar</span></a>" +
                		"{baz=<a class='link' href='index.html#stubAnnotationBaz' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::stubAnnotationBaz'><span class='identifier'>stubAnnotationBaz</span></a>{s=<span class='literal'>&quot;baz&quot;</span>;};}</li>")));
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

    private void assertExceptions(File destDir) throws Exception {
        String sectionHeader = "<table id='section-exceptions'";
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(sectionHeader+".*"+"<td id='StubError'"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile(sectionHeader+".*\n.*"+"<td id='StubException'"));
    }
    
    private void assertModuleDependencies(File destDir) throws Exception {
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<table id='section-dependencies'")));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td class='shrink'><span title='shared import of module org.eclipse.ceylon.ceylondoc.test.modules.dependency.b 1.0'><i class='icon-module'><i class='icon-module-exported-decoration'></i></i></span><code class='decl-label'><a class='link' href='http://acme.com/repo/org/eclipse/ceylon/ceylondoc/test/modules/dependency/b/1.0/module-doc/api/index.html' title='Go to module'>org.eclipse.ceylon.ceylondoc.test.modules.dependency.b</a></code></td><td class='shrink'><code>1.0</code></td><td><div class='description import-description'></div></td></tr>")));        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile(Pattern.quote("<tr><td class='shrink'><span title='import of module org.eclipse.ceylon.ceylondoc.test.modules.dependency.c 1.0'><i class='icon-module'></i></span><code class='decl-label'><a class='link' href='http://acme.com/repo/org/eclipse/ceylon/ceylondoc/test/modules/dependency/c/1.0/module-doc/api/index.html' title='Go to module'>org.eclipse.ceylon.ceylondoc.test.modules.dependency.c</a></code></td><td class='shrink'><code>1.0</code></td><td><div class='description import-description'></div></td></tr>")));        
    }

    private void assertBug659ShowInheritedMembers(File destDir) throws Exception {
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile(Pattern.quote("Inherited Methods")));
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile(Pattern.quote("<td>Methods inherited from: <i class='icon-interface'><i class='icon-decoration-enumerated'></i></i><code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code><div class='inherited-members'><a class='link' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.defaultDeprecatedMethodFromStubInterface'><code><span class='identifier'>defaultDeprecatedMethodFromStubInterface()</span></code></a>, <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface'><code><span class='identifier'>formalMethodFromStubInterface()</span></code></a>")));
    }

    private void assertBug691AbbreviatedOptionalType(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> <span title='ceylon.language::String'><span class='type-identifier'>String</span></span>? <span class='identifier'>bug691AbbreviatedOptionalType1</span>()</code>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> <span class='type-parameter'>Element</span>? <span class='identifier'>bug691AbbreviatedOptionalType2</span>&lt;<span class='type-parameter'>Element</span>&gt;()</code>")));
    }
    
    private void assertBug839(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<code class='signature'><span class='modifiers'>shared</span> \\{&lt;<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>-&gt;<span class='type-parameter'>Element</span>&amp;<span title='ceylon.language::Object'><span class='type-identifier'>Object</span></span>&gt;\\*\\} <span class='identifier'>bug839</span>&lt;<span class='type-parameter'>Element</span>&gt;\\(\\)</code>"));
    }

    private void assertBug968(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> {<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>*} <span class='identifier'>bug968_1</span>()</code>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<code class='signature'><span class='modifiers'>shared</span> {<span title='ceylon.language::Integer'><span class='type-identifier'>Integer</span></span>+} <span class='identifier'>bug968_2</span>()</code>")));
    }

    private void assertBug927LoadingAndSortingInheritedMembers(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("Inherited Attributes"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td>Attributes inherited from: <i class='icon-class'><i class='icon-decoration-abstract'></i></i><code><span title='ceylon.language::Object'><span class='type-identifier'>Object</span></span></code><div class='inherited-members'><code>hash</code>, <code>string</code></div></td>")));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile(Pattern.quote("<td>Attributes inherited from: <i class='icon-interface'><i class='icon-decoration-enumerated'></i></i><code><a class='link' href='StubInterface.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface'><span class='type-identifier'>StubInterface</span></a></code><div class='inherited-members'><a class='link' href='StubInterface.type.html#string' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubInterface.string'><code><span class='identifier'>string</span></code></a></div></td>")));
    }
    
    private void assertBug1619BrokenLinkFromInheritedDoc(File destDir) throws Exception {
        assertMatchInFile(destDir, "a/StubClassExtended.type.html", 
                Pattern.compile("StubClass = <code><a class='link' href='../StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'><span class='type-identifier'>StubClass</span></a></code>"));
        assertMatchInFile(destDir, "a/StubClassExtended.type.html", 
                Pattern.compile("imported A1 = <code><a class='link' href='../a/A1.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A1'><span class='type-identifier'>A1</span></a></code>"));
    }
    
    private void assertBug1619BrokenLinkWithNewLine(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>bug1619BrokenLinkWithNewLine</span>\\(\\)</code><div class='description'><div class='doc section'><p><a class='link-custom-text' href='StubClass.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single::StubClass'>foo\nbar"));
    }
    
    private void assertBug2307AliasedName(File destDir) throws Exception {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='identifier'>bug2307AliasedName</span>\\(<a class='link' href='a/A2.type.html' title='Go to org.eclipse.ceylon.ceylondoc.test.modules.single.a::A2'><span class='type-identifier'>AliasA2</span></a>"));
    }
    
    private File getOutputDir(CeylonDocTool tool, String moduleName, String moduleVersion) {
        Module module = new Module();
        module.setName(Arrays.asList(moduleName));
        module.setVersion(moduleVersion);
        return getOutputDir(tool, module);
    }
    
    private File getOutputDir(CeylonDocTool tool, Module module) {
        String outputRepo = tool.getOut();
        return new File(new File(org.eclipse.ceylon.compiler.java.util.Util.getModulePath(new File(outputRepo), module),
                "module-doc"), "api");
    }

        
    private void compile(String pathname, String moduleName) throws Exception {
        compile(pathname, "build/ceylon-cars", moduleName);
    }
    
    private void compile(String pathname, String destDir, String moduleName) throws Exception {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", destDir, "-sysrep", "../dist/dist/repo", "-cp", CompilerTests.getClassPathAsPath());
        JavacTask task = compiler.getTask(null, null, null, options, Arrays.asList(moduleName), null);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
    }

    private void compileJavaModule(String pathname, String... fileNames) throws Exception {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", "build/ceylon-cars", "-sysrep", "../dist/dist/repo", "-cp", CompilerTests.getClassPathAsPath());
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
