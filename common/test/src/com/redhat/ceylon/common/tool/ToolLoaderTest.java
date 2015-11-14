package com.redhat.ceylon.common.tool;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;
import com.redhat.ceylon.common.tool.example.TestExampleTool;

public class ToolLoaderTest {

    @Test
    public void testExampleTool() throws Exception {
        final ToolModel<TestExampleTool> model = new TestingToolLoader().loadToolModel("example");
        Assert.assertNotNull(model);
        Assert.assertEquals("example", model.getName());
        
        final OptionModel<?> longOptionModel = model.getOption("long-name");
        Assert.assertEquals("long-name", longOptionModel.getLongName());
        Assert.assertEquals(TestExampleTool.class.getMethod("setLongName", boolean.class), longOptionModel.getArgument().getSetter());
        Assert.assertEquals(longOptionModel, model.getOptionByShort('F'));
        Assert.assertEquals(null, longOptionModel.getArgument().getName());
        Assert.assertEquals(ArgumentType.NOT_ALLOWED, longOptionModel.getArgumentType());
        Assert.assertEquals(0, longOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, longOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel<?> shortOptionModel = model.getOption("short-name");
        Assert.assertEquals("short-name", shortOptionModel.getLongName());
        Assert.assertEquals(TestExampleTool.class.getMethod("setShortName", String.class), shortOptionModel.getArgument().getSetter());
        Assert.assertEquals(shortOptionModel, model.getOptionByShort('b'));
        Assert.assertEquals("value", shortOptionModel.getArgument().getName());
        Assert.assertEquals(ArgumentType.REQUIRED, shortOptionModel.getArgumentType());
        Assert.assertEquals(0, shortOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, shortOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel<?> listOptionModel = model.getOption("list-option");
        Assert.assertEquals("list-option", listOptionModel.getLongName());
        Assert.assertEquals(null, listOptionModel.getShortName());
        Assert.assertEquals("bars", listOptionModel.getArgument().getName());
        Assert.assertEquals(ArgumentType.REQUIRED, listOptionModel.getArgumentType());
        Assert.assertEquals(0, listOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(Integer.MAX_VALUE, listOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel<?> pureOptionModel = model.getOption("pure-option");
        Assert.assertEquals("pure-option", pureOptionModel.getLongName());
        Assert.assertEquals(null, pureOptionModel.getShortName());
        Assert.assertEquals(null, pureOptionModel.getArgument().getName());
        Assert.assertEquals(ArgumentType.NOT_ALLOWED, pureOptionModel.getArgumentType());
        Assert.assertEquals(0, pureOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, pureOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel<?> verboseModel = model.getOption("verbose");
        Assert.assertEquals("verbose", verboseModel.getLongName());
        Assert.assertEquals(null, verboseModel.getShortName());
        Assert.assertEquals("keys", verboseModel.getArgument().getName());
        Assert.assertEquals(ArgumentType.OPTIONAL, verboseModel.getArgumentType());
        Assert.assertEquals(0, verboseModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, verboseModel.getArgument().getMultiplicity().getMax());
        
        Assert.assertEquals(1, model.getArguments().size());
        
        final ArgumentModel<?> bazModel = model.getArguments().get(0);
        Assert.assertEquals(String.class, bazModel.getType());
        
    }

}
