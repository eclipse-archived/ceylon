/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import ceylon.language.meta.declaration.CallableConstructorDeclaration;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassWithConstructorsDeclarationImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class ClassWithConstructorsDeclarationImpl 
        extends ClassDeclarationImpl 
        implements ceylon.language.meta.declaration.ClassWithConstructorsDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassWithConstructorsDeclarationImpl.class);
    
    public ClassWithConstructorsDeclarationImpl(org.eclipse.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @Override
    public CallableConstructorDeclaration getDefaultConstructor() {
        return (CallableConstructorDeclaration)getConstructorDeclaration("");
    }
    
}
