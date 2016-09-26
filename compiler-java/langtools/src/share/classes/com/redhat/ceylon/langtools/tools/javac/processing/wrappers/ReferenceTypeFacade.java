package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.ReferenceType;

public class ReferenceTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.ReferenceType {

    public ReferenceTypeFacade(ReferenceType f) {
        super(f);
    }

}
