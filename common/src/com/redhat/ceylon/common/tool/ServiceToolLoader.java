package com.redhat.ceylon.common.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class ServiceToolLoader extends ToolLoader {

    private final Class<?> serviceClass;
    
    public ServiceToolLoader(Class<?> serviceClass) {
        super();
        this.serviceClass = serviceClass;
    }
    
    public ServiceToolLoader(ClassLoader loader, Class<?> serviceClass) {
        super(loader);
        this.serviceClass = serviceClass;
    }

    protected Enumeration<URL> getServiceMeta() {
        /* Use the same conventions as java.util.ServiceLoader but without 
         * requiring us to load the Service classes
         */
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("META-INF/services/"+serviceClass.getName());
        } catch (IOException e) {
            throw new ToolException(e);
        }
        return resources;
    }
    
    private List<String> parseServiceInfo(final URL url) {
        List<String> result = new ArrayList<>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            try {
                String className = reader.readLine();
                while (className != null) {
                    className = className.trim().replaceAll("#.*", "");
                    if (!className.isEmpty()) {
                        result.add(className);    
                    }
                    className = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new ToolException("Error reading service file " + url, e);
        }
        return result;
    }

    @Override
    protected Iterable<String> toolClassNames() {
        List<String> result = new ArrayList<>();
        Enumeration<URL> urls = getServiceMeta();
        while (urls.hasMoreElements()) {
            result.addAll(parseServiceInfo(urls.nextElement()));
        }
        return result;
    }
}
