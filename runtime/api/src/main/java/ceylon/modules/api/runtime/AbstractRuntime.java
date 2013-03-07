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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

import ceylon.modules.CeylonRuntimeException;
import ceylon.modules.Configuration;
import ceylon.modules.spi.Constants;
import com.redhat.ceylon.compiler.java.metadata.Module;

/**
 * Abstract Ceylon Modules runtime.
 * Useful for potential extension.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRuntime implements ceylon.modules.spi.runtime.Runtime {

    public static final String MODULE_INFO_CLASS = ".module_";
    public static final String RUN_INFO_CLASS = "run";

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
        try {
            Class<?> klass = cl.loadClass(moduleClassName);
            return klass.getAnnotation(Module.class);
        } catch (ClassNotFoundException ignored) {
            return null; // looks like no such module class is available
        }

    }

    protected Module readModule(String name, File moduleFile) throws Exception {
        final URL url = moduleFile.toURI().toURL();
        final ClassLoader parent = getClass().getClassLoader();
        final ClassLoader cl = new URLClassLoader(new URL[]{url}, parent);
        return loadModule(cl, name);
    }

    protected static void invokeRun(ClassLoader cl, String runClassName, final String[] args) throws Exception {
        final Class<?> runClass;
        ClassLoader oldClassLoader = SecurityActions.setContextClassLoader(cl);
        try{
            try {
                char firstChar = runClassName.charAt(0);
                int lastDot = runClassName.lastIndexOf('.');
                if (lastDot > 0) {
                    firstChar = runClassName.charAt(lastDot + 1);
                }
                // we add _ to run class
                runClass = cl.loadClass(Character.isLowerCase(firstChar) ? runClassName + "_" : runClassName);
            } catch (ClassNotFoundException ignored) {
                Logger.getLogger("ceylon.runtime").severe("Could not find class or method '" + runClassName + "'");
                return; // looks like no such run class is available
            }

            SecurityActions.invokeRun(runClass, args);
        }finally{
        	SecurityActions.setContextClassLoader(oldClassLoader);
        }
    }

    public void execute(Configuration conf) throws Exception {
        String exe = conf.module;
        // FIXME: argument checks could be done earlier
        if (exe == null)
            throw new CeylonRuntimeException("No initial module defined");

        int p = exe.indexOf("/");
        if (p == 0)
            throw new CeylonRuntimeException("Missing runnable info: " + exe);
        if (p == exe.length() - 1)
            throw new CeylonRuntimeException("Missing version info: " + exe);

        String name = exe.substring(0, p > 0 ? p : exe.length());
        String mv = (p > 0 ? exe.substring(p + 1) : null);

        ClassLoader cl = createClassLoader(name, mv, conf);
        Module runtimeModule = loadModule(cl, name);
        if (runtimeModule != null) {
            final String mn = runtimeModule.name();
            if (name.equals(mn) == false)
                throw new CeylonRuntimeException("Input module name doesn't match module's name: " + name + " != " + mn);

            final String version = runtimeModule.version();
            if (mv.equals(version) == false && Constants.DEFAULT.toString().equals(name) == false)
                throw new CeylonRuntimeException("Input module version doesn't match module's version: " + mv + " != " + version);
        } else if (Constants.DEFAULT.toString().equals(name) == false) {
            throw new CeylonRuntimeException("Missing module.class info: " + name); // TODO -- dump some more useful msg
        }

        execute(conf, name, cl);
    }

    protected void execute(Configuration conf, String name, ClassLoader cl) throws Exception {
        String runClassName = conf.run;
        if (runClassName == null || runClassName.isEmpty()) {
            // "default" is not a package name
            if (name.equals(Constants.DEFAULT.toString()))
                runClassName = RUN_INFO_CLASS;
            else
                runClassName = name + "." + RUN_INFO_CLASS;
        }
        invokeRun(cl, runClassName, conf.arguments);
    }
}
