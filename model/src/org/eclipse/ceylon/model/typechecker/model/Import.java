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

public class Import {
    
    private TypeDeclaration typeDeclaration;
    private String alias;
    private Declaration declaration;
    private boolean wildcardImport;
    private boolean ambiguous;
    
    public Import() {}

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public boolean isAmbiguous() {
        return ambiguous;
    }
    
    public void setAmbiguous(boolean ambiguous) {
        this.ambiguous = ambiguous;
    }
    
    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }
    
    public void setTypeDeclaration(TypeDeclaration typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }
    
    @Override
    public String toString() {
        return "import " + 
                (typeDeclaration==null ? 
                        "" : typeDeclaration.getName() + " { ") + 
                alias + " = " + 
                declaration.getName() +
                (typeDeclaration==null ? 
                        "" : " }");
    }
    
    public boolean isWildcardImport() {
        return wildcardImport;
    }
    
    public void setWildcardImport(boolean wildcardImport) {
        this.wildcardImport = wildcardImport;
    }

}
