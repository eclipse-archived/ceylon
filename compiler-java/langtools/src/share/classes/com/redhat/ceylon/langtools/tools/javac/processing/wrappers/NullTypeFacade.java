package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.NullType;

public class NullTypeFacade extends ReferenceTypeFacade implements javax.lang.model.type.NullType {

    public NullTypeFacade(NullType f) {
        super(f);
    }

}
