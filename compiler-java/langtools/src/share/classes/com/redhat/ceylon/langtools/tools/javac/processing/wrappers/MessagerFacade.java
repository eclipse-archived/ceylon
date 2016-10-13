package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import com.redhat.ceylon.javax.annotation.processing.Messager;

public class MessagerFacade implements javax.annotation.processing.Messager {

    private Messager f;

    public MessagerFacade(Messager f) {
        this.f = f;
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1) {
        f.printMessage(Wrappers.wrap(arg0), arg1);
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2));
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2, AnnotationMirror arg3) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2), Facades.unfacade(arg3));
    }

    @Override
    public void printMessage(Kind arg0, CharSequence arg1, Element arg2, AnnotationMirror arg3, AnnotationValue arg4) {
        f.printMessage(Wrappers.wrap(arg0), arg1, Facades.unfacade(arg2), Facades.unfacade(arg3), Facades.unfacade(arg4));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MessagerFacade == false)
            return false;
        return f.equals(((MessagerFacade)obj).f);
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
