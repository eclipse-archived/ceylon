package org.eclipse.ceylon.model.typechecker.model;

/**
 * Any program element with a name - including declarations,
 * packages, and modules.
 * 
 * @author Gavin King
 *
 */
public interface Referenceable extends Sourced {
	public String getNameAsString();
}
