package com.redhat.ceylon.compiler.typechecker.model;

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

}
