package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.element.ElementVisitor;
import com.redhat.ceylon.javax.lang.model.element.ExecutableElement;
import com.redhat.ceylon.javax.lang.model.element.PackageElement;
import com.redhat.ceylon.javax.lang.model.element.TypeElement;
import com.redhat.ceylon.javax.lang.model.element.TypeParameterElement;
import com.redhat.ceylon.javax.lang.model.element.VariableElement;

public class ElementVisitorWrapper<R, P> implements ElementVisitor<R, P> {

    private javax.lang.model.element.ElementVisitor<R, P> d;

    public ElementVisitorWrapper(javax.lang.model.element.ElementVisitor<R, P> d) {
        this.d = d;
    }

    @Override
    public R visit(Element e, P p) {
        return d.visit(Facades.facade(e), p);
    }

    @Override
    public R visit(Element e) {
        return d.visit(Facades.facade(e));
    }

    @Override
    public R visitPackage(PackageElement e, P p) {
        return d.visitPackage(Facades.facade(e), p);
    }

    @Override
    public R visitType(TypeElement e, P p) {
        return d.visitType(Facades.facade(e), p);
    }

    @Override
    public R visitVariable(VariableElement e, P p) {
        return d.visitVariable(Facades.facade(e), p);
    }

    @Override
    public R visitExecutable(ExecutableElement e, P p) {
        return d.visitExecutable(Facades.facade(e), p);
    }

    @Override
    public R visitTypeParameter(TypeParameterElement e, P p) {
        return d.visitTypeParameter(Facades.facade(e), p);
    }

    @Override
    public R visitUnknown(Element e, P p) {
        try{
            return d.visitUnknown(Facades.facade(e), p);
        }catch(javax.lang.model.element.UnknownElementException x){
            throw Wrappers.wrap(x);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ElementVisitorWrapper == false)
            return false;
        return d.equals(((ElementVisitorWrapper)obj).d);
    }
    
    @Override
    public int hashCode() {
        return d.hashCode();
    }
    
    @Override
    public String toString() {
        return d.toString();
    }
}
