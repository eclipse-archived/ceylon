package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
@SatisfiedTypes("ceylon.language::OptionalAnnotation<java.lang::Native, ceylon.language.meta.declaration::ValueDeclaration|ceylon.language.meta.declaration::FunctionDeclaration>")
public class Native 
        implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Native, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = 7112532346531035488L;

    @Ignore
    public Native(Native$annotation$ ignored) {
    }
    public Native() {
    }
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Native.$TypeDescriptor$;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Native.class);
    
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Native$annotation$.class;
    }
}