package com.redhat.ceylon.tools.version;

import java.util.ResourceBundle;

class CeylonVersionMessages extends com.redhat.ceylon.common.Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonVersionMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}