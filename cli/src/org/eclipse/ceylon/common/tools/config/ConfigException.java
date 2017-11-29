/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.config;

import org.eclipse.ceylon.common.tool.ToolError;


public class ConfigException extends ToolError {

    private static final long serialVersionUID = -7893648169111228746L;

    public ConfigException(String message) {
        super(message);
    }
}
