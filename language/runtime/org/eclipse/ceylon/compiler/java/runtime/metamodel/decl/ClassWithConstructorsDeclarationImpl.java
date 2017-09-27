package org.eclipse.ceylon.compiler.java.runtime.metamodel.decl;

import ceylon.language.meta.declaration.CallableConstructorDeclaration;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassWithConstructorsDeclarationImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
public class ClassWithConstructorsDeclarationImpl 
        extends ClassDeclarationImpl 
        implements ceylon.language.meta.declaration.ClassWithConstructorsDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassWithConstructorsDeclarationImpl.class);
    
    public ClassWithConstructorsDeclarationImpl(org.eclipse.ceylon.model.typechecker.model.Class declaration) {
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
