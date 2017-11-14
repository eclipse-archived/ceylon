/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules;

import org.eclipse.ceylon.common.tool.ToolError;

/**
 * @author Stephane Epardaud
 */
@SuppressWarnings("serial")
public class CeylonRuntimeException extends ToolError {
    public CeylonRuntimeException(String string) {
        super(string);
    }
}
