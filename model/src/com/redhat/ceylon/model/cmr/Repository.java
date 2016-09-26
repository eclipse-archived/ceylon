package com.redhat.ceylon.model.cmr;

/**
 * Abstraction over the CMR Repository type
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface Repository {
    /**
     * Returns a display string that represents this Repository
     */
    String getDisplayString();

    /**
     * Returns the name of the repository namespace.
     * <code>null</code> means the default Ceylon namespace.
     */
    String getNamespace();
}
