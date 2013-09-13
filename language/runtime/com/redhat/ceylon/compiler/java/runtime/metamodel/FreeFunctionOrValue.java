package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.declaration.FunctionOrValueDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeFunctionOrValue 
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.model.declaration.FunctionOrValueDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunctionOrValue.class);
    
    protected Parameter parameter;

    public FreeFunctionOrValue(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
        this.parameter = Metamodel.getParameterFromTypedDeclaration(declaration);
    }
    
    @Override
    @Ignore
    public FunctionOrValueDeclaration$impl $ceylon$language$model$declaration$FunctionOrValueDeclaration$impl() {
        return null;
    }

    @Override
    public boolean getDefaulted(){
        return parameter == null ? false : parameter.isDefaulted();
    }
    
    @Override
    public boolean getVariadic(){
        return parameter == null ? false : parameter.isSequenced();
    }

    @Override
    public boolean getParameter(){
        return parameter != null;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
