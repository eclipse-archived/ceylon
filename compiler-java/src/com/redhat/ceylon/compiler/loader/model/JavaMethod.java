package com.redhat.ceylon.compiler.loader.model;

import com.redhat.ceylon.compiler.typechecker.model.Method;

/**
 * Instance method that allows us to remember the exact method name
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaMethod extends Method {

    private String realName;

    public void setRealName(String name) {
        this.realName = name;
    }

    public String getRealName(){
        return realName;
    }
}
