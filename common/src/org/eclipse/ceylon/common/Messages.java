/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    public static String msg(ResourceBundle bundle, String msgKey, Object... msgArgs) {
        String msg;
        try{
            msg = bundle.getString(msgKey);
        }catch(MissingResourceException x){
            msg = msgKey;
        }
        if (msgArgs != null) {
            msg = MessageFormat.format(msg, msgArgs);
        }
        return msg;
    }

}