/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.Overloaded;

import ceylon.language.Annotated;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Overloaded,ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language.meta.declaration::CallableConstructorDeclaration,ceylon.language::Anything>"})
public class Overloaded 
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Overloaded, Annotated, java.lang.Object>, 
            java.io.Serializable { 
    
    private static final long serialVersionUID = -5466420866210193001L;

    @Ignore
    public Overloaded(Overloaded$annotation$ ignored) {
    }
    public Overloaded() {
    }
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Overloaded.$TypeDescriptor$;
    }
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Overloaded.class);
    
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Overloaded$annotation$.class;
    }
}