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
package com.redhat.ceylon.tools.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils.CeylonRepoManagerBuilder;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolError;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.tools.importjar.CeylonImportJarTool;
import com.redhat.ceylon.tools.importjar.ImportJarException;

public class ImportJarToolTests extends AbstractToolTests {

    private final List<String> defaultOptions;
    private final File destDir;
    private final File cacheDir;
    
    public ImportJarToolTests() {
        String destDirGeneral = "build/test-cars";
        String cacheDirGeneral = "build/test-cache";
        
        Package pakage = getClass().getPackage();
        String moduleName = pakage.getName();
        String destDir = destDirGeneral + File.separator;
        String cacheDir = cacheDirGeneral + File.separator;
        
        int lastDot = moduleName.lastIndexOf('.');
        if (lastDot == -1) {
            destDir += moduleName;
            cacheDir += moduleName;
        } else {
            destDir += moduleName.substring(lastDot+1);
            cacheDir += moduleName.substring(lastDot+1);
        }
        this.destDir = new File(destDir);
        this.cacheDir = new File(cacheDir);
        
        defaultOptions = Arrays.asList("--out", destDir, "--cacherep", cacheDir);
    }

    private List<String> options(String... opts) {
        List<String> allOpts = new ArrayList<String>(defaultOptions.size() + opts.length);
        allOpts.addAll(defaultOptions);
        allOpts.addAll(Arrays.asList(opts));
        return allOpts;
    }
    
    private File destFile(String f) {
        return new File(destDir, f);
    }
    
    private File cacheFile(String f) {
        return new File(cacheDir, f);
    }
    
