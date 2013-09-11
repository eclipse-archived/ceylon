package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.model.declaration.OpenType;
import ceylon.language.model.declaration.OpenType$impl;
import ceylon.language.model.declaration.OpenUnion$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeUnionType 
    implements ceylon.language.model.declaration.OpenUnion, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeUnionType.class);
    
    protected Sequential<ceylon.language.model.declaration.OpenType> caseTypes;

    // this is only used for equals
    private com.redhat.ceylon.compiler.typechecker.model.ProducedType model;
    
    FreeUnionType(com.redhat.ceylon.compiler.typechecker.model.UnionType union){
        this.model = union.getType();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes = union.getCaseTypes();
        ceylon.language.model.declaration.OpenType[] types = new ceylon.language.model.declaration.OpenType[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(ceylon.language.model.declaration.OpenType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public OpenType$impl $ceylon$language$model$declaration$OpenType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public OpenUnion$impl $ceylon$language$model$declaration$OpenUnion$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.model.declaration::OpenType>")
    public ceylon.language.Sequential<? extends ceylon.language.model.declaration.OpenType> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use caseTypes.hashCode because order is not significant
        Iterator<? extends OpenType> iterator = caseTypes.iterator();
        Object obj;
        while((obj = iterator.next()) != finished_.$get()){
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
        if(obj instanceof FreeUnionType == false)
            return false;
        FreeUnionType other = (FreeUnionType) obj;
        return other.model.isExactly(model);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends ceylon.language.model.declaration.OpenType> iterator = caseTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('|').append(next);
        }
        return sb.toString();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
