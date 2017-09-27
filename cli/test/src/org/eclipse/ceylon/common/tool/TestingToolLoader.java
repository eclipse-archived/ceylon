package org.eclipse.ceylon.common.tool;

import org.eclipse.ceylon.common.tool.ServiceToolLoader;
import org.eclipse.ceylon.common.tool.example.Workaround;

public class TestingToolLoader extends ServiceToolLoader {

    public TestingToolLoader() {
        super(Workaround.class);
    }

    @Override
    public String getToolName(String className) {
        return camelCaseToDashes(className.replaceAll("^(.*\\.)?Test(.*)Tool$", "$2"));
    }

}
