package com.redhat.ceylon.tools.test;

import java.util.Collections;

import org.junit.Test;

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

}
