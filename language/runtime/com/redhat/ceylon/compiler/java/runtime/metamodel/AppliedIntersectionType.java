package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Type;
import ceylon.language.metamodel.Type$impl;
import ceylon.language.metamodel.IntersectionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class AppliedIntersectionType 
    implements ceylon.language.metamodel.IntersectionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(AppliedIntersectionType.class);
    
    protected Sequential<ceylon.language.metamodel.Type> satisfiedTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends Type> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    AppliedIntersectionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes){
        ceylon.language.metamodel.Type[] types = new ceylon.language.metamodel.Type[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.satisfiedTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.Type.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public Type$impl $ceylon$language$metamodel$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public IntersectionType$impl $ceylon$language$metamodel$IntersectionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::Type>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.Type> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
