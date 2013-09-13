package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.declaration.OpenClassType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClassType extends FreeClassOrInterfaceType implements ceylon.language.model.declaration.OpenClassType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassType.class);

    FreeClassType(ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public OpenClassType$impl $ceylon$language$model$declaration$OpenClassType$impl() {
        return null;
    }

    @Override
    public ceylon.language.model.declaration.ClassDeclaration getDeclaration() {
        return (ceylon.language.model.declaration.ClassDeclaration)getDeclaration();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
