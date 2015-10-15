package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class ReferenceDeclarationImpl 
    extends ValueDeclarationImpl
    implements ceylon.language.meta.declaration.ReferenceDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ReferenceDeclarationImpl.class);
    
    public ReferenceDeclarationImpl(com.redhat.ceylon.model.typechecker.model.Value declaration) {
        super(declaration);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

}
