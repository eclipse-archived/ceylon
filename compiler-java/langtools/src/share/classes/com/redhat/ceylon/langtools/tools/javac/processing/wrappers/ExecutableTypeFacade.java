package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.type.ExecutableType;

public class ExecutableTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.ExecutableType {

    public ExecutableTypeFacade(ExecutableType type) {
        super(type);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getParameterTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getParameterTypes());
    }

    @Override
    public javax.lang.model.type.TypeMirror getReturnType() {
        return Facades.facade(((ExecutableType)f).getReturnType());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getThrownTypes() {
        return Facades.facadeTypeMirrorList(((ExecutableType)f).getThrownTypes());
    }

    @Override
    public List<? extends javax.lang.model.type.TypeVariable> getTypeVariables() {
        return Facades.facadeTypeVariableList(((ExecutableType)f).getTypeVariables());
    }

}
