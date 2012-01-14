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
    @Deprecated List<Declaration> getMembers();

    @Deprecated List<String> getQualifiedName();

    String getQualifiedNameString();

    ProducedType getDeclaringType(Declaration d);

    Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature);

    Declaration getMember(String name, List<ProducedType> signature);

    Declaration getDirectMemberOrParameter(String name, List<ProducedType> signature);

    Declaration getDirectMember(String name, List<ProducedType> signature);

    boolean isInherited(Declaration d);
    public TypeDeclaration getInheritingDeclaration(Declaration d);

    Scope getContainer();
    
    Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity);
    
}
