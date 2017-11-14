
package org.eclipse.ceylon.common.tool;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

class ToolMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.eclipse.ceylon.common.tool.tools");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}