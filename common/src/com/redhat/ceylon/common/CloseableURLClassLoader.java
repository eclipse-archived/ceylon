package com.redhat.ceylon.common;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Collection;
import java.util.jar.JarFile;

/**
 * A URLClassLoader we can clean up. Otherwise it keeps files open.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class CloseableURLClassLoader extends URLClassLoader {

    public CloseableURLClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public CloseableURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public CloseableURLClassLoader(URL[] urls) {
        super(urls);
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
}
