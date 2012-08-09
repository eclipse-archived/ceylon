package com.redhat.ceylon.tools;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.DocTool;
import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;

public class DocToolTest {
    
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    
    @Test
    public void testDoc()  throws Exception {
        PluginModel<DocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        DocTool tool = pluginFactory.bindArguments(model, Arrays.asList("-jhelp"));
        tool.run();
    }
    
}
