package com.redhat.ceylon.compiler.loader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/** A ModuleManager that loads modules from js files.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManager extends ModuleManager {

    public JsModuleManager(Context context, String encoding) {
        super();
    }


    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("js");
    }

    @Override
    public Set<String> getSupportedBackends() {
        return Collections.singleton(Backend.JavaScript.nativeAnnotation);
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        final Module module = new JsonModule();
        module.setName(moduleName);
        module.setVersion(version);
        Unit u = new Unit();
        u.setFilename(Constants.MODULE_DESCRIPTOR);
        u.setFullPath(moduleName+"/"+version);
        module.setUnit(u);
        JsonModule dep = (JsonModule)findLoadedModule(Module.LANGUAGE_MODULE_NAME, null);
        //This can only happen during initCoreModules()
        if (!(module.getNameAsString().equals(Module.DEFAULT_MODULE_NAME) || module.getNameAsString().equals(Module.LANGUAGE_MODULE_NAME))) {
            //Load the language module if we're not inside initCoreModules()
            if (dep == null) {
                dep = (JsonModule)getModules().getLanguageModule();
            }
            //Add language module as a dependency
            //This will cause the dependency to be loaded later
            ModuleImport imp = new ModuleImport(dep, false, false);
            module.addImport(imp);
            module.setLanguageModule(dep);
            //Fix 280 part 1 -- [Tako] I have the feeling this can't be correct
//            Backend backend = null; // TODO Figure out if this dependency is only for a specific backend
//            getContext().getModules().getDefaultModule().addImport(new ModuleImport(module, false, false, backend));
        }
        return module;
    }

    @Override
    public com.redhat.ceylon.model.typechecker.model.Package createPackage(String pkgName, Module module) {
        if (module!=null && module == getModules().getDefaultModule()) {
            com.redhat.ceylon.model.typechecker.model.Package pkg = module.getPackage(pkgName);
            if (pkg != null) {
                return pkg;
            }
        }
        final JsonPackage pkg = new JsonPackage(pkgName);
        List<String> name = pkgName.isEmpty() ? Arrays.asList("") : splitModuleName(pkgName);
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }
}
