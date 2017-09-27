package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.Transient;

@Ceylon(major = 8)
@Class
@ceylon.language.FinalAnnotation$annotation$
@ceylon.language.AnnotationAnnotation$annotation$
public class Transient 
        implements org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType, 
            ceylon.language.OptionalAnnotation<Transient, ceylon.language.meta.declaration.ValueDeclaration, 
            java.lang.Object>, java.io.Serializable { 
    private static final long serialVersionUID = 7200292480342940723L;

    @Ignore
    public Transient(Transient$annotation$ ignored) {
    }
    public Transient() {
    }
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return Transient.$TypeDescriptor$;
    }
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Transient.class);
    
    @java.lang.Override
    @org.eclipse.ceylon.compiler.java.metadata.Ignore
    public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType() {
        return Transient$annotation$.class;
    }
}