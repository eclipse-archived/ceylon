package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.ErrorType;

public class ErrorTypeFacade extends DeclaredTypeFacade implements javax.lang.model.type.ErrorType {

    public ErrorTypeFacade(ErrorType f) {
        super(f);
    }

}
