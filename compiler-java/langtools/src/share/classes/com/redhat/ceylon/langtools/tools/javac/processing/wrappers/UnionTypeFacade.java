package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import com.redhat.ceylon.javax.lang.model.type.UnionType;

public class UnionTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.UnionType {

    public UnionTypeFacade(UnionType f) {
        super(f);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getAlternatives() {
        return Facades.facadeTypeMirrorList(((UnionType)f).getAlternatives());
    }

}
