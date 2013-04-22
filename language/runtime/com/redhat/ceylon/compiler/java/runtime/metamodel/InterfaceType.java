package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.InterfaceType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class InterfaceType 
    extends ClassOrInterfaceType
    implements ceylon.language.metamodel.InterfaceType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(InterfaceType.class);
    
    public InterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public InterfaceType$impl $ceylon$language$metamodel$InterfaceType$impl() {
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
        return $TypeDescriptor;
    }
}
