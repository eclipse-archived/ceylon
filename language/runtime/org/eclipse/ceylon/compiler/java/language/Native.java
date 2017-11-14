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
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.Native;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@SatisfiedTypes("ceylon.language::OptionalAnnotation<java.lang::Native, ceylon.language.meta.declaration::ValueDeclaration|ceylon.language.meta.declaration::FunctionDeclaration>")
public class Native 
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Native, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = 7112532346531035488L;

    @Ignore
    public Native(Native$annotation$ ignored) {
    }
    public Native() {
    }
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Native.$TypeDescriptor$;
    }
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Native.class);
    
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Native$annotation$.class;
    }
}