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
import ceylon.modules.api.util.ModuleVersion;
import ceylon.modules.spi.Constants;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

/**
 * Abstract Ceylon Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRuntime implements ceylon.modules.spi.runtime.Runtime {

    public static final String MODULE_INFO_CLASS = ".module";
    public static final String RUN_INFO_CLASS = ".run";

    /**
     * Load module instance.
     *
     * @param cl         the classloader used to load the module descriptor.
     * @param moduleName the module name
     * @return new module instance or null if no such descriptor
     * @throws Exception for any error
     */
    public static Module loadModule(ClassLoader cl, String moduleName) throws Exception {
        final String moduleClassName = moduleName + MODULE_INFO_CLASS;
        final Class<?> moduleClass;
        try {
            moduleClass = cl.loadClass(moduleClassName);
        } catch (ClassNotFoundException ignored) {
            return null; // looks like no such module class is available
        }

        return AccessController.doPrivileged(new PrivilegedExceptionAction<Module>() {
            @Override
            public Module run() throws Exception {
                final Method getModule = moduleClass.getDeclaredMethod("getModule");
                getModule.setAccessible(true);
                return (Module) getModule.invoke(null); // it should be a static method
            }
        });
    }

    protected static void invokeRun(ClassLoader cl, String moduleName, final String... args) throws Exception {
        final String runClassName = moduleName + RUN_INFO_CLASS;
        final Class<?> runClass;
        try {
            runClass = cl.loadClass(runClassName);
        } catch (ClassNotFoundException ignored) {
            return; // looks like no such run class is available
        }

        AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            @Override
            public Object run() throws Exception {
                final Method main = runClass.getDeclaredMethod("main", String[].class);
                main.setAccessible(true);
                main.invoke(null, args);
                return null;
            }
        });
    }

    public void execute(Map<String, String> args) throws Exception {
        String exe = args.get(Constants.MODULE.toString());
        if (exe == null)
            throw new IllegalArgumentException("No initial module defined: " + args);

        int p = exe.indexOf("/");
        if (p == 0)
            throw new IllegalArgumentException("Missing runnable info: " + exe);
        if (p == exe.length() - 1)
            throw new IllegalArgumentException("Missing version info: " + exe);

        String name = exe.substring(0, p > 0 ? p : exe.length());
        ModuleVersion mv = (p > 0 ? ModuleVersion.parseVersion(exe.substring(p + 1)) : ModuleVersion.DEFAULT_VERSION);

        ClassLoader cl = createClassLoader(name, mv.toString(), args);
        Module runtimeModule = loadModule(cl, name);
        if (runtimeModule == null)
            throw new IllegalArgumentException("Something went very wrong, missing runtime module!"); // TODO -- dump some more useful msg

        invokeRun(cl, name, null); // TODO
    }
}
