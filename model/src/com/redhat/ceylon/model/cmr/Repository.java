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
     * Return true if this is a Maven repo
     */
    boolean isMaven();

}
