package com.redhat.ceylon.compiler.js;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;

public class CompileJsToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);

    private List<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    @Test
    public void testLoad() {
        PluginModel<CompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        pluginFactory.bindArguments(tool, args());
    }
    
}
