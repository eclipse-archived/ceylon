/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package ceylon.modules.bootstrap.loader;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Security actions.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class SecurityActions {
    static String getProperty(final String key) {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty(key);
            }
        });
    }

    static String getProperty(final String key, final String defaultValue) {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty(key, defaultValue);
            }
        });
    }

    static boolean getBoolean(final String key) {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                return Boolean.getBoolean(key);
            }
        });
    }

    static Method findModule() {
        return AccessController.doPrivileged(new PrivilegedAction<Method>() {
            public Method run() {
                try {
                    final Method declaredMethod = ModuleLoader.class.getDeclaredMethod("findModule", ModuleIdentifier.class);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
