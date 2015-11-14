package com.redhat.ceylon.model.loader.mirror;

/**
 * Represents an program element (class, method, constructor, field) with visibility access restrictions
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AccessibleMirror {
    
    /**
     * Returns true if the element is public
     */
    boolean isPublic();

    /**
     * Returns true if the element is protected
     */
    boolean isProtected();

    /**
     * Returns true if the element is package-protected
     */
    boolean isDefaultAccess();

}
