package com.redhat.ceylon.common.tool;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.example.CeylonExampleTool;

public class ToolLoaderTest {

    @Test
    public void testExampleTool() throws Exception {
        final ToolModel<CeylonExampleTool> model = new ToolLoader(new ArgumentParserFactory()).loadToolModel("example");
        Assert.assertNotNull(model);
        Assert.assertEquals("example", model.getName());
        Assert.assertEquals(1, model.getPostConstruct().size());
        Assert.assertEquals(CeylonExampleTool.class.getMethod("init"), model.getPostConstruct().get(0));
        
        final OptionModel longOptionModel = model.getOption("long-name");
        Assert.assertEquals("long-name", longOptionModel.getLongName());
        Assert.assertEquals(CeylonExampleTool.class.getMethod("setLongName", boolean.class), longOptionModel.getArgument().getSetter());
        Assert.assertEquals(longOptionModel, model.getOptionByShort('F'));
        Assert.assertEquals(null, longOptionModel.getArgument().getName());
        Assert.assertEquals(true, longOptionModel.isPureOption());
        Assert.assertEquals(0, longOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, longOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel shortOptionModel = model.getOption("short-name");
        Assert.assertEquals("short-name", shortOptionModel.getLongName());
        Assert.assertEquals(CeylonExampleTool.class.getMethod("setShortName", String.class), shortOptionModel.getArgument().getSetter());
        Assert.assertEquals(shortOptionModel, model.getOptionByShort('b'));
        Assert.assertEquals("value", shortOptionModel.getArgument().getName());
        Assert.assertEquals(false, shortOptionModel.isPureOption());
        Assert.assertEquals(0, shortOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, shortOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel listOptionModel = model.getOption("list-option");
        Assert.assertEquals("list-option", listOptionModel.getLongName());
        Assert.assertEquals(null, listOptionModel.getShortName());
        Assert.assertEquals("bars", listOptionModel.getArgument().getName());
        Assert.assertEquals(false, listOptionModel.isPureOption());
        Assert.assertEquals(0, listOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(Integer.MAX_VALUE, listOptionModel.getArgument().getMultiplicity().getMax());
        
        final OptionModel pureOptionModel = model.getOption("pure-option");
        Assert.assertEquals("pure-option", pureOptionModel.getLongName());
        Assert.assertEquals(null, pureOptionModel.getShortName());
        Assert.assertEquals(null, pureOptionModel.getArgument().getName());
        Assert.assertEquals(true, pureOptionModel.isPureOption());
        Assert.assertEquals(0, pureOptionModel.getArgument().getMultiplicity().getMin());
        Assert.assertEquals(1, pureOptionModel.getArgument().getMultiplicity().getMax());
        
        Assert.assertEquals(1, model.getArguments().size());
        
        final ArgumentModel bazModel = model.getArguments().get(0);
        Assert.assertEquals(String.class, bazModel.getType());
        
    }

}
