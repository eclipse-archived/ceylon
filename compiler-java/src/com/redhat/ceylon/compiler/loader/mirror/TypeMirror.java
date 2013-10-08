/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.loader.mirror;

import java.util.List;

import javax.lang.model.type.TypeKind;

/**
 * Represents a generic type.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface TypeMirror {

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
