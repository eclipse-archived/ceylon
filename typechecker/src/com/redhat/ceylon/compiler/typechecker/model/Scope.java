package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

/**
 * Represents a namespace which contains named
 * members: a method, attribute, class, interface, 
 * package, or module.
 * 
 * @author Gavin King
 *
 */
public interface Scope {
    @Deprecated
	List<Declaration> getMembers();
	@Deprecated
	List<String> getQualifiedName();
	String getQualifiedNameString();
	ProducedType getDeclaringType(Declaration d);
	Declaration getMemberOrParameter(Unit unit, String name);
	Declaration getMember(String name);
	Declaration getDirectMemberOrParameter(String name);
    Declaration getDirectMember(String name);
    Scope getContainer();
}
