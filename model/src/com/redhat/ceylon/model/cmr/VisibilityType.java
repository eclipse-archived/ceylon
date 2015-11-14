package com.redhat.ceylon.model.cmr;

/**
 * Visibility type.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public enum VisibilityType {
    STRICT, // all deps must be explicity declared; even JDK paths
    LOOSE
}
