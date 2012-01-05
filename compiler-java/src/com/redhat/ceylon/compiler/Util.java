package com.redhat.ceylon.compiler;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.main.OptionName;

public class Util {

    public static final boolean allowWarnings(Context context) {
        Options options = Options.instance(context);
        boolean allow = options.get(OptionName.CEYLONALLOWWARNINGS) != null;
        String option = System.getProperty("ceylon.typechecker.warnings");
        if ("warn".equals(option)) {
            allow = true;
        } else if ("error".equals(option)) {
            allow = false;
        }
        return allow;
    }
}
