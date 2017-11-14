package org.eclipse.ceylon.common;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

class CommonMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CommonMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}