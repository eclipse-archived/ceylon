package com.redhat.ceylon.model.loader.mirror;

/**
 * Represents a package.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface PackageMirror {
    
    /**
     * Returns the fully-qualified name of this package
     */
    String getQualifiedName();
}
