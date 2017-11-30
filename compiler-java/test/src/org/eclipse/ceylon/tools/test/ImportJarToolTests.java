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
package org.eclipse.ceylon.tools.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.MavenArtifactContext;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils.CeylonRepoManagerBuilder;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.tool.OptionArgumentException;
import org.eclipse.ceylon.common.tool.ToolError;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.common.tool.ToolUsageError;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.eclipse.ceylon.tools.importjar.CeylonImportJarTool;
import org.eclipse.ceylon.tools.importjar.ImportJarException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class ImportJarToolTests extends AbstractToolTests {

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
    }

    protected List<String> toolOptions(String... opts) {
        List<String> ret = super.toolOptions("--out", destDir.getPath());
        ret.addAll(Arrays.asList(opts));
        return ret;
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions());
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions("my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions("test", "my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "test/1.0", "test/src/org/eclipse/ceylon/tools/test/nonexistent.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            String jarName = "test/src/org/eclipse/ceylon/tools/test/nonexistent.jar";
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
                                                toolOptions("test/1.0",
                                                        "test/src/org/eclipse/ceylon/tools/test/test.jar",
                                                        "test/src/org/eclipse/ceylon/tools/test/nonexistent-sources.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            String srcJarName = "test/src/org/eclipse/ceylon/tools/test/nonexistent-sources.jar";
            srcJarName = srcJarName.replace('/', File.separatorChar);
            Assert.assertEquals("Source jar file " +srcJarName+ " does not exist", e.getMessage());
        }
    }
    
    @Test
    public void testDescriptorSuffix() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/ImportJarToolTests.java",
                    "test/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Descriptor file test/src/org/eclipse/ceylon/tools/test/ImportJarToolTests.java does not end with '.xml' or '.properties' extension", e.getMessage().replace('\\', '/'));
        }
    }
    
    @Test
    public void testSimpleModuleVersionJar() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions("--force", "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
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
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions("--verbose", "--force", "importtest.imptest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
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
                                                toolOptions( "--force",
                                                        "source.import.test/1.0",
                                                        "test/src/org/eclipse/ceylon/tools/test/test.jar",
                                                        "test/src/org/eclipse/ceylon/tools/test/test-source.jar"));
        
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
                                                toolOptions( "--force",
                                                         "--verbose",
                                                         "source.import.test/1.0",
                                                         "test/src/org/eclipse/ceylon/tools/test/test.jar",
                                                         "test/src/org/eclipse/ceylon/tools/test/test-source.jar"));
        
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-nonexistent-descriptor.xml", 
                    "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            String xmlName = "test/src/org/eclipse/ceylon/tools/test/test-nonexistent-descriptor.xml";
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor-broken.xml", 
                    "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Descriptor file test/src/org/eclipse/ceylon/tools/test/test-descriptor-broken.xml is not a valid module.xml file: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content is not allowed in prolog.", e.getMessage().replace('\\', '/'));
        }
    }

    @Test
    public void testWithInvalidPropertiesDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor-broken.properties", 
                    "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
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
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor.xml", 
                "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
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
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor.properties", 
                "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/1.0/module.properties");
        File f4 = destFile("importtest/1.0/module.properties.sha1");
        Assert.assertTrue(f1.exists());
        Assert.assertTrue(f2.exists());
        Assert.assertTrue(f3.exists());
        Assert.assertTrue(f4.exists());
    }

    @Test
    public void testSourceJarWithXmlDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                pluginFactory.bindArguments(model,
                                            getMainTool(),
                                            toolOptions("--descriptor",
                                                    "test/src/org/eclipse/ceylon/tools/test/test-descriptor.xml",
                                                    "source.import.test/1.0",
                                                    "test/src/org/eclipse/ceylon/tools/test/test.jar",
                                                    "test/src/org/eclipse/ceylon/tools/test/test-source.jar"));
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        File moduleXml = destFile("source/import/test/1.0/module.xml");
        File sha1ModuleXml = destFile("source/import/test/1.0/module.xml");

        Assert.assertTrue(jarFile.exists() && sha1JarFile.exists());
        Assert.assertTrue(sourceJarFile.exists() && sha1SourceJarFile.exists());
        Assert.assertTrue(moduleXml.exists());
        Assert.assertTrue(sha1ModuleXml.exists());
    }
    
    @Test
    public void testSourceJarWithPropertiesDescriptor() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = 
                pluginFactory.bindArguments(model,
                                            getMainTool(),
                                            toolOptions("--descriptor",
                                                    "test/src/org/eclipse/ceylon/tools/test/test-descriptor.properties",
                                                    "source.import.test/1.0",
                                                    "test/src/org/eclipse/ceylon/tools/test/test.jar",
                                                    "test/src/org/eclipse/ceylon/tools/test/test-source.jar"));
        tool.run();
        File jarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar");
        File sha1JarFile = destFile("source/import/test/1.0/source.import.test-1.0.jar.sha1");
        File sourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar");
        File sha1SourceJarFile = destFile("source/import/test/1.0/source.import.test-1.0-sources.jar.sha1");
        File propertiesFile = destFile("source/import/test/1.0/module.properties");
        File sha1PropertiesFile = destFile("source/import/test/1.0/module.properties");
        
        Assert.assertTrue(jarFile.exists());
        Assert.assertTrue(sha1JarFile.exists());
        Assert.assertTrue(sourceJarFile.exists());
        Assert.assertTrue(sha1SourceJarFile.exists());
        Assert.assertTrue(propertiesFile.exists());
        Assert.assertTrue(sha1PropertiesFile.exists());
    }
    
    @Test
    public void testWithUnknownModule() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor-unknown.properties", 
                    "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
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
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                "--dry-run",
                "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor.properties", 
                "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = destFile("importtest/1.0/module.properties");
        File f4 = destFile("importtest/1.0/module.properties.sha1");
        Assert.assertTrue(!f1.exists());
        Assert.assertTrue(!f2.exists());
        Assert.assertTrue(!f3.exists());
        Assert.assertTrue(!f4.exists());
    }
    
    @Test
    public void testBug1630() throws Exception {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
                    "--dry-run",
                    "--descriptor", "test/src/org/eclipse/ceylon/tools/test/test-descriptor.properties", 
                    "importtest/1.0", "test/src/org/eclipse/ceylon/tools/test/test2.jar"));
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
        FileUtil.copyAll(new File("test/src/org/eclipse/ceylon/tools/test/test-descriptor.properties"), destDir);
        FileUtil.copyAll(new File("test/src/org/eclipse/ceylon/tools/test/test.jar"), destDir);
        
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--cwd", destDir.getPath(),
                "--cacherep", getCachePath(),
                "--sysrep", getSysRepPath(),
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
    	// Jetty is compiled for JDK8
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8);
        
    	CeylonRepoManagerBuilder builder = CeylonUtils.repoManager();
    	RepositoryManager repository = builder.buildManager();
    	File artifact = repository.getArtifact(MavenArtifactContext.NAMESPACE, "org.eclipse.jetty:jetty-server", "9.3.2.v20150730");
    	Assert.assertNotNull(artifact);
    	
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);

        // no descriptor
        CeylonImportJarTool tool;
        StringBuilder b = new StringBuilder();
        // make sure we don't get a missing package with array shit in there: [Ljavax.servlet;
        try{
        	tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.setOut(b);
        	tool.run();
        	Assert.fail();
        } catch (ToolUsageError e) {
        	Assert.assertEquals("Problems were found, aborting. Try adding a descriptor file, see help for more information.", e.getMessage());
        	Assert.assertEquals(
        	        "The following JDK modules are used and could be declared as imports:\n"+
        	        "    java.base ... [shared]\n"+
        	        "    java.jdbc ... [shared]\n"+
        	        "    java.tls ... [shared]\n"+
        	        "    javax.naming\n"+
        	        "Modules containing the following packages need to be declared as imports:\n"+
        	        "(Tip: try running again with the '--show-suggestions' option)\n"+
        	        "    javax.servlet ... [shared]\n"+
        	        "    javax.servlet.descriptor ... [shared]\n"+
        	        "    javax.servlet.http ... [shared]\n"+
        	        "    org.eclipse.jetty.http ... [shared]\n"+
        	        "    org.eclipse.jetty.io ... [shared]\n"+
        	        "    org.eclipse.jetty.io.ssl ... [shared]\n"+
        	        "    org.eclipse.jetty.jmx ... [shared]\n"+
        	        "    org.eclipse.jetty.util ... [shared]\n"+
        	        "    org.eclipse.jetty.util.annotation ... [shared]\n"+
        	        "    org.eclipse.jetty.util.component ... [shared]\n"+
        	        "    org.eclipse.jetty.util.log ... [shared]\n"+
        	        "    org.eclipse.jetty.util.resource ... [shared]\n"+
        	        "    org.eclipse.jetty.util.ssl ... [shared]\n"+
        	        "    org.eclipse.jetty.util.statistic ... [shared]\n"+
        	        "    org.eclipse.jetty.util.thread ... [shared]\n"
        			, b.toString());
        }
        	

        // all OK
        tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-module.properties",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// exact
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jmx",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// **
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.**.jmx",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// *
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jm*",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        try{
        	// with missing module
        	tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        			"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        			// * with dots
        			"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.*.jmx",
        			"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.run();
        	Assert.fail();
        } catch (ToolUsageError e) {
        	Assert.assertEquals("Problems were found, aborting.", e.getMessage());
        }

        // with missing module
        tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        		"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        		// ?
        		"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound/9.3.2.v20150730=org.eclipse.jetty.jm?",
        		"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        tool.run();

        try{
        	// with invalid pattern
        	tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions(
        			"--descriptor", "test/src/org/eclipse/ceylon/tools/test/jetty-server-missing-optional-module.properties",
        			"--missing-dependency-packages", "org.eclipse.jetty:jetty-jmxnotfound",
        			"org.eclipse.jetty.jetty-server/9.3.2.v20150730", artifact.getAbsolutePath()));
        	tool.run();
        	Assert.fail();
        } catch (ToolError e) {
        	Assert.assertEquals("Invalid missing dependencies descriptor : 'org.eclipse.jetty:jetty-jmxnotfound'. 'Syntax is module-name/module-version=package-wildcard(,package-wildcard)*'.", e.getMessage());
        }
    }

    @Test
    public void testSystemRepositoryModuleDescriptors() throws Exception {
    	CeylonRepoManagerBuilder builder = CeylonUtils.repoManager();
        builder.outRepo(destDir.getPath())
        	.cacheRepo(cacheDir.getPath())
        	.systemRepo(getSysRepPath());
    	final RepositoryManager repository = builder.buildManager();
    	final Path repoPath = Paths.get(getSysRepPath());
    	Files.walkFileTree(repoPath, new SimpleFileVisitor<Path>(){
    		@Override
    		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    			if(file.getFileName().toString().endsWith(".jar") 
    					|| file.getFileName().toString().endsWith(".car")){
    			    System.err.println("Checking file "+file);
    				Path p = repoPath.relativize(file.getParent());
    				String module = p.getParent().toString().replace('/', '.');
    				String version = p.getFileName().toString();
    				try {
						checkModuleDescriptor(repository, module, version);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
    			}
    			return super.visitFile(file, attrs);
    		}
    	});
    }

	protected void checkModuleDescriptor(RepositoryManager repository, String module, String version) throws Exception {
		System.err.println("Checking "+module+"/"+version);
    	File artifact = repository.getArtifact(new ArtifactContext(null, module, version, ArtifactContext.CAR, ArtifactContext.JAR));
    	File descr = new File(artifact.getParentFile(), "module.xml");
    	Assert.assertNotNull(artifact);
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);

        CeylonImportJarTool tool;
        List<String> options = toolOptions(
        		"--dry-run", "--allow-cars",
        		"--descriptor", descr.getAbsolutePath(),
        		module+"/"+version, artifact.getAbsolutePath());
        if(module.equals("org.apache.commons.logging")){
        	options.addAll(0, Arrays.asList("--missing-dependency-packages", "org.apache.avalon.framework/4.1.3=org.apache.avalon.framework.**",
        			"--missing-dependency-packages", "org.apache.log4j/1.2.12=org.apache.log4j.**",
        			"--missing-dependency-packages", "org.apache.logkit/1.0.1=org.apache.log.**"
        			));
        }
        if(module.startsWith("org.eclipse.ceylon.aether")){
        	options.addAll(0, Arrays.asList("--ignore-annotations"));
        }
        tool = pluginFactory.bindArguments(model, getMainTool(), options);
        tool.run();
	}
}
