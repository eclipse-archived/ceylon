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

import org.eclipse.ceylon.compiler.java.language.Synchronized;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Synchronized,ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language.meta.declaration::ValueDeclaration,ceylon.language::Anything>"})
public class Synchronized 
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Synchronized, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = 7469006294191443025L;

    @Ignore
    public Synchronized(Synchronized$annotation$ ignored) {
    }
    public Synchronized() {
    }
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Synchronized.$TypeDescriptor$;
    }
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Synchronized.class);
    
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Synchronized$annotation$.class;
    }
}