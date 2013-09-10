package com.redhat.ceylon.compiler.js;

import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

public class CeylonRunJsMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.redhat.ceylon.compiler.js.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
}
