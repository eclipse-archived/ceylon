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
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.Assert;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.importjar.CeylonImportJarTool;
import com.redhat.ceylon.importjar.ImportJarException;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class ImportJarToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    @Test
    public void testNoArgs() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Collections.<String>singletonList("my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList("test", "my.jar"));
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
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/nonexistent.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Jar file test/src/com/redhat/ceylon/tools/test/nonexistent.jar does not exist", e.getMessage());
        }
    }
    
    @Test
    public void testDescriptorSuffix() {
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/ImportJarToolTest.java",
                    "test/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/ImportJarToolTest.java does not end with '.xml' or '.properties' extension", e.getMessage());
        }
    }
    
    @Test
    public void testSimpleModuleVersionJar() {
        FileUtil.delete(new File("modules/importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList("importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = new File("modules/importtest/1.0/importtest-1.0.jar");
        File f2 = new File("modules/importtest/1.0/importtest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }
    
    @Test
    public void testVerbosePackageModuleVersionJar() {
        FileUtil.delete(new File("modules/importtest/imptest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList("--verbose", "importtest.imptest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = new File("modules/importtest/imptest/1.0/importtest.imptest-1.0.jar");
        File f2 = new File("modules/importtest/imptest/1.0/importtest.imptest-1.0.jar.sha1");
        Assert.assertTrue(f1.exists() && f2.exists());
    }
    
    @Test
    public void testWithMissingXmlDescriptor() {
        FileUtil.delete(new File("modules/importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                    "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml", 
                    "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getCause() instanceof ImportJarException);
            Assert.assertEquals("Descriptor file test/src/com/redhat/ceylon/tools/test/test-nonexistent-descriptor.xml does not exist", e.getCause().getMessage());
        }
    }
    
    @Test
    public void testWithXmlDescriptor() {
        FileUtil.delete(new File("modules/importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.xml", 
                "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = new File("modules/importtest/1.0/importtest-1.0.jar");
        File f2 = new File("modules/importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = new File("modules/importtest/1.0/module.xml");
        Assert.assertTrue(f1.exists() && f2.exists());
        Assert.assertTrue(f3.exists());
    }
    
    @Test
    public void testWithPropertiesDescriptor() {
        FileUtil.delete(new File("modules/importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--descriptor", "test/src/com/redhat/ceylon/tools/test/test-descriptor.properties", 
                "importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
        tool.run();
        File f1 = new File("modules/importtest/1.0/importtest-1.0.jar");
        File f2 = new File("modules/importtest/1.0/importtest-1.0.jar.sha1");
        File f3 = new File("modules/importtest/1.0/module.properties");
        Assert.assertTrue(f1.exists() && f2.exists());
        Assert.assertTrue(f3.exists());
    }

}
