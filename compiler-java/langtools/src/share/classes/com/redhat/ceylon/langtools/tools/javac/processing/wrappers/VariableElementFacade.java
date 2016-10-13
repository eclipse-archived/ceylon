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
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VariableElementFacade == false)
            return false;
        return f.equals(((VariableElementFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
