package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

public class ElementFacade implements Element {

    protected com.redhat.ceylon.javax.lang.model.element.Element f;

    public ElementFacade(com.redhat.ceylon.javax.lang.model.element.Element f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public TypeMirror asType() {
        return Facades.facade(f.asType());
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return f.getAnnotation(annotationType);
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return Facades.facadeAnnotationMirrors(f.getAnnotationMirrors());
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return Facades.facadeElementList(f.getEnclosedElements());
    }

    @Override
    public Element getEnclosingElement() {
        return Facades.facade(f.getEnclosingElement());
    }

    @Override
    public ElementKind getKind() {
        return Facades.facade(f.getKind());
    }

    @Override
    public Set<Modifier> getModifiers() {
        return Facades.facadeModifiers(f.getModifiers());
    }

    @Override
    public Name getSimpleName() {
        return Facades.facade(f.getSimpleName());
    }

}
