package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Variable$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class Variable<Type> 
    extends Value<Type>
    implements ceylon.language.metamodel.Variable<Type> {

    public Variable(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Variable$impl<Type> $ceylon$language$metamodel$Variable$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object set(Type val) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(Variable.class, $reifiedType);
    }
}
