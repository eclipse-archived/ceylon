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

import java.util.List;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.OpenIntersectionTypeImpl;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.OpenType;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class OpenIntersectionTypeImpl 
    implements ceylon.language.meta.declaration.OpenIntersection, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(OpenIntersectionTypeImpl.class);
    
    protected Sequential<ceylon.language.meta.declaration.OpenType> satisfiedTypes;
    
    // this is only used for equals
    private org.eclipse.ceylon.model.typechecker.model.Type model;

    public OpenIntersectionTypeImpl(org.eclipse.ceylon.model.typechecker.model.IntersectionType intersection){
        this.model = intersection.getType();
        List<org.eclipse.ceylon.model.typechecker.model.Type> satisfiedTypes = intersection.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenType[] types = new ceylon.language.meta.declaration.OpenType[satisfiedTypes.size()];
        int i=0;
        for(org.eclipse.ceylon.model.typechecker.model.Type pt : satisfiedTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.satisfiedTypes = Util.sequentialWrapper(ceylon.language.meta.declaration.OpenType.$TypeDescriptor$, types);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenType>")
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use satisfiedTypes.hashCode because order is not significant
        Iterator<? extends OpenType> iterator = satisfiedTypes.iterator();
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
        if(obj instanceof OpenIntersectionTypeImpl == false)
            return false;
        OpenIntersectionTypeImpl other = (OpenIntersectionTypeImpl) obj;
        return other.model.isExactly(model);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends ceylon.language.meta.declaration.OpenType> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
