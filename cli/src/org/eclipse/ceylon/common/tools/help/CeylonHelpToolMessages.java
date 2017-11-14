
package org.eclipse.ceylon.common.tools.help;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.eclipse.ceylon.common.Messages;
import org.eclipse.ceylon.common.tool.Tools;

public class CeylonHelpToolMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonHelpToolMessages.class.getPackage().getName() + ".resources.sections");
    
    public static String msg(String msgKey, Object... msgArgs) {
        return Messages.msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
    
    static String getMoreInfo() {
        return MessageFormat.format(CeylonHelpToolMessages.msg("more.info"), Tools.progName());   
    }
    
}