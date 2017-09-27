package org.eclipse.ceylon.langtools.tools.javac.resources;

import java.util.ListResourceBundle;

import org.eclipse.ceylon.common.Versions;

public final class version extends ListResourceBundle {
    private static final Object CEYLON_VERSION = "ceylon " + Versions.CEYLON_VERSION;

    protected final Object[][] getContents() {
        return new Object[][] {
            { "full", CEYLON_VERSION },
            { "jdk", CEYLON_VERSION },
            { "release", CEYLON_VERSION },
        };
    }
}
