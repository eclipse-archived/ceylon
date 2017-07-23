package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Annotated;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Overloaded,ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language.meta.declaration::CallableConstructorDeclaration,ceylon.language::Anything>"})
public class Overloaded 
        implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Overloaded, Annotated, java.lang.Object>, 
            java.io.Serializable { 
    
    private static final long serialVersionUID = -5466420866210193001L;

    @Ignore
    public Overloaded(Overloaded$annotation$ ignored) {
    }
    public Overloaded() {
    }
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Overloaded.$TypeDescriptor$;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Overloaded.class);
    
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Overloaded$annotation$.class;
    }
}