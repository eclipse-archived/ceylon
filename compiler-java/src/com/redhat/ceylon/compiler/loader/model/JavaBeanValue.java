package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Normal value which allows us to remember if it's a "get" or "is" type of getter for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaBeanValue extends Value {
    private String getterName;
    private String setterName;

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getGetterName() {
        return getterName;
    }
    
}