    @Test
    public void testNoArgs() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument 'module' to command 'import-jar' should appear at least 1 time(s)", e.getMessage());
        }
    }
    
    @Test
    public void testJarOnly() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options("my.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Invalid value 'my.jar' given for argument 'module' to command 'import-jar'", e.getMessage());
        }
    }
    
    @Test
    public void testModuleOnlyJar() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options("test", "my.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Invalid value 'test' given for argument 'module' to command 'import-jar'", e.getMessage());
        }
    }
    
    @Test
    public void testNonexistentJar() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/nonexistent.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            String jarName = "test/src/com/redhat/ceylon/tools/test/nonexistent.jar";
            jarName = jarName.replace('/', File.separatorChar);
            Assert.assertEquals("Jar file " +jarName + " does not exist", e.getMessage());
        }
    }

    @Test
    public void testNonexistentSourceJar() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = 
                    pluginFactory.bindArguments(model, 
                                                getMainTool(), 
                                                options("test/1.0",
                                                        "test/src/com/redhat/ceylon/tools/test/test.jar",
                                                        "test/src/com/redhat/ceylon/tools/test/nonexistent-sources.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            String srcJarName = "test/src/com/redhat/ceylon/tools/test/nonexistent-sources.jar";
            srcJarName = srcJarName.replace('/', File.separatorChar);
            Assert.assertEquals("Source jar file " +srcJarName+ " does not exist", e.getMessage());
        }
    }
    
    @Test
    public void testDescriptorSuffix() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/ImportJarToolTests.java",
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/ImportJarToolTests.java does not end with '.xml' or '.properties' extension", e.getMessage().replace('\\', '/'));
        }
    }
    
    @Test
    public void testSimpleModuleVersionJar() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--force", "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }
    
    @Test
    public void testVerbosePackageModuleVersionJar() throws Exception {
        FileUtil.delete(destFile("importtest/imptest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--verbose", "--force", "importtest.imptest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/imptest/1.0/importtest.imptest-1.0.jar");
        File f2 = destFile("importtest/imptest/1.0/importtest.imptest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }

    @Test
    public void testJarAndSourceJarNoDescriptor() throws Exception {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                    pluginFactory.bindArguments(model, 
                                                getMainTool(), 
                                                options( "--force",
                                                        "source.import.test/1.0",
                                                        "test/src/com/redhat/ceylon/tools/test/test.jar",
                                                        "test/src/com/redhat/ceylon/tools/test/test-source.jar"));
        
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        
        Assert.assertTrue(jarFile.exists() && 
                          sha1JarFile.exists() && 
                          sourceJarFile.exists() &&
                          sha1SourceJarFile.exists());
    }

    @Test
    public void testJarAndSourceJarNoDescriptorVerbose() throws Exception {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                    pluginFactory.bindArguments(model, 
                                                getMainTool(), 
                                                options( "--force",
                                                         "--verbose",
                                                         "source.import.test/1.0",
                                                         "test/src/com/redhat/ceylon/tools/test/test.jar",
                                                         "test/src/com/redhat/ceylon/tools/test/test-source.jar"));
        
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        
        Assert.assertTrue(jarFile.exists() && 
                          sha1JarFile.exists() && 
                          sourceJarFile.exists() &&
                          sha1SourceJarFile.exists());
    }
    
    @Test
    public void testWithMissingXmlDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            String xmlName = "test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml";
            xmlName = xmlName.replace('/', File.separatorChar);
            Assert.assertEquals("Descriptor file " + xmlName + " does not exist", e.getMessage());
        }
    }

    @Test
    public void testWithInvalidXmlDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.xml", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.xml is not a valid module.xml file: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content is not allowed in prolog.", e.getMessage().replace('\\', '/'));
        }
    }

    @Test
    public void testWithInvalidPropertiesDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.properties", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Invalid module version '' in module descriptor dependency list", e.getMessage());
        }
    }

    @Test
    public void testWithXmlDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.xml", 
                "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/1.0/module.xml");
        Assert.assertTrue(f1.exists() && f2.exists());
        Assert.assertTrue(f3.exists());
    }
    
    @Test
    public void testWithPropertiesDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.properties", 
                "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/1.0/module.properties");
        Assert.assertTrue(f1.exists() && f2.exists());
        Assert.assertTrue(f3.exists());
    }

    @Test
    public void testSourceJarWithXmlDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                pluginFactory.bindArguments(model,
                                            getMainTool(),
                                            options("--descriptor",
                                                    "test/src/com/redhat/ceylon/tools/test/test-descriptor.xml",
                                                    "source.import.test/1.0",
                                                    "test/src/com/redhat/ceylon/tools/test/test.jar",
                                                    "test/src/com/redhat/ceylon/tools/test/test-source.jar"));
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        File moduleXml = destFile("source/import/test/1.0/module.xml");

        Assert.assertTrue(jarFile.exists() && sha1JarFile.exists());
        Assert.assertTrue(sourceJarFile.exists() && sha1SourceJarFile.exists());
        Assert.assertTrue(moduleXml.exists());
    }
    
    @Test
    public void testSourceJarWithPropertiesDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                pluginFactory.bindArguments(model,
                                            getMainTool(),
                                            options("--descriptor",
                                                    "test/src/com/redhat/ceylon/tools/test/test-descriptor.properties",
                                                    "source.import.test/1.0",
                                                    "test/src/com/redhat/ceylon/tools/test/test.jar",
                                                    "test/src/com/redhat/ceylon/tools/test/test-source.jar"));
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        File propertiesFile = destFile("source/import/test/1.0/module.properties");
        
        Assert.assertTrue(jarFile.exists() && sha1JarFile.exists());
        Assert.assertTrue(sourceJarFile.exists() && 
                          sha1SourceJarFile.exists());
        Assert.assertTrue(propertiesFile.exists());
    }
    
    @Test
    public void testWithUnknownModule() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-unknown.properties", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ToolUsageError e) {
            Assert.assertEquals("Problems were found, aborting.", e.getMessage());
        }
    }
    
    @Test
    public void testDryRun() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                "--dry-run",
                "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.properties", 
                "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/1.0/module.properties");
        Assert.assertTrue(!f1.exists() && !f2.exists());
        Assert.assertTrue(!f3.exists());
    }
    
    @Test
    public void testBug1630() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), options(
                    "--dry-run",
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.properties", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test2.jar"));
            tool.run();
            Assert.fail();
        } catch (ToolUsageError e) {
            Assert.assertEquals("Problems were found, aborting.", e.getMessage());
        }
    }
    
    @Test
    public void testWithPropertiesDescriptorCwd() throws Exception {
        File destDir = destFile("importtest");
        FileUtil.delete(destDir);
        destDir.mkdirs();
        FileUtil.copyAll(new File("test/src/com/redhat/ceylon/tools/test/test-descriptor.properties"), destDir);
        FileUtil.copyAll(new File("test/src/com/redhat/ceylon/tools/test/test.jar"), destDir);
        
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--cwd", destDir.getPath(),
                "--descriptor", "test-descriptor.properties", 
                "importtest/1.0", "test.jar"));
        tool.run();
        File f1 = destFile("importtest/modules/importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/modules/importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/modules/importtest/1.0/module.properties");
        Assert.assertTrue(f1.exists() && f2.exists());
        Assert.assertTrue(f3.exists());
    }

    @Test
    public void testMissingOptionalPackages() throws Exception {
    	CeylonRepoManagerBuilder builder = CeylonUtils.repoManager();
    	RepositoryManager repository = builder.buildManager();
    	File artifact = repository.getArtifact("org.eclipse.jetty:jetty-server", "9.3.2.v20150730");
    	Assert.assertNotNull(artifact);
    	
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);

        // no descriptor
        CeylonImportJarTool tool;
        StringBuilder b = new StringBuilder();
        // make sure we don't get a missing package with array shit in there: [Ljavax.servlet;
        try{
        	tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.setOut(b);
        	tool.run();
        	Assert.fail();
        } catch (ToolUsageError e) {
        	Assert.assertEquals("Problems were found, aborting. Try adding a descriptor file, see help for more information.", e.getMessage());
        	Assert.assertEquals("The following JDK modules are used and could be declared as shared imports:\n"+
        			"    java.base\n"+
        			"Modules containing the following packages need to be declared as shared imports:\n"+
        			"(Tip: try running again with the '--show-suggestions' option)\n"+
        			"    javax.servlet\n"+
        			"    javax.servlet.http\n"+
        			"    org.eclipse.jetty.http\n"+
        			"    org.eclipse.jetty.io\n"+
        			"    org.eclipse.jetty.jmx\n"+
        			"    org.eclipse.jetty.util\n"+
        			"    org.eclipse.jetty.util.component\n"+
        			"    org.eclipse.jetty.util.resource\n"+
        			"    org.eclipse.jetty.util.thread\n"
        			, b.toString());
        }
        	

        // all OK
        tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-module.properties",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// exact
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jmx",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// **
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.**.jmx",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// *
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jm*",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        try{
        	// with missing module
        	tool = pluginFactory.bindArguments(model, getMainTool(), options(
        			"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        			// * with dots
        			"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.*.jmx",
        			"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.run();
        	Assert.fail();
        } catch (ToolUsageError e) {
        	Assert.assertEquals("Problems were found, aborting.", e.getMessage());
        }

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), options(
        		"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// ?
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jm?",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        try{
        	// with invalid pattern
        	tool = pluginFactory.bindArguments(model, getMainTool(), options(
        			"--descriptor", "test/src/com/redhat/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        			"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound",
        			"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.run();
        	Assert.fail();
        } catch (ToolError e) {
        	Assert.assertEquals("Invalid missing dependencies descriptor : 'org.eclipse.jetty:jetty-jmxnotfound'. 'Syntax is module-name/module-version=package-wildcard(,package-wildcard)*'.", e.getMessage());
        }
    }
}
