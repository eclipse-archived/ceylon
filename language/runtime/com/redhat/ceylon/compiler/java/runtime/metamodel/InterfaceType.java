package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.InterfaceType$impl;

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
public class InterfaceType<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterfaceType<Type>
    implements ceylon.language.metamodel.InterfaceType<Type> {

    public InterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public InterfaceType$impl<Type> $ceylon$language$metamodel$InterfaceType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Interface<Type>")
    public ceylon.language.metamodel.Interface<? extends Type> getDeclaration() {
        return (Interface<? extends Type>) super.getDeclaration();
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(InterfaceType.class, this.declaration.$getReifiedType());
    }
}
