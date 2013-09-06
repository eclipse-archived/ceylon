package ceylon.modules.bootstrap;

import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

public class CeylonRunMessages extends Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("ceylon.modules.bootstrap.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
}
