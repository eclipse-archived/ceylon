package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;
import java.util.Map;

public class UnionType extends TypeDeclaration {

    public UnionType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String getName() {
        return getType().getProducedTypeName();
    }
    
    @Override
    public String getQualifiedNameString() {
        return getType().getProducedTypeQualifiedName();
    }
    
    @Override
    public String toString() {
        return "UnionType[" + getName() + "]";
    }
    
    @Override @Deprecated
    public List<String> getQualifiedName() {
        throw new UnsupportedOperationException("union types don't have names");
    }
    
    @Override
    public ProducedType getType() {
        if (getCaseTypes().size()==0) {
            return unit.getBottomDeclaration().getType();
        }
        else if (getCaseTypes().size()==1) {
            return getCaseTypes().get(0).getType();
        }
        else {
            return super.getType();
        }
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingMemberDeclarations(String startingWith, int proximity) {
    	//TODO: this can result in the wrong parameter types, and the
    	//      same bug also affects intersection types
    	Map<String, DeclarationWithProximity> result = super.getMatchingMemberDeclarations(startingWith, proximity);
		result.putAll(getCaseTypes().get(0).getDeclaration().getMatchingMemberDeclarations(startingWith, proximity));
    	for (ProducedType ct: getCaseTypes()) {
    		result.keySet().retainAll(ct.getDeclaration().getMatchingMemberDeclarations(startingWith, proximity).keySet());
    	}
    	return result;
    }

    @Override
    public DeclarationKind getDeclarationKind() {
        return null;
    }
    
    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("union types don't have well-defined equality");
    }

}
