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

    static String getProperty(final String key, final String defaultValue) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty(key, defaultValue);
                }
            });
        } else {
            return System.getProperty(key, defaultValue);
        }
    }

    static boolean getBoolean(final String key) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                public Boolean run() {
                    return Boolean.getBoolean(key);
                }
            });
        } else {
            return Boolean.getBoolean(key);
        }
    }

    static Method findModule() {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<Method>() {
                public Method run() {
                    return findModuleInternal();
                }
            });
        } else {
            return findModuleInternal();
        }
    }

    private static Method findModuleInternal() throws RuntimeException {
        try {
            final Method declaredMethod = ModuleLoader.class.getDeclaredMethod("findModule", ModuleIdentifier.class);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
