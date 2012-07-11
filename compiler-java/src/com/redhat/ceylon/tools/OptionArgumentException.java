package com.redhat.ceylon.tools;

import java.lang.reflect.InvocationTargetException;

public class OptionArgumentException extends PluginException {

    public OptionArgumentException(String message) {
        super(message);
    }

    public OptionArgumentException(InvocationTargetException e) {
        super(e.getCause().getLocalizedMessage(), e);
    }
}
