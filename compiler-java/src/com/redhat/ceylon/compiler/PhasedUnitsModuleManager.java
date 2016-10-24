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
package com.redhat.ceylon.compiler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.redhat.ceylon.ceylondoc.Util;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.java.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.impl.reflect.model.ReflectionModule;
import com.redhat.ceylon.model.loader.impl.reflect.model.ReflectionModuleManager;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.loader.model.LazyPackage;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;

public class PhasedUnitsModuleManager extends ReflectionModuleManager {

    private List<ModuleSpec> modulesSpecs;
    private Callable<PhasedUnits> getPhasedUnits;
    private RepositoryManager outputRepositoryManager;
    private boolean bootstrapCeylon;

    private List<String> compiledClasses;
    
    public PhasedUnitsModuleManager(Callable<PhasedUnits> getPhasedUnits, Context context, List<ModuleSpec> modules, RepositoryManager outputRepositoryManager, boolean bootstrapCeylon, Logger log) {
        super();
        this.outputRepositoryManager = outputRepositoryManager;
        this.modulesSpecs = modules;
        this.getPhasedUnits = getPhasedUnits;
        this.bootstrapCeylon = bootstrapCeylon;
    }

    @Override
    public boolean isModuleLoadedFromSource(String moduleName) {
        for(ModuleSpec spec : modulesSpecs){
            if(spec.getName().equals(moduleName))
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
                public void loadFromSource(com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration decl) {
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
        String name = com.redhat.ceylon.compiler.java.util.Util.getName(moduleName);
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
