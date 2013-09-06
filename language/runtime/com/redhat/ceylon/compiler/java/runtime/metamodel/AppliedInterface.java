package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.Interface$impl;
import ceylon.language.model.InterfaceModel$impl;

import com.redhat.ceylon.compiler.java.Util;
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

    private Object instance;

    // FIXME: get rid of duplicate instantiations of AppliedInterfaceType when the type in question has no type parameters
    public AppliedInterface(@Ignore TypeDescriptor $reifiedType, 
                            com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType,
                            Object instance) {
        super(null, producedType);
        this.instance = instance;
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

    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof AppliedInterface == false)
            return false;
        AppliedInterface other = (AppliedInterface) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return getDeclaration().equals(other.getDeclaration())
                && Util.eq(instance, other.instance)
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedInterface.class, $reifiedType);
    }
}
