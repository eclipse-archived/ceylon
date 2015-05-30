package com.redhat.ceylon.model.typechecker.model;

/**
 * Any program element with a name - including declarations,
 * packages, and modules.
 * 
 * @author Gavin King
 *
 */
public interface Referenceable {
	public Unit getUnit();
	public String getNameAsString();
}
