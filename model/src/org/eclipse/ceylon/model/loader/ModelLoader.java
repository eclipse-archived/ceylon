/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader;

import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Type;

/**
 * Represents a ModelLoader's public API
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface ModelLoader {
    
    /**
     * The type of declaration we're looking for. This is useful for toplevel attributes and classes who
     * can share the same name (in the case of singletons).
     *
     * @author Stéphane Épardaud <stef@epardaud.fr>
     */
    enum DeclarationType {
        /**
         * We're looking for a type
         */
        TYPE, 
        /**
         * We're looking for an attribute
         */
        VALUE;
    }

    /**
     * Loads a declaration by name and type
     * 
     * @param module the module to load it from
     * @param typeName the fully-qualified declaration name
     * @param declarationType the declaration type
     * @return the declaration, if found, or null.
     */
    public Declaration getDeclaration(Module module, String typeName, DeclarationType declarationType);
    
    /**
     * Returns the Type of a name in a given scope

     * @param module the module to load it from
     * @param pkg the package name
     * @param name the fully-qualified name of the type
     * @param scope the scope in which to find it
     * @return the Type found
     */
    public Type getType(Module module, String pkg, String name, Scope scope);

    /**
     * Returns the Declaration of a name in a given scope

     * @param module the module to load it from
     * @param pkg the package name
     * @param name the fully-qualified name of the type
     * @param scope the scope in which to find it
     * @return the Type found
     */
    public Declaration getDeclaration(Module module, String pkg, String name, Scope scope);

    /**
     * Returns a loaded module by name and version
     * @return null if module is not already loaded
     */
    public Module getLoadedModule(String moduleName, String version);

    /**
     * Return true if we're running with a dynamic metamodel
     */
    public boolean isDynamicMetamodel();
}
