package com.redhat.ceylon.common.tool;

import java.io.File;

import com.redhat.ceylon.common.tool.ToolLoader;
//import com.redhat.ceylon.common.tool.example.TestTool;

public class TestingToolLoader extends ToolLoader {

    public TestingToolLoader(ArgumentParserFactory argParserFactory,
            ClassLoader loader) {
        super(argParserFactory, loader);
    }

    public TestingToolLoader(ArgumentParserFactory argParserFactory) {
        super(argParserFactory);
    }

    @Override
    protected String getTopLevelToolClassName() {
        return null;//TestTool.class.getName();
    }

    @Override
    protected String getToolName(String className) {
        return camelCaseToDashes(className.replaceAll("^(.*\\.)?Test(.*)Tool$", "$2"));
    }

}
