/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.exceptions;

/**
 * Unable to find the language module and hence essential types for the type checker
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class LanguageModuleNotFoundException extends RuntimeException {    
    private static final long serialVersionUID = -3520692521592355218L;
    public LanguageModuleNotFoundException(String message) {
        super(message);
    }
}
