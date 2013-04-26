package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.AppliedProducedType$impl;
import ceylon.language.metamodel.AppliedUnionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class AppliedUnionType 
    implements ceylon.language.metamodel.AppliedUnionType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(AppliedUnionType.class);
    
    protected Sequential<ceylon.language.metamodel.AppliedProducedType> caseTypes;
    
    AppliedUnionType(List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes){
        ceylon.language.metamodel.AppliedProducedType[] types = new ceylon.language.metamodel.AppliedProducedType[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.AppliedProducedType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public AppliedProducedType$impl $ceylon$language$metamodel$AppliedProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AppliedUnionType$impl $ceylon$language$metamodel$AppliedUnionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.metamodel::AppliedProducedType>")
    public ceylon.language.Sequential<? extends ceylon.language.metamodel.AppliedProducedType> getCaseTypes() {
        return caseTypes;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
