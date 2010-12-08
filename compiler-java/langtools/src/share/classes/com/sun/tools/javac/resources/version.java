package com.sun.tools.javac.resources;

import java.util.ListResourceBundle;

public final class version extends java.util.ListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "full", "1.6.0_17-b17" },
            { "jdk", "1.6.0_17" },
            { "release", "1.6.0_17" },
        };
    }
}
