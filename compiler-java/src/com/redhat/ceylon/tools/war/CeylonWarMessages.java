package com.redhat.ceylon.tools.war;

import java.util.ResourceBundle;

public class CeylonWarMessages extends com.redhat.ceylon.common.Messages {
	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonWarMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
    	return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }	
}
