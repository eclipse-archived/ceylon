package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Annotated$impl;
import ceylon.language.metamodel.declaration.Setter;
import ceylon.language.metamodel.declaration.Variable;
import ceylon.language.metamodel.declaration.Variable$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Value;


@Ceylon(major = 5)
@Class
@SatisfiedTypes("ceylon.language.metamodel.declaration::Variable")
public class FreeVariable
        extends FreeValue
        implements Variable {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeVariable.class);
    
    public FreeVariable(Value value) {
        super(value);
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
    
    @Override
    public Variable$impl $ceylon$language$metamodel$declaration$Variable$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @TypeInfo("ceylon.language.metamodel.declaration::Setter")
    @Override
    public Setter getSetter() {
        return new FreeSetter(this);
    }
    
}
