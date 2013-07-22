package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.declaration.FunctionOrAttributeDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeFunctionOrAttribute 
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.metamodel.declaration.FunctionOrAttributeDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunctionOrAttribute.class);
    
    protected Parameter parameter;

    public FreeFunctionOrAttribute(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
        this.parameter = Metamodel.getParameterFromTypedDeclaration(declaration);
    }
    
    @Override
    @Ignore
    public FunctionOrAttributeDeclaration$impl $ceylon$language$metamodel$declaration$FunctionOrAttributeDeclaration$impl() {
        // TODO Auto-generated method stub
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
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
