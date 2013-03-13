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
                try {
                    if (JsModuleManagerFactory.isVerbose()) {
                        System.out.println("Loading metamodel from " + js);
                    }
                    Map<String,Object> model = loadMetamodel(js);
                    if (model != null) {
                        loadModuleFromMap(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies,
                                forCompiledModule, model);
                    }
                } catch (IOException ex) {
                    //nothing to do here, will attempt reading .src
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
        Module module = new JsonModule(this);
        module.setName(moduleName);
        module.setVersion(version);
        return module;
    }

    @Override
    protected JsonPackage createPackage(String pkgName, Module module) {
        final JsonPackage pkg = new JsonPackage(pkgName);
        List<String> name = pkgName.isEmpty() ? Collections.<String>emptyList() : splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
            if (module instanceof JsonModule) {
                pkg.setModel(((JsonModule)module).getModelForPackage(pkgName));
            }
        }
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
                        imp.getModule().getVersion());
                ac.setSuffix(".js");
                artifact = getContext().getRepositoryManager().getArtifactResult(ac);
                if (artifact != null) {
                    resolveModule(artifact, imp.getModule(), imp, dependencyTree,
                            phasedUnitsOfDependencies, forCompiledModule & imp.isExport());
                }
            }
        }
        ((JsonModule)module).loadDeclarations();
        //module.setAvailable(true);
        return;
    }

    /** Find the metamodel declaration in a js file, parse it as a Map and return it. */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> loadMetamodel(File jsFile) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(jsFile));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if ((line.startsWith("var $$metamodel$$={") || line.startsWith("$$metamodel$$={")) && line.endsWith("};")) {
                    line = line.substring(line.indexOf("{"), line.length()-1);
                    return (Map<String,Object>)JSONValue.parse(line);
                }
            }
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    protected PhasedUnits createPhasedUnits() {
    	PhasedUnits units = super.createPhasedUnits();
    	if(encoding != null)
    		units.setEncoding(encoding);
    	return units;
    }
}
