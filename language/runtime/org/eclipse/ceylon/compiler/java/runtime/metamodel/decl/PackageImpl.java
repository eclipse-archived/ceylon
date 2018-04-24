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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.language.ObjectArrayIterable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.AnnotationBearing;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Predicates;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.AliasDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassOrInterfaceDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.FunctionDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ModuleImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.PackageImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueDeclarationImpl;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.AnnotatedDeclaration;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class PackageImpl implements ceylon.language.meta.declaration.Package, 
        AnnotationBearing,
        ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(PackageImpl.class);
    
    private org.eclipse.ceylon.model.typechecker.model.Package declaration;

    private ModuleImpl module;

    public PackageImpl(org.eclipse.ceylon.model.typechecker.model.Package declaration){
        this.declaration = declaration;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        Class<?> javaClass = Metamodel.getJavaClass(declaration);
        return javaClass != null ? javaClass.getAnnotations() : AnnotationBearing.NONE;
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(Class<? extends Annotation> annotationType) {
        final AnnotatedElement element = Metamodel.getJavaClass(declaration);;
        return element != null ? element.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation"))
    public <Annotation extends java.lang.annotation.Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    public String getQualifiedName() {
        return getName();
    }

    @Override
    public ceylon.language.meta.declaration.Module getContainer() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign module twice we get the same result
        if(module == null){
            module = Metamodel.getOrCreateMetamodel(declaration.getModule());
        }
        return module;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Sequential<? extends Kind> 
    members(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate<?> predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("Kind")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Kind 
    getMember(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredMember($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"), 
        @TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation") 
    })
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration, Annotation extends java.lang.annotation.Annotation> Sequential<? extends Kind> 
    annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationOfKind($reifiedKind),
                Predicates.isDeclarationAnnotatedWith($reifiedAnnotation));
        
        return filteredMembers($reifiedKind, predicate);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Sequential<? extends Kind> filteredMembers(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential<? extends Kind>)empty_.get_();
        }
        List<org.eclipse.ceylon.model.typechecker.model.Declaration> modelMembers = declaration.getMembers();
        ArrayList<Kind> members = new ArrayList<Kind>(modelMembers.size());
        for(org.eclipse.ceylon.model.typechecker.model.Declaration modelDecl : modelMembers){
            if (!modelDecl.isNativeHeader() && predicate.accept(modelDecl)) {
                Kind member = (Kind)Metamodel.getOrCreateMetamodel(modelDecl);
                members.add(member);
            }
        }
        java.lang.Object[] array = members.toArray(new java.lang.Object[0]);
        ObjectArrayIterable<Kind> iterable = 
                new ObjectArrayIterable<Kind>($reifiedKind, (Kind[]) array);
        return (ceylon.language.Sequential) iterable.sequence();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <Kind> Kind filteredMember(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return null;
        }
        List<org.eclipse.ceylon.model.typechecker.model.Declaration> modelMembers = declaration.getMembers();
        for(org.eclipse.ceylon.model.typechecker.model.Declaration modelDecl : modelMembers){
            if (!modelDecl.isNativeHeader() && predicate.accept(modelDecl)) {
                return (Kind)Metamodel.getOrCreateMetamodel(modelDecl);
            }
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.ValueDeclaration getValue(String name) {
        org.eclipse.ceylon.model.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof org.eclipse.ceylon.model.typechecker.model.Value == false)
            return null;
        org.eclipse.ceylon.model.typechecker.model.Value decl = (org.eclipse.ceylon.model.typechecker.model.Value) toplevel;
        return (ValueDeclarationImpl) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.FunctionDeclaration getFunction(String name) {
        org.eclipse.ceylon.model.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof org.eclipse.ceylon.model.typechecker.model.Function == false)
            return null;
        org.eclipse.ceylon.model.typechecker.model.Function decl = (org.eclipse.ceylon.model.typechecker.model.Function) toplevel;
        return (FunctionDeclarationImpl) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassOrInterfaceDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.ClassOrInterfaceDeclaration getClassOrInterface(String name) {
        org.eclipse.ceylon.model.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof org.eclipse.ceylon.model.typechecker.model.ClassOrInterface == false)
            return null;
        org.eclipse.ceylon.model.typechecker.model.ClassOrInterface decl = (org.eclipse.ceylon.model.typechecker.model.ClassOrInterface) toplevel;
        return (ClassOrInterfaceDeclarationImpl) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::AliasDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.AliasDeclaration getAlias(String name) {
        org.eclipse.ceylon.model.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof org.eclipse.ceylon.model.typechecker.model.TypeAlias == false)
            return null;
        org.eclipse.ceylon.model.typechecker.model.TypeAlias decl = (org.eclipse.ceylon.model.typechecker.model.TypeAlias) toplevel;
        return (AliasDeclarationImpl) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    public boolean getShared() {
        return declaration.isShared();
    }

    @Override
    public int hashCode() {
        int result = 1;
        AnnotatedDeclaration container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.Package == false)
            return false;
        ceylon.language.meta.declaration.Package other = (ceylon.language.meta.declaration.Package) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "package " + getName();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

}
