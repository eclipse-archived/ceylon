package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.untyped.Type$impl;
import ceylon.language.metamodel.untyped.UnionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeUnionType 
    implements ceylon.language.metamodel.untyped.UnionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeUnionType.class);
    
    protected Sequential<ceylon.language.metamodel.untyped.Type> caseTypes;
    
    FreeUnionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes){
        ceylon.language.metamodel.untyped.Type[] types = new ceylon.language.metamodel.untyped.Type[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.untyped.Type.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public Type$impl $ceylon$language$metamodel$untyped$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public UnionType$impl $ceylon$language$metamodel$untyped$UnionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel.untyped::Type>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.untyped.Type> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
