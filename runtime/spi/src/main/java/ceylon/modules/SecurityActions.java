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

package ceylon.modules;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * Security actions.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class SecurityActions {
    static String getProperty(final String key) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(key);
                }
            });
        } else {
            return System.getProperty(key);
        }
    }

    static void exit(final int status) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    System.exit(status);
                    return null;
                }
            });
        } else {
            System.exit(status);
        }
    }

    static <T> T instantiate(final Class<T> expectedType, final String defaultImpl) throws Exception {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {
                public T run() throws Exception {
                    return instantiateInternal(expectedType, defaultImpl);
                }
            });
        } else {
            return instantiateInternal(expectedType, defaultImpl);
        }
    }

    private static <T> T instantiateInternal(final Class<T> expectedType, final String defaultImpl) throws Exception {
        final String impl = System.getProperty(expectedType.getName(), defaultImpl);
        final ClassLoader cl = expectedType.getClassLoader();
        final Class<?> clazz = cl.loadClass(impl);
        final Object result = clazz.newInstance();
        return expectedType.cast(result);
    }
}
