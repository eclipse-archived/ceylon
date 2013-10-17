package com.redhat.ceylon.common.tool;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.OptionArgumentException.InvalidArgumentValueException;
import com.redhat.ceylon.common.tool.OptionArgumentException.InvalidOptionValueException;
import com.redhat.ceylon.common.tool.example.TestExampleTool;
import com.redhat.ceylon.common.tool.example.TestMinimumsTool;
import com.redhat.ceylon.common.tool.example.TestSubtoolTool;

public class ToolFactoryTest {
    
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new TestingToolLoader();
    
    @Test
    public void testLongOptionArgument() {
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

        // If a long option argument has a not optional argument then the
        // argument may come from the next argument
        tool = pluginFactory.bindArguments(model, Arrays.asList("--short-name", "foo"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertEquals("foo", tool.getShortName());
        Assert.assertTrue(tool.getListArgument() == null);
        Assert.assertTrue(tool.isInited());
        
    }
    
    @Test
    public void testLongOptionArgumentMissing() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Option '--short-name' to command 'example' should be followed by an argument", e.getMessage());
        }
    }
     
    @Test
    public void testShortOptionArgumentApart() {
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
    public void testShortOptionArgumentTogether() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("-btrue"));
        Assert.assertEquals("true", tool.getShortName());
        Assert.assertTrue(tool.isInited());
    }
    @Test
    public void testShortOptionArgumentMissing() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-b"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Option '-b' to command 'example' should be followed by an argument", e.getMessage());
        }
    }
    @Test
    public void testOptionArgumentTooMany() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-b1", "-b2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Option '-b' to command 'example' should appear at most 1 time(s)", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name=1", "-b2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option '--short-name'/'-b' to command 'example' should appear at most 1 time(s)"));
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--short-name=1", "--short-name=2"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().equals("Option '--short-name' to command 'example' should appear at most 1 time(s)"));
        }
    }
    
    @Test
    public void testLongOptionList() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--list-option=true", "--list-option=false"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getListOption());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testFileOption() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--file=foo"));
        Assert.assertEquals("foo", tool.getFile().getName());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testEnumOption() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--thread-state=NEW"));
        Assert.assertEquals(Thread.State.NEW, tool.getThreadState());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testArgumentList() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("true", "false"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertTrue(tool.getListOption() == null);
        Assert.assertEquals(Arrays.asList("true", "false"), tool.getListArgument());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testEoo() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        TestExampleTool tool = pluginFactory.bindArguments(model, Arrays.asList("--", "--list-option=true", "--", "-b", "true", "-btrue"));
        Assert.assertFalse(tool.isLongName());
        Assert.assertNull(tool.getShortName());
        Assert.assertTrue(tool.getListOption() == null);
        Assert.assertEquals(Arrays.asList("--list-option=true", "--", "-b", "true", "-btrue"), tool.getListArgument());
        Assert.assertTrue(tool.isInited());
    }
    
    @Test
    public void testMiniumums() {
        ToolModel<TestMinimumsTool> model = pluginLoader.loadToolModel("minimums");
        try {
            pluginFactory.bindArguments(model, Arrays.<String>asList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument 'value' to command 'minimums' should appear at least 3 time(s)", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("true", "false"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument 'value' to command 'minimums' should appear at least 3 time(s)", e.getMessage());
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
            Assert.assertEquals("Unrecognised short option '-l' to command 'example'", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-Fl"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised short option '-l' to command 'example' (in combined options '-Fl')", e.getMessage());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("-lalala"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised short option '-l' to command 'example' (in combined options '-lalala')", e.getMessage());
        }        
    }
    
    @Test
    public void testUnknownLongOption() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--lalala"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised long option '--lalala' to command 'example'", e.getMessage());
        }
    }
    
    @Test
    public void testUnknownLongOptionArgument() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--lalala=f"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised long option '--lalala=f' to command 'example'", e.getMessage());
        }
    }
    
    @Test
    public void testVerbose() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        
        TestExampleTool ex = pluginFactory.bindArguments(model, Arrays.asList("--verbose=foo"));
        Assert.assertEquals("foo", ex.getVerbose());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("--verbose="));
        Assert.assertEquals("", ex.getVerbose());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("--verbose"));
        Assert.assertEquals("", ex.getVerbose());   
    }
    
    @Test
    public void testVerbosities() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        
        TestExampleTool ex = pluginFactory.bindArguments(model, Arrays.asList("--verbosities=1"));
        Assert.assertEquals(Arrays.asList("1"), ex.getVerbosities());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("--verbosities="));
        Assert.assertEquals(Collections.singletonList(""), ex.getVerbosities());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("--verbosities"));
        Assert.assertEquals(Collections.<String>singletonList(null), ex.getVerbosities());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("--verbosities=1", "--verbosities=2", "--verbosities"));
        Assert.assertEquals(Arrays.asList("1", "2", null), ex.getVerbosities());
    }
    
    @Test
    public void testSubtool() {
        ToolModel<TestSubtoolTool> model = pluginLoader.loadToolModel("subtool");
        
        TestSubtoolTool ex = pluginFactory.bindArguments(model, Arrays.asList("subtool1"));
        Assert.assertEquals(TestSubtoolTool.Subtool1.class, ex.getAction().getClass());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("subtool1", "--foo"));
        Assert.assertEquals(TestSubtoolTool.Subtool1.class, ex.getAction().getClass());
        
        try {
            ex = pluginFactory.bindArguments(model, Arrays.asList("subtool1", "--bar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised long option '--bar' to command 'subtool1'", e.getMessage());
        }
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("subtool2"));
        Assert.assertEquals(TestSubtoolTool.Subtool2.class, ex.getAction().getClass());
        
        ex = pluginFactory.bindArguments(model, Arrays.asList("subtool2", "--bar"));
        Assert.assertEquals(TestSubtoolTool.Subtool2.class, ex.getAction().getClass());
        
        try {
            ex = pluginFactory.bindArguments(model, Arrays.asList("subtool2", "--foo"));
        Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unrecognised long option '--foo' to command 'subtool2'", e.getMessage());
        }
        
        try {
            ex = pluginFactory.bindArguments(model, Arrays.asList("subtool3"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            String message = e.getMessage();
            Assert.assertTrue(message, message.startsWith("Invalid value 'subtool3' given for argument 'action'"));
            //Assert.assertTrue(message, message.contains("subtool1"));
            //Assert.assertTrue(message, message.contains("subtool2"));
        }
    }
    
    @Test
    public void testInitThrows() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.Exception"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("java.lang.Exception", e.getCause().getClass().getName());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.RuntimeException"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
        }
    }
    
    @Test
    public void testOptionThrows() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.Exception", "--option-throw"));
            Assert.fail();
        } catch (InvalidOptionValueException e) {
            Assert.assertEquals("java.lang.Exception", e.getCause().getClass().getName());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.RuntimeException", "--option-throw"));
            Assert.fail();
        } catch (InvalidOptionValueException e) {
            Assert.assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
        }
    }
    
    @Test
    public void testOptionArgumentThrows() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.Exception", "--option-argument-throw=s"));
            Assert.fail();
        } catch (InvalidOptionValueException e) {
            Assert.assertEquals("java.lang.Exception", e.getCause().getClass().getName());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.RuntimeException", "--option-argument-throw=s"));
            Assert.fail();
        } catch (InvalidOptionValueException e) {
            Assert.assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
        }
    }
    
    @Test
    public void testArgumentThrows() {
        ToolModel<TestExampleTool> model = pluginLoader.loadToolModel("example");
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.Exception", "argument"));
            Assert.fail();
        } catch (InvalidArgumentValueException e) {
            Assert.assertEquals("java.lang.Exception", e.getCause().getClass().getName());
        }
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--throwable-class-name=java.lang.RuntimeException", "argument"));
            Assert.fail();
        } catch (InvalidArgumentValueException e) {
            Assert.assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
        }
    }

}
