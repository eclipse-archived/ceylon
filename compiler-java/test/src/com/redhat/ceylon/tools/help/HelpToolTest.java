package com.redhat.ceylon.tools.help;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.ToolTest;
import com.redhat.ceylon.tools.help.HelpTool;

public class HelpToolTest extends ToolTest {

    @Test
    public void testHelp() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelpExample() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("example"));
        tool.run();
    }
    
    @Test
    public void testHelpHelp() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("help"));
        tool.run();
    }
    
    @Test
    public void testHelpCompiler() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("compile"));
        tool.setToolLoader(pluginLoader);
        tool.run();
    }
    
    @Test
    public void testHelpDoc() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("doc"));
        tool.run();
    }
    
    @Test
    public void testHelpImportJar() {
        PluginModel<HelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("import-jar"));
        tool.run();
    }

}
