package com.redhat.ceylon.model.typechecker.model;


public final class Enumerated extends Class {
    
    @Override
    public boolean isConstructor() {
        return true;
    }
    
    //TODO: this is what I really want to do here:
    
    /*@Override
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }
    
    @Override
    public Type getDeclaringType(Declaration d) {
        if (d.isMember()) {
            return getContainer().getDeclaringType(d);
        }
        else {
            return null;
        }
    }*/
    
}