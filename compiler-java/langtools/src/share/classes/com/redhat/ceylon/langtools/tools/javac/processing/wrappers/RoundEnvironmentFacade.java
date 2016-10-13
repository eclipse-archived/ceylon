package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.lang.annotation.Annotation;
import java.util.Set;

import com.redhat.ceylon.javax.annotation.processing.RoundEnvironment;

public class RoundEnvironmentFacade implements javax.annotation.processing.RoundEnvironment {

    private RoundEnvironment f;

    public RoundEnvironmentFacade(RoundEnvironment f) {
        this.f = f;
    }

    @Override
    public boolean processingOver() {
        return f.processingOver();
    }

    @Override
    public boolean errorRaised() {
        return f.errorRaised();
    }

    @Override
    public Set<? extends javax.lang.model.element.Element> getRootElements() {
        return Facades.facadeElementSet(f.getRootElements());
    }

    @Override
    public Set<? extends javax.lang.model.element.Element> getElementsAnnotatedWith(javax.lang.model.element.TypeElement a) {
        return Facades.facadeElementSet(f.getElementsAnnotatedWith(Facades.unfacade(a)));
    }

    @Override
    public Set<? extends javax.lang.model.element.Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
        return Facades.facadeElementSet(f.getElementsAnnotatedWith(a));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RoundEnvironmentFacade == false)
            return false;
        return f.equals(((RoundEnvironmentFacade)obj).f);
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
