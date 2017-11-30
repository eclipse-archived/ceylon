/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.p2;

import org.eclipse.ceylon.common.tool.ToolError;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@SuppressWarnings("serial")
public class CeylonP2ToolException extends ToolError {

    public CeylonP2ToolException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CeylonP2ToolException(Throwable cause) {
        super(cause);
    }

    public CeylonP2ToolException(String message) {
        super(message);
    }

}
