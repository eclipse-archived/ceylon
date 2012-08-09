package com.redhat.ceylon.tools;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

public class HelpToolTest extends ToolTest {

    @Test
    public void testHelp() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelpExample() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("example"));
        tool.run();
    }
    
    @Test
    public void testHelpHelp() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("help"));
        tool.run();
    }
    
    @Test
    public void testHelpCompiler() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("compile"));
        tool.setToolLoader(tl);
        tool.run();
    }
    
    @Test
    public void testHelpDoc() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("doc"));
        tool.run();
    }
    
    @Test
    public void testHelpImportJar() {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("import-jar"));
        tool.run();
    }

}
