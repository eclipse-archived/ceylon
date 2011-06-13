package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

public class BottomType extends TypeDeclaration {
    
    @Override
    public String getName() {
        return "Bottom";
    }
    
    @Override
    public List<String> getQualifiedName() {
        return null;
    }

}
