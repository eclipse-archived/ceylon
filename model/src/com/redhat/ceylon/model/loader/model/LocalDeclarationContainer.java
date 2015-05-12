package com.redhat.ceylon.model.loader.model;

import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Scope;

/**
 * Scope used to contain local declarations.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface LocalDeclarationContainer extends Scope {
    
    /**
     * Gets a local declaration by (prefixed) name
     */
    Declaration getLocalDeclaration(String name);
    
    /**
     * Adds a new local declaration. Its prefixed name must be unique.
     */
    void addLocalDeclaration(Declaration decl);
}
