package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.Interface$impl;
import ceylon.language.model.InterfaceModel$impl;

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
    implements ceylon.language.model.Interface<Type> {

    // FIXME: get rid of duplicate instantiations of AppliedInterfaceType when the type in question has no type parameters
    public AppliedInterface(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(null, producedType);
    }
    
    @Override
    @Ignore
    public Interface$impl<Type> $ceylon$language$model$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public InterfaceModel$impl<Type> $ceylon$language$model$InterfaceModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::InterfaceDeclaration")
    public ceylon.language.model.declaration.InterfaceDeclaration getDeclaration() {
        return (FreeInterface) super.getDeclaration();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedInterface.class, $reifiedType);
    }
}
