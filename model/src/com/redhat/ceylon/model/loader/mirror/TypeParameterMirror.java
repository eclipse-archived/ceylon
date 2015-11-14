package com.redhat.ceylon.model.loader.mirror;

import java.util.List;

/**
 * Represents a type parameter.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface TypeParameterMirror {

    /**
     * Returns the name of the type parameter.
     */
    String getName();

    /**
     * Returns the list of bounds for this type parameter.
     */
    List<TypeMirror> getBounds();

}
