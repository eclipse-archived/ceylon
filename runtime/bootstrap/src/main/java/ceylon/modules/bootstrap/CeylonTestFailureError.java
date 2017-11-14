/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.bootstrap;

import org.eclipse.ceylon.common.tool.ToolError;

@SuppressWarnings("serial")
public class CeylonTestFailureError extends ToolError {

    public CeylonTestFailureError() {
        super("Tests failed", 100);
    }

}
