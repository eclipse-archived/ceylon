package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.element.ElementKind;
import com.redhat.ceylon.javax.lang.model.element.ElementVisitor;
import com.redhat.ceylon.javax.lang.model.element.Modifier;
import com.redhat.ceylon.javax.lang.model.element.Name;
import com.redhat.ceylon.javax.lang.model.element.NestingKind;
import com.redhat.ceylon.javax.lang.model.element.TypeElement;
import com.redhat.ceylon.javax.lang.model.element.TypeParameterElement;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;

public class TypeElementWrapper implements TypeElement {

    private javax.lang.model.element.TypeElement d;

    public TypeElementWrapper(javax.lang.model.element.TypeElement d) {
        this.d = d;
    }

    @Override
    public TypeMirror asType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ElementKind getKind() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Modifier> getModifiers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NestingKind getNestingKind() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Name getQualifiedName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Name getSimpleName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TypeMirror getSuperclass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Element getEnclosingElement() {
        // TODO Auto-generated method stub
        return null;
    }

}
