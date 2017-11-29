/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.ValueFlags.VARIABLE;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.model.loader.mirror.MethodMirror;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Normal value which allows us to remember if it's a "get" or "is" type of getter for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaBeanValue extends Value implements LocalDeclarationContainer {
    private String getterName;
    private String setterName;
    
    private Map<String,Declaration> localDeclarations;
    public final MethodMirror mirror;

    public JavaBeanValue(MethodMirror mirror) {
        this.mirror = mirror;
    }

    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }

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
    
    @Override
    public Declaration getLocalDeclaration(String name) {
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }

    @Override
    public boolean isJava() {
        Scope container = getContainer();
        while(container != null && container instanceof Declaration == false)
            container = container.getContainer();
        return container != null ? ((Declaration) container).isJava() : false;
    }
    
    @Override
    public boolean isVariable() {
        if ((flags&VARIABLE)==0 && actualCompleter != null) {
            completeActual();
        }
        return (flags&VARIABLE)!=0;
    }
}
