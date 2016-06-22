package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.element.ExecutableElement;

public class ExecutableElementFacade extends ElementFacade implements javax.lang.model.element.ExecutableElement {

    public ExecutableElementFacade(ExecutableElement f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.AnnotationValue getDefaultValue() {
        return Facades.facade(((ExecutableElement)f).getDefaultValue());
    }

    @Override
    public List<? extends javax.lang.model.element.VariableElement> getParameters() {
        return Facades.facadeVariableElementList(((ExecutableElement)f).getParameters());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableElement)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableElement)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.element.TypeParameterElement> getTypeParameters() {
        return Facades.facadeTypeParameterElementList(((ExecutableElement)f).getTypeParameters());
    }

    @Override
    public boolean isVarArgs() {
        return ((ExecutableElement)f).isVarArgs();
    }

}
