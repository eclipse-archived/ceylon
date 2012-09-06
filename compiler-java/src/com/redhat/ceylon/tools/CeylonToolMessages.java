package com.redhat.ceylon.tools;

import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

class CeylonToolMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.redhat.ceylon.tools.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}