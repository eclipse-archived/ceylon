package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.Map;

import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;

public class AnnotationMirrorFacade implements javax.lang.model.element.AnnotationMirror {

    protected AnnotationMirror f;

    public AnnotationMirrorFacade(AnnotationMirror f) {
        this.f = f;
    }

    @Override
    public javax.lang.model.type.DeclaredType getAnnotationType() {
        return Facades.facade(f.getAnnotationType());
    }

    @Override
    public Map<? extends javax.lang.model.element.ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> getElementValues() {
        return Facades.facadeElementValues(f.getElementValues());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnnotationMirrorFacade == false)
            return false;
        return f.equals(((AnnotationMirrorFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
