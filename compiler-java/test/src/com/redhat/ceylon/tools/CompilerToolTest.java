package com.redhat.ceylon.tools;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.compiler.CompileTool;

public class CompilerToolTest {
    
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);

    @Test
    public void testCompileJhelp()  throws Exception {
        PluginModel<CompileTool> model = pluginLoader.loadToolModel("compile");
        Assert.assertNotNull(model);
        CompileTool tool = pluginFactory.bindArguments(model, Arrays.asList("-jhelp"));
        tool.run();
    }
    
}
