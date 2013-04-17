package com.redhat.ceylon.compiler.java.metamodel;

import ceylon.language.metamodel.TypeParameter$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class TypeParameter
    extends Declaration
    implements ceylon.language.metamodel.TypeParameter {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(TypeParameter.class);
    
    public TypeParameter(com.redhat.ceylon.compiler.typechecker.model.TypeParameter declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public TypeParameter$impl $ceylon$language$metamodel$TypeParameter$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
