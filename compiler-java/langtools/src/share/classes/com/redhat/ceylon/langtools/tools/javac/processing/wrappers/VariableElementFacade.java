package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.element.VariableElement;

public class VariableElementFacade extends ElementFacade implements javax.lang.model.element.VariableElement {

    public VariableElementFacade(VariableElement f) {
        super(f);
    }

    @Override
    public Object getConstantValue() {
        return ((VariableElement)f).getConstantValue();
    }
}
