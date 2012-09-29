package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

public class TypeAlias extends TypeDeclaration {

    @Override
    public ProducedType getProducedType(ProducedType outerType, 
            List<ProducedType> typeArguments) {
    	return aliasType(outerType, typeArguments);
    }
    
	@Override
	public DeclarationKind getDeclarationKind() {
		return DeclarationKind.TYPE;
	}
	
	@Override
	public boolean isAlias() {
		return true;
	}
	
}
