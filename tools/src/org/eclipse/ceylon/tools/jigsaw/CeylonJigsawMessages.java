
package org.eclipse.ceylon.tools.jigsaw;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonJigsawMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonJigsawMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}