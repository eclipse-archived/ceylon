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

package ceylon.modules.api.runtime;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * Security actions.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class SecurityActions {

    /**
     * Invoke run::main.
     *
     * @param runClass the run class
     * @param args     the args
     * @throws Exception for any error
     */
    static void invokeRun(final Class<?> runClass, final String[] args) throws Exception {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    invokeRunInternal(runClass, args);
                    return null;
                }
            });
        } else {
            invokeRunInternal(runClass, args);
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private static void invokeRunInternal(final Class<?> runClass, final String[] args) throws Exception {
        try {
            final Method main = runClass.getDeclaredMethod("main", String[].class);
            main.setAccessible(true);
            final Object sfa = args;
            main.invoke(null, sfa);
        } catch (NoSuchMethodException ex) {
            // Errors about a missing main() will confuse users, so we throw our own version
            throw new NoSuchMethodException(runClass.getName() + "(Void)");
        }
    }

    public static ClassLoader setContextClassLoader(final ClassLoader cl) throws Exception {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                public Object run() throws Exception {
                    ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                    Thread.currentThread().setContextClassLoader(cl);
                    return oldClassLoader;
                }
            });
        } else {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(cl);
            return oldClassLoader;
        }
    }
}
