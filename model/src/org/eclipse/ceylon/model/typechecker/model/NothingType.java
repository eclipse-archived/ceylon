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

import java.util.List;

/**
 * The bottom type Nothing.
 * 
 * @author Gavin King
 *
 */
public class NothingType extends TypeDeclaration {
    
    public NothingType(Unit unit) {
        if (unit==null) {
            throw new IllegalArgumentException("null unit");
        }
        this.unit = unit;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmptyType() {
        return true;
    }
    
    @Override
    public boolean isTupleType() {
        return true;
    }
    
    @Override
    public boolean isSequenceType() {
        return true;
    }
    
    @Override
    public boolean isSequentialType() {
        return true;
    }
    
    @Override
    public String getName() {
        return "Nothing";
    }
    
    @Override
    public Scope getContainer() {
        return unit.getAnythingDeclaration().getContainer();
    }
    
    @Override
    public boolean isClassMember() {
        return false;
    }
    
    @Override
    public boolean isInterfaceMember() {
        return false;
    }
    
    @Override
    public boolean isClassOrInterfaceMember() {
        return false;
    }
    
    @Override
    public boolean isToplevel() {
        return true;
    }
    
    @Override
    public String getQualifiedNameString() {
        return "ceylon.language::Nothing";
    }
    
    @Override
    public String toString() {
        return "type Nothing";
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        return true;
    }
    
    @Override
    public boolean equals(Object object) {
        return object instanceof NothingType;
    }

    @Override
    protected int hashCodeForCache() {
        return 17987123;
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        return equals(o);
    }
    
    @Override
    protected boolean needsSatisfiedTypes() {
        return false;
    }
    
}
