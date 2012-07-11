package com.redhat.ceylon.tools;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.CompileTool;
import com.redhat.ceylon.tools.example.ExampleTool;
import com.redhat.ceylon.tools.importjar.ImportJarTool;

public class ToolBuilderTest {

    private final ArgumentParserFactory apf = new ArgumentParserFactory();
    private PluginFactory tb = new PluginFactory(apf);
    private PluginLoader tl = new PluginLoader(apf);
    
    @Test
    public void testLongOptionArgument() throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("--foo=true"));
        Assert.assertTrue(tool.isFoo());
        Assert.assertNull(tool.getBar());
        Assert.assertTrue(tool.getBazes() == null);
        Assert.assertTrue(tool.isInited());
        
        tool = tb.bindArguments(model, Arrays.asList("--bar=false"));
        Assert.assertFalse(tool.isFoo());
        Assert.assertEquals("false", tool.getBar());
        Assert.assertTrue(tool.getBazes() == null);
        Assert.assertTrue(tool.isInited());
    }
     
    @Test
    public void testShortOptionArgumentApart() throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("-b", "true"));
        Assert.assertEquals("true", tool.getBar());
        Assert.assertTrue(tool.getBazes() == null);
        Assert.assertTrue(tool.isInited());
        
        tool = tb.bindArguments(model, Arrays.asList("-b", "false"));
        Assert.assertEquals("false", tool.getBar());
        Assert.assertTrue(tool.getBazes() == null);
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testShortOptionArgumentTogether() throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("-btrue"));
        Assert.assertEquals("true", tool.getBar());
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testShortOptionArgumentMissing() throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        try {
            tb.bindArguments(model, Arrays.asList("-b"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            
        }
    }
    
    @Test
    public void testLongOptionList()  throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("--bars=true", "--bars=false"));
        Assert.assertFalse(tool.isFoo());
        Assert.assertNull(tool.getBar());
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getBars());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testArgumentList()  throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("true", "false"));
        Assert.assertFalse(tool.isFoo());
        Assert.assertNull(tool.getBar());
        Assert.assertTrue(tool.getBars() == null);
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getBazes());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testEoo()  throws InvocationTargetException {
        PluginModel<ExampleTool> model = tl.loadToolModel("example");
        ExampleTool tool = tb.bindArguments(model, Arrays.asList("--", "--bars=true", "--", "-b", "true", "-btrue"));
        Assert.assertFalse(tool.isFoo());
        Assert.assertNull(tool.getBar());
        Assert.assertTrue(tool.getBars() == null);
        Assert.assertEquals(Arrays.asList("--bars=true", "--", "-b", "true", "-btrue"), tool.getBazes());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testRoot()  throws Exception {
        PluginModel<Tool> model = tl.loadToolModel("");
        Assert.assertNotNull(model);
        Tool tool = tb.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelp()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelpExample()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("example"));
        tool.run();
    }
    
    @Test
    public void testHelpHelp()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("help"));
        tool.run();
    }
    
    @Test
    public void testHelpCompiler()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("compile"));
        tool.setToolLoader(tl);
        tool.run();
    }
    
    @Test
    public void testHelpDoc()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("doc"));
        tool.run();
    }
    
    @Test
    public void testHelpImportJar()  throws Exception {
        PluginModel<HelpTool> model = tl.loadToolModel("help");
        Assert.assertNotNull(model);
        HelpTool tool = tb.bindArguments(model, Arrays.asList("import-jar"));
        tool.run();
    }
    
    @Test
    public void testCompileJhelp()  throws Exception {
        PluginModel<CompileTool> model = tl.loadToolModel("compile");
        Assert.assertNotNull(model);
        CompileTool tool = tb.bindArguments(model, Arrays.asList("-jhelp"));
        tool.run();
    }
}
