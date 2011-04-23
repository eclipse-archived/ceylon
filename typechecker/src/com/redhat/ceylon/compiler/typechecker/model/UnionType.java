package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.List;

public class UnionType extends TypeDeclaration {

    @Override
    public List<Declaration> getMembers() {
        return Collections.emptyList();
    }
    
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

}
