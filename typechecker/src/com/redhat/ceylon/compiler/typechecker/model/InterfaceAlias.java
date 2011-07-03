package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.List;

public class InterfaceAlias extends Interface {
    
    @Override
    public ProducedType getProducedType(ProducedType outerType, 
            List<ProducedType> typeArguments) {
        return getExtendedType().substitute(arguments(this, outerType, typeArguments));
    }
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
}
