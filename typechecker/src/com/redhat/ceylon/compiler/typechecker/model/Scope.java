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
	List<Declaration> getMembers();
	Scope getContainer();
	List<String> getQualifiedName();
	//Declaration getMember(boolean includeParameters, String name);
	ProducedType getDeclaringType(Declaration d);
	Declaration getMember(Unit unit, boolean includeParameters, String name);
}
