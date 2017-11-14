/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;


/**
 * Helper methods for tools
 * @author tom
 */
public class Tools {

    private Tools() {}
    
    public static String progName() {
        return System.getProperty("org.eclipse.ceylon.common.tool.progname", "ceylon");
    }

}
