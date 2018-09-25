/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.ceylon.ceylondoc.Util;
import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.compiler.java.loader.SourceDeclarationVisitor;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnit;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnits;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.loader.impl.reflect.model.ReflectionModule;
import org.eclipse.ceylon.model.loader.impl.reflect.model.ReflectionModuleManager;
import org.eclipse.ceylon.model.loader.mirror.ClassMirror;
import org.eclipse.ceylon.model.loader.model.LazyModule;
import org.eclipse.ceylon.model.loader.model.LazyPackage;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Modules;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class PhasedUnitsModuleManager extends ReflectionModuleManager {

    private List<ModuleSpec> modulesSpecs;
    private Callable<PhasedUnits> getPhasedUnits;
    private RepositoryManager outputRepositoryManager;
    private boolean bootstrapCeylon;

    private List<String> compiledClasses;
    
    private Set<String> sourceImportedModules = new HashSet<>();
    private Set<String> npmImportedModules = new HashSet<>();
    
    public PhasedUnitsModuleManager(Callable<PhasedUnits> getPhasedUnits, Context context, List<ModuleSpec> modules, RepositoryManager outputRepositoryManager, boolean bootstrapCeylon, Logger log) {
        super();
        this.outputRepositoryManager = outputRepositoryManager;
        this.modulesSpecs = modules;
        this.getPhasedUnits = getPhasedUnits;
        this.bootstrapCeylon = bootstrapCeylon;
    }

    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("car", "jar", "src", "js");
    }
    
    @Override
    protected void loadPackageDescriptors() {
        getModelLoader().loadPackageDescriptors();
    }
    
    public void addSourceImportedModule(String moduleName) {
    	sourceImportedModules.add(moduleName);
    }

    public void addNpmImportedModule(String moduleName) {
    	npmImportedModules.add(moduleName);
    }

    public boolean isNpmImportedModule(String moduleName) {
    	return npmImportedModules.contains(moduleName);
    }

    @Override
    public boolean isModuleLoadedFromSource(String moduleName) {
        for(ModuleSpec spec : modulesSpecs){
            if(spec.getName().equals(moduleName))
                return true;
        }
        
        if(sourceImportedModules.contains(moduleName)) {
        	return true;
        }

        return false;
    }
    
    private void initTypeCheckedUnits() {
        for(PhasedUnit unit : getPhasedUnits().getPhasedUnits()){
            // obtain the unit container path
            final String pkgName = Util.getUnitPackageName(unit); 
            unit.getCompilationUnit().visit(new SourceDeclarationVisitor(){
                @Override
                public void loadFromSource(org.eclipse.ceylon.compiler.typechecker.tree.Tree.Declaration decl) {
                    compiledClasses.add(Util.getQuotedFQN(pkgName, decl));
                }

                @Override
                public void loadFromSource(ModuleDescriptor that) {
                    // don't think we care about these
                }

                @Override
                public void loadFromSource(PackageDescriptor that) {
                    // don't think we care about these
                }
            });
        }
    }
    
    protected List<String> getCompiledClasses() {
        if (compiledClasses == null) {
            compiledClasses = new LinkedList<String>();
            initTypeCheckedUnits();
        }
        return compiledClasses;
    }

    @Override
    protected AbstractModelLoader createModelLoader(Modules modules) {
        return new PhasedUnitsModelLoader(this, modules, getPhasedUnits, bootstrapCeylon){
            @Override
            protected boolean isLoadedFromSource(String className) {
                return getCompiledClasses().contains(className);
            }
            
            @Override
            public ClassMirror lookupNewClassMirror(Module module, String name) {
                // don't load it from class if we are compiling it
                if(getCompiledClasses().contains(name)){
                    logVerbose("Not loading "+name+" from class because we are typechecking them");
                    return null;
                }
                return super.lookupNewClassMirror(module, name);
            }
            @Override
            protected void logError(String message) {
                log.error(message);
            }
            @Override
            protected void logVerbose(String message) {
                log.debug(message);
            }
            @Override
            protected void logWarning(String message) {
                log.warning(message);
            }
            @Override
            public boolean loadCompiledModule(Module module) {
            	if (npmImportedModules.contains(module.getNameAsString())) {
            		module.setJvmMajor(8);
            		module.setJvmMinor(1);
            		Package pkg = new Package() {
            		    private final Map<String,Declaration> decs = new HashMap<>();

            			@Override
            		    public Declaration getDirectMember(String name, List<Type> signature, boolean variadic) {
            		        Declaration d = decs.get(name);
            		        if (d == null) {
            		            if (Character.isUpperCase(name.charAt(0))) {
            		                // TODO: it looks like this needs to be a special 
            		                // class that can return any member in a fashion 
            		                // similar to this package
            		                d = new Class();
            		                ParameterList plist = new ParameterList();
            		                plist.setNamedParametersSupported(true);
            		                plist.setFirst(true);
            		                for (int i=0; i<10; i++) {
            		                    Parameter p = new Parameter();
            		                    p.setName("arg" + i);
            		                    Value v = new Value();
            		                    v.setUnit(d.getUnit());
            		                    v.setType(getUnit().getUnknownType());
            		                    v.setDynamic(true);
            		                    v.setDynamicallyTyped(true);
            		                    v.setInitializerParameter(p);
            		                    v.setContainer((Class)d);
            		                    p.setModel(v);
            		                    p.setDeclaration(d);
            		                    p.setDefaulted(true);
            		                    plist.getParameters().add(p);
            		                }
            		                ((Class)d).setParameterList(plist);
            		            } else {
            		                d = new Function();
            		                ((Function)d).setDynamicallyTyped(true);
            		            }
            		            d.setDynamic(true);
            		            d.setName(name);
            		            d.setUnit(getUnit());
            		            d.setShared(true);
            		            d.setContainer(this);
            		            d.setScope(this);
            		            decs.put(name, d);
            		        }
            		        return d;
            		    }            			
            		};
            		pkg.setModule(module);
            		pkg.setShared(true);
            		pkg.setName(module.getName());
            		pkg.setUnit(new Unit());
                    pkg.getUnit().setPackage(pkg);

            		module.getPackages().add(pkg);
            		return true;
            	}
            	return super.loadCompiledModule(module);
            }
        };
    }

    @Override
    public Package createPackage(String pkgName, Module module) {
        // never create a lazy package for ceylon.language when we're documenting it
        if((pkgName.equals(AbstractModelLoader.CEYLON_LANGUAGE)
                || pkgName.startsWith(AbstractModelLoader.CEYLON_LANGUAGE+"."))
            && isModuleLoadedFromSource(AbstractModelLoader.CEYLON_LANGUAGE))
            return super.createPackage(pkgName, module);
        final Package pkg = new LazyPackage(getModelLoader());
        List<String> name = pkgName.isEmpty() ? Collections.<String>emptyList() : splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        String name = org.eclipse.ceylon.compiler.java.util.Util.getName(moduleName);
        // never create a reflection module for ceylon.language when we're documenting it
        Module module;
        if(name.equals(AbstractModelLoader.CEYLON_LANGUAGE) 
                && isModuleLoadedFromSource(AbstractModelLoader.CEYLON_LANGUAGE))
            module = new Module();
        else
            module = new ReflectionModule(this);
        module.setName(moduleName);
        module.setVersion(version);
        if(module instanceof ReflectionModule)
            setupIfJDKModule((LazyModule) module);
        return module;
    }
    
    @Override
    public void modulesVisited() {
        // this is very important!
        try{
            super.modulesVisited();
        }catch(Exception x){
            // this can only throw if we're trying to document the language module and it's missing
            throw new RuntimeException("Failed to find the language module sources in the specified source paths");
        }
        for(Module module : getModules().getListOfModules()){
            if(isModuleLoadedFromSource(module.getNameAsString())){
                addOutputModuleToClassPath(module);
            }
        }
    }

    private void addOutputModuleToClassPath(Module module) {
        ArtifactContext ctx = new ArtifactContext(null, module.getNameAsString(), module.getVersion(), ArtifactContext.CAR);
        ArtifactResult result = outputRepositoryManager.getArtifactResult(ctx);
        if(result != null)
            getModelLoader().addModuleToClassPath(module, result);
    }

    @Override
    public Backends getSupportedBackends() {
        // This is most likely not the correct solution but it
        // still works for all current cases and allows generating
        // docs for non-JVM modules at the same time
        return Backends.JAVA.merged(Backend.JavaScript);
    }
    
    private PhasedUnits getPhasedUnits() {
        try {
            return getPhasedUnits.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
