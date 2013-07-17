package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Type;
import ceylon.language.metamodel.declaration.OpenType$impl;
import ceylon.language.metamodel.declaration.OpenIntersection$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeIntersectionType 
    implements ceylon.language.metamodel.declaration.OpenIntersection, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeIntersectionType.class);
    
    protected Sequential<ceylon.language.metamodel.declaration.OpenType> satisfiedTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends ceylon.language.metamodel.declaration.OpenType> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    FreeIntersectionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes){
        ceylon.language.metamodel.declaration.OpenType[] types = new ceylon.language.metamodel.declaration.OpenType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.satisfiedTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.declaration.OpenType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public OpenType$impl $ceylon$language$metamodel$declaration$OpenType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public OpenIntersection$impl $ceylon$language$metamodel$declaration$OpenIntersection$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel.declaration::OpenType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.declaration.OpenType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
