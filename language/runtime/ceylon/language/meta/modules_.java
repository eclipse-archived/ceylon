/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language.meta;

import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.findLoadedModule;
import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getDefaultModule;
import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getModuleList;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.Object;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.Module;

@Ceylon(major = 8)
@Object
public final class modules_ implements ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(modules_.class);
    
    private modules_() {}
    
    @Ignore
    private static final modules_ value = new modules_();
    
    @Ignore
    public static modules_ get_() {
        return value;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Module>")
    public Sequential<? extends Module> getList() {
        return getModuleList();
    }
    
    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Module")
    public Module find(@Name("name") String name, 
                       @Name("version") String version) {
        return findLoadedModule(name, version);
    }

    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Module")
    public Module getDefault() {
        return getDefaultModule();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$(){
        return $TypeDescriptor$;
    }
}
