package com.redhat.ceylon.tools;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.ToolLoader;

public class CeylonToolLoader extends ToolLoader {
    public CeylonToolLoader(ArgumentParserFactory argParserFactory,
            ClassLoader loader) {
        super(argParserFactory, loader);
    }

    @Override
    protected String getToolClassName(String toolName) {
        if (toolName.isEmpty()) {
            return CeylonTool.class.getName();
        } else {
            return super.getToolClassName(toolName);
        }
    }

    @Override
    protected String getToolName(String className) {
        String toolName = className.replaceAll("^.*\\.", "").replaceAll("^Ceylon(.*)Tool$", "$1");
        return camelCaseToDashes(toolName);
    }
}