package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/** ModuleManagerFactory for the JsModuleManager.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManagerFactory implements ModuleManagerFactory {

    private static boolean verbose;
    private final String encoding;

    public JsModuleManagerFactory(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public ModuleManager createModuleManager(Context context) {
        return new JsModuleManager(context, encoding);
    }

    public static void setVerbose(boolean flag) {
        verbose = flag;
    }
    public static boolean isVerbose() {
        return verbose;
    }

    @Override
    public ModuleSourceMapper createModuleManagerUtil(Context context, ModuleManager moduleManager) {
        return new JsModuleSourceMapper(context, moduleManager, encoding);
    }

}
