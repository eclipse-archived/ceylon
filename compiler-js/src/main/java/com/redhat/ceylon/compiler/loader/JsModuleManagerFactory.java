package com.redhat.ceylon.compiler.loader;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;

/** ModuleManagerFactory for the JsModuleManager.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManagerFactory implements ModuleManagerFactory {

    private final Map<String, Object> clmod;
    private static boolean verbose;
    private String encoding;

    public JsModuleManagerFactory(String encoding) {
        clmod = null;
        this.encoding = encoding;
    }

    public JsModuleManagerFactory(Map<String, Object> jsonCL) {
        clmod = jsonCL;
    }

    @Override
    public ModuleManager createModuleManager(Context context) {
        return new JsModuleManager(context, clmod, encoding);
    }

    public static void setVerbose(boolean flag) {
        verbose = flag;
    }
    public static boolean isVerbose() {
        return verbose;
    }
}
