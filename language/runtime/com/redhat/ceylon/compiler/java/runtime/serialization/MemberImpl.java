package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.serialization.Member;
import ceylon.language.serialization.reach_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Implementation of ceylon.language.serialization.Member
 * 
 * This has to be implemented in Java because it needs to call
 * {@link Serializable#$get$(ceylon.language.serialization.ReachableReference)},
 * whose name is not permitted in Ceylon, 
 * but must be illegal so it cannot collide with a user class member 
 */
@Ceylon(major=8, minor=0)
@Class(identifiable=false)
public class MemberImpl implements Member, ReifiedType {
    
    private static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(MemberImpl.class);
    private ValueDeclaration attribute;
    
    public MemberImpl(ValueDeclaration attribute) {
        if (attribute == null) {
            throw new NullPointerException();
        }
        this.attribute = attribute;
    }
    
    @TypeInfo("ceylon.language::Anything")
    @Override
    public java.lang.Object referred(
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object instance) {
        return reach_.get().get(instance, this);
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration")
    @Override
    public ValueDeclaration getAttribute() {
        return attribute;
    }

    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Override
    public String toString() {
        return "Member [attribute=" + attribute + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attribute == null) ? 0 : attribute.hashCode());
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
        MemberImpl other = (MemberImpl) obj;
        if (attribute == null) {
            if (other.attribute != null)
                return false;
        } else if (!attribute.equals(other.attribute))
            return false;
        return true;
    }
    
    
}