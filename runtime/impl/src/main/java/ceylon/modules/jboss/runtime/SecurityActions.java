/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.jboss.runtime;

import org.jboss.modules.Module;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 * Security actions.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SecurityActions {
    /**
     * Get classloader from a module.
     *
     * @param module the current module
     * @return module's classloader
     */
    static ClassLoader getClassLoader(final Module module) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                public ClassLoader run() {
                    return module.getClassLoader();
                }
            });
        } else {
            return module.getClassLoader();
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
