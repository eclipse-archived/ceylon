package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnionType extends TypeDeclaration {

    public UnionType(Unit unit) {
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
    protected boolean needsSatisfiedTypes() {
        return false;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public ProducedType getType() {
        List<ProducedType> cts = getCaseTypes();
        for (ProducedType pt: cts) {
            if (pt==null || pt.isUnknown()) {
                return unit.getUnknownType();
            }
        }
        if (cts.size()==0) {
            return unit.getNothingType();
        }
        else if (cts.size()==1) {
            return cts.get(0).getType();
        }
        else {
            return super.getType();
        }
    }

    @Override
    public Map<String, DeclarationWithProximity> 
    getMatchingMemberDeclarations(Unit unit, Scope scope, 
            String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = 
    	        super.getMatchingMemberDeclarations(unit, 
    	                scope, startingWith, proximity);
		TypeDeclaration d = 
		        getCaseTypes().get(0).getDeclaration();
		Iterator<Map.Entry<String, DeclarationWithProximity>> iter = 
		        d.getMatchingMemberDeclarations(unit, scope, 
		                startingWith, proximity)
		                    .entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, DeclarationWithProximity> e = 
                    iter.next();
		    Declaration member = 
		            getMember(e.getKey(), null, false);
            if (member!=null) {
		        result.put(e.getKey(), 
		                new DeclarationWithProximity(member, 
		                        e.getValue()));
		    }
		}
    	return result;
    }

    @Override
    public DeclarationKind getDeclarationKind() {
        return null;
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        List<ProducedType> cts = getCaseTypes();
        if (!cts.isEmpty()) {
//            ProducedType type = getType();
//        for (int i=0, size=cts.size(); i<size; i++) {
            //actually the loop is unnecessary, we
            //only need to consider the first case
            List<TypeDeclaration> candidates =
                    new ArrayList<TypeDeclaration>(results);
            ProducedType firstCase = cts.get(0);
            firstCase.getDeclaration()
                .collectSupertypeDeclarations(candidates);
            for (int j=results.size(), 
                    max=candidates.size(); 
                    j<max; j++) {
                TypeDeclaration std = candidates.get(j);
//                ProducedType st = type.getSupertype(std);
//                if (st!=null && !st.isNothing()) {
//                    results.add(std);
//                }
                if (inherits(std)) {
                    results.add(std);
                }
            }
        }
//        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        for (ProducedType ct: getCaseTypes()) {
            if (!ct.getDeclaration().inherits(dec)) {
                return false;
            }
        }
        ProducedType st = getType().getSupertype(dec);
        return st!=null && !st.isNothing();
    }
    
    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("union types don't have well-defined equality");
    }    

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        List<ProducedType> caseTypes = getCaseTypes();
        for(int i=0,l=caseTypes.size();i<l;i++) {
            ret = (37 * ret) + caseTypes.get(i).hashCode();
        }
        return ret;
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || !(o instanceof UnionType)) {
            return false;
        }
        UnionType b = (UnionType) o;
        List<ProducedType> caseTypesA = getCaseTypes();
        List<ProducedType> caseTypesB = b.getCaseTypes();
        if (caseTypesA.size() != caseTypesB.size()) {
            return false;
        }
        for (int i=0,l=caseTypesA.size(); i<l; i++) {
            if (!caseTypesA.get(i).equals(caseTypesB.get(i))) {
                return false;
            }
        }
        return true;
    }
}
