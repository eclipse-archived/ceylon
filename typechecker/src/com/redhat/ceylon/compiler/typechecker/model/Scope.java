package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a namespace which contains named
 * members: a method, attribute, class, interface,
 * package, or module.
 *
 * @author Gavin King
 */
public interface Scope {
    
    public String getQualifiedNameString();

    public ProducedType getDeclaringType(Declaration d);
    
    /**
     * Get a member declared directly in this scope.
     */
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis);
    
    /**
     * Resolve a qualified reference.
     */
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis);
    
    /**
     * Resolve an unqualified reference.
     */
    //TODO: should be renamed getBase() since it also looks in containing scopes
    public Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature, boolean ellipsis);

    public boolean isInherited(Declaration d);
    public TypeDeclaration getInheritingDeclaration(Declaration d);

    public Scope getContainer();
    public Scope getScope();
    
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity);
    
    @Deprecated 
    public List<Declaration> getMembers();
    
    public void addMember(Declaration declaration);
    
}
