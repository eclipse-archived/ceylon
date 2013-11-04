package com.redhat.ceylon.common.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.OSUtil;

public abstract class ServiceToolLoader extends ToolLoader {

    private final Class<?> serviceClass;
    private Set<String> pathPlugins;
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
        if(pathPlugins == null){
            pathPlugins = new TreeSet<String>();
            findPathPlugins();
        }
        return pathPlugins;
    }

    private void findPathPlugins() {
        Set<String> names = new HashSet<String>();
        // First the ones from CEYLON_HOME/bin
        File ceylonHome = FileUtil.getInstallDir();
        if(ceylonHome != null)
            findPluginInPath(new File(ceylonHome, "bin"), names);
        // Then look in ~/.ceylon/bin
        File defUserDir = new File(FileUtil.getDefaultUserDir(), "bin");
        findPluginInPath(defUserDir, names);
        // And finally in the user's PATH
        File[] paths = FileUtil.getExecPath();
        for (File part : paths) {
            findPluginInPath(part, names);
        }
    }

    private void findPluginInPath(File dir, final Set<String> names) {
        if(dir.isDirectory() && dir.canRead()){
            File[] matches = dir.listFiles(new FileFilter(){
                @Override
                public boolean accept(File f) {
                    if(f.isFile() && f.canExecute() && f.getName().toLowerCase().startsWith("ceylon-")){
                        String name = f.getName().substring(7);
                        if(OSUtil.isWindows()){
                            // script must end with ".bat"
                            if(!name.toLowerCase().endsWith(".bat"))
                                return false;
                            // strip it
                            name = name.substring(0, name.length()-4);
                        }
                        // refuse any name with dots in there (like ceylon-completion.bash)
                        if(name.indexOf('.') != -1)
                            return false;
                        // also refuse ceylon-sh-setup
                        if(name.equalsIgnoreCase("sh-setup"))
                            return false;
                        // we're good if it's unique
                        return names.add(name);
                    }
                    return false;
                }
            });
            for(File sub : matches){
                String name = SCRIPT_PREFIX+sub.getAbsolutePath();
                pathPlugins.add(name);
            }
        }
    }
}
