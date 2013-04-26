package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.ProducedType$impl;
import ceylon.language.metamodel.UnionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class UnionType 
    implements ceylon.language.metamodel.UnionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(UnionType.class);
    
    protected Sequential<ceylon.language.metamodel.ProducedType> caseTypes;
    
    UnionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes){
        ceylon.language.metamodel.ProducedType[] types = new ceylon.language.metamodel.ProducedType[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.ProducedType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public ProducedType$impl $ceylon$language$metamodel$ProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public UnionType$impl $ceylon$language$metamodel$UnionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::ProducedType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.ProducedType> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
