package com.redhat.ceylon.common.config;

import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

public class CeylonConfigToolMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.redhat.ceylon.common.config.config-tool");

    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}