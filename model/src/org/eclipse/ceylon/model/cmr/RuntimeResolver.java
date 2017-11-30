/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.cmr;

/**
 * Contract to determine the runtime version of a module 
 * from the compile time version.
 */
public interface RuntimeResolver {

    /**
     * Resolve the version of a module being used at runtime.
     * @param moduleName The module name  
     * @param moduleVersion The compile-time version
     * @return The runtime version
     */
    String resolveVersion(String moduleName, String moduleVersion);
    
}
