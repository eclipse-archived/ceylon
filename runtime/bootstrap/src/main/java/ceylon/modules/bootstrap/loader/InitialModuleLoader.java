/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.bootstrap.loader;

import java.util.Set;

import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalModuleFinder;
import org.jboss.modules.ModuleFinder;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

public class InitialModuleLoader extends ModuleLoader {

    public InitialModuleLoader() {
        super(new ModuleFinder[] { new LocalModuleFinder() });
    }
    
    @Override
    protected ModuleSpec findModule(ModuleIdentifier module) throws ModuleLoadException {
        if(JDKUtils.isJDKModule(module.getName())){
            ModuleSpec.Builder builder = ModuleSpec.build(module);
            Set<String> jdkPaths = JDKUtils.getJDKPathsByModule(module.getName());
            builder.addDependency(DependencySpec.createSystemDependencySpec(jdkPaths, true));
            return builder.create();
        }
        if(JDKUtils.isOracleJDKModule(module.getName())){
            ModuleSpec.Builder builder = ModuleSpec.build(module);
            Set<String> jdkPaths = JDKUtils.getOracleJDKPathsByModule(module.getName());
            builder.addDependency(DependencySpec.createSystemDependencySpec(jdkPaths, true));
            return builder.create();
        }
        return super.findModule(module);
    }
}
