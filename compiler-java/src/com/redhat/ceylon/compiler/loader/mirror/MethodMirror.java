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

/**
 * Represents a method.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface MethodMirror extends AnnotatedMirror {

    /**
     * Returns the method name
     */
    String getName();

    /**
     * Returns true if this method is static
     */
    boolean isStatic();

    /**
     * Returns true if this method is public
     */
    boolean isPublic();
    
    /**
     * Returns true if this method is protected
     */
    boolean isProtected();

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
     * Returns the list of parameters
     */
    List<VariableMirror> getParameters();

    /**
     * Returns the return type for this method 
     */
    TypeMirror getReturnType();

    /**
     * Returns the list of type parameters for this method
     */
    List<TypeParameterMirror> getTypeParameters();
}
