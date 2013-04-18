package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.ProducedType$impl;
import ceylon.language.metamodel.IntersectionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class IntersectionType 
    implements ceylon.language.metamodel.IntersectionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(IntersectionType.class);
    
    protected Sequential<ceylon.language.metamodel.ProducedType> satisfiedTypes;
    
    IntersectionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes){
        ceylon.language.metamodel.ProducedType[] types = new ceylon.language.metamodel.ProducedType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.satisfiedTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.ProducedType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public ProducedType$impl $ceylon$language$metamodel$ProducedType$impl() {
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
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::ProducedType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.ProducedType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
