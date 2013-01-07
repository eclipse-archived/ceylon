package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Iterator;
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
    public String getName(Unit unit) {
    	return getType().getProducedTypeName(unit);
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
            return unit.getNothingDeclaration().getType();
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
		TypeDeclaration d = getCaseTypes().get(0).getDeclaration();
		Iterator<Map.Entry<String, DeclarationWithProximity>> iter = d.getMatchingMemberDeclarations(startingWith, proximity)
		        .entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, DeclarationWithProximity> e = iter.next();
		    if (getMember(e.getKey(), null, false)!=null) {
		        result.put(e.getKey(), e.getValue());
		    }
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
