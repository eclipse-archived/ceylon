package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.util.Context;

public class ExtensionFinder {
    private static final Context.Key<ExtensionFinder> extensionFinderKey =
        new Context.Key<ExtensionFinder>();

    public static ExtensionFinder instance(Context context) {
        ExtensionFinder instance = context.get(extensionFinderKey);
        if (instance == null) {
            instance = new ExtensionFinder(context);
            context.put(extensionFinderKey, instance);
        }
        return instance;
    }
    
    private final Symtab syms;
    private final Types types;

    private ExtensionFinder(Context context) {
        syms = Symtab.instance(context);
        types = Types.instance(context);        
    }
    
    public void extend(Type source, Type target) {
        System.out.println("Attempting to extend " + source + " to " + target);
    }
}
