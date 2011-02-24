package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.List;

public class BottomType extends TypeDeclaration {

    @Override
    public List<Declaration> getMembers() {
        return Collections.emptyList();
    }
    
    @Override
    public String getName() {
        return "Bottom";
    }
    
    @Override
    public String getQualifiedName() {
        return getName();
    }

}
