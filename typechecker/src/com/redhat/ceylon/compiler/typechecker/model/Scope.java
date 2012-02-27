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

    public Declaration getMember(String name, List<ProducedType> signature);
    public Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature);
    public Declaration getDirectMember(String name, List<ProducedType> signature);
    public Declaration getDirectMemberOrParameter(String name, List<ProducedType> signature);

    public boolean isInherited(Declaration d);
    public TypeDeclaration getInheritingDeclaration(Declaration d);

    public Scope getContainer();
    
    public Map<DeclarationKey, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity);
    
    @Deprecated 
    public List<Declaration> getMembers();

    @Deprecated 
    public List<String> getQualifiedName();

}
