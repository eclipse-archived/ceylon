package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;

import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.Value$impl;
import ceylon.language.metamodel.Model$impl;
import ceylon.language.metamodel.AttributeModel$impl;
import ceylon.language.metamodel.Variable;
import ceylon.language.metamodel.Variable$impl;
import ceylon.language.metamodel.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeVariableWithAppliedVariable<Type> 
    extends FreeVariable 
    implements Variable<Type> {

    private AppliedVariable<Type> typeDelegate;
    @Ignore
    private TypeDescriptor $reifiedType;

    protected FreeVariableWithAppliedVariable(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
        this.$reifiedType = $reifiedType;
        ProducedTypedReference typedReference = declaration.getProducedTypedReference(null, Collections.<ProducedType>emptyList());
        typeDelegate = new AppliedVariable<Type>($reifiedType, this, typedReference, null);
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$metamodel$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AttributeModel$impl<Type> $ceylon$language$metamodel$AttributeModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Value$impl<Type> $ceylon$language$metamodel$Value$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Variable$impl<Type> $ceylon$language$metamodel$Variable$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type get() {
        return typeDelegate.get();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return typeDelegate.getType();
    }

    @Override
    public Object set(@Name("newValue") @TypeInfo("Type") Type newValue) {
        return typeDelegate.set(newValue);
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::ValueDeclaration")
    public ValueDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Value<ceylon.language::Anything>")
    public Value<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        return this;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeVariableWithAppliedVariable.class, $reifiedType);
    }
}
