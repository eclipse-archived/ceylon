package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.serialization.Element;
import ceylon.language.serialization.reach_;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Implementation of ceylon.language.serialization.Element
 * 
 * This has to be implemented in Java because it needs to call
 * {@link Serializable#$get$(ceylon.language.serialization.ReachableReference)},
 * whose name is not permitted in Ceylon, 
 * but must be illegal so it cannot collide with a user class member 
 */
@Ceylon(major=8, minor=0)
@Class(identifiable=false)
public class ElementImpl implements Element, ReifiedType {
    
    private static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ElementImpl.class);
    
    private int index;

    public ElementImpl(long index) {
        this.index = Util.toInt(index);
    }
    
    @Override
    public long getIndex() {
        return index;
    }
    
    @TypeInfo("ceylon.language::Anything")
    @Override
    public java.lang.Object referred(
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object instance) {
        return reach_.get().get(instance, this);
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Override
    public String toString() {
        return "Element [index=" + index + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ElementImpl other = (ElementImpl) obj;
        if (index != other.index)
            return false;
        return true;
    }
}