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

package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

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
     * Returns the ProducedType of a name in a given scope

     * @param module the module to load it from
     * @param pkg the package name
     * @param name the fully-qualified name of the type
     * @param scope the scope in which to find it
     * @return the ProducedType found
     */
    public ProducedType getType(Module module, String pkg, String name, Scope scope);

    /**
     * Returns the Declaration of a name in a given scope

     * @param module the module to load it from
     * @param pkg the package name
     * @param name the fully-qualified name of the type
     * @param scope the scope in which to find it
     * @return the ProducedType found
     */
    public Declaration getDeclaration(Module module, String pkg, String name, Scope scope);

    /**
     * Returns a loaded module by name
     * @return null if module is not already loaded
     */
    public Module getLoadedModule(String moduleName);
}
