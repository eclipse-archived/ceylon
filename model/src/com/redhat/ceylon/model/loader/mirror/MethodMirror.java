package com.redhat.ceylon.model.loader.mirror;

import java.util.List;

/**
 * Represents a method.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface MethodMirror extends AnnotatedMirror, AccessibleMirror {

    /**
     * Returns true if this method is static
     */
    boolean isStatic();

    /**
     * Returns true if this method is a constructor
     */
    boolean isConstructor();

    /**
     * Returns true if this method is abstract
     */
    boolean isAbstract();
    
    /**
     * Returns true if this method is final
     */
    boolean isFinal();

    /**
     * Returns true if this method is a static initialiser
     */
    boolean isStaticInit();

    /**
     * Returns true if this method is variadic
     */
    boolean isVariadic();
    
    /**
     * Returns the list of parameters
     */
    List<VariableMirror> getParameters();

    /**
     * Returns the return type for this method 
     */
    TypeMirror getReturnType();
    
    boolean isDeclaredVoid();

    /**
     * Returns the list of type parameters for this method
     */
    List<TypeParameterMirror> getTypeParameters();
    
    /**
     * If this is a method on an annotation type, whether the method has a 
     * {@code default} expression;
     */
    boolean isDefault();

    /**
     * Return this method's enclosing class.
     */
    ClassMirror getEnclosingClass();
}
