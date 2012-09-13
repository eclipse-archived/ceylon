package com.redhat.ceylon.common.tool;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class TestingToolLoader extends ToolLoader {

    public TestingToolLoader(ClassLoader loader) {
        super(loader);
    }

    public TestingToolLoader() {
        super();
    }

    @Override
    protected String getToolName(String className) {
        return camelCaseToDashes(className.replaceAll("^(.*\\.)?Test(.*)Tool$", "$2"));
    }
    
    @Override
    protected Enumeration<URL> getServiceMeta() {
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("META-INF/services/"+Tool.class.getName()+"-test");
        } catch (IOException e) {
            throw new ToolException(e);
        }
        return resources;
    }

}
