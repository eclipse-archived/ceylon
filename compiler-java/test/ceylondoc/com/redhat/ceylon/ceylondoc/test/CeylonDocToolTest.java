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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.redhat.ceylon.ceylondoc.CeylonDocTool;
import com.redhat.ceylon.ceylondoc.Util;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.file.JavacFileManager;

public class CeylonDocToolTest {

    @Rule 
    public TestName name = new TestName();
    
    private CeylonDocTool tool(List<File> pathname, List<String> moduleName, 
            boolean throwOnError, String... repositories)
            throws IOException {
        CeylonDocTool tool = new CeylonDocTool();
        tool.setSourceFolders(pathname); 
        tool.setRepositories(Arrays.asList(repositories));
        tool.setModuleSpecs(moduleName);
        tool.setHaltOnError(throwOnError);
        tool.init();
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName());
        if (dir.exists()) {
            Util.delete(dir);
        }
        tool.setOutputRepository(dir.getAbsolutePath(), null, null);
        return tool;
    }
    
    private CeylonDocTool tool(String pathname, String moduleName, 
            boolean throwOnError, String... repositories)
            throws IOException {
        return tool(Arrays.asList(new File(pathname)),
                Arrays.asList(moduleName),
                throwOnError, repositories);
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
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern, GrepAsserter asserter) throws IOException {
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
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern) throws IOException {
        assertMatchInFile(destDir, path, pattern, AT_LEAST_ONE_MATCH);
    }
    
    protected void assertNoMatchInFile(File destDir, String path, Pattern pattern) throws IOException {
        assertMatchInFile(destDir, path, pattern, NO_MATCHES);
    }
    
    @Test
    public void moduleA() throws IOException {
        moduleA(false);
    }
    
    @Test
    public void moduleAWithPrivate() throws IOException {
        moduleA(true);
    }

    private void moduleA(boolean includeNonShared) throws IOException {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.single";

        CeylonDocTool tool = tool(pathname, moduleName, true);
        tool.setIncludeNonShared(includeNonShared);
        tool.setIncludeSourceCode(true);
        tool.makeDoc();
        
        Module module = new Module();
        module.setName(Arrays.asList(moduleName));
        module.setVersion("3.1.4");
        
        File destDir = getOutputDir(tool, module);
        
        assertFileExists(destDir, includeNonShared);
        assertBasicContent(destDir, includeNonShared);
        assertBy(destDir);
        assertLicense(destDir);
        assertParametersDocumentation(destDir);
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
        assertAnythingReturnType(destDir);
        assertFencedCodeBlockWithSyntaxHighlighter(destDir);
        assertWikiStyleLinkSyntax(destDir);
        assertConstants(destDir);
        assertLinksToRefinedDeclaration(destDir);
        assertGenericTypeParams(destDir);
        assertObjectPageDifferences(destDir);
        assertSharedParameterOfClass(destDir);
        assertBug659ShowInheritedMembers(destDir);
        assertBug691AbbreviatedOptionalType(destDir);
        assertBug839(destDir);
        assertBug927LoadingAndSortingInheritedMembers(destDir);
        assertBug968(destDir);
    }

    @Test
    public void externalLinksWithModuleNamePattern() throws IOException {
        List<String> links = new ArrayList<String>();
        links.add("com.redhat=file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName());
        links.add("ceylon=https://modules.ceylon-lang.org/test/");
        
        externalLinks(links);
    }
    
    @Test
    public void externalLinksWithoutModuleNamePattern() throws IOException {
        // Relies on having a ceylon.collection in your ~/.ceylon/repo or cache
        List<String> links = new ArrayList<String>();
        links.add("file://not-existing-dir");
        links.add("https://not-existing-url");        
        links.add("file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName());
        links.add("https://modules.ceylon-lang.org/test/");
        
        externalLinks(links);
    }    
    
    private void externalLinks(List<String> links) throws IOException {
        // Relies on having a ceylon.collection in your ~/.ceylon/repo or cache
        compile("test/ceylondoc", "com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        compile("test/ceylondoc", "com.redhat.ceylon.ceylondoc.test.modules.dependency.c");

        List<String> modules = new ArrayList<String>();
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.dependency.c");
        modules.add("com.redhat.ceylon.ceylondoc.test.modules.externallinks");

        CeylonDocTool tool = tool(Arrays.asList(new File("test/ceylondoc")), modules, true, "build/ceylon-cars");
        tool.setLinks(links);
        tool.makeDoc();

        Module module = new Module();
        module.setName(Arrays.asList("com.redhat.ceylon.ceylondoc.test.modules.externallinks"));
        module.setVersion("1.0");        

        File destDir = getOutputDir(tool, module);
        assertExternalLinks(destDir);
    }

    @Test
    public void dependentOnBinaryModule() throws IOException {
        String pathname = "test/ceylondoc";
        
        // compile the b module
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.dependency.b");
        
        CeylonDocTool tool = tool(pathname, "com.redhat.ceylon.ceylondoc.test.modules.dependency.c", true, "build/ceylon-cars");
        tool.makeDoc();
    }

    @Test
    public void classLoading() throws IOException {
        String pathname = "test/ceylondoc";
        
        // compile the a and b modules
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.a");
        compile(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.b");
        
        // now run docs on c, which uses b, which uses a
        CeylonDocTool tool = tool(pathname, "com.redhat.ceylon.ceylondoc.test.modules.classloading.c", true, "build/ceylon-cars");
        tool.makeDoc();
    }

    @Test
    public void containsJavaCode() throws IOException {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.mixed";
        
        // compile the java code first
        compileJavaModule(pathname, "com/redhat/ceylon/ceylondoc/test/modules/mixed/Java.java");
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.makeDoc();
    }

    @Test
    public void documentSingleModule() throws IOException {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.multi.a";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.makeDoc();

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
    public void documentPackage() throws IOException {
        String pathname = "test/ceylondoc";
        String moduleName = "com.redhat.ceylon.ceylondoc.test.modules.multi.a.sub";
        
        try{
            CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
            tool.makeDoc();
        }catch(RuntimeException x){
            Assert.assertEquals("Can't find module: com.redhat.ceylon.ceylondoc.test.modules.multi.a.sub", x.getMessage());
            return;
        }
        Assert.fail("Expected exception");
    }

    @Test
    public void documentDefaultModule() throws IOException {
        String pathname = "test/ceylondoc";
        String moduleName = "default";
        
        CeylonDocTool tool = tool(pathname, moduleName, true, "build/ceylon-cars");
        tool.makeDoc();

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
    }

    @Test
    public void ceylonLanguage() throws IOException {
        String pathname = "../ceylon.language/src";
        String moduleName = "ceylon.language";
        CeylonDocTool tool = tool(pathname, moduleName, true);
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.makeDoc();
        
        Module module = makeModule("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION);
        File destDir = getOutputDir(tool, module);
        
        assertFileExists(destDir, "index.html");
    }

    @Test
    public void ceylonSdk() throws IOException {
        File sdkDir = new File("../ceylon-sdk");
        if (!sdkDir.exists()
                || !sdkDir.isDirectory()) {
            Assert.fail("You don't have ceylon-sdk checked out at " + sdkDir.getAbsolutePath() + " so this test doesn't apply");
        }
        String[] fullModuleNames = {
                "ceylon.file",
                "ceylon.collection", 
                "ceylon.dbc",
                "ceylon.io",
                "ceylon.interop.java", 
                "ceylon.net",
                "ceylon.json", 
                "ceylon.process",
                "ceylon.math",
                "ceylon.time",
                "ceylon.test",
        };
        
        compileSdkJavaFiles();
        
        CeylonDocTool tool = tool(Arrays.asList(new File("../ceylon-sdk/source")), Arrays.asList(fullModuleNames), true, 
                "build/CeylonDocToolTest/" + name.getMethodName() + "-native");
        tool.setIncludeNonShared(false);
        tool.setIncludeSourceCode(true);
        tool.makeDoc();
        
        for(String moduleName : fullModuleNames){
            Module module = makeModule(moduleName, "0.5");
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
        File dir = new File("build", "CeylonDocToolTest/" + name.getMethodName() + "-native");
        if (dir.exists()) {
            Util.delete(dir);
        }
        dir.mkdirs();

        // download a required jar
        RepositoryManager repoManager = CeylonUtils.repoManager().buildManager();
        File artifact = repoManager.getArtifact(new ArtifactContext("org.jboss.xnio.api", "3.1.0.Beta9", ".jar"));

        // fire up the java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = Arrays.asList("-sourcepath", "../ceylon-sdk/source", "-d", dir.getAbsolutePath(), "-classpath", artifact.getAbsolutePath());
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        String[] fileNames = new String[]{
                "ceylon/net/http/server/internal/JavaHelper.java",
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
        File jarFolder = new File(dir, "ceylon/net/0.5");
        jarFolder.mkdirs();
        File jarFile = new File(jarFolder, "ceylon.net-0.5.car");
        // now jar it up
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        for(String name : fileNames){
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

    private void assertFileExists(File destDir, boolean includeNonShared) {
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
        }
        else {
            assertFileNotExists(destDir, "PrivateClass.type.html");
        }
    }

    private void assertBasicContent(File destDir, boolean includeNonShared) throws IOException {
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

    private void assertBy(File destDir) throws IOException {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span class='title'>By: </span><span class='value'>Tom Bentley</span>"));
        assertMatchInFile(destDir, "Types.type.html", 
                Pattern.compile("<span class='title'>By: </span><span class='value'>Tom Bentley</span>"));
    }
    
    private void assertLicense(File destDir) throws IOException {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("<span class='title'>License: </span><span class='value'>http://www.gnu.org/licenses/gpl.html</span>"));
    }

    private void assertParametersDocumentation(File destDir) throws IOException {
    	assertMatchInFile(destDir, "index.html", 
    			Pattern.compile("<div class='parameters section'><span class='title'>Parameters: </span><ul><li><span class='parameter'>numbers</span><p>Sequenced parameters <code>numbers</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='parameters section'><span class='title'>Parameters:"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter'>a</span><p>Initializer parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter'>b</span><p>Initializer parameter <code>b</code></p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter'>a</span><p>Method parameter <code>a</code></p>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<li><span class='parameter'>b</span><p>Method parameter <code>b</code></p>"));
	}

	private void assertThrows(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='throws section'><span class='title'>Throws: </span><ul><li>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("OverflowException<p>if the number is too large to be represented as an integer</p>"));        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubException.type.html'>StubException</a><p><code>when</code> with <strong>WIKI</strong> syntax</p>"));
    }

    private void assertSee(File destDir) throws IOException {
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html'>StubClass</a>, <a class='link' href='index.html#stubTopLevelMethod'>stubTopLevelMethod</a></span></div>"));
        assertMatchInFile(destDir, "index.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html'>StubClass</a>, <a class='link' href='index.html#stubTopLevelAttribute'>stubTopLevelAttribute</a></span></div>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubInterface.type.html'>StubInterface</a>, <a class='link' href='index.html#stubTopLevelAttribute'>stubTopLevelAttribute</a>, <a class='link' href='index.html#stubTopLevelMethod'>stubTopLevelMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html#methodWithSee'>methodWithSee</a>, <a class='link' href='stubObject.object.html#foo'>stubObject.foo</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<div class='see section'><span class='title'>See also: </span><span class='value'><a class='link' href='StubClass.type.html#attributeWithSee'>attributeWithSee</a>, <a class='link' href='StubException.type.html'>StubException</a>, <a class='link' href='a/A1.type.html'>A1</a>"));
    }
    
    private void assertIcons(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<i class='icon-interface'></i><span class='sub-navbar-name'>StubInterface</span>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-formal'></i></i>"));
        assertMatchInFile(destDir, "StubInterface.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'></i></i>"));

        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-interface'></i><a class='link' href='StubInterface.type.html'>StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='StubInnerClass' nowrap><i class='icon-class'></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<i class='icon-class'></i><span class='sub-navbar-name'>StubClass</span>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='formalMethodFromStubInterface' nowrap><i class='icon-shared-member'><i class='icon-decoration-impl'></i></i>"));
        assertMatchInFile(destDir, "StubClass.type.html", Pattern.compile("<td id='defaultDeprecatedMethodFromStubInterface' nowrap><i class='icon-decoration-deprecated'><i class='icon-shared-member'><i class='icon-decoration-over'></i></i></i>"));        
    }
    
    private void assertInnerTypesDoc(File destDir) throws IOException {
        assertFileExists(destDir, "StubClass.StubInnerInterface.type.html");
        assertFileExists(destDir, "StubClass.StubInnerClass.type.html");
        assertFileExists(destDir, "StubClass.StubInnerException.type.html");
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Interfaces"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerInterface.type.html'>StubInnerInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Classes"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerClass.type.html'>StubInnerClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Nested Exceptions"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<a class='link' href='StubClass.StubInnerException.type.html'>StubInnerException</a>"));
        
        assertMatchInFile(destDir, "StubClass.StubInnerInterface.type.html", 
                Pattern.compile("<div class='enclosingType section'><span class='title'>Enclosing class: </span><i class='icon-class'></i><a class='link' href='StubClass.type.html'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html", 
                Pattern.compile("<div class='enclosingType section'><span class='title'>Enclosing class: </span><i class='icon-class'></i><a class='link' href='StubClass.type.html'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.StubInnerClass.type.html", 
                Pattern.compile("<div class='satisfied section'><span class='title'>Satisfied Interfaces: </span><a class='link' href='StubClass.StubInnerInterface.type.html'>StubInnerInterface</a>"));                
    }
    
    private void assertDeprecated(File destDir) throws IOException {
        assertFileExists(destDir, "DeprecatedClass.type.html");
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<td id='DeprecatedClass' nowrap><i class='icon-decoration-deprecated'><i class='icon-class'></i></i><a class='link-discreet' href='DeprecatedClass.type.html'>DeprecatedClass</a></td>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <a class='link' href='DeprecatedClass.type.html'>DeprecatedClass</a></div><div class='description'><div class='deprecated section'><p><span class='title'>Deprecated: </span>This is <code>DeprecatedClass</code></p>"));
        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this attribute!"));
        assertMatchInFile(destDir, "DeprecatedClass.type.html",
                Pattern.compile("<div class='deprecated section'><p><span class='title'>Deprecated: </span>Don't use this method"));
    }
    
    private void assertTagged(File destDir) throws IOException {
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
    
    private void assertDocumentationOfRefinedMember(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Description of StubInterface.formalMethodFromStubInterface"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Description of StubInterface.defaultDeprecatedMethodFromStubInterface"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Deprecated in StubInterface.defaultDeprecatedMethodFromStubInterface"));
    }
    
	private void assertSequencedParameter(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<span class='void'>void</span> methodWithSequencedParameter\\(Integer\\[\\] numbers\\)"));
	}
    
    private void assertCallableParameter(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithCallableParameter1\\(<span class='void'>void</span> onClick\\(\\)\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                  Pattern.compile("methodWithCallableParameter2<span class='type-parameter'>&lt;Element&gt;</span>\\(Boolean selecting\\(<span class='type-parameter'>Element</span> element\\)\\)"));

        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithCallableParameter3\\(<span class='void'>void</span> fce1\\(<span class='void'>void</span> fce2\\(<span class='void'>void</span> fce3\\(\\)\\)\\)\\)"));
    }
    
    private void assertTupleParameter(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithTouple1\\(\\[Integer, Float\\] t\\)"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("methodWithTouple2<span class='type-parameter'>&lt;T&gt;</span>\\(\\[String|<span class='type-parameter'>T</span>, Integer=, Float\\*\\] t\\)"));
    }
    
    private void assertAnythingReturnType(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("Anything methodWithAnything\\(\\)"));
    }

    private void assertFencedCodeBlockWithSyntaxHighlighter(File destDir) throws IOException {
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
    
    private void assertWikiStyleLinkSyntax(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass = <a class='link' href='StubClass.type.html'>StubClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface = <a class='link' href='StubInterface.type.html'>StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInnerException = <a class='link' href='StubClass.StubInnerException.type.html'>StubInnerException</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelMethod = <a class='link' href='index.html#stubTopLevelMethod'>stubTopLevelMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubTopLevelAttribute = <a class='link' href='index.html#stubTopLevelAttribute'>stubTopLevelAttribute</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface'>StubInterface.formalMethodFromStubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass = <a class='link' href='StubClass.StubInnerClass.type.html'>StubClass.StubInnerClass</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubClass.StubInnerClass.innerMethod = <a class='link' href='StubClass.StubInnerClass.type.html#innerMethod'>StubClass.StubInnerClass.innerMethod</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("StubInterface with custom name = <a class='link' href='StubInterface.type.html'>custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("unresolvable = unresolvable"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject = <a class='link' href='stubObject.object.html'>stubObject</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.foo = <a class='link' href='stubObject.object.html#foo'>stubObject.foo</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject = <a class='link' href='stubObject.stubInnerObject.object.html'>stubObject.stubInnerObject</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("stubObject.stubInnerObject.fooInner = <a class='link' href='stubObject.stubInnerObject.object.html#fooInner'>stubObject.stubInnerObject.fooInner</a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported A1 = <a class='link' href='a/A1.type.html'>A1</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("imported AliasA2 = <a class='link' href='a/A2.type.html'>AliasA2</a>"));
        
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface = <a class='link' href='StubInterface.type.html'>com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface.formalMethodFromStubInterface = <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface'>com.redhat.ceylon.ceylondoc.test.modules.single::StubInterface.formalMethodFromStubInterface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullStubInterface with custom name = <a class='link' href='StubInterface.type.html'>full custom stub interface</a>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("fullUnresolvable = unresolvable::StubInterface"));
    }
    
    private void assertConstants(File destDir) throws IOException {
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("String constAbc<span class='specifier-operator'> = </span><span class='specifier-start'> \"abcdef\"</span><span class='specifier-semicolon'>;</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("String constLoremIpsumMultiLine<span class='specifier-operator'> = </span><span class='specifier-start'> \"Lorem ipsum dolor sit amet, consectetur adipisicing elit, </span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>                                          sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("String\\[\\] constAbcArray<span class='specifier-operator'> = </span><span class='specifier-start'> \\[</span><a class='specifier-ellipsis' href='#' title='Click for expand the rest of value.'>...</a><div class='specifier-rest'>    \"abc\","));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("Character constCharA<span class='specifier-operator'> = </span><span class='specifier-start'> 'A'</span><span class='specifier-semicolon'>;</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("Integer constNumTwo<span class='specifier-operator'> = </span><span class='specifier-start'> constNumZero \\+ 1 \\+ 1</span><span class='specifier-semicolon'>;</span>"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("Float constNumPI<span class='specifier-operator'> = </span><span class='specifier-start'> 3.14</span><span class='specifier-semicolon'>;</span>"));
    }
    
    private void assertLinksToRefinedDeclaration(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refined declaration: </span><a class='link' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface'>defaultDeprecatedMethodFromStubInterface</a><span class='value'></span></div>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='refined section'><span class='title'>Refined declaration: </span><a class='link' href='StubInterface.type.html#formalMethodFromStubInterface'>formalMethodFromStubInterface</a><span class='value'></span></div>"));
    }
    
    private void assertGenericTypeParams(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<span class='sub-navbar-name'>StubClassWithGenericTypeParams<span class='type-parameter'>&lt;<span class='type-parameter-keyword'>in </span>ContravariantType, T1, T2, T3, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter-keyword'> = </span>\\{<a class='link' href='StubClass.type.html'>StubClass</a>\\*\\}&gt;</span></span>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T1</span><span class='type-parameter-keyword'> satisfies </span>Number &amp; Closeable</div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T2</span><span class='type-parameter-keyword'> of </span>Number | String</div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T3</span>\\(String s\\)</div></div>"));
        
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> methodWithGenericTypeParams<span class='type-parameter'>&lt;<span class='type-parameter-keyword'>in </span>ContravariantType, X1, X2, X3, <span class='type-parameter-keyword'>out </span>CovariantType, DefaultedType<span class='type-parameter-keyword'> = </span>\\{<a class='link' href='StubClass.type.html'>StubClass</a>\\*\\}&gt;</span>\\(\\)"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>X1</span><span class='type-parameter-keyword'> satisfies </span>Number &amp; Closeable</div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>X2</span><span class='type-parameter-keyword'> of </span>Number | String</div>"));
        assertMatchInFile(destDir, "StubClassWithGenericTypeParams.type.html",
                Pattern.compile("<div class='type-parameter-constraint'><span class='type-parameter-keyword'>given </span><span class='type-parameter'>T3</span>\\(String s\\)</div>"));
    }
    
    private void assertObjectPageDifferences(File destDir) throws IOException {
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
    
    private void assertExternalLinks(File destDir) throws IOException {
        String url = "file://" + new File("").getAbsolutePath() + "/build/CeylonDocToolTest/" + name.getMethodName();
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<a class='link' href='"+url+"/com/redhat/ceylon/ceylondoc/test/modules/dependency/b/1.0/module-doc/B.type.html'>B</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<a class='link' href='"+url+"/com/redhat/ceylon/ceylondoc/test/modules/dependency/c/1.0/module-doc/C.type.html'>C</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("<a class='link' href='https://modules.ceylon-lang.org/test/ceylon/collection/0.5/module-doc/HashMap.type.html'>HashMap</a>"));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("zero = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/decimal/index.html#zero'>zero</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("Decimal = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/decimal/Decimal.type.html'>Decimal</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("Decimal.divided = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/decimal/Decimal.type.html#divided'>Decimal.divided</a>"));
        
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("ceylon.math.whole::one = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/whole/index.html#one'>ceylon.math.whole::one</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("ceylon.math.whole::Whole = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/whole/Whole.type.html'>ceylon.math.whole::Whole</a>"));
        assertMatchInFile(destDir, "index.html",
                Pattern.compile("ceylon.math.whole::Whole.power = <a class='link' href='https://modules.ceylon-lang.org/test/ceylon/math/0.5/module-doc/whole/Whole.type.html#power'>ceylon.math.whole::Whole.power</a>"));
    }
    
    private void assertSharedParameterOfClass(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<td id='printHello' nowrap><i class='icon-shared-member'></i>printHello</td>"));
        assertMatchInFile(destDir, "StubClass.type.html", 
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <span class='void'>void</span> printHello\\(String name\\)</div>"));
    }

    private void assertBug659ShowInheritedMembers(File destDir) throws IOException {
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile("Inherited Methods"));
    	assertMatchInFile(destDir, "StubClass.type.html",
    			Pattern.compile("<td>Methods inherited from: <i class='icon-interface'></i><a class='link' href='StubInterface.type.html'>StubInterface</a><div class='inherited-members'><a class='link' href='StubInterface.type.html#defaultDeprecatedMethodFromStubInterface'>defaultDeprecatedMethodFromStubInterface</a>, <a class='link' href='StubInterface.type.html#formalMethodFromStubInterface'>formalMethodFromStubInterface</a>"));
    }

    private void assertBug691AbbreviatedOptionalType(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> String\\? bug691AbbreviatedOptionalType1\\(\\)</div>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> <span class='type-parameter'>Element</span>\\? bug691AbbreviatedOptionalType2<span class='type-parameter'>&lt;Element&gt;</span>\\(\\)</div>"));
    }
    
    private void assertBug839(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> \\{&lt;Integer-&gt;<span class='type-parameter'>Element</span>&amp;Object&gt;\\*\\} bug839<span class='type-parameter'>&lt;Element&gt;</span>\\(\\)</div>"));
    }

    private void assertBug968(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> \\{Integer\\*\\} bug968_1\\(\\)</div>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<div class='signature'><span class='modifiers'>shared</span> \\{Integer\\+\\} bug968_2\\(\\)</div>"));
    }

    private void assertBug927LoadingAndSortingInheritedMembers(File destDir) throws IOException {
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("Inherited Attributes"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<td>Attributes inherited from: <i class='icon-class'><i class='icon-decoration-abstract'></i></i>Object<div class='inherited-members'>hash, string</div></td>"));
        assertMatchInFile(destDir, "StubClass.type.html",
                Pattern.compile("<td>Attributes inherited from: <i class='icon-interface'></i><a class='link' href='StubInterface.type.html'>StubInterface</a><div class='inherited-members'><a class='link' href='StubInterface.type.html#string'>string</a></div></td>"));
    }
    
    private File getOutputDir(CeylonDocTool tool, Module module) {
        String outputRepo = tool.getOutputRepository();
        return new File(com.redhat.ceylon.compiler.java.util.Util.getModulePath(new File(outputRepo), module),
                "module-doc");
    }

    private void compile(String pathname, String moduleName) throws IOException {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", "build/ceylon-cars");
        JavacTask task = compiler.getTask(null, null, null, options, Arrays.asList(moduleName), null);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
    }

    private void compileJavaModule(String pathname, String... fileNames) throws IOException {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", "build/ceylon-cars");
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
