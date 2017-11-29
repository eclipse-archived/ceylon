/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.importjar;

import org.eclipse.ceylon.common.tool.ToolError;

@SuppressWarnings("serial")
public class ImportJarException extends ToolError {

    public ImportJarException(String msgKey) {
        super(ImportJarMessages.msg(msgKey));
    }

    public ImportJarException(String msgKey, Exception cause) {
        super(ImportJarMessages.msg(msgKey), cause);
    }

    public ImportJarException(String msgKey, Object[] msgArgs, Exception cause) {
        super(ImportJarMessages.msg(msgKey, msgArgs), cause);
    }

}
