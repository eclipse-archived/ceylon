package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Interface$impl;
import ceylon.language.metamodel.InterfaceModel$impl;

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
public class AppliedInterface<Type> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.metamodel.Interface<Type> {

    // FIXME: get rid of duplicate instantiations of AppliedInterfaceType when the type in question has no type parameters
    public AppliedInterface(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(null, producedType);
    }
    
    @Override
    @Ignore
    public Interface$impl<Type> $ceylon$language$metamodel$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public InterfaceModel$impl<Type> $ceylon$language$metamodel$InterfaceModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::InterfaceDeclaration")
    public ceylon.language.metamodel.declaration.InterfaceDeclaration getDeclaration() {
        return (FreeInterface) super.getDeclaration();
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedInterface.class, $reifiedType);
    }
}
