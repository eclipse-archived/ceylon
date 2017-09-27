package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.Strictfp;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Strictfp,ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language.meta.declaration::ValueDeclaration|ceylon.language.meta.declaration::ClassDeclaration|ceylon.language.meta.declaration::InterfaceDeclaration,ceylon.language::Anything>"})
public class Strictfp 
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Strictfp, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    
    private static final long serialVersionUID = -5466420866210193001L;

    @Ignore
    public Strictfp(Strictfp$annotation$ ignored) {
    }
    public Strictfp() {
    }
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Strictfp.$TypeDescriptor$;
    }
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Strictfp.class);
    
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Strictfp$annotation$.class;
    }
}