package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Parameter;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FunctionOrValueDeclarationImpl 
    extends NestableDeclarationImpl
    implements ceylon.language.meta.declaration.FunctionOrValueDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FunctionOrValueDeclarationImpl.class);
    
    protected Parameter parameter;

    public FunctionOrValueDeclarationImpl(com.redhat.ceylon.model.typechecker.model.Declaration declaration) {
        super(declaration);
        this.parameter = declaration instanceof com.redhat.ceylon.model.typechecker.model.TypedDeclaration ? Metamodel.getParameterFromTypedDeclaration((com.redhat.ceylon.model.typechecker.model.TypedDeclaration)declaration) : null;
    }
    
    @Override
    public boolean getDefaulted(){
        return parameter == null ? false : parameter.isDefaulted();
    }
    
    @Override
    public boolean getVariadic(){
        return parameter == null ? false : parameter.isSequenced();
    }

    @Override
    public boolean getParameter(){
        return parameter != null;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
