/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
