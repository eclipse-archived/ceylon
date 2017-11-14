package org.eclipse.ceylon.common;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    public static String msg(ResourceBundle bundle, String msgKey, Object... msgArgs) {
        String msg;
        try{
            msg = bundle.getString(msgKey);
        }catch(MissingResourceException x){
            msg = msgKey;
        }
        if (msgArgs != null) {
            msg = MessageFormat.format(msg, msgArgs);
        }
        return msg;
    }

}