package com.redhat.ceylon.compiler.typechecker.model;


import java.util.List;

public class InterfaceAlias extends Interface {
    
    @Override
    public ProducedType getProducedType(ProducedType outerType, 
            List<ProducedType> typeArguments) {
        return aliasType(outerType, typeArguments);
    }
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
}
