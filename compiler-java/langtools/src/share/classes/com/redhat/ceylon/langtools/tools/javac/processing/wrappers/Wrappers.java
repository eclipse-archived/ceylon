package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.element.AnnotationValueVisitor;
import com.redhat.ceylon.javax.lang.model.element.ElementVisitor;

import com.redhat.ceylon.javax.lang.model.SourceVersion;
import com.redhat.ceylon.javax.lang.model.element.TypeElement;
import com.redhat.ceylon.javax.lang.model.type.TypeVisitor;
import com.redhat.ceylon.javax.tools.Diagnostic;
import com.redhat.ceylon.javax.tools.JavaFileObject;

public class Wrappers {

    public static SourceVersion wrap(javax.lang.model.SourceVersion sourceVersion) {
        return SourceVersion.valueOf(sourceVersion.name());
    }
    
    public static Diagnostic.Kind wrap(javax.tools.Diagnostic.Kind kind) {
        return Diagnostic.Kind.valueOf(kind.name());
    }


    public static TypeElement wrap(javax.lang.model.element.TypeElement a) {
        return new TypeElementWrapper(a);
    }

    public static <R, P> ElementVisitor<R, P> wrap(javax.lang.model.element.ElementVisitor<R, P> v) {
        return new ElementVisitorWrapper<R, P>(v);
    }

    public static <R, P> TypeVisitor<R, P> wrap(javax.lang.model.type.TypeVisitor<R, P> v) {
        return new TypeVisitorWrapper<R, P>(v);
    }

    public static <R, P> AnnotationValueVisitor<R, P> wrap(javax.lang.model.element.AnnotationValueVisitor<R, P> v) {
        return new AnnotationValueVisitorWrapper<R, P>(v);
    }

    public static JavaFileObject.Kind wrap(javax.tools.JavaFileObject.Kind kind) {
        return JavaFileObject.Kind.valueOf(kind.name());
    }

}
