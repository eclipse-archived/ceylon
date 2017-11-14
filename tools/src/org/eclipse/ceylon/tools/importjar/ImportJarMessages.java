
package org.eclipse.ceylon.tools.importjar;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class ImportJarMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ImportJarMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}