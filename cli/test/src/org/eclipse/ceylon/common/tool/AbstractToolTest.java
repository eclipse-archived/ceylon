package org.eclipse.ceylon.common.tool;

import org.eclipse.ceylon.common.tool.ToolFactory;
import org.eclipse.ceylon.common.tool.ToolLoader;
import org.eclipse.ceylon.common.tools.CeylonTool;

public class AbstractToolTest {
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new TestingToolLoader();

    protected CeylonTool getMainTool() {
        return pluginLoader.instance("", null);
    }
}
