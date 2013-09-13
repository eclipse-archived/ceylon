package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.declaration.OpenInterfaceType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeInterfaceType extends FreeClassOrInterfaceType implements ceylon.language.model.declaration.OpenInterfaceType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeInterfaceType.class);

    FreeInterfaceType(ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public OpenInterfaceType$impl $ceylon$language$model$declaration$OpenInterfaceType$impl() {
        return null;
    }

    @Override
    public ceylon.language.model.declaration.InterfaceDeclaration getDeclaration() {
        return (ceylon.language.model.declaration.InterfaceDeclaration)getDeclaration();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
