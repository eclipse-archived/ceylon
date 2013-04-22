package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Class;
import ceylon.language.metamodel.ClassType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class ClassType 
    extends ClassOrInterfaceType
    implements ceylon.language.metamodel.ClassType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(ClassType.class);
    
    public ClassType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public ClassType$impl $ceylon$language$metamodel$ClassType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class")
    public ceylon.language.metamodel.Class getDeclaration() {
        return (Class) super.getDeclaration();
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
