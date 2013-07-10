package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Attribute;
import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeAttributeWithType<Type> 
    extends FreeAttribute 
    implements Attribute<Type> {

    private AppliedValue<Type> typeDelegate;

    protected FreeAttributeWithType(@Ignore TypeDescriptor $reifiedType, TypedDeclaration declaration) {
        super(declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        // FIXME: container?
        typeDelegate = new AppliedValue<Type>(null, this, modelDecl.getType(), null);
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Attribute$impl<Type> $ceylon$language$metamodel$Attribute$impl() {
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
    @TypeInfo("ceylon.language.metamodel.declaration::AttributeDeclaration")
    public AttributeDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Attribute<ceylon.language::Anything>")
    public Attribute<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        return this;
    }
    
    @Override
    public TypeDescriptor $getType() {
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeAttributeWithType.class, args[0]);
    }
}
