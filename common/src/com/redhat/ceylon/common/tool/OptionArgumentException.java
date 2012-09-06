package com.redhat.ceylon.common.tool;

import java.lang.reflect.InvocationTargetException;

/**
 * Exception for problems parsing options and arguments.
 */
public class OptionArgumentException extends ToolException {

    public OptionArgumentException(String msgKey, Object...msgArgs) {
        super(ToolMessages.msg(msgKey, msgArgs));
    }

    public OptionArgumentException(InvocationTargetException e) {
        super(e.getCause().getLocalizedMessage(), e);
    }
}
