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
