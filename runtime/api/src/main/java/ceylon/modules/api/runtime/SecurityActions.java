/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
