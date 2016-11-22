package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
public class Volatile 
        implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Volatile, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = -2399372241815625080L;

    @Ignore
    public Volatile(Volatile$annotation$ ignored) {
    }
    public Volatile() {
    }
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Volatile.$TypeDescriptor$;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Volatile.class);
    
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Volatile$annotation$.class;
    }
}