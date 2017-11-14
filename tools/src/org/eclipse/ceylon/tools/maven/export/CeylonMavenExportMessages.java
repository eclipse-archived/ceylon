
package org.eclipse.ceylon.tools.maven.export;

import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;

public class CeylonMavenExportMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonMavenExportMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}