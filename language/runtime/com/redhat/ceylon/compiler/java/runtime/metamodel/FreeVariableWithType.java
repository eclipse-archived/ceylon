package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.Value$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.AttributeType$impl;
import ceylon.language.metamodel.Variable;
import ceylon.language.metamodel.Variable$impl;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeVariableWithType<Type> 
    extends FreeVariable 
    implements Variable<Type> {

    private AppliedVariable<Type> typeDelegate;

    protected FreeVariableWithType(com.redhat.ceylon.compiler.typechecker.model.Value declaration) {
        super(declaration);
        // FIXME: container?
        typeDelegate = new AppliedVariable<Type>(null, this, declaration.getType(), null);
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AttributeType$impl<Type> $ceylon$language$metamodel$AttributeType$impl() {
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
    @TypeInfo("ceylon.language.metamodel.declaration::AttributeDeclaration")
    public AttributeDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Value<ceylon.language::Anything>")
    public Value<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        return this;
    }

    @Override
    public TypeDescriptor $getType() {
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeVariableWithType.class, args[0]);
    }
}
