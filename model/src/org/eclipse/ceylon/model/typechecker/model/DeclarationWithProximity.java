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

public class DeclarationWithProximity {
    private Declaration declaration;
    private int proximity;
    private String name;
    private NamedArgumentList namedArgumentList; 
    private boolean unimported;
    private boolean alias;
    
    public NamedArgumentList getNamedArgumentList() {
        return namedArgumentList;
    }
    
    public DeclarationWithProximity(Parameter parameter, NamedArgumentList nal) {
        this.declaration = parameter.getModel();
        this.proximity = 0;
        this.name = declaration.getName();
        this.namedArgumentList = nal;
    }
    
    public DeclarationWithProximity(Declaration declaration, int proximity) {
        this.declaration = declaration;
        this.proximity = proximity;
        this.name = declaration.getName();
    }
    
    public DeclarationWithProximity(Declaration declaration, int proximity, boolean unimported) {
        this.declaration = declaration;
        this.proximity = proximity;
        this.name = declaration.getName();
        this.unimported = unimported;
    }
    
    public DeclarationWithProximity(Declaration declaration, DeclarationWithProximity dwp) {
        this.declaration = declaration;
        this.proximity = dwp.proximity;
        this.name = dwp.name;
        this.unimported = dwp.unimported;
    }
    
    public DeclarationWithProximity(Import imp, int proximity) {
        this.declaration = imp.getDeclaration();
        this.proximity = proximity;
        this.name = imp.getAlias();
    }
    
    public DeclarationWithProximity(String alias, Declaration member, int proximity) {
        this.name = alias;
        this.alias = true;
        this.declaration = member;
        this.proximity = proximity;
    }

    public Declaration getDeclaration() {
        return declaration;
    }
    
    public int getProximity() {
        return proximity;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isUnimported() {
        return unimported;
    }

    public boolean isAlias(){
        return alias;
    }
    
    @Override
    public String toString() {
        return name + ":" + declaration.toString() + "@" + proximity + "(alias: "+alias+")";
    }

    public String packageName() {
        return declaration.isToplevel() ? 
                declaration
                    .getContainer()
                    .getQualifiedNameString() :
                null;
    }
    
    public String realName(Unit unit) {
        return declaration.getName(unit);
    }
    
}
