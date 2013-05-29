package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.declaration.OpenType$impl;
import ceylon.language.metamodel.declaration.IntersectionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeIntersectionType 
    implements ceylon.language.metamodel.declaration.IntersectionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeIntersectionType.class);
    
    protected Sequential<ceylon.language.metamodel.declaration.OpenType> satisfiedTypes;
    
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
    public IntersectionType$impl $ceylon$language$metamodel$declaration$IntersectionType$impl() {
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
