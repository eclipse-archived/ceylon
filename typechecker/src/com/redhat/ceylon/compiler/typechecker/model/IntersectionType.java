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
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
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
        return "IntersectionType[" + getName() + "]";
    }
    
    @Override
    public ProducedType getType() {
        List<ProducedType> sts = getSatisfiedTypes();
        for (ProducedType pt: sts) {
            if (pt.isUnknown()) {
                List<ProducedType> list = new ArrayList<ProducedType>(sts.size()-1);
                for (ProducedType st: sts) {
                    if (!st.isUnknown()) {
                        list.add(st);
                    }
                }
                IntersectionType it = new IntersectionType(unit);
                it.setSatisfiedTypes(list);
                return it.getType();
            }
        }
        if (sts.isEmpty()) {
            return unit.getType(unit.getAnythingDeclaration());
        }
        else if (sts.size()==1) {
            return sts.get(0).getType();
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
	    List<ProducedType> sts = getSatisfiedTypes();
		if (sts.isEmpty()) {
	        return unit.getAnythingDeclaration();
	    }
	    else if (sts.size()==1) {
	    	ProducedType st = sts.get(0);
            if (st.isNothing()) {
	    		return st.getDeclaration();
	    	}
	    }
		for (ProducedType st: sts) {
			if (st.getDeclaration() instanceof UnionType) {
				TypeDeclaration result = new UnionType(unit);
                List<ProducedType> caseTypes = st.getDeclaration().getCaseTypes();
				List<ProducedType> ulist = new ArrayList<ProducedType>(caseTypes.size());
                for (ProducedType ct: caseTypes) {
					List<ProducedType> ilist = new ArrayList<ProducedType>(sts.size());
					for (ProducedType pt: sts) {
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
