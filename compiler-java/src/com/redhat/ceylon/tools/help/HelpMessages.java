package com.redhat.ceylon.tools.help;

import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

public class HelpMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.redhat.ceylon.tools.help.resources.sections");
    
    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
    
}