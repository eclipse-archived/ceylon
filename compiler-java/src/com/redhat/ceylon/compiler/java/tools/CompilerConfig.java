package com.redhat.ceylon.compiler.java.tools;

import java.io.File;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.sun.tools.javac.util.Context;

public class CompilerConfig {

    public static CeylonConfig instance(Context context) {
        CeylonConfig instance = context.get(CeylonConfig.class);
        if (instance == null) {
            instance = CeylonConfig.createFromLocalDir(new File("."));
            context.put(CeylonConfig.class, instance);
        }
        return instance;
    }
}
