package com.redhat.ceylon.tools;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.tools.example.ExampleTool;

public class ModelBuilderTest {

    @Test
    public void testExampleTool() throws Exception {
        final PluginModel<ExampleTool> model = new PluginLoader(new ArgumentParserFactory()).loadToolModel("example");
        Assert.assertNotNull(model);
        Assert.assertEquals("example", model.getName());
        Assert.assertEquals(1, model.getPostConstruct().size());
        Assert.assertEquals(ExampleTool.class.getMethod("init"), model.getPostConstruct().get(0));
        
        final OptionModel fooModel = model.getOption("foo");
        Assert.assertEquals("foo", fooModel.getLongName());
        Assert.assertEquals(ExampleTool.class.getMethod("setFoo", boolean.class), fooModel.getArgument().getSetter());
        Assert.assertEquals(fooModel, model.getOptionByShort('F'));
        
        final OptionModel barModel = model.getOption("bar");
        Assert.assertEquals("bar", barModel.getLongName());
        Assert.assertEquals(ExampleTool.class.getMethod("setBar", String.class), barModel.getArgument().getSetter());
        Assert.assertEquals(barModel, model.getOptionByShort('b'));
        
        Assert.assertEquals(1, model.getArguments().size());
        
        final ArgumentModel bazModel = model.getArguments().get(0);
        Assert.assertEquals(String.class, bazModel.getType());
        
    }

}
