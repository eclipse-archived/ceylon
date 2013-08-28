package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.model.Type;
import ceylon.language.model.Type$impl;
import ceylon.language.model.IntersectionType$impl;

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
    @TypeParameter(value = "Intersection", variance = Variance.OUT),
})
public class AppliedIntersectionType<Intersection>
    implements ceylon.language.model.IntersectionType<Intersection>, ReifiedType {

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedIntersectionType.class, $reifiedIntersection);
    }
        
    @Ignore
    protected TypeDescriptor $reifiedIntersection;

    protected Sequential<ceylon.language.model.Type<?>> satisfiedTypes;
    
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

    AppliedIntersectionType(@Ignore TypeDescriptor $reifiedType, List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes){
        this.$reifiedIntersection = $reifiedType;
        ceylon.language.model.Type<?>[] types = new ceylon.language.model.Type<?>[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.satisfiedTypes = (Sequential)Util.sequentialInstance(TypeDescriptor.klass(ceylon.language.model.Type.class, ceylon.language.Anything.$TypeDescriptor), types);
    }

    @Override
    @Ignore
    public Type$impl $ceylon$language$model$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public IntersectionType$impl $ceylon$language$model$IntersectionType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.model::Type<ceylon.language::Anything>")
    public ceylon.language.Sequential<? extends ceylon.language.model.Type<?>> getSatisfiedTypes() {
        return satisfiedTypes;
    }

}
