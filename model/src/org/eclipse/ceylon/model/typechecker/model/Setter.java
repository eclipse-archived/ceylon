/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

/**
 * An attribute setter.
 *
 * @author Gavin King
 */
public class Setter extends FunctionOrValue implements Scope {

    private Value getter;
    private Parameter parameter;

    public Value getGetter() {
        return getter;
    }

    public void setGetter(Value getter) {
        this.getter = getter;
    }
    
    public Parameter getParameter() {
        return parameter;
    }
    
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
    
    @Override
    public String getQualifiedNameString() {
        if (getter!=null) {
            return getter.getQualifiedNameString();
        }
        else {
            return super.getQualifiedNameString();
        }
    }
    
    @Override
    public boolean isShared() {
        if (getter!=null) {
            return getter.isShared();
        }
        else {
            return super.isShared();
        }
    }
    
    @Override
    public boolean isVariable() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.SETTER;
    }
    
    @Override
    public boolean isSetter() {
        return true;
    }

    @Override
    public String getQualifier() {
        Value getter = getGetter();
        if(getter == null)
            return null;
        String getterQualifier = getter.getQualifier();
        // use the same qualifier as the getter with a $setter$ prefix
        return getterQualifier == null ? null : "$setter$"+getterQualifier;
    }
    
    @Override
    public String toString() {
        return "assign " + toStringName();
    }
}
