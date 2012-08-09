package com.redhat.ceylon.tools.importjar;

import java.util.Collections;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.tools.importjar.ImportJarTool;

import junit.framework.Assert;

public class ImportJarToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    
    @Test
    public void testNoArgs() {
        PluginModel<ImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            ImportJarTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testJarOnly() {
        PluginModel<ImportJarTool> model = pluginLoader.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            ImportJarTool tool = pluginFactory.bindArguments(model, Collections.<String>singletonList("my.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }

}
