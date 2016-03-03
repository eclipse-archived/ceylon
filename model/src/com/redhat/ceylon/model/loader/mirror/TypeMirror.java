package com.redhat.ceylon.model.loader.mirror;

import java.util.List;

/**
 * Represents a generic type.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface TypeMirror {

    /**
     * Returns the qualifying type, if any.
     */
    TypeMirror getQualifyingType();
    
    /**
     * Returns the fully-qualified name of this type with no type argument.
     */
    String getQualifiedName();

    /**
     * Returns the list of type arguments for this type
     */
    List<TypeMirror> getTypeArguments();

    /**
     * Returns the kind of type this is 
     */
    TypeKind getKind();

    /**
     * Returns the component type of this type, if this is an array type. Returns null otherwise.
     */
    // for arrays
    TypeMirror getComponentType();

    /**
     * Returns true if this type represents a Java primitive
     */
    boolean isPrimitive();

    /**
     * Returns true if this type is a Raw type (missing some type parameters)
     */
    boolean isRaw();
    
    /**
     * Returns the upper bound of this wildcard type if this is a wildcard type with an upper bound. Returns null otherwise.
     */
    TypeMirror getUpperBound();

    /**
     * Returns the lower bound of this wildcard type if this is a wildcard type with an lower bound. Returns null otherwise.
     */
    TypeMirror getLowerBound();

    /**
     * Returns the underlying class declaration of this type if it represents a class type
     */
    ClassMirror getDeclaredClass();
    
    /**
     * Returns the underlying type parameter declaration of this type if it represents a class type
     */
    TypeParameterMirror getTypeParameter();
}
