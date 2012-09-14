package com.redhat.ceylon.tools;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import com.redhat.ceylon.common.tool.ToolException;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class TestingToolLoader extends CeylonToolLoader {
    
    private boolean includeParentServices;

    public TestingToolLoader(ClassLoader loader, boolean includeParentServices) {
        super(loader);
        this.includeParentServices = includeParentServices;
    }

    @Override
    protected Enumeration<URL> getServiceMeta() {
        Vector<URL> result = new Vector<>();
        if (includeParentServices) {
            Enumeration<URL> resources = super.getServiceMeta();
            while (resources.hasMoreElements()) {
                result.add(resources.nextElement());
            }
        }
        
        try {
            Enumeration<URL> resources = loader.getResources(CeylonExampleTool.class.getName().replace(".", "/")+".properties");
            while (resources.hasMoreElements()) {
                result.add(resources.nextElement());
            }
        } catch (IOException e) {
            throw new ToolException(e);
        }
        
        return result.elements();
    }
}