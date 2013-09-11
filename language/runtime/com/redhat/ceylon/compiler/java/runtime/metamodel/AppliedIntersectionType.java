package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.model.IntersectionType$impl;
import ceylon.language.model.Type;
import ceylon.language.model.Type$impl;

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

    private com.redhat.ceylon.compiler.typechecker.model.ProducedType model;

    protected Sequential<ceylon.language.model.Type<?>> satisfiedTypes;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends Type<?>> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    AppliedIntersectionType(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.IntersectionType intersection){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = intersection.getSatisfiedTypes();
        this.model = intersection.getType();
        this.$reifiedIntersection = $reifiedType;
        ceylon.language.model.Type<?>[] types = new ceylon.language.model.Type<?>[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        this.satisfiedTypes = Util.sequentialInstance(TypeDescriptor.klass(ceylon.language.model.Type.class, ceylon.language.Anything.$TypeDescriptor), types);
    }

    @Override
    @Ignore
    public Type$impl<Intersection> $ceylon$language$model$Type$impl() {
        return null;
    }

    @Override
    @Ignore
    public IntersectionType$impl<Intersection> $ceylon$language$model$IntersectionType$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.model::Type<ceylon.language::Anything>")
    public ceylon.language.Sequential<? extends ceylon.language.model.Type<?>> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use caseTypes.hashCode because order is not significant
        Iterator<? extends Type<?>> iterator = satisfiedTypes.iterator();
        Object obj;
        while((obj = iterator.next()) != finished_.$get()){
            result = result + obj.hashCode();
        }
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof AppliedIntersectionType == false)
            return false;
        AppliedIntersectionType<?> other = (AppliedIntersectionType<?>) obj;
        return other.model.isExactly(model);
    }
}
