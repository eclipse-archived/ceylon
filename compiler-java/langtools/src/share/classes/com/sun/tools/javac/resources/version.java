package com.sun.tools.javac.resources;

import java.util.ListResourceBundle;

public final class version extends ListResourceBundle {
    private static final Object CEYLON_VERSION = "ceylonc 0.4 'Ratatouille'";

    protected final Object[][] getContents() {
        return new Object[][] {
            { "full", CEYLON_VERSION },
            { "jdk", CEYLON_VERSION },
            { "release", CEYLON_VERSION },
        };
    }
}
