package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Anything;
import ceylon.language.metamodel.ProducedType;
import ceylon.language.metamodel.Value$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class Value<Type> 
    extends Declaration
    implements ceylon.language.metamodel.Value<Type> {

    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    private ProducedType type;

    public Value(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
        // FIXME
        $reifiedType = Anything.$TypeDescriptor;

        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public Value$impl<Type> $ceylon$language$metamodel$Value$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type get() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ProducedType")
    public ProducedType getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(Value.class, $reifiedType);
    }
}
