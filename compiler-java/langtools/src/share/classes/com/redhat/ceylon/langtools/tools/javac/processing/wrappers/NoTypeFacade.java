package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.NoType;

public class NoTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.NoType {

    public NoTypeFacade(NoType f) {
        super(f);
    }

}
