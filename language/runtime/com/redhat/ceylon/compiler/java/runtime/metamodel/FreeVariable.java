package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.declaration.AnnotatedDeclaration;
import ceylon.language.model.declaration.SetterDeclaration;
import ceylon.language.model.declaration.VariableDeclaration;
import ceylon.language.model.declaration.VariableDeclaration$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;


@Ceylon(major = 5)
@Class
@SatisfiedTypes("ceylon.language.model.declaration::Variable")
public class FreeVariable
        extends FreeAttribute
        implements VariableDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeVariable.class);
    
    public FreeVariable(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration value) {
        super(value);
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        AnnotatedDeclaration container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.model.declaration.VariableDeclaration == false)
            return false;
        ceylon.language.model.declaration.VariableDeclaration other = (ceylon.language.model.declaration.VariableDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
    
    @Ignore
    @Override
    public VariableDeclaration$impl $ceylon$language$model$declaration$VariableDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @TypeInfo("ceylon.language.model.declaration::SetterDeclaration")
    @Override
    public SetterDeclaration getSetter() {
        // FIXME: let's not allocate all the time
        return new FreeSetter(this);
    }
    
}
