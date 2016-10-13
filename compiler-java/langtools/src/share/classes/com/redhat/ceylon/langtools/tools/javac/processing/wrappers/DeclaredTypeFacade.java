package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.type.DeclaredType;

public class DeclaredTypeFacade extends ReferenceTypeFacade implements javax.lang.model.type.DeclaredType {

    public DeclaredTypeFacade(DeclaredType f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.Element asElement() {
        return Facades.facade(((DeclaredType)f).asElement());
    }

    @Override
    public javax.lang.model.type.TypeMirror getEnclosingType() {
        return Facades.facade(((DeclaredType)f).getEnclosingType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getTypeArguments() {
        return Facades.facadeTypeMirrorList(((DeclaredType)f).getTypeArguments());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DeclaredTypeFacade == false)
            return false;
        return f.equals(((DeclaredTypeFacade)obj).f);
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
