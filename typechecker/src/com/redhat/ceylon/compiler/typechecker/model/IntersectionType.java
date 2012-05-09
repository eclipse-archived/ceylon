package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType extends TypeDeclaration {

    public IntersectionType(Unit unit) {
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
        return "IntersectionType[" + getName() + "]";
    }
    
    @Override @Deprecated
    public List<String> getQualifiedName() {
        throw new UnsupportedOperationException("intersection types don't have names");
    }
    
    @Override
    public ProducedType getType() {
        if (getSatisfiedTypes().size()==0) {
            return unit.getVoidDeclaration().getType();
        }
        else if (getSatisfiedTypes().size()==1) {
            return getSatisfiedTypes().get(0).getType();
        }
        else {
            return super.getType();
        }
    }

    /**
     * Apply the distributive rule X&(Y|Z) == X&Y|X&Z to simplify the 
     * intersection to a canonical form with no parens. The result is 
     * a union of intersections, instead of an intersection of unions.
     */
	public TypeDeclaration canonicalize() {
		for (ProducedType st: getSatisfiedTypes()) {
			if (st.getDeclaration() instanceof UnionType) {
				TypeDeclaration result = new UnionType(unit);
				List<ProducedType> ulist = new ArrayList<ProducedType>();
				for (ProducedType ct: st.getDeclaration().getCaseTypes()) {
					List<ProducedType> ilist = new ArrayList<ProducedType>();
					for (ProducedType pt: getSatisfiedTypes()) {
						if (pt==st) {
							addToIntersection(ilist, ct, unit);
						}
						else {
							addToIntersection(ilist, pt, unit);
						}
					}
					IntersectionType it = new IntersectionType(unit);
					it.setSatisfiedTypes(ilist);
					addToUnion(ulist, it.canonicalize().getType());
				}
				result.setCaseTypes(ulist);
				return result;
			}
		}
		return this;
	}
	
    @Override
    public DeclarationKind getDeclarationKind() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("intersection types don't have well-defined equality");
    }

}
