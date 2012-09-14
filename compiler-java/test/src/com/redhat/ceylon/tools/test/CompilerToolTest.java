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
import com.redhat.ceylon.compiler.CeylonCompileTool;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class CompilerToolTest {
    
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);

    @Test
    public void testNoModules()  throws Exception {
        ToolModel<CeylonCompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument moduleOrFile should appear at least 1 time(s)", e.getMessage());
        }
    }
    
    @Test
    public void testCompile()  throws Exception {
        ToolModel<CeylonCompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        CeylonCompileTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testCompileVerbose()  throws Exception {
        ToolModel<CeylonCompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        CeylonCompileTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--verbose", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    
}
