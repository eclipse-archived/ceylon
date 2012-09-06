package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;

/** ModuleManagerFactory for the JsModuleManager.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManagerFactory implements ModuleManagerFactory {

    @Override
    public ModuleManager createModuleManager(Context context) {
        return new JsModuleManager(context);
    }

}
