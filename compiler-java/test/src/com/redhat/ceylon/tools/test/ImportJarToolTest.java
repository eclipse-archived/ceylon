package com.redhat.ceylon.tools.test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.importjar.CeylonImportJarTool;
import com.redhat.ceylon.tools.CeylonToolLoader;

import junit.framework.Assert;

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
            // asserting this is thrown
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
            // asserting this is thrown
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
            // asserting this is thrown
        }
    }
    
    @Test
    public void testSimpleModuleVersionJar() {
        FileUtil.delete(new File("modules/importtest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList("importtest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            File f1 = new File("modules/importtest/1.0/importtest-1.0.jar");
            File f2 = new File("modules/importtest/1.0/importtest-1.0.jar.sha1");
            Assert.assertTrue(f1.exists() && f2.exists());
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testVerbosePackageModuleVersionJar() {
        FileUtil.delete(new File("modules/importtest/imptest"));
        ToolModel<CeylonImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            CeylonImportJarTool tool = pluginFactory.bindArguments(model, Arrays.asList("--verbose", "importtest.imptest/1.0", "test/src/com/redhat/ceylon/tools/test/test.jar"));
            tool.run();
            File f1 = new File("modules/importtest/imptest/1.0/importtest.imptest-1.0.jar");
            File f2 = new File("modules/importtest/imptest/1.0/importtest.imptest-1.0.jar.sha1");
            Assert.assertTrue(f1.exists() && f2.exists());
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }

}
