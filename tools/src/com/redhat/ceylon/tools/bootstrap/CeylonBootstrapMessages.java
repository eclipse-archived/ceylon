package com.redhat.ceylon.tools.bootstrap;

import java.util.ResourceBundle;

public class CeylonBootstrapMessages extends com.redhat.ceylon.common.Messages {
	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonBootstrapMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
    	return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }	
}
