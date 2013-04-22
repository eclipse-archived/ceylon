package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.AppliedProducedType$impl;
import ceylon.language.metamodel.AppliedIntersectionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class AppliedIntersectionType 
    implements ceylon.language.metamodel.AppliedIntersectionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(AppliedIntersectionType.class);
    
    protected Sequential<ceylon.language.metamodel.AppliedProducedType> satisfiedTypes;
    
    AppliedIntersectionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes){
        ceylon.language.metamodel.AppliedProducedType[] types = new ceylon.language.metamodel.AppliedProducedType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.satisfiedTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.AppliedProducedType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public AppliedProducedType$impl $ceylon$language$metamodel$AppliedProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AppliedIntersectionType$impl $ceylon$language$metamodel$AppliedIntersectionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::AppliedProducedType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.AppliedProducedType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
