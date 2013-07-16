package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.declaration.SetterDeclaration;
import ceylon.language.metamodel.declaration.VariableDeclaration;
import ceylon.language.metamodel.declaration.VariableDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Value;


@Ceylon(major = 5)
@Class
@SatisfiedTypes("ceylon.language.metamodel.declaration::Variable")
public class FreeVariable
        extends FreeAttribute
        implements VariableDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeVariable.class);
    
    public FreeVariable(Value value) {
        super(value);
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
    
    @Ignore
    @Override
    public VariableDeclaration$impl $ceylon$language$metamodel$declaration$VariableDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @TypeInfo("ceylon.language.metamodel.declaration::SetterDeclaration")
    @Override
    public SetterDeclaration getSetter() {
        // FIXME: let's not allocate all the time
        return new FreeSetter(this);
    }
    
}
