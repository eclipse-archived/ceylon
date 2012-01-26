package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.typechecker.model.Value;

/**
 * Normal value which allows us to remember if it's a "get" or "is" type of getter for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaBeanValue extends Value {
    private boolean isGet = false;

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean isGet) {
        this.isGet = isGet;
    }
    
}
