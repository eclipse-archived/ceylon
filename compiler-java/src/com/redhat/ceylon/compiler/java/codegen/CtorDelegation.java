package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Map;


import com.redhat.ceylon.compiler.typechecker.model.Constructor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;

public class CtorDelegation {
    
    private Constructor ctor;
    private Declaration extending;
    
    public CtorDelegation(Constructor ctor, Declaration extending){
        this.ctor = ctor;
        this.extending = extending;
    }
    
    public Constructor getConstructor() {
        return ctor;
    }
    public Declaration getExtending() {
        return extending;
    }
    public Constructor getExtendingConstructor() {
        return extending instanceof Constructor ? (Constructor)extending : null;
    }
    public boolean isSelfDelegation() {
        return ctor != null && extending instanceof Constructor && ctor.getContainer().equals(extending.getContainer());
    }
    public boolean isConcreteSelfDelegation() {
        return isSelfDelegation() && !((Constructor)extending).isAbstract();
    }
    public boolean isAbstractSelfDelegation() {
        return isSelfDelegation() && ((Constructor)extending).isAbstract();
    }
    public boolean isAbstractSelfOrSuperDelegation() {
        return extending instanceof Constructor == false || ((Constructor)extending).isAbstract();
    }
    /**
     * Does any other constructor in the given map delegate to the given constructor? 
     */
    public static boolean isDelegatedTo(Map<Constructor, CtorDelegation> allDelegations, Constructor ctor) {
        for (CtorDelegation d : allDelegations.values()) {
            if (ctor.equals(d.getExtending())) {
                return true;
            }
        }
        return false;
    }
}
