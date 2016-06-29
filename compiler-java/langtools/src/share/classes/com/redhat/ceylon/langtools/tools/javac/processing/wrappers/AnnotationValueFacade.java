package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import javax.lang.model.element.AnnotationValueVisitor;

import com.redhat.ceylon.javax.lang.model.element.AnnotationValue;

public class AnnotationValueFacade implements javax.lang.model.element.AnnotationValue {

    protected AnnotationValue f;

    public AnnotationValueFacade(AnnotationValue f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public Object getValue() {
        return f.getValue();
    }

}
