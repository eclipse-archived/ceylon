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

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.AnnotationBearing;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassValueConstructorImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ValueConstructorImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.FunctionOrValueDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.SetterDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueConstructorDeclarationImpl;

import ceylon.language.Anything;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.SetterDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration$impl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class ValueConstructorDeclarationImpl 
        extends FunctionOrValueDeclarationImpl
        implements ValueConstructorDeclaration, 
                AnnotationBearing,
                ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ValueConstructorDeclarationImpl.class);
    
    public final Constructor constructor;
    
    private OpenType type;

    private SetterDeclarationImpl setter;

    
    public ValueConstructorDeclarationImpl(Value value,
            org.eclipse.ceylon.model.typechecker.model.Constructor constructor) {
        super(value);
        this.type = Metamodel.getMetamodel(value.getType());
        this.constructor = constructor;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation"))
    public <Annotation extends java.lang.annotation.Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    public String getName() {
        return constructor.getName() == null ? "" : constructor.getName();
    }

    @Override
    public String getQualifiedName() {
        String name = getName();
        return ((Class)constructor.getContainer()).getQualifiedNameString() + (name.isEmpty() ? "" : "." + getName());
    }

    @Override
    public OpenType getOpenType() {
        return Metamodel.getMetamodel(constructor.getType());
    }

    @Override
    public Module getContainingModule() {
        return getContainer().getContainingModule();
    }

    @Override
    public Package getContainingPackage() {
        return getContainer().getContainingPackage();
    }

    @Override
    public boolean getToplevel() {
        return false;
    }

    @Override
    public ClassDeclaration getContainer() {
        return ((ClassDeclaration)Metamodel.getOrCreateMetamodel(((Class)constructor.getContainer())));
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        return ValueConstructorImpl.getJavaMethod(this).getAnnotations();
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType) {
        final AnnotatedElement element = ValueConstructorImpl.getJavaMethod(this);
        return element != null ? element.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @Override
    public String toString() {
        return "new "+getQualifiedName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof ValueConstructorDeclarationImpl) {
            return getContainer().equals(((ValueConstructorDeclarationImpl)other).getContainer())
                    && getName().equals(((ValueConstructorDeclarationImpl)other).getName());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getContainer().hashCode() ^ getName().hashCode();
    }

    @Override
    public ValueConstructorDeclaration$impl $ceylon$language$meta$declaration$ValueConstructorDeclaration$impl() {
        return null;
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Value<Get>")
    @TypeParameters({
        @TypeParameter("Get"),
    })
    public <Get> ceylon.language.meta.model.ValueConstructor<Get> apply(@Ignore TypeDescriptor $reifiedGet){
        // TODO if(!getToplevel())
        //    throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a member declaration with no container type: use memberApply");
        // TODO what is Set is anything other than Nothing?
           // c.f. classApply with an incorect parameters
        org.eclipse.ceylon.model.typechecker.model.Value modelDecl = (org.eclipse.ceylon.model.typechecker.model.Value)declaration;
        org.eclipse.ceylon.model.typechecker.model.TypedReference typedReference = modelDecl.appliedTypedReference(null, Collections.<Type>emptyList());

        org.eclipse.ceylon.model.typechecker.model.Type getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType.getQualifyingType());
        // immutable values have Set=Nothing
//        org.eclipse.ceylon.model.typechecker.model.Type setType = getVariable() ? 
//                getType : modelDecl.getUnit().getNothingType();
//        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("apply", "Value<$1>", 
                Variance.OUT, getType, $reifiedGet);
        // XXX This is a lie, and we only get away with it due to erasure
        ClassDeclaration clsDecl = getContainer();
        ceylon.language.meta.model.Class<? extends Get, ?> cls 
        = clsDecl.<Get, Sequential<? extends java.lang.Object>>classApply(
                $reifiedGet, Nothing.NothingType, (Sequential)empty_.get_());
        return (ceylon.language.meta.model.ValueConstructor<Get>)
                new ValueConstructorImpl<Get>(
                        reifiedGet,  this, typedReference, (ClassImpl)cls, null);
    }

    @TypeInfo("ceylon.language.meta.model::Attribute<Container,Get>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Get")
    })
    @Override
    public <Container, Get>
        ceylon.language.meta.model.MemberClassValueConstructor<Container, Get> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedGet,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType){
        if(getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a toplevel declaration to a container type: use apply");
        Type qualifyingType = Metamodel.getModel(containerType);
        Metamodel.checkQualifyingType(qualifyingType, (Declaration)declaration.getContainer());
        org.eclipse.ceylon.model.typechecker.model.Value modelDecl = (org.eclipse.ceylon.model.typechecker.model.Value)declaration;
        // find the proper qualifying type
        Type memberQualifyingType = qualifyingType.getSupertype((TypeDeclaration) modelDecl.getContainer().getContainer());
        org.eclipse.ceylon.model.typechecker.model.TypedReference typedReference = modelDecl.appliedTypedReference(memberQualifyingType, Collections.<Type>emptyList());
        TypeDescriptor reifiedContainer = Metamodel.getTypeDescriptorForProducedType(qualifyingType);
        
        org.eclipse.ceylon.model.typechecker.model.Type getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType.getQualifyingType());
        // immutable values have Set=Nothing
//        org.eclipse.ceylon.model.typechecker.model.Type setType = getVariable() ? 
//                getType : modelDecl.getUnit().getNothingType();
//        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("memberApply", "Attribute<$1,$2>", 
                Variance.IN, memberQualifyingType, $reifiedContainer,
                Variance.OUT, getType, $reifiedGet);
        
        ClassDeclaration clsDecl = getContainer();
        ceylon.language.meta.model.MemberClass cls 
        = clsDecl.memberClassApply(
                $reifiedContainer, $reifiedGet, TypeDescriptor.NothingType, containerType);
        return (ceylon.language.meta.model.MemberClassValueConstructor)new MemberClassValueConstructorImpl<Container,Get>(
                reifiedContainer, reifiedGet, this, typedReference, (MemberClassImpl)cls);
    }
    
    
    
    
    ////////////////////////////////////////
    
    
    //@Override
    public boolean getVariable(){
        return ((org.eclipse.ceylon.model.typechecker.model.TypedDeclaration) declaration).isVariable();
    }
    
    
    //@Override
    public boolean getObjectValue(){
        return type instanceof ceylon.language.meta.declaration.OpenClassType
                && ((ceylon.language.meta.declaration.OpenClassType) type).getDeclaration().getAnonymous();
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration|ceylon.language::Null")
    //@Override
    public ceylon.language.meta.declaration.ClassDeclaration getObjectClass(){
        if(type instanceof ceylon.language.meta.declaration.OpenClassType){
            ceylon.language.meta.declaration.OpenClassType decl = (ceylon.language.meta.declaration.OpenClassType)type;
            if(decl.getDeclaration().getAnonymous())
                return decl.getDeclaration();
        }
        return null;
    }

    
    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object get(){
        return apply(Anything.$TypeDescriptor$).get();
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object memberGet(@Name("container") @TypeInfo("ceylon.language::Object") Object container){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, containerType).bind(container).get();
    }

   
    @TypeInfo("ceylon.language::Anything")
    //@Override
    public Object set(@TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        return apply(Anything.$TypeDescriptor$).$setIfAssignable(newValue);
    }

    @TypeInfo("ceylon.language::Anything")
    //@Override
    public Object memberSet(@Name("container") @TypeInfo("ceylon.language::Object") Object container,
            @TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, containerType).bind(container).$setIfAssignable(newValue);
    }
    
    @TypeInfo("ceylon.language.meta.declaration::SetterDeclaration|ceylon.language::Null")
    //@Override
    public SetterDeclaration getSetter() {
        if(setter == null && ((org.eclipse.ceylon.model.typechecker.model.Value)declaration).getSetter() != null){
            synchronized(Metamodel.getLock()){
                if(setter == null){
                    // must be deferred because getter/setter refer to one another
                    org.eclipse.ceylon.model.typechecker.model.Setter setterModel = ((org.eclipse.ceylon.model.typechecker.model.Value)declaration).getSetter();
                    if(setterModel != null)
                        this.setter = (SetterDeclarationImpl) Metamodel.getOrCreateMetamodel(setterModel);
                }
            }
        }
        return setter;
    }
    
    @Nullable
    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object staticGet(
            @NonNull
            @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Object>")
            ceylon.language.meta.model.Type<?> containerType) {
        throw new ceylon.language.meta.model.TypeApplicationException("Cannot staticGet a value constructor, use get"); 
    }
}
