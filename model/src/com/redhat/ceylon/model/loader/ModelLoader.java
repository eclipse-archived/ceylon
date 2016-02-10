package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;

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
}
