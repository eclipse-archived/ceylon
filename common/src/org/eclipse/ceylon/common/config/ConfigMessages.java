package org.eclipse.ceylon.common.config;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class ConfigMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.eclipse.ceylon.common.config.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}