package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.model.Type;
import ceylon.language.model.Type$impl;
import ceylon.language.model.UnionType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Union", variance = Variance.OUT),
})
public class AppliedUnionType<Union>
    implements ceylon.language.model.UnionType<Union>, ReifiedType {

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedUnionType.class, $reifiedUnion);
    }
    
    @Ignore
    protected TypeDescriptor $reifiedUnion;
    
    protected Sequential<ceylon.language.model.Type<Union>> caseTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends Type> iterator = caseTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('|').append(next);
        }
        return sb.toString();
    }

    AppliedUnionType(@Ignore TypeDescriptor $reifiedType, List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> caseTypes){
        this.$reifiedUnion = $reifiedType;
        ceylon.language.model.Type<?>[] types = new ceylon.language.model.Type<?>[caseTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : caseTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.caseTypes = (Sequential)Util.sequentialInstance(TypeDescriptor.klass(ceylon.language.model.Type.class, ceylon.language.Anything.$TypeDescriptor), types);
    }
    
    @Override
    @Ignore
    public Type$impl $ceylon$language$model$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public UnionType$impl $ceylon$language$model$UnionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.model::Type<Union>>")
    public ceylon.language.Sequential<? extends ceylon.language.model.Type<? extends Union>> getCaseTypes() {
        return caseTypes;
    }

}
