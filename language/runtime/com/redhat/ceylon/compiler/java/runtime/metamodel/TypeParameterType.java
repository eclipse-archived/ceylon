package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.ProducedType$impl;
import ceylon.language.metamodel.TypeParameterType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class TypeParameterType 
    implements ceylon.language.metamodel.TypeParameterType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(TypeParameterType.class);
    
    protected ceylon.language.metamodel.TypeParameter declaration;
    
    TypeParameterType(ceylon.language.metamodel.TypeParameter declaration){
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public ProducedType$impl $ceylon$language$metamodel$ProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public TypeParameterType$impl $ceylon$language$metamodel$TypeParameterType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::TypeParameter")
    public ceylon.language.metamodel.TypeParameter getDeclaration() {
        return declaration;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
