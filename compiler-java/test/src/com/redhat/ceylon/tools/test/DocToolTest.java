package com.redhat.ceylon.tools.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.CeylonDocTool;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class DocToolTest {
    
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    @Test
    public void testNoModules()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
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
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocNonShared()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--non-shared", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocSourceCode()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--source-code", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
}
