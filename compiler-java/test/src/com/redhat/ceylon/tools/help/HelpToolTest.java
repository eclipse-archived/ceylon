package com.redhat.ceylon.tools.help;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.help.CeylonHelpTool;

public class HelpToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final ToolFactory pluginFactory = new ToolFactory(apf);
    protected final ToolLoader pluginLoader = new CeylonToolLoader(apf, null);
    @Test
    public void testHelp() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertTrue(model.isPorcelain());
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelpExample() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("example"));
        tool.run();
    }
    
    @Test
    public void testHelpHelp() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("help"));
        tool.run();
    }
    
    @Test
    public void testHelpCompiler() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("compile"));
        tool.setToolLoader(pluginLoader);
        tool.run();
    }
    
    @Test
    public void testHelpDoc() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("doc"));
        tool.run();
    }
    
    @Test
    public void testHelpImportJar() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("import-jar"));
        tool.run();
    }
    
    @Test
    public void testHelpDocTool() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("doc-tool"));
        tool.run();
    }

}
