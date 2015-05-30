package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

public class InterfaceAlias extends Interface {
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) { 
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
        
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) {
            return et.getDeclaration().inherits(dec);
        }
        return false;
    }
    
}
