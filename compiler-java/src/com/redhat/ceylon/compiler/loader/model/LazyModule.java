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

package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;

/**
 * Represents a lazy Module declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class LazyModule extends Module {

    private boolean isJava = false;

    public LazyModule() {
    }

    @Override
    public Package getPackage(String name) {
        // try here first
        Package pkg = null;
        
        // unless we're the default module, in which case we have to check this at the end,
        // since every package can be part of the default module
        boolean defaultModule = isDefault();
        if(!defaultModule){
            pkg = findPackageInModule(this, name);
            if(pkg != null)
                return pkg;
        }
        // then try in dependencies
        for(ModuleImport dependency : getImports()){
            // we don't have to worry about the default module here since we can't depend on it
            pkg = findPackageInModule((LazyModule) dependency.getModule(), name);
            if(pkg != null)
                return pkg;
        }
        // do the lookup of the default module last
        if(defaultModule)
            pkg = getModelLoader().findOrCreatePackage(this, name);
        return pkg;
    }

    private Package findPackageInModule(LazyModule module, String name) {
        String moduleName = module.getNameAsString();
        // is it the same package as the module, or a subpackage of it?
        if(name.equals(moduleName)
                || name.startsWith(moduleName+"."))
            return getModelLoader().findOrCreatePackage(module, name);
        return null;
    }

    protected abstract AbstractModelLoader getModelLoader();

    public boolean isJava() {
        return isJava;
    }

    public void setJava(boolean isJava) {
        this.isJava = isJava;
    }

}
