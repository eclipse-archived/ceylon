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

import java.util.List;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.OpenUnionTypeImpl;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.OpenType;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class OpenUnionTypeImpl 
    implements ceylon.language.meta.declaration.OpenUnion, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(OpenUnionTypeImpl.class);
    
    protected Sequential<ceylon.language.meta.declaration.OpenType> caseTypes;

    // this is only used for equals
    private org.eclipse.ceylon.model.typechecker.model.Type model;
    
    public OpenUnionTypeImpl(org.eclipse.ceylon.model.typechecker.model.UnionType union){
        this.model = union.getType();
        List<org.eclipse.ceylon.model.typechecker.model.Type> caseTypes = union.getCaseTypes();
        ceylon.language.meta.declaration.OpenType[] types = new ceylon.language.meta.declaration.OpenType[caseTypes.size()];
        int i=0;
        for(org.eclipse.ceylon.model.typechecker.model.Type pt : caseTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.caseTypes = Util.sequentialWrapper(ceylon.language.meta.declaration.OpenType.$TypeDescriptor$, types);
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.meta.declaration::OpenType>")
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use caseTypes.hashCode because order is not significant
        Iterator<? extends OpenType> iterator = caseTypes.iterator();
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
        if(obj instanceof OpenUnionTypeImpl == false)
            return false;
        OpenUnionTypeImpl other = (OpenUnionTypeImpl) obj;
        return other.model.isExactly(model);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends ceylon.language.meta.declaration.OpenType> iterator = caseTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('|').append(next);
        }
        return sb.toString();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
