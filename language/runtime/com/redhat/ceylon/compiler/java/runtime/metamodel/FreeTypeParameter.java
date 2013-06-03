package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.declaration.Declaration$impl;
import ceylon.language.metamodel.declaration.TypeParameter$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeTypeParameter
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.metamodel.declaration.TypeParameter {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeTypeParameter.class);
    
    public FreeTypeParameter(com.redhat.ceylon.compiler.typechecker.model.TypeParameter declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$declaration$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public TypeParameter$impl $ceylon$language$metamodel$declaration$TypeParameter$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
