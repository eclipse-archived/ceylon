package com.redhat.ceylon.common.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

public abstract class ServiceToolLoader extends ToolLoader {

    private final Class<?> serviceClass;
    private PathPlugins pathPlugins = new PathPlugins();
    private List<String> toolClassNames;
    
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
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
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
    protected synchronized Iterable<String> toolClassNames() {
        if (toolClassNames == null) {
            List<String> result = new ArrayList<>();
            Enumeration<URL> urls = getServiceMeta();
            while (urls.hasMoreElements()) {
                result.addAll(parseServiceInfo(urls.nextElement()));
            }
            result.addAll(getPathPlugins());
            toolClassNames = Collections.unmodifiableList(result);
        }
        return toolClassNames;
    }

    protected Set<String> getPathPlugins() {
        return pathPlugins.getPathPlugins();
    }

}
