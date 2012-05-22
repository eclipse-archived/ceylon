package com.redhat.ceylon.compiler.loader;

import com.sun.tools.javac.util.Context;

public interface ModelLoaderFactory {
    AbstractModelLoader createModelLoader(Context context);
}
