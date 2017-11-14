
package org.eclipse.ceylon.tools.moduleloading;

import java.util.ResourceBundle;


public class ModuleLoadingMessages extends org.eclipse.ceylon.common.Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ModuleLoadingMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}