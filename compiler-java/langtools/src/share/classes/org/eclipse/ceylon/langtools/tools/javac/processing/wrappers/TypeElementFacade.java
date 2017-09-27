package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import org.eclipse.ceylon.javax.lang.model.element.TypeElement;

public class TypeElementFacade extends ElementFacade implements javax.lang.model.element.TypeElement {

    public TypeElementFacade(TypeElement f) {
        super(f);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getInterfaces() {
        return Facades.facadeTypeMirrorList(((TypeElement)f).getInterfaces());
    }

    @Override
    public javax.lang.model.element.NestingKind getNestingKind() {
        return Facades.facade(((TypeElement)f).getNestingKind());
    }

    @Override
    public javax.lang.model.element.Name getQualifiedName() {
        return Facades.facade(((TypeElement)f).getQualifiedName());
    }

    @Override
    public javax.lang.model.type.TypeMirror getSuperclass() {
        return Facades.facade(((TypeElement)f).getSuperclass());
    }

    @Override
    public List<? extends javax.lang.model.element.TypeParameterElement> getTypeParameters() {
        return Facades.facadeTypeParameterElementList(((TypeElement)f).getTypeParameters());
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypeElementFacade == false)
            return false;
        return f.equals(((TypeElementFacade)obj).f);
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
