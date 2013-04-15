package com.redhat.ceylon.compiler.java.metamodel;

import java.lang.annotation.RetentionPolicy;

import ceylon.language.metamodel.Interface$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class Interface<Type> 
    extends ClassOrInterface<Type>
    implements ceylon.language.metamodel.Interface<Type> {

    public Interface(com.redhat.ceylon.compiler.typechecker.model.Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Interface$impl<Type> $ceylon$language$metamodel$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(Interface.class, $reifiedType);
    }
}
