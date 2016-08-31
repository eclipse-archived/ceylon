package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.element.TypeParameterElement;


public class TypeParameterElementFacade extends ElementFacade implements javax.lang.model.element.TypeParameterElement {

    public TypeParameterElementFacade(TypeParameterElement f) {
        super(f);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getBounds() {
        return Facades.facadeTypeMirrorList(((TypeParameterElement)f).getBounds());
    }

    @Override
    public javax.lang.model.element.Element getGenericElement() {
        return Facades.facade(((TypeParameterElement)f).getGenericElement());
    }

}
