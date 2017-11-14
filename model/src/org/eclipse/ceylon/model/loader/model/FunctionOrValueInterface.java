/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import java.util.List;

import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;

/**
 * Wrapper class which pretends a function or value is an interface, so that they can
 * be used to qualify local types in runtime reified checks.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class FunctionOrValueInterface extends Interface {

    private final TypedDeclaration declaration;

    public FunctionOrValueInterface(TypedDeclaration declaration){
        this.declaration = declaration;
    }
    
    @Override
    public String getQualifier() {
        return declaration.getQualifier();
    }
    
    @Override
    public String getName() {
        return declaration.getName();
    }
    
    @Override
    public Scope getContainer() {
        return declaration.getContainer();
    }
    
    @Override
    public List<TypeParameter> getTypeParameters() {
        return declaration.getTypeParameters();
    }
    
    @Override
    public boolean isParameterized() {
        return declaration.isParameterized();
    }

    @Override
    public Unit getUnit() {
        return declaration.getUnit();
    }
    
    public TypedDeclaration getUnderlyingDeclaration() {
        return declaration;
    }
}
