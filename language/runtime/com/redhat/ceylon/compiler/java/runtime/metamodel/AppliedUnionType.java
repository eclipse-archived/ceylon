package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.AppliedType$impl;
import ceylon.language.metamodel.UnionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class AppliedUnionType 
    implements ceylon.language.metamodel.UnionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(AppliedUnionType.class);
    
    protected Sequential<ceylon.language.metamodel.AppliedType> caseTypes;
    
    AppliedUnionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes){
        ceylon.language.metamodel.AppliedType[] types = new ceylon.language.metamodel.AppliedType[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.AppliedType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public AppliedType$impl $ceylon$language$metamodel$AppliedType$impl() {
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
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::AppliedType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.AppliedType> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
