package com.redhat.ceylon.model.typechecker.model;

/**
 * Represents a namespace which contains named
 * members: a method, attribute, class, interface,
 * package, or module.
 *
 * @author Gavin King
 */
public interface Scope extends Scoped {
    
    /**
     * Add a member to this scope.
     * 
     * @param declaration the member to add
     */
    public void addMember(Declaration declaration);
    

}
