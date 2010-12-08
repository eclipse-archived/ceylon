package com.sun.tools.javac.resources;

import java.util.ListResourceBundle;

public final class version extends ListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "full", "1.6.0_ceylon" },
            { "jdk", "1.6.0_ceylon" },
            { "release", "1.6.0_ceylon" },
        };
    }
}
