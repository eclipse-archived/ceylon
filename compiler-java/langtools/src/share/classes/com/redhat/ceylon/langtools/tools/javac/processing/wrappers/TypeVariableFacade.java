package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.TypeVariable;

public class TypeVariableFacade extends ReferenceTypeFacade implements javax.lang.model.type.TypeVariable {

    public TypeVariableFacade(TypeVariable f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.Element asElement() {
        return Facades.facade(((TypeVariable)f).asElement());
    }

    @Override
    public javax.lang.model.type.TypeMirror getLowerBound() {
        return Facades.facade(((TypeVariable)f).getLowerBound());
    }

    @Override
    public javax.lang.model.type.TypeMirror getUpperBound() {
        return Facades.facade(((TypeVariable)f).getUpperBound());
    }

}
