
package org.eclipse.ceylon.common.tools;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

class CeylonToolMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonToolMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}