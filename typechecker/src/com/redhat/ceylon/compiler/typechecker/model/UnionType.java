package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

public class UnionType extends TypeDeclaration {

    @Override
    public String getName() {
        String name = "";
        for (ProducedType pt: caseTypes) {
            if (pt==null) {
                name+="<unknown>";
            }
            else {
                name+=pt.getProducedTypeName();
            }
            name+="|";
        }
        return name.substring(0,name.length()-1);
    }
    
    @Override
    public String toString() {
        return "UnionType[" + getName() + "]";
    }
    
    @Override
    public List<String> getQualifiedName() {
        return null;
    }
    
    @Override
    public ProducedType getType() {
        if (getCaseTypes().size()==0) {
            return new BottomType().getType();
        }
        else if (getCaseTypes().size()==1) {
            return getCaseTypes().get(0).getType();
        }
        else {
            return super.getType();
        }
    }

}
