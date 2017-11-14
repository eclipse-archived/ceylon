/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.loader;

import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/** ModuleManagerFactory for the JsModuleManager.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManagerFactory implements ModuleManagerFactory {

    private static boolean verbose;
    private final String encoding;

    public JsModuleManagerFactory(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public ModuleManager createModuleManager(Context context) {
        return new JsModuleManager(context, encoding);
    }

    public static void setVerbose(boolean flag) {
        verbose = flag;
    }
    public static boolean isVerbose() {
        return verbose;
    }

    @Override
    public ModuleSourceMapper createModuleManagerUtil(Context context, ModuleManager moduleManager) {
        return new JsModuleSourceMapper(context, moduleManager, encoding);
    }

}
