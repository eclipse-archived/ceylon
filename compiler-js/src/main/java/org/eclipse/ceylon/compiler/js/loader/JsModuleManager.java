/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.loader;

import static org.eclipse.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/** A ModuleManager that loads modules from js files.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManager extends ModuleManager {

    public JsModuleManager(Context context, String encoding) {
        super();
    }


    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("js");
    }

    @Override
    public Backends getSupportedBackends() {
        return Backend.JavaScript.asSet();
    }
    
    @Override
    protected Module createModule(List<String> moduleName, String version) {
        final Module module = new JsonModule();
        module.setName(moduleName);
        module.setVersion(version);
        Unit u = new Unit();
        u.setFilename(Constants.MODULE_DESCRIPTOR);
        u.setFullPath(moduleName+"/"+version);
        module.setUnit(u);
        JsonModule dep = (JsonModule)findLoadedModule(LANGUAGE_MODULE_NAME, null);
        //This can only happen during initCoreModules()
        if (!(module.isDefaultModule() || module.isLanguageModule())) {
            //Load the language module if we're not inside initCoreModules()
            if (dep == null) {
                dep = (JsonModule)getModules().getLanguageModule();
            }
            //Add language module as a dependency
            //This will cause the dependency to be loaded later
            ModuleImport imp = new ModuleImport(null, dep, false, false);
            module.addImport(imp);
            module.setLanguageModule(dep);
            //Fix 280 part 1 -- [Tako] I have the feeling this can't be correct
//            Backend backend = null; // TODO Figure out if this dependency is only for a specific backend
//            getContext().getModules().getDefaultModule().addImport(new ModuleImport(module, false, false, backend));
        }
        return module;
    }

    @Override
    public org.eclipse.ceylon.model.typechecker.model.Package createPackage(String pkgName, Module module) {
        if (module!=null && module == getModules().getDefaultModule()) {
            org.eclipse.ceylon.model.typechecker.model.Package pkg = module.getDirectPackage(pkgName);
            if (pkg != null) {
                return pkg;
            }
        }
        final JsonPackage pkg = new JsonPackage(pkgName);
        List<String> name = pkgName.isEmpty() ? Arrays.asList("") : splitModuleName(pkgName);
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }
}
