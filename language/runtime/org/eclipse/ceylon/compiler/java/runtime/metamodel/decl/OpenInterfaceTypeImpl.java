/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Type;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.OpenClassOrInterfaceTypeImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.OpenInterfaceTypeImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class OpenInterfaceTypeImpl extends OpenClassOrInterfaceTypeImpl implements ceylon.language.meta.declaration.OpenInterfaceType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(OpenInterfaceTypeImpl.class);

    public OpenInterfaceTypeImpl(Type producedType) {
        super(producedType);
    }

    @Override
    public ceylon.language.meta.declaration.InterfaceDeclaration getDeclaration() {
        return (ceylon.language.meta.declaration.InterfaceDeclaration)super.getDeclaration();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
