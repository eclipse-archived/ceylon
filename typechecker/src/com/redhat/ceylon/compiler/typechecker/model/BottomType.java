package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Arrays;
import java.util.List;

public class BottomType extends TypeDeclaration {
    
    @Override
    public String getName() {
        return "Bottom";
    }
    
    @Override @Deprecated
    public List<String> getQualifiedName() {
        return Arrays.asList("ceylon","language","Bottom");
    }

    @Override
    public String getQualifiedNameString() {
        return "ceylon.language.Bottom";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
}
