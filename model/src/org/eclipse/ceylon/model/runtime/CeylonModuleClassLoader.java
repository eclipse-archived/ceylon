/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.runtime;

import org.eclipse.ceylon.model.cmr.RuntimeResolver;

/**
 * This interface is here just so the language module can use a special hook in the ceylon-runtime project
 * without having to depend on it, which would be a circular dependency. Otherwise it would belong in the runtime
 * project.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface CeylonModuleClassLoader {
    
    @SuppressWarnings("serial")
    public static class ModuleLoadException extends Exception {
        public ModuleLoadException(Exception cause) {
            super(cause);
        }
    }
    
    public void registerInMetaModel();
    public String getModuleName();
    public String getModuleVersion();
    public RuntimeResolver getRuntimeResolver();
    public CeylonModuleClassLoader loadModule(String name, String version) throws ModuleLoadException;
}
