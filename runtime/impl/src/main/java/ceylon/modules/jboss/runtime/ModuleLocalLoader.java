/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

    @SuppressWarnings({"unchecked"})
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
