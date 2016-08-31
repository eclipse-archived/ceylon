package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.PrimitiveType;

public class PrimitiveTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.PrimitiveType {

    public PrimitiveTypeFacade(PrimitiveType f) {
        super(f);
    }

}
