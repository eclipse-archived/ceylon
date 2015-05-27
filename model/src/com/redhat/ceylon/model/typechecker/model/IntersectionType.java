package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.Util.addToUnion;

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
        return getName();
    }
    
    @Override
    public ProducedType getType() {
        List<ProducedType> sts = getSatisfiedTypes();
        for (ProducedType pt: sts) {
            if (pt==null || pt.isUnknown()) {
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (sts.size()-1);
                for (ProducedType st: sts) {
                    if (!st.isUnknown()) {
                        list.add(st);
                    }
                }
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(list);
                return it.getType();
            }
        }
        if (sts.isEmpty()) {
            return unit.getAnythingType();
        }
        else if (sts.size()==1) {
            return sts.get(0).getType();
        }
        else {
            return super.getType();
        }
    }
    
    public TypeDeclaration canonicalize() {
        return canonicalize(true);
    }

    /**
     * Apply the distributive rule X&(Y|Z) == X&Y|X&Z to simplify the 
     * intersection to a canonical form with no parens. The result is 
     * a union of intersections, instead of an intersection of unions.
     */
	public TypeDeclaration canonicalize(boolean reduceDisjointTypes) {
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
			if (st.isUnion()) {
				TypeDeclaration result = 
				        new UnionType(unit);
                List<ProducedType> caseTypes = 
                        st.getCaseTypes();
				List<ProducedType> ulist = 
				        new ArrayList<ProducedType>
				            (caseTypes.size());
                for (ProducedType ct: caseTypes) {
					List<ProducedType> ilist = 
					        new ArrayList<ProducedType>
					            (sts.size());
					for (ProducedType pt: sts) {
						if (pt==st) {
							addToIntersection(ilist, ct, 
							        unit, 
							        reduceDisjointTypes);
						}
						else {
							addToIntersection(ilist, pt, 
							        unit, reduceDisjointTypes);
						}
					}
					IntersectionType it = 
					        new IntersectionType(unit);
					it.setSatisfiedTypes(ilist);
					addToUnion(ulist, 
					        it.canonicalize(reduceDisjointTypes)
					            .getType());
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
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        List<ProducedType> stds = getSatisfiedTypes();
        for (int i=0, l=stds.size(); i<l; i++) {
            ProducedType st = stds.get(i);
            st.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("intersection types don't have well-defined equality");
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        List<ProducedType> satisfiedTypes = getSatisfiedTypes();
        for(int i=0, l=satisfiedTypes.size(); i<l; i++) {
            ret = (37 * ret) + satisfiedTypes.get(i).hashCode();
        }
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || !(o instanceof IntersectionType)) {
            return false;
        }
        IntersectionType b = (IntersectionType) o;
        List<ProducedType> satisfiedTypesA = getSatisfiedTypes();
        List<ProducedType> satisfiedTypesB = b.getSatisfiedTypes();
        if (satisfiedTypesA.size() != satisfiedTypesB.size()) {
            return false;
        }
        for (int i=0, l=satisfiedTypesA.size(); i<l; i++) {
            if (!satisfiedTypesA.get(i).equals(satisfiedTypesB.get(i))) {
                return false;
            }
        }
        return true;
    }
}
