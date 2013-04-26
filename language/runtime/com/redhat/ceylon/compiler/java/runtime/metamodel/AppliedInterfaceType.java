package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.AppliedInterfaceType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
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
public class AppliedInterfaceType<Type> 
    extends AppliedClassOrInterfaceType<Type>
    implements ceylon.language.metamodel.AppliedInterfaceType<Type> {

    public AppliedInterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public AppliedInterfaceType$impl<Type> $ceylon$language$metamodel$AppliedInterfaceType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Interface")
    public ceylon.language.metamodel.Interface getDeclaration() {
        return (Interface) super.getDeclaration();
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedInterfaceType.class, this.declaration.$getReifiedType());
    }
}
