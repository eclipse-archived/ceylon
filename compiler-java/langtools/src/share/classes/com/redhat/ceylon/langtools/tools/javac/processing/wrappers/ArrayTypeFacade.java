package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.ArrayType;

public class ArrayTypeFacade extends ReferenceTypeFacade implements javax.lang.model.type.ArrayType {

    public ArrayTypeFacade(ArrayType f) {
        super(f);
    }

    @Override
    public javax.lang.model.type.TypeMirror getComponentType() {
        return Facades.facade(((ArrayType)f).getComponentType());
    }

}
