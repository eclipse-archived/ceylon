/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ceylon.modules.jboss.runtime;

import org.jboss.modules.LocalLoader;
import org.jboss.modules.Module;
import org.jboss.modules.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Module local loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class ModuleLocalLoader implements LocalLoader {

    private static final Method getPackage;
    private final Module module;

    static {
        getPackage = AccessController.doPrivileged(new PrivilegedAction<Method>() {
            public Method run() {
                for (Method method : ClassLoader.class.getDeclaredMethods()) {
                    if (method.getName().equals("getPackage")) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                            method.setAccessible(true);
                            return method;
                        }
                    }
                }
                throw new IllegalStateException("No getPackage method found on ClassLoader");
            }
        });
    }

    ModuleLocalLoader(Module module) {
        this.module = module;
    }

    public Class<?> loadClassLocal(String name, boolean resolve) {
        try {
            return module.getClassLoader().loadClass(name, resolve);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public List<Resource> loadResourceLocal(final String name) {
        final Enumeration<URL> urls = module.getExportedResources(name);
        final List<Resource> list = new ArrayList<Resource>();
        while (urls.hasMoreElements())
            list.add(new URLResource(urls.nextElement()));
        return list;
    }

    @Override
    public Package loadPackageLocal(String name) {
        try {
            return (Package) getPackage.invoke(module.getClassLoader(), name);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (InvocationTargetException e) {
            try {
                throw e.getCause();
            } catch (RuntimeException re) {
                throw re;
            } catch (Error er) {
                throw er;
            } catch (Throwable throwable) {
                throw new UndeclaredThrowableException(throwable);
            }
        }
    }

    final static class URLResource implements Resource {
        private final URL url;

        public URLResource(final URL url) {
            this.url = url;
        }

        public String getName() {
            return url.getPath();
        }

        public URL getURL() {
            return url;
        }

        public InputStream openStream() throws IOException {
            return url.openStream();
        }

        public long getSize() {
            return 0L;
        }
    }
}
