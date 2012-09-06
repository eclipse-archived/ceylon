package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.example.TestExampleTool;
import com.redhat.ceylon.common.tool.example.TestMinimumsTool;

public class ToolFactoryTest {
    
    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final ToolFactory pluginFactory = new ToolFactory(apf);
    protected final ToolLoader pluginLoader = new TestingToolLoader(apf);
    
    @Test
    public void testLongOptionArgument() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--long-name=true"));
        Assert.assertTrue(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertTrue(tool.getListArgument() == null);
        Assert.assertTrue(tool.isInited());
        
        tool = pluginFactory.bindArguments(model, Arrays.asList("--short-name=false"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertEquals("false", tool.getShortName());
        Assert.assertTrue(tool.getListArgument() == null);
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testLongOptionArgumentMissing() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option --short-name should be followed by an argument"));
        }
    }
     
    @Test
    public void testShortOptionArgumentApart() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("-b", "true"));
        Assert.assertEquals("true", tool.getShortName());
        Assert.assertTrue(tool.getListArgument() == null);
        Assert.assertTrue(tool.isInited());
        
        tool = pluginFactory.bindArguments(model, Arrays.asList("-b", "false"));
        Assert.assertEquals("false", tool.getShortName());
        Assert.assertTrue(tool.getListArgument() == null);
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testShortOptionArgumentTogether() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("-btrue"));
        Assert.assertEquals("true", tool.getShortName());
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testShortOptionArgumentMissing() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-b"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option -b should be followed by an argument"));
        }
    }
    @Test
    public void testOptionArgumentTooMany() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-b1", "-b2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option --short-name/-b should appear at most 1 time(s)"));
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name=1", "-b2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option --short-name/-b should appear at most 1 time(s)"));
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name=1", "--short-name=2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option --short-name/-b should appear at most 1 time(s)"));
        }
    }
    
    @Test
    public void testLongOptionList()  throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--list-option=true", "--list-option=false"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getListOption());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testFileOption() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--file=foo"));
        Assert.assertEquals("foo", tool.getFile().getName());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testEnumOption() throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--thread-state=NEW"));
        Assert.assertEquals(Thread.State.NEW, tool.getThreadState());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testArgumentList()  throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("true", "false"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertTrue(tool.getListOption() == null);
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getListArgument());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testEoo()  throws InvocationTargetException {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--", "--list-option=true", "--", "-b", "true", "-btrue"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertTrue(tool.getListOption() == null);
        Assert.assertEquals(Arrays.asList("--list-option=true", "--", "-b", "true", "-btrue"), tool.getListArgument());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testMiniumums()  throws InvocationTargetException {
        ToolModel<TestMinimumsTool> model = pluginLoader.loadToolModel("minimums");
        try {
            pluginFactory.bindArguments(model, Arrays.<String>asList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument value should appear at least 3 time(s)", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("true", "false"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument value should appear at least 3 time(s)", e.getMessage());
        }
        pluginFactory.bindArguments(model, Arrays.asList("true", "false", "3"));
        
    }
    
    @Test
    public void testUnknownShortOption() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-l"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised option(s): -l", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-Fl"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised option(s): l (in -Fl)", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-lalala"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised option(s): l (in -lalala)", e.getMessage());
        }        
    }
    
    @Test
    public void testUnknownLongOption() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--lalala"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised option(s): --lalala", e.getMessage());
        }
    }
    
    @Test
    public void testUnknownLongOptionArgument() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--lalala=f"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised option(s): --lalala=f", e.getMessage());
        }
    }
}
