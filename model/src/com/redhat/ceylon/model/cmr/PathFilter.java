package com.redhat.ceylon.model.cmr;

/**
 * Filter used to determine whether a path should be included or excluded from imports and exports.
 *
 * @author John Bailey
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface PathFilter {

    /**
     * Determine whether a path should be accepted.  The given name is a path separated
     * by "{@code /}" characters.
     *
     * @param path the path to check
     * @return true if the path should be accepted, false if not
     */
    boolean accept(String path);
}