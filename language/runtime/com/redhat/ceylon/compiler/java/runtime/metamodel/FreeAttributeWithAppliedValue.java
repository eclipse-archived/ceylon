package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;

import ceylon.language.model.Value;
import ceylon.language.model.Value$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.ValueModel$impl;
import ceylon.language.model.declaration.ValueDeclaration;

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
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeAttributeWithAppliedValue<Type> 
    extends FreeAttribute 
    implements Value<Type> {

    private AppliedValue<Type> typeDelegate;

    protected FreeAttributeWithAppliedValue(@Ignore TypeDescriptor $reifiedType, TypedDeclaration declaration) {
        super(declaration);
        ProducedTypedReference typedReference = declaration.getProducedTypedReference(null, Collections.<ProducedType>emptyList());
        typeDelegate = new AppliedValue<Type>($reifiedType, this, typedReference, null);
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ValueModel$impl $ceylon$language$model$ValueModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Value$impl<Type> $ceylon$language$model$Value$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type get() {
        return typeDelegate.get();
    }

    @Override
    @TypeInfo("ceylon.language.model::Type")
    public ceylon.language.model.Type getType() {
        return typeDelegate.getType();
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::ValueDeclaration")
    public ValueDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.model::Value<ceylon.language::Anything>")
    public Value<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        return this;
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeAttributeWithAppliedValue.class, args[0]);
    }
}
