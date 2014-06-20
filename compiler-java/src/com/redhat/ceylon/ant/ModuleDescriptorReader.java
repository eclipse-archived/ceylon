/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ant;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import com.redhat.ceylon.launcher.Launcher;

/*
 * Really crappy proxy class for com.redhat.ceylon.compiler.ModuleDescriptorReader 
 * to prevent a problem with that class accessing all kinds of needed dependencies
 * before we have been able to configure our class loader with the required paths
 */
class ModuleDescriptorReader {
    private String moduleName;
    private File srcDir;
    
    private boolean setupOk = false;
    
    private String readModuleName;
    private String readModuleVersion;
    private String readModuleLicense;
    private List<String> readModuleAuthors;
    
    public ModuleDescriptorReader(String moduleName, File srcDir) {
        this.moduleName = moduleName;
        this.srcDir = srcDir;
    }
    
    @SuppressWarnings("unchecked")
    private void setup() {
        if (!setupOk) {
            try {
                Launcher.executeInContext(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Class<?> mdr = loader.loadClass("com.redhat.ceylon.compiler.ModuleDescriptorReader");
                        Method moduleNameMth = mdr.getMethod("getModuleName");
                        moduleNameMth.setAccessible(true);
                        Method moduleVersionMth = mdr.getMethod("getModuleVersion");
                        moduleVersionMth.setAccessible(true);
                        Method moduleLicenseMth = mdr.getMethod("getModuleLicense");
                        moduleLicenseMth.setAccessible(true);
                        Method moduleAuthorsMth = mdr.getMethod("getModuleAuthors");
                        moduleAuthorsMth.setAccessible(true);
                        Constructor<?> constructor = mdr.getConstructor(String.class, File.class);
                        constructor.setAccessible(true);
                        Object instance = constructor.newInstance(moduleName, srcDir);
                        readModuleName = (String)moduleNameMth.invoke(instance);
                        readModuleVersion = (String)moduleVersionMth.invoke(instance);
                        readModuleLicense = (String)moduleLicenseMth.invoke(instance);
                        readModuleAuthors = (List<String>)moduleAuthorsMth.invoke(instance);
                        return null;
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            setupOk = true;
        }
    }
    
    /**
     * Gets the module version
     * @return The module version, or null if no version could be found
     */
    public String getModuleVersion() {
        setup();
        return readModuleVersion;
    }
    
    /**
     * Gets the module name
     * @return The module version, or null if no version could be found
     */
    public String getModuleName() {
        setup();
        return readModuleName;
    }
    
    /**
     * Gets the module license
     * @return The module version, or null if no version could be found
     */
    public String getModuleLicense() {
        setup();
        return readModuleLicense;
    }

    /**
     * Gets the module authors
     * @return The list of module authors, or empty list of no authors could be found
     */
    public List<String> getModuleAuthors() {
        setup();
        return readModuleAuthors;
    }
}
