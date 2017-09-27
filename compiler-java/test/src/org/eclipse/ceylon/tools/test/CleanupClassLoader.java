package org.eclipse.ceylon.tools.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.jar.JarFile;

public class CleanupClassLoader extends URLClassLoader {

    public CleanupClassLoader(URL[] urls) {
        super(urls);
    }

    public CleanupClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /**
     * Cleans up any resource associated with this class loader. This class loader will not be usable after calling this
     * method, so any code using it to run better not be running anymore.
     */
    public void clearCache() {
        try {
            Class<?> klass = java.net.URLClassLoader.class;
            Field ucp = klass.getDeclaredField("ucp");
            ucp.setAccessible(true);
            Object sunMiscURLClassPath = ucp.get(this);
            Field loaders = sunMiscURLClassPath.getClass().getDeclaredField("loaders");
            loaders.setAccessible(true);
            Object collection = loaders.get(sunMiscURLClassPath);
            for (Object sunMiscURLClassPathJarLoader : ((Collection<?>) collection).toArray()) {
                try {
                    Field loader = sunMiscURLClassPathJarLoader.getClass().getDeclaredField("jar");
                    loader.setAccessible(true);
                    Object jarFile = loader.get(sunMiscURLClassPathJarLoader);
                    ((JarFile) jarFile).close();
                } catch (Throwable t) {
                    // not a JAR loader?
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            // Something's wrong
            t.printStackTrace();
        }
        return;
    }

    @Override
    public void close() throws IOException {
        super.close();
        clearCache();
    }
}
