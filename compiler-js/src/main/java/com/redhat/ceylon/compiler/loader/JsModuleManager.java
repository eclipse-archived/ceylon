package com.redhat.ceylon.compiler.loader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.impl.JSUtils;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.compiler.js.CeylonRunJsException;
import com.redhat.ceylon.compiler.js.CompilerErrorException;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Unit;

/** A ModuleManager that loads modules from js files.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManager extends ModuleManager {

    /** Tells whether the language module has been loaded yet. */
    private boolean clLoaded;
	private String encoding;
    private static final String BIN_VERSION = Versions.JS_BINARY_MAJOR_VERSION + "." + Versions.JS_BINARY_MINOR_VERSION;
    //ugly-ass hack for #490
    private static final String V1_1_BIN_VERSION = Versions.V1_1_BINARY_MAJOR_VERSION + "." + Versions.V1_1_BINARY_MINOR_VERSION;

    public JsModuleManager(Context context, String encoding) {
        super(context);
        this.encoding = encoding;
    }

    @Override
    public void resolveModule(final ArtifactResult artifact, final Module module,
            final ModuleImport moduleImport, LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule) {
        if (!clLoaded) {
            clLoaded = true;
            //If we haven't loaded the language module yet, we need to load it first
            if (!(Module.LANGUAGE_MODULE_NAME.equals(artifact.name())
                    && artifact.artifact().getName().endsWith(ArtifactContext.JS_MODEL))) {
                if (JsModuleManagerFactory.isVerbose()) {
                    System.out.println("Loading JS language module before any other modules");
                }
                ArtifactContext ac = new ArtifactContext(Module.LANGUAGE_MODULE_NAME,
                        module.getLanguageModule().getVersion(), ArtifactContext.JS_MODEL);
                ac.setIgnoreDependencies(true);
                ac.setThrowErrorIfMissing(true);
                ArtifactResult lmar = getContext().getRepositoryManager().getArtifactResult(ac);
                resolveModule(lmar, module.getLanguageModule(), null, dependencyTree,
                        phasedUnitsOfDependencies, forCompiledModule);
            }
            //Then we continue loading whatever they asked for first.
        }
        //Create a similar artifact but with -model.js extension
        File js = artifact.artifact();
        if (js.getName().endsWith(ArtifactContext.JS) && !js.getName().endsWith(ArtifactContext.JS_MODEL)) {
            ArtifactContext ac = new ArtifactContext(artifact.name(),
                    artifact.version(), ArtifactContext.JS_MODEL);
            ac.setIgnoreDependencies(true);
            ac.setThrowErrorIfMissing(true);
            ArtifactResult lmar = getContext().getRepositoryManager().getArtifactResult(ac);
            js = lmar.artifact();
        }
        if (module instanceof JsonModule) {
            if (((JsonModule)module).getModel() != null) {
                return;
            }
            if (js.exists() && js.isFile() && js.canRead() && js.getName().endsWith(ArtifactContext.JS_MODEL)) {
                if (JsModuleManagerFactory.isVerbose()) {
                    System.out.println("Loading model from " + js);
                }
                Map<String,Object> model = loadJsonModel(js);
                if (model == null) {
                    if (JsModuleManagerFactory.isVerbose()) {
                        System.out.println("Model not found in " + js);
                    }
                } else {
                    loadModuleFromMap(artifact, module, dependencyTree, phasedUnitsOfDependencies,
                            forCompiledModule, model);
                    return;
                }
            }
        }
        super.resolveModule(artifact, module, moduleImport, dependencyTree,
                phasedUnitsOfDependencies, forCompiledModule);
    }

    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("js");
    }

    @Override
    public boolean supportsBackend(Backend backend) {
        return backend == Backend.JavaScript;
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
                dep = (JsonModule)getContext().getModules().getLanguageModule();
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
    protected com.redhat.ceylon.model.typechecker.model.Package createPackage(String pkgName, Module module) {
        if (module!=null && module == getContext().getModules().getDefaultModule()) {
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

    protected void loadModuleFromMap(ArtifactResult artifact, Module module, LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule,
            Map<String, Object> model) {
        @SuppressWarnings("unchecked")
        List<Object> deps = (List<Object>)model.get("$mod-deps");
        if (deps != null) {
            for (Object dep : deps) {
                final String s;
                boolean optional = false;
                boolean export = false;
                if (dep instanceof Map) {
                    @SuppressWarnings("unchecked")
                    final Map<String,Object> depmap = (Map<String,Object>)dep;
                    s = (String)depmap.get("path");
                    optional = depmap.containsKey("opt");
                    export = depmap.containsKey("exp");
                } else {
                    s = (String)dep;
                }
                int p = s.indexOf('/');
                String depname = null;
                String depv = null;
                if (p > 0) {
                    depname = s.substring(0,p);
                    depv = s.substring(p+1);
                    if (depv.isEmpty()) {
                        depv = null;
                    }
                    //TODO Remove this hack after next bin compat breaks
                    if (Module.LANGUAGE_MODULE_NAME.equals(depname) && "1.1.0".equals(depv)) {
                        depv = "1.1.1";
                    }
                } else {
                    depname = s;
                }
                //This will cause the dependency to be loaded later
                JsonModule mod = (JsonModule)getOrCreateModule(splitModuleName(depname), depv);
                Backend backend = Backend.fromAnnotation(mod.getNative());
                ModuleImport imp = new ModuleImport(mod, optional, export, backend);
                module.addImport(imp);
            }
            model.remove("$mod-deps");
        }
        ((JsonModule)module).setModel(model);
        for (ModuleImport imp : module.getImports()) {
            if (!imp.getModule().getNameAsString().equals(Module.LANGUAGE_MODULE_NAME)) {
                ArtifactContext ac = new ArtifactContext(imp.getModule().getNameAsString(),
                        imp.getModule().getVersion(), ArtifactContext.JS_MODEL);
                artifact = getContext().getRepositoryManager().getArtifactResult(ac);
                if (artifact != null) {
                    resolveModule(artifact, imp.getModule(), imp, dependencyTree,
                            phasedUnitsOfDependencies, forCompiledModule & imp.isExport());
                }
            }
        }
        ((JsonModule)module).loadDeclarations();
        return;
    }

    /** Read the metamodel declaration from a js file, check it's the right version and return the model as a Map. */
    public static Map<String,Object> loadJsonModel(File jsFile) {
        try {
            Map<String,Object> model = JSUtils.readJsonModel(jsFile);
            if (model == null) {
                throw new CompilerErrorException("Can't find metamodel definition in " + jsFile.getAbsolutePath());
            }
            if (!model.containsKey("$mod-bin")) {
                throw new CeylonRunJsException("The JavaScript module " + jsFile +
                        " is not compatible with the current version of ceylon-js");
            } else {
                final String binVersion = model.get("$mod-bin").toString();
                final String modname = model.get("$mod-name").toString();
                final boolean isNewest = binVersion.equals(BIN_VERSION);
                //TODO remove this shit when we break bincompat again
                final boolean isRecent = binVersion.equals(V1_1_BIN_VERSION);
                if ((Module.LANGUAGE_MODULE_NAME.equals(modname) && !isNewest)
                        || !(isNewest || isRecent)) {
                    throw new CompilerErrorException(String.format("The Ceylon-JS module %s has binary version %s is incompatible with the compiler version %s",
                        modname, binVersion, BIN_VERSION));
                }
            }
            return model;
        } catch (IOException ex) {
            throw new CompilerErrorException("Error loading model from " + jsFile);
        }
    }

    @Override
    protected PhasedUnits createPhasedUnits() {
    	PhasedUnits units = super.createPhasedUnits();
        String fileEncoding = encoding;
        if (fileEncoding == null) {
            fileEncoding = CeylonConfig.get(DefaultToolOptions.DEFAULTS_ENCODING);
        }
        if (fileEncoding != null) {
    		units.setEncoding(fileEncoding);
        }
    	return units;
    }

}
