package com.redhat.ceylon.tools.version;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class CeylonVersionToolTest {
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    @Test
    public void testHelp() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList("--set", "3.0", "ceylon.collection"));
        tool.setSourceFolders(Collections.singletonList(new File("../ceylon-sdk/source")));
        tool.run();
    }

    
}
