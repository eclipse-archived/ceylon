package com.redhat.ceylon.common.tool;

import com.redhat.ceylon.common.tools.CeylonTool;

public class AbstractToolTest {
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new TestingToolLoader();

    protected CeylonTool getMainTool() {
        return pluginLoader.instance("", null);
    }
}
