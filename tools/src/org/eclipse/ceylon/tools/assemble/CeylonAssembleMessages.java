
package org.eclipse.ceylon.tools.assemble;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonAssembleMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonAssembleMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}