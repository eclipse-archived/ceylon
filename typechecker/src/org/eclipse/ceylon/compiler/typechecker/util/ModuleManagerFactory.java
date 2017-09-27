package org.eclipse.ceylon.compiler.typechecker.util;

import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/**
 * Factory to specify a custom type of ModuleManager to be created by the TypeChecker. 
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface ModuleManagerFactory {

    /**
     * Creates a new instance of ModuleManager for the TypeChecker.
     */
    ModuleManager createModuleManager(Context context);

    /**
     * Creates a new instance of ModuleManager for the TypeChecker.
     */
    ModuleSourceMapper createModuleManagerUtil(Context context, ModuleManager moduleManager);

}
