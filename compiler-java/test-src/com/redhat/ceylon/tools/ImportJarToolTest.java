package com.redhat.ceylon.tools;

import java.util.Collections;

import org.junit.Test;

import com.redhat.ceylon.tools.importjar.ImportJarTool;

import junit.framework.Assert;

public class ImportJarToolTest extends ToolTest {

    @Test
    public void testNoArgs() {
        PluginModel<ImportJarTool> model = tl.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            ImportJarTool tool = tb.bindArguments(model, Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testJarOnly() {
        PluginModel<ImportJarTool> model = tl.loadToolModel("import-jar");
        Assert.assertNotNull(model);
        try {
            ImportJarTool tool = tb.bindArguments(model, Collections.<String>singletonList("my.jar"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }

}
