package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersection;

import java.util.ArrayList;
import java.util.List;

/**
 * A "fake" declaration for an intersection type.
 * 
 * @author Gavin King
 *
 */
public class IntersectionType extends TypeDeclaration {

    public IntersectionType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    protected boolean needsSatisfiedTypes() {
        return false;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName() {
        return getType().asString();
    }
    
    @Override
    public String getName(Unit unit) {
    	return getType().asString(unit);
    }
    
    @Override
    public String getQualifiedNameString() {
        return getType().asQualifiedString();
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public Type getType() {
        List<Type> sts = getSatisfiedTypes();
        for (Type pt: sts) {
            if (pt==null || pt.isUnknown()) {
                List<Type> list = 
                        new ArrayList<Type>
                            (sts.size()-1);
                for (Type st: sts) {
                    if (st!=null && !st.isUnknown()) {
                        list.add(st);
                    }
                }
                return intersection(list, unit);
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
    
    /**
     * Apply the distributive rule X&(Y|Z) == X&Y|X&Z to simplify the 
     * intersection to a canonical form with no parens. The result is 
     * a union of intersections, instead of an intersection of unions.
     */
	public TypeDeclaration canonicalize() {
	    List<Type> sts = getSatisfiedTypes();
		if (sts.isEmpty()) {
	        return unit.getAnythingDeclaration();
	    }
	    else if (sts.size()==1) {
	    	Type st = sts.get(0);
            if (st.isNothing()) {
	    		return st.getDeclaration();
	    	}
	    }
		for (Type st: sts) {
			if (st.isUnion()) {
                List<Type> caseTypes = st.getCaseTypes();
				List<Type> ulist = 
				        new ArrayList<Type>
				            (caseTypes.size());
                for (Type ct: caseTypes) {
					List<Type> ilist = 
					        new ArrayList<Type>
					            (sts.size());
					for (Type pt: sts) {
						if (pt==st) {
							addToIntersection(ilist, ct, 
							        unit);
						}
						else {
							addToIntersection(ilist, pt, 
							        unit);
						}
					}
					Type it = canonicalIntersection(ilist, 
					        unit);
                    addToUnion(ulist, it);
				}
                TypeDeclaration result = 
                        new UnionType(unit);
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
        List<Type> stds = getSatisfiedTypes();
        for (int i=0, l=stds.size(); i<l; i++) {
            Type st = stds.get(i);
            st.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec.isAnything()) {
            return true;
        }
        else {
            List<Type> sts = getSatisfiedTypes();
            for (int i = 0, s=sts.size(); i<s; i++) {
                Type st = sts.get(i);
                if (st.getDeclaration().inherits(dec)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("intersection types don't have well-defined equality");
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        List<Type> satisfiedTypes = getSatisfiedTypes();
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
        List<Type> satisfiedTypesA = getSatisfiedTypes();
        List<Type> satisfiedTypesB = b.getSatisfiedTypes();
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
