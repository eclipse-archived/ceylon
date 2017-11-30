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

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.UnionTypeImpl;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.model.Type;
import ceylon.language.meta.model.Type$impl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Union", variance = Variance.OUT),
})
public class UnionTypeImpl<Union>
    implements ceylon.language.meta.model.UnionType<Union>, ReifiedType {

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(UnionTypeImpl.class, $reifiedUnion);
    }
    
    @Ignore
    public final TypeDescriptor $reifiedUnion;
    
    public final org.eclipse.ceylon.model.typechecker.model.Type model;
    
    protected final Sequential<? extends ceylon.language.meta.model.Type<? extends Union>> caseTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends Type<?>> iterator = caseTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('|').append(next);
        }
        return sb.toString();
    }

    public UnionTypeImpl(@Ignore TypeDescriptor $reifiedType, org.eclipse.ceylon.model.typechecker.model.UnionType union){
        List<org.eclipse.ceylon.model.typechecker.model.Type> caseTypes = union.getCaseTypes();
        this.model = union.getType();
        this.$reifiedUnion = $reifiedType;
        @SuppressWarnings("unchecked")
        ceylon.language.meta.model.Type<? extends Union>[] types = new ceylon.language.meta.model.Type[caseTypes.size()];
        int i=0;
        for(org.eclipse.ceylon.model.typechecker.model.Type pt : caseTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.caseTypes = Util.<ceylon.language.meta.model.Type<? extends Union>>sequentialWrapper(TypeDescriptor.klass(ceylon.language.meta.model.Type.class, ceylon.language.Anything.$TypeDescriptor$), types);
    }
    
    @Override
    @Ignore
    public Type$impl<Union> $ceylon$language$meta$model$Type$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<Union>>")
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Union>> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use caseTypes.hashCode because order is not significant
        Iterator<? extends Type<?>> iterator = caseTypes.iterator();
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
        if(obj instanceof UnionTypeImpl == false)
            return false;
        UnionTypeImpl<?> other = (UnionTypeImpl<?>) obj;
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
