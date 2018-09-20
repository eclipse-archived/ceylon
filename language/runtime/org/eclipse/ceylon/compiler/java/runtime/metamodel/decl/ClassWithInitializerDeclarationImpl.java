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

import ceylon.language.DeprecationAnnotation;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.SharedAnnotation;
import ceylon.language.Singleton;
import ceylon.language.ThrownExceptionAnnotation;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.model.Type;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Predicates;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Predicates.Predicate;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassWithInitializerDeclarationConstructor;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassWithInitializerDeclarationImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class ClassWithInitializerDeclarationImpl 
        extends ClassDeclarationImpl 
        implements ceylon.language.meta.declaration.ClassWithInitializerDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassWithInitializerDeclarationImpl.class);
    
    public ClassWithInitializerDeclarationImpl(org.eclipse.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ceylon.language.meta.declaration.ClassWithInitializerDeclaration$impl 
    $ceylon$language$meta$declaration$ClassWithInitializerDeclaration$impl() {
        return new ceylon.language.meta.declaration.ClassWithInitializerDeclaration$impl(this);
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration[]")
    @Override
    public Sequence<? extends CallableConstructorDeclaration> constructorDeclarations() {
        return new Singleton<CallableConstructorDeclaration>(CallableConstructorDeclaration.$TypeDescriptor$, getDefaultConstructor());
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration[]")
    @Override
    public <A extends java.lang.annotation.Annotation> Sequential<? extends CallableConstructorDeclaration> annotatedConstructorDeclarations(TypeDescriptor reified$Annotation) {
        Type<?> at = Metamodel.getAppliedMetamodel(reified$Annotation);
        if (at.subtypeOf(Metamodel.getAppliedMetamodel(
                TypeDescriptor.union(SharedAnnotation.$TypeDescriptor$,
                        DeprecationAnnotation.$TypeDescriptor$,
                        ThrownExceptionAnnotation.$TypeDescriptor$)))) {
            Predicate<Declaration> p = Predicates.isDeclarationAnnotatedWith(reified$Annotation, at);
            return !declaration.isNativeHeader() && p.accept(declaration) ? constructorDeclarations() : (Sequential)empty_.get_();
        } else {
            return (Sequential)empty_.get_();
        }
    }
    
    @Override
    @NonNull
    public CallableConstructorDeclaration getDefaultConstructor() {
        return new ClassWithInitializerDeclarationConstructor((org.eclipse.ceylon.model.typechecker.model.Class)declaration);
    }
    
    @TypeInfo("ceylon.language.meta.declaration::CallableConstructorDeclaration")
    @Override
    public ceylon.language.meta.declaration.CallableConstructorDeclaration getConstructorDeclaration(String name) {
        return name.isEmpty() ? getDefaultConstructor() : null;
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::FunctionOrValueDeclaration>")
    @NonNull
    public Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> getParameterDeclarations(){
        return Util.assertExists(super.getParameterDeclarations());
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionOrValueDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.FunctionOrValueDeclaration getParameterDeclaration(@Name("name") String name){
        return super.getParameterDeclaration(name);
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        java.lang.reflect.Constructor<?> ctor = Metamodel.getJavaConstructor((org.eclipse.ceylon.model.typechecker.model.Class)declaration, null);
        java.lang.annotation.Annotation[] classAnnos = super.$getJavaAnnotations$();
        java.lang.annotation.Annotation[] ctorAnnos;
        if (ctor != null) {
            ctorAnnos = ctor.getAnnotations();
        } else {
            ctorAnnos = new java.lang.annotation.Annotation[0];
        }
        java.lang.annotation.Annotation[] result = new java.lang.annotation.Annotation[classAnnos.length + ctorAnnos.length];
        System.arraycopy(classAnnos, 0, result, 0, classAnnos.length);
        System.arraycopy(ctorAnnos, 0, result, classAnnos.length, ctorAnnos.length);
        return result;
    }
}
