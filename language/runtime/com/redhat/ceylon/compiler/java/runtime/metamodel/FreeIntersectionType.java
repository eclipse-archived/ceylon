package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Finished;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.OpenIntersection$impl;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.OpenType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeIntersectionType 
    implements ceylon.language.meta.declaration.OpenIntersection, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeIntersectionType.class);
    
    protected Sequential<ceylon.language.meta.declaration.OpenType> satisfiedTypes;
    
    // this is only used for equals
    private com.redhat.ceylon.compiler.typechecker.model.ProducedType model;

    FreeIntersectionType(com.redhat.ceylon.compiler.typechecker.model.IntersectionType intersection){
        this.model = intersection.getType();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = intersection.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenType[] types = new ceylon.language.meta.declaration.OpenType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            types[i++] = Metamodel.getMetamodel(pt);
        }
        this.satisfiedTypes = Util.sequentialInstance(ceylon.language.meta.declaration.OpenType.$TypeDescriptor, types);
    }

    @Override
    @Ignore
    public OpenType$impl $ceylon$language$meta$declaration$OpenType$impl() {
        return null;
    }

    @Override
    @Ignore
    public OpenIntersection$impl $ceylon$language$meta$declaration$OpenIntersection$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.Sequential<ceylon.language.meta.declaration::OpenType>")
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // do not use satisfiedTypes.hashCode because order is not significant
        Iterator<? extends OpenType> iterator = satisfiedTypes.iterator();
        Object obj;
        while((obj = iterator.next()) != finished_.get_()){
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
        if(obj instanceof FreeIntersectionType == false)
            return false;
        FreeIntersectionType other = (FreeIntersectionType) obj;
        return other.model.isExactly(model);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<? extends ceylon.language.meta.declaration.OpenType> iterator = satisfiedTypes.iterator();
        Object next=iterator.next();
        sb.append(next);
        while (!((next=iterator.next()) instanceof Finished)) {
            sb.append('&').append(next);
        }
        return sb.toString();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
