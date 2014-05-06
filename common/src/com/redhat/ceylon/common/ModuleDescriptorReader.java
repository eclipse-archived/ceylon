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

package com.redhat.ceylon.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/*
 * Really crappy proxy class for com.redhat.ceylon.compiler.ModuleDescriptorReader 
 * to prevent a problem with that class accessing all kinds of needed dependencies
 * before we have been able to configure our class loader with the required paths
 */
public class ModuleDescriptorReader {
    private Object instance;
    private Method moduleVersion;
    private Method moduleName;
    private Method moduleLicense;
    private Method moduleAuthors;

    @SuppressWarnings("serial")
    public static class NoSuchModuleException extends Exception {

        public NoSuchModuleException(String string) {
            super(string);
        }
    }

    public ModuleDescriptorReader(String moduleName, File srcDir) throws NoSuchModuleException {
        try {
            Class<?> mdr = ModuleDescriptorReader.class.getClassLoader().loadClass("com.redhat.ceylon.compiler.ModuleDescriptorReader");
            this.moduleVersion = mdr.getMethod("getModuleVersion");
            this.moduleVersion.setAccessible(true);
            this.moduleName = mdr.getMethod("getModuleName");
            this.moduleName.setAccessible(true);
            this.moduleLicense = mdr.getMethod("getModuleLicense");
            this.moduleLicense.setAccessible(true);
            this.moduleAuthors = mdr.getMethod("getModuleAuthors");
            this.moduleAuthors.setAccessible(true);
            Constructor<?> constructor = mdr.getConstructor(String.class, File.class);
            constructor.setAccessible(true);
            this.instance = constructor.newInstance(moduleName, srcDir);
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof NoSuchModuleException)
                throw (NoSuchModuleException)e.getCause();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module version
     * @return The module version, or null if no version could be found
     */
    public String getModuleVersion() {
        try {
            return (String)moduleVersion.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module name
     * @return The module version, or null if no version could be found
     */
    public String getModuleName() {
        try {
            return (String)moduleName.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Gets the module license
     * @return The module version, or null if no version could be found
     */
    public String getModuleLicense() {
        try {
            return (String)moduleLicense.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the module authors
     * @return The list of module authors, or empty list of no authors could be found
     */
    @SuppressWarnings("unchecked")
    public List<String> getModuleAuthors() {
        try {
            return (List<String>)moduleAuthors.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
