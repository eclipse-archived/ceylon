package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.meta.declaration.CallableConstructorDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClassWithConstructors 
        extends FreeClass 
        implements ceylon.language.meta.declaration.ClassWithConstructorsDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeClassWithConstructors.class);
    
    public FreeClassWithConstructors(com.redhat.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ceylon.language.meta.declaration.ClassWithConstructorsDeclaration$impl 
    $ceylon$language$meta$declaration$ClassWithConstructorsDeclaration$impl() {
        return new ceylon.language.meta.declaration.ClassWithConstructorsDeclaration$impl(this);
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @Override
    public CallableConstructorDeclaration getDefaultConstructor() {
        return (CallableConstructorDeclaration)getConstructorDeclaration("");
    }
    
}
