package com.redhat.ceylon.compiler.typechecker.model;

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
    public String toString() {
        return "UnionType[" + getName() + "]";
    }
    
    @Override
    public ProducedType getType() {
        List<ProducedType> cts = getCaseTypes();
        for (ProducedType pt: cts) {
            if (pt.isUnknown()) {
                return pt;
            }
        }
        if (cts.size()==0) {
            return unit.getNothingDeclaration().getType();
        }
        else if (cts.size()==1) {
            return cts.get(0).getType();
        }
        else {
            return super.getType();
        }
    }

    @Override
    public Map<String, DeclarationWithProximity> getMatchingMemberDeclarations(Scope scope, String startingWith, int proximity) {
    	//TODO: this can result in the wrong parameter types, and the
    	//      same bug also affects intersection types
    	Map<String, DeclarationWithProximity> result = super.getMatchingMemberDeclarations(scope, startingWith, proximity);
		TypeDeclaration d = getCaseTypes().get(0).getDeclaration();
		Iterator<Map.Entry<String, DeclarationWithProximity>> iter = d.getMatchingMemberDeclarations(scope, startingWith, proximity)
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

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        List<ProducedType> caseTypes = getCaseTypes();
        for(int i=0,l=caseTypes.size();i<l;i++){
            ret = (37 * ret) + caseTypes.get(i).hashCode();
        }
        return ret;
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || o instanceof UnionType == false)
            return false;
        UnionType b = (UnionType) o;
        List<ProducedType> caseTypesA = getCaseTypes();
        List<ProducedType> caseTypesB = b.getCaseTypes();
        if(caseTypesA.size() != caseTypesB.size())
            return false;
        for(int i=0,l=caseTypesA.size();i<l;i++){
            if(!caseTypesA.get(i).equals(caseTypesB.get(i)))
                return false;
        }
        return true;
    }
}
