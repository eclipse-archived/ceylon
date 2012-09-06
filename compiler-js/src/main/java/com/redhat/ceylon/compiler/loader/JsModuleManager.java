package com.redhat.ceylon.compiler.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

    public JsModuleManager(Context context) {
        super(context);
    }

    @Override
    public void resolveModule(ArtifactResult artifact, Module module,
            ModuleImport moduleImport, LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies) {
        //Create a similar artifact but with .js extension
        File js = artifact.artifact();
        if (!artifact.name().equals("ceylon.language") && js.getName().endsWith(".js")) {
            if (js.exists() && js.isFile() && js.canRead()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(js));
                    String line = reader.readLine();
                    Map<String,Object> model = null;
                    while (model == null && (line = reader.readLine()) != null) {
                        if (line.startsWith("$$metamodel$$={") && line.endsWith("};")) {
                            line = line.substring(14, line.length()-1);
                            model = (Map<String,Object>)JSONValue.parse(line);
                            line = null;
                        }
                    }
                    if (model != null) {
                        System.out.println("Loading metamodel for " + model.get("$mod-name") + "/" + model.get("$mod-version"));
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
                                Module dep = new Module();
                                dep.setVersion(depv);
                                dep.setName(Arrays.asList(depname.split("\\.")));
                                findModule(dep, dependencyTree, true);
                                ModuleImport imp = new ModuleImport(dep, false, false);
                                module.getImports().add(imp);
                            }
                        }
                        return;
                    }
                } catch (IOException ex) {
                    //nothing to do here, will attempt reading .src
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ex) {
                            //nothing to do here
                        }
                    }
                }
            }
        }
        super.resolveModule(artifact, module, moduleImport, dependencyTree,
                phasedUnitsOfDependencies);
    }

    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("js", "src");
    }
}
