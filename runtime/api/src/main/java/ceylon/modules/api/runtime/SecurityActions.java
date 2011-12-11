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

package ceylon.modules.api.runtime;

import ceylon.language.descriptor.Module;

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
     * Get module.
     *
     * @param moduleClass the module class
     * @return module instance
     * @throws Exception for any error
     */
    static Module getModule(final Class<?> moduleClass) throws Exception {
        return AccessController.doPrivileged(new PrivilegedExceptionAction<Module>() {
            @Override
            public Module run() throws Exception {
                final Method getModule = moduleClass.getDeclaredMethod("getModule");
                getModule.setAccessible(true);
                return (Module) getModule.invoke(null); // it should be a static method
            }
        });
    }

    /**
     * Invoke run::main.
     *
     * @param runClass the run class
     * @param args     the args
     * @throws Exception for any error
     */
    static void invokeRun(final Class<?> runClass, final String[] args) throws Exception {
        AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            @SuppressWarnings("UnnecessaryLocalVariable")
            @Override
            public Object run() throws Exception {
                final Method main = runClass.getDeclaredMethod("main", String[].class);
                main.setAccessible(true);
                final Object sfa = args;
                main.invoke(null, sfa);
                return null;
            }
        });
    }
}
