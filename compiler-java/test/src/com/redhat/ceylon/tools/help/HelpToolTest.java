package com.redhat.ceylon.tools.help;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.help.CeylonHelpTool;

public class HelpToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
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
    public void testHelpWibble() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("wibble"));
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
        
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--", "compile", "--javac="));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unexpected argument --javac=", e.getMessage());
        }
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
