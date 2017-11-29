/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import java.util.List;

import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.IntersectionTypeImpl;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.model.Type;
import ceylon.language.meta.model.Type$impl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Intersection", variance = Variance.OUT),
})
public class IntersectionTypeImpl<Intersection>
    implements ceylon.language.meta.model.IntersectionType<Intersection>, ReifiedType {

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(IntersectionTypeImpl.class, $reifiedIntersection);
    }
        
    @Ignore
    public final TypeDescriptor $reifiedIntersection;

    public final org.eclipse.ceylon.model.typechecker.model.Type model;

    protected final Sequential<ceylon.language.meta.model.Type<?>> satisfiedTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends Type<?>> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    public IntersectionTypeImpl(@Ignore TypeDescriptor $reifiedType, org.eclipse.ceylon.model.typechecker.model.IntersectionType intersection){
        List<org.eclipse.ceylon.model.typechecker.model.Type> satisfiedTypes = intersection.getSatisfiedTypes();
        this.model = intersection.getType();
        this.$reifiedIntersection = $reifiedType;
        ceylon.language.meta.model.Type<?>[] types = new ceylon.language.meta.model.Type<?>[satisfiedTypes.size()];
        int i=0;
        for(org.eclipse.ceylon.model.typechecker.model.Type pt : satisfiedTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.satisfiedTypes = Util.sequentialWrapper(TypeDescriptor.klass(ceylon.language.meta.model.Type.class, ceylon.language.Anything.$TypeDescriptor$), types);
    }

    @Override
    @Ignore
    public Type$impl<Intersection> $ceylon$language$meta$model$Type$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use caseTypes.hashCode because order is not significant
        Iterator<? extends Type<?>> iterator = satisfiedTypes.iterator();
        Object obj;
        while((obj = iterator.next()) != finished_.get_()){
            result = result + obj.hashCode();
        }
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof IntersectionTypeImpl == false)
            return false;
        IntersectionTypeImpl<?> other = (IntersectionTypeImpl<?>) obj;
        return other.model.isExactly(model);
    }

    @Override
    public boolean typeOf(@TypeInfo("ceylon.language::Anything") @Nullable Object instance){
        return Metamodel.isTypeOf(model, instance);
    }

    @Override
    public boolean supertypeOf(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isSuperTypeOf(model, type);
    }
    
    @Override
    public boolean subtypeOf(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isSubTypeOf(model, type);
    }

    @Override
    public boolean exactly(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isExactly(model, type);
    }
    
    @Override
    public <Other> ceylon.language.meta.model.Type<?> union(TypeDescriptor reified$Other, ceylon.language.meta.model.Type<? extends Other> other) {
        return Metamodel.union(this, other);
    }
    
    @Override
    public <Other> ceylon.language.meta.model.Type<?> intersection(TypeDescriptor reified$Other, ceylon.language.meta.model.Type<? extends Other> other) {
        return Metamodel.intersection(this, other);
    }
}
