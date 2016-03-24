package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import ceylon.language.meta.declaration.CallableConstructorDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class ClassWithConstructorsDeclarationImpl 
        extends ClassDeclarationImpl 
        implements ceylon.language.meta.declaration.ClassWithConstructorsDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassWithConstructorsDeclarationImpl.class);
    
    public ClassWithConstructorsDeclarationImpl(com.redhat.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
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
