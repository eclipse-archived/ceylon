package com.redhat.ceylon.tools.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.DocTool;
import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;

public class DocToolTest {
    
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    
    @Test
    public void testNoModules()  throws Exception {
        PluginModel<DocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument modules should appear at least 1 time(s)", e.getMessage());
        }
    }
    
    @Test
    public void testDoc()  throws Exception {
        PluginModel<DocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        DocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocNonShared()  throws Exception {
        PluginModel<DocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        DocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--non-shared", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocSourceCode()  throws Exception {
        PluginModel<DocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        DocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--source-code", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
}
