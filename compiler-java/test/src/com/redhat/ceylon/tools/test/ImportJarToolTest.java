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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.importjar.CeylonImportJarTool;
import com.redhat.ceylon.tools.importjar.ImportJarException;

public class ImportJarToolTest {

    private final List<String> defaultOptions;
    private final File destDir;
    private final File cacheDir;
    
    public ImportJarToolTest() {
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

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options());
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options("my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options("test", "my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/nonexistent.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            String jarName = "test/src/com/redhat/ceylon/tools/test/nonexistent.jar";
            jarName = jarName.replace('/', File.separatorChar);
            Assert.assertEquals("Jar file " +jarName + " does not exist", e.getMessage());
        }
    }
    
    @Test
    public void testDescriptorSuffix() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/ImportJarToolTest.java",
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/ImportJarToolTest.java does not end with '.xml' or '.properties' extension", e.getMessage());
        }
    }
    
    @Test
    public void testSimpleModuleVersionJar() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, options("--force", "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/1.0/importtest-1.0.jar");
        File f2 = destFile("importtest/1.0/importtest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }
    
    @Test
    public void testVerbosePackageModuleVersionJar() throws IOException {
        FileUtil.delete(destFile("importtest/imptest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, options("--verbose", "--force", "importtest.imptest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = destFile("importtest/imptest/1.0/importtest.imptest-1.0.jar");
        File f2 = destFile("importtest/imptest/1.0/importtest.imptest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }
    
    @Test
    public void testWithMissingXmlDescriptor() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getCause() instanceof ImportJarException);
            String xmlName = "test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml";
            xmlName = xmlName.replace('/', File.separatorChar);
            Assert.assertEquals("Descriptor file " + xmlName + " does not exist", e.getCause().getMessage());
        }
    }

    @Test
    public void testWithInvalidXmlDescriptor() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.xml", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.xml is not a valid module.xml file: org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content is not allowed in prolog.", e.getMessage());
        }
    }

    @Test
    public void testWithInvalidPropertiesDescriptor() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-broken.properties", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ImportJarException e) {
            Assert.assertEquals("Invalid module version '' in module descriptor dependency list", e.getMessage());
        }
    }

    @Test
    public void testWithXmlDescriptor() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
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
    public void testWithPropertiesDescriptor() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
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
    public void testWithUnknownModule() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor-unknown.properties", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (ToolUsageError e) {
            Assert.assertEquals("Problems were found, aborting.", e.getMessage());
        }
    }
    
    @Test
    public void testDryRun() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
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
    public void testBug1630() throws IOException {
        FileUtil.delete(destFile("importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, options(
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
    public void testWithPropertiesDescriptorCwd() throws IOException {
        File destDir = destFile("importtest");
        FileUtil.delete(destDir);
        destDir.mkdirs();
        FileUtil.copy(new File("test/src/com/redhat/ceylon/tools/test/test-descriptor.properties"), destDir);
        FileUtil.copy(new File("test/src/com/redhat/ceylon/tools/test/test.jar"), destDir);
        
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
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

}
