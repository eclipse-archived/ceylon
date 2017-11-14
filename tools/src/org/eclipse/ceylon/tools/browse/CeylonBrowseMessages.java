
package org.eclipse.ceylon.tools.browse;

import java.util.ResourceBundle;

class CeylonBrowseMessages extends org.eclipse.ceylon.common.Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonBrowseMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}