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
import ceylon.modules.Configuration;
import ceylon.modules.api.util.CeylonToJava;
import ceylon.modules.spi.Constants;
import com.redhat.ceylon.cmr.api.Repository;

import java.util.logging.Logger;

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

        return SecurityActions.getModule(moduleClass);
    }

    protected static void invokeRun(ClassLoader cl, String runClassName, final String[] args) throws Exception {
        final Class<?> runClass;
        try {
            runClass = cl.loadClass(runClassName);
        } catch (ClassNotFoundException ignored) {
            Logger.getLogger("ceylon.runtime").warning("No " + runClassName + " available, nothing to run!");
            return; // looks like no such run class is available
        }

        SecurityActions.invokeRun(runClass, args);
    }

    public void execute(Configuration conf) throws Exception {
        String exe = conf.module;
        // FIXME: argument checks could be done earlier
        if (exe == null)
            throw new IllegalArgumentException("No initial module defined");

        int p = exe.indexOf("/");
        if (p == 0)
            throw new IllegalArgumentException("Missing runnable info: " + exe);
        if (p == exe.length() - 1)
            throw new IllegalArgumentException("Missing version info: " + exe);

        String name = exe.substring(0, p > 0 ? p : exe.length());
        String mv = (p > 0 ? exe.substring(p + 1) : Repository.NO_VERSION);

        ClassLoader cl = createClassLoader(name, mv, conf);
        Module runtimeModule = loadModule(cl, name);
        if (runtimeModule != null) {
            final String mn = CeylonToJava.toString(runtimeModule.getName());
            if (name.equals(mn) == false)
                throw new IllegalArgumentException("Input module name doesn't match module's name: " + name + " != " + mn);

            final String version = CeylonToJava.toString(runtimeModule.getVersion());
            if (mv.equals(version) == false && Constants.DEFAULT.toString().equals(name) == false)
                throw new IllegalArgumentException("Input module version doesn't match module's version: " + mv + " != " + version);
        } else if (Constants.DEFAULT.toString().equals(name) == false) {
            throw new IllegalArgumentException("Missing module.class info: " + name); // TODO -- dump some more useful msg
        }

        String runClassName = conf.run;
        if (runClassName == null || runClassName.isEmpty())
            runClassName = name + RUN_INFO_CLASS;
        invokeRun(cl, runClassName, conf.arguments);
    }
}
