package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;

public class TypeMirrorFacade implements javax.lang.model.type.TypeMirror {

    protected TypeMirror f;

    public TypeMirrorFacade(TypeMirror f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(javax.lang.model.type.TypeVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public javax.lang.model.type.TypeKind getKind() {
        return Facades.facade(f.getKind());
    }

    @Override
    public String toString() {
        return f.toString();
    }

    // Java 8 method    
//    @Override
    public <A extends Annotation> A getAnnotation(Class<A> arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotation", Class.class);
            return (A) method.invoke(f, arg0);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Java 8 method
//    @Override
    public List<? extends javax.lang.model.element.AnnotationMirror> getAnnotationMirrors() {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotationMirrors");
            return Facades.facadeAnnotationMirrors((List<AnnotationMirror>) method.invoke(f));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Java 8 method
//    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> arg0) {
        // must use reflection for it to work on Java 7
        try {
            Method method = TypeMirror.class.getMethod("getAnnotationsByType", Class.class);
            return (A[]) method.invoke(f, arg0);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
