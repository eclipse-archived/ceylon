package org.eclipse.ceylon.compiler.js;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonCompileJsMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.eclipse.ceylon.compiler.js.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
}
