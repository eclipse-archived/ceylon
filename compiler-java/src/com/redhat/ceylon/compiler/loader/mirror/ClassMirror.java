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

import com.redhat.ceylon.compiler.loader.ModelLoader;
import com.redhat.ceylon.compiler.typechecker.model.Module;

/**
 * Represents a Java Class definition.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface ClassMirror extends AnnotatedMirror, AccessibleMirror {

    /**
     * Returns true if this is an interface
     */
    boolean isInterface();
    
    /**
     * Returns true if this is an {@code @interface}.
     */
    boolean isAnnotationType();
    
    /**
     * Returns true if this is an abstract class
     */
    boolean isAbstract();
    
    /**
     * Returns true if this is a static class
     */
    boolean isStatic();

    /**
     * Returns true if this class is an inner class
     */
    boolean isInnerClass();

    /**
     * Returns true if this class is a local class
     */
    boolean isLocalClass();

    /**
     * Returns true if this class is an anonymous class
     */
    boolean isAnonymous();
    
    /**
     * Returns true if this class is an enum class
     */
    boolean isEnum();

    /**
     * Returns the fully-qualified class name
     */
    String getQualifiedName();
    
    /**
     * Returns this class's package
     */
    PackageMirror getPackage();
    
    /**
     * Returns the list of methods and constructors defined by this class. Does not include inherited
     * methods and constructors.
     */
    List<MethodMirror> getDirectMethods();

    /**
     * Returns the list of fields defined by this class. Does not include inherited
     * fields.
     */
    List<FieldMirror> getDirectFields();

    /**
     * Returns the list of type parameters for this class
     */
    List<TypeParameterMirror> getTypeParameters();

    /**
     * Returns the list of inner classes directly contained in this class. Does not include inherited
     * inner classes. 
     */
    List<ClassMirror> getDirectInnerClasses();

    /**
     * Returns this class's superclass, or null if it doesn't have any
     */
    TypeMirror getSuperclass();

    /**
     * Returns this class's containing class, if any
     */
    ClassMirror getEnclosingClass();

    /**
     * Returns the list of interfaces implemented by this class
     */
    List<TypeMirror> getInterfaces();

    /**
     * Returns true if this class represents a toplevel attribute
     */
    boolean isCeylonToplevelAttribute();

    /**
     * Returns true if this class represents a toplevel object
     */
    boolean isCeylonToplevelObject();

    /**
     * Returns true if this class represents a toplevel method
     */
    boolean isCeylonToplevelMethod();
    
    /**
     * Returns true if this class was loaded from source, false if it was loaded from a compiled form
     */
    boolean isLoadedFromSource();

    /**
     * Returns true if this class was loaded from a Java source file, false if it came from a ceylon
     * source file or from a class file
     */
    boolean isJavaSource();

    /**
     * Returns true if this class is final
     */
    boolean isFinal();

    String getCacheKey(Module module);
}
