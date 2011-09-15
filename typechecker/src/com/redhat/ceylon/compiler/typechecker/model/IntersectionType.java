package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;

import java.util.ArrayList;
import java.util.List;

public class IntersectionType extends TypeDeclaration {

    @Override
    public String getName() {
        String name = "";
        for (ProducedType pt: getSatisfiedTypes()) {
            if (pt==null) {
                name+="<unknown>";
            }
            else {
                name+=pt.getProducedTypeName();
            }
            name+="&";
        }
        return name.substring(0,name.length()-1);
    }
    
    @Override
    public String getQualifiedNameString() {
        String name = "";
        for (ProducedType pt: getSatisfiedTypes()) {
            if (pt==null) {
                name+="<unknown>";
            }
            else {
                name+=pt.getProducedTypeQualifiedName();
            }
            name+="&";
        }
        return name.substring(0,name.length()-1);
    }
    
    @Override
    public String toString() {
        return "IntersectionType[" + getName() + "]";
    }
    
    @Override @Deprecated
    public List<String> getQualifiedName() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ProducedType getType() {
        if (getSatisfiedTypes().size()==0) {
            //TODO: should return Void type
            throw new RuntimeException();
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
				TypeDeclaration result = new UnionType();
				List<ProducedType> ulist = new ArrayList<ProducedType>();
				for (ProducedType ct: st.getDeclaration().getCaseTypes()) {
					List<ProducedType> ilist = new ArrayList<ProducedType>();
					for (ProducedType pt: getSatisfiedTypes()) {
						if (pt==st) {
							addToIntersection(ilist, ct);
						}
						else {
							addToIntersection(ilist, pt);
						}
					}
					IntersectionType it = new IntersectionType();
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
    DeclarationKind getDeclarationKind() {
        return null;
    }

}
