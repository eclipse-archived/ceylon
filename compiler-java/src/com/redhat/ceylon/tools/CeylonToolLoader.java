package com.redhat.ceylon.tools;

import com.redhat.ceylon.common.tool.ServiceToolLoader;
import com.redhat.ceylon.common.tool.Tool;

public class CeylonToolLoader extends ServiceToolLoader {
    public CeylonToolLoader(ClassLoader loader) {
        super(loader, Tool.class);
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