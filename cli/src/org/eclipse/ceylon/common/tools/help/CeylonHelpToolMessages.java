/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.tool.Tools;

public class CeylonHelpToolMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonHelpToolMessages.class.getPackage().getName() + ".resources.sections");
    
    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
    
    static String getMoreInfo() {
        return MessageFormat.format(CeylonHelpToolMessages.msg("more.info"), Tools.progName());   
    }
    
}