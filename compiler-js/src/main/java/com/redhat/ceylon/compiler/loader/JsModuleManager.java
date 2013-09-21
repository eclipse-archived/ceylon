package com.redhat.ceylon.compiler.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONValue;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.compiler.js.CeylonRunJsException;
import com.redhat.ceylon.compiler.js.CompilerErrorException;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

/** A ModuleManager that loads modules from js files.
 * 
 * @author Enrique Zamudio
 */
public class JsModuleManager extends ModuleManager {

    /** Tells whether the language module has been loaded yet. */
    private boolean clLoaded;
    private final Map<String, Object> clmod;
	private String encoding;

    public JsModuleManager(Context context, Map<String, Object> jsonCL, String encoding) {
        super(context);
        clmod = jsonCL;
        this.encoding = encoding;
    }

    @Override
    public void resolveModule(final ArtifactResult artifact, final Module module,
            final ModuleImport moduleImport, LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule) {
        if (!clLoaded) {
            clLoaded = true;
            //If we haven't loaded the language module yet, we need to load it first
            if (!("ceylon.language".equals(artifact.name()) && artifact.artifact().getName().endsWith(".js"))) {
                if (JsModuleManagerFactory.isVerbose()) {
                    System.out.println("Loading JS language module before any other modules");
                }
                if (clmod == null) {
                    ArtifactContext ac = new ArtifactContext("ceylon.language", module.getLanguageModule().getVersion(), ".js");
                    ac.setFetchSingleArtifact(true);
                    ac.setThrowErrorIfMissing(true);
                    ArtifactResult lmar = getContext().getRepositoryManager().getArtifactResult(ac);
                    resolveModule(lmar, module.getLanguageModule(), null, dependencyTree,
                            phasedUnitsOfDependencies, forCompiledModule);
                } else {
                    loadModuleFromMap(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies, forCompiledModule, clmod);
                }
            }
            //Then we continue loading whatever they asked for first.
        }
        //Create a similar artifact but with .js extension
        File js = artifact.artifact();
        if (module instanceof JsonModule && js.getName().endsWith(".js")) {
            if (((JsonModule)module).getModel() != null) {
                return;
            }
            if (js.exists() && js.isFile() && js.canRead()) {
                if (JsModuleManagerFactory.isVerbose()) {
                    System.out.println("Loading metamodel from " + js);
                }
                Map<String,Object> model = loadMetamodel(js);
                if (model != null) {
                    loadModuleFromMap(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies,
                            forCompiledModule, model);
                }
            }
        }
        super.resolveModule(artifact, module, moduleImport, dependencyTree,
                phasedUnitsOfDependencies, forCompiledModule);
    }

    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("js", "src");
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        Module module = new JsonModule();
        module.setName(moduleName);
        module.setVersion(version);
        JsonModule dep = (JsonModule)findLoadedModule("ceylon.language", null);
        //This can only happen during initCoreModules()
        if (!(module.getNameAsString().equals(Module.DEFAULT_MODULE_NAME) || module.getNameAsString().equals("ceylon.language")) && dep == null) {
            //Load the language module if we're not inside initCoreModules()
            dep = (JsonModule)getContext().getModules().getLanguageModule();
            //Add language module as a dependency
            //This will cause the dependency to be loaded later
            ModuleImport imp = new ModuleImport(dep, false, false);
            module.getImports().add(imp);
            module.setLanguageModule(dep);
        }
        return module;
    }

    @Override
    protected com.redhat.ceylon.compiler.typechecker.model.Package createPackage(String pkgName, Module module) {
        if (module == null) return super.createPackage(pkgName, module);
        final JsonPackage pkg = new JsonPackage(pkgName);
        List<String> name = pkgName.isEmpty() ? Collections.<String>emptyList() : splitModuleName(pkgName); 
        pkg.setName(name);
        module.getPackages().add(pkg);
        pkg.setModule(module);
        return pkg;
    }

    protected void loadModuleFromMap(ArtifactResult artifact, Module module,
            ModuleImport moduleImport, LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule,
            Map<String, Object> model) {
        @SuppressWarnings("unchecked")
        List<String> deps = (List<String>)model.get("$mod-deps");
        if (deps != null) {
            for (String s : deps) {
                int p = s.indexOf('/');
                String depname = null;
                String depv = null;
                if (p > 0) {
                    depname = s.substring(0,p);
                    depv = s.substring(p+1);
                    if (depv.isEmpty()) depv = null;
                } else {
                    depname = s;
                }
                //This will cause the dependency to be loaded later
                JsonModule dep = (JsonModule)getOrCreateModule(splitModuleName(depname), depv);
                ModuleImport imp = new ModuleImport(dep, false, false);
                module.getImports().add(imp);
            }
        }
        ((JsonModule)module).setModel(model);
        for (ModuleImport imp : module.getImports()) {
            if (!imp.getModule().getNameAsString().equals("ceylon.language")) {
                ArtifactContext ac = new ArtifactContext(imp.getModule().getNameAsString(),
                        imp.getModule().getVersion(), ArtifactContext.JS);
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

    /** Find the metamodel declaration in a js file, parse it as a Map and return it. */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> loadMetamodel(File jsFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(jsFile))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if ((line.startsWith("var $$METAMODEL$$=")) && line.endsWith("};")) {
                    line = line.substring(line.indexOf("{"), line.length()-1);
                    Map<String,Object> model = (Map<String,Object>)JSONValue.parse(line);
                    if (!model.containsKey("$mod-bin")) {
                        throw new CeylonRunJsException("The JS module " + jsFile +
                                " is not compatible with the current version of ceylon-js");
                    } else if (!model.get("$mod-bin").toString().equals(JsCompiler.BINARY_VERSION)) {
                        throw new CompilerErrorException(String.format("This JavaScript module has binary version %s incompatible with the compiler version %s",
                                model.get("$mod-bin"), JsCompiler.BINARY_VERSION));
                    }
                    return model;
                }
            }
            throw new CompilerErrorException("Can't find metamodel definition in " + jsFile.getAbsolutePath());
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
