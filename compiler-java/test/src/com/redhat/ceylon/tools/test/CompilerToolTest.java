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
import com.redhat.ceylon.compiler.CompileTool;

public class CompilerToolTest {
    
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);

    @Test
    public void testNoModules()  throws Exception {
        PluginModel<CompileTool> model = pluginLoader.loadToolModel("compile");
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
        PluginModel<CompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        CompileTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testCompileVerbose()  throws Exception {
        PluginModel<CompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        CompileTool tool = pluginFactory.bindArguments(model, 
                Arrays.asList("--verbose", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    
}
