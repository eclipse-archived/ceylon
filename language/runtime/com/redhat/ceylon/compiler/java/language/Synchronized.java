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
@com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes({"ceylon.language::OptionalAnnotation<java.lang::Synchronized,ceylon.language.meta.declaration::FunctionDeclaration|ceylon.language.meta.declaration::ValueDeclaration,ceylon.language::Anything>"})
public class Synchronized 
        implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Synchronized, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = 7469006294191443025L;

    @Ignore
    public Synchronized(Synchronized$annotation$ ignored) {
    }
    public Synchronized() {
    }
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Synchronized.$TypeDescriptor$;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Synchronized.class);
    
    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Synchronized$annotation$.class;
    }
}