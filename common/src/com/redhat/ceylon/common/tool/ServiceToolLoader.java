package com.redhat.ceylon.common.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class ServiceToolLoader extends ToolLoader {

    private final Class<?> serviceClass;
    private Set<String> pathPlugins;
    
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
        result.addAll(getPathPlugins());
        return result;
    }

    private Set<String> getPathPlugins() {
        if(pathPlugins == null){
            pathPlugins = new TreeSet<String>();
            findPathPlugins();
        }
        return pathPlugins;
    }

    private void findPathPlugins() {
        String path = System.getenv("PATH");
        if(path == null || path.isEmpty()){
            return;
        }
        int sep;
        while((sep = path.indexOf(File.pathSeparatorChar)) != -1){
            String part = path.substring(0, sep);
            findPluginInPath(part);
            path = path.substring(sep+1);
        }
        findPluginInPath(path);
    }

    private void findPluginInPath(String path) {
        File file = new File(path);
        if(file.isDirectory() && file.canRead()){
            File[] matches = file.listFiles(new FileFilter(){
                @Override
                public boolean accept(File f) {
                    if(f.isFile() && f.canExecute() && f.getName().startsWith("ceylon-")){
                        String name = f.getName().substring(7);
                        if(IS_WINDOWS){
                            // script must end with ".bat"
                            if(!name.endsWith(".bat"))
                                return false;
                            // srip it
                            name = name.substring(0, name.length()-4);
                        }
                        // refuse any name with dots in there (like ceylon-completion.bash)
                        if(name.indexOf('.') != -1)
                            return false;
                        // also refuse ceylon-sh-setup
                        if(name.equals("ceylon-sh-setup"))
                            return false;
                        // we're good
                        return true;
                    }
                    return false;
                }
            });
            for(File sub : matches){
                String name = "PATH:"+sub.getName();
                pathPlugins.add(name);
            }
        }
    }
}
