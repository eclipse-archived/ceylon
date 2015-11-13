package com.redhat.ceylon.compiler.java.runtime.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.impl.reflect.model.ReflectionModule;
import com.redhat.ceylon.model.loader.impl.reflect.model.ReflectionModuleManager;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.loader.model.LazyPackage;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class RuntimeModuleManager extends ReflectionModuleManager {

    public RuntimeModuleManager() {
        super();
    }

    @Override
    public boolean isModuleLoadedFromSource(String moduleName) {
        return false;
    }
    
    @Override
    protected AbstractModelLoader createModelLoader(Modules modules) {
        return new RuntimeModelLoader(this, modules);
    }

    @Override
    public Package createPackage(String pkgName, Module module) {
        final Package pkg = new LazyPackage(getModelLoader());
        List<String> name = pkgName.isEmpty() ? 
        		Collections.<String>emptyList() : 
        			splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        Module module;
        module = new ReflectionModule(this);
        module.setName(moduleName);
        module.setVersion(version);
        if(module instanceof ReflectionModule)
            setupIfJDKModule((LazyModule) module);
        return module;
    }

    public boolean loadModule(String name, String version, 
    		ArtifactResult artifact, ClassLoader classLoader) {
        RuntimeModelLoader modelLoader = getModelLoader();
        synchronized(modelLoader.getLock()){
            Module module = getOrCreateModule(splitModuleName(name), version);
            // The default module is created as available, so we use a different test for it, because we are the only
            // ones setting the module's Unit
            if(module.isDefault() 
                    ? module.getUnit() != null
                    : module.isAvailable())
                return false;
            modelLoader.addModuleToClassPath(module, artifact);
            modelLoader.addModuleClassLoader(module, classLoader);
            module.setAvailable(true);
            Unit u = new Unit();
            u.setFilename(artifact.name());
            if(artifact.artifact() != null) {
                u.setFullPath(artifact.artifact().getAbsolutePath());
            }
            module.setUnit(u);

            if(!module.isDefault()){
                // FIXME: dependencies of Ceylon modules?
                if(!modelLoader.loadCompiledModule(module)){
                    // we didn't find module.class so it must be a java module if it's not the default module
                    ((LazyModule)module).setJava(true);
                    module.setNativeBackends(Backend.Java.asSet());

                    // Java modules must have their dependencies set by the artifact result, as there is no module info in the jar
                    for (ArtifactResult dep : artifact.dependencies()) {
                        Module dependency = 
                        		getOrCreateModule(splitModuleName(dep.name()), 
                        				dep.version());

                        ModuleImport depImport = 
                        		findImport(module, dependency);
                        if (depImport == null) {
                            ModuleImport moduleImport = 
                            		new ModuleImport(dependency, false, false, Backend.Java);
                            module.addImport(moduleImport);
                        }
                    }
                }
            }
            return true;
        }
    }
    
    @Override
    public Module getOrCreateModule(List<String> moduleName, String version) {
        // Override to support getting the runtime version of the Module
        version = runtimeVersion(ModelUtil.formatPath(moduleName), version);
        return super.getOrCreateModule(moduleName, version);
    }

    protected String runtimeVersion(String moduleName, String version) {
        Object contextModuleLoader;
        try {
            contextModuleLoader= org.jboss.modules.Module.getContextModuleLoader();
        } catch (NullPointerException e) {
            contextModuleLoader = null;
        }
        if (contextModuleLoader instanceof RuntimeResolver) {
            version = ((RuntimeResolver)contextModuleLoader).resolveVersion(moduleName, version);
        }
        return version;
    }
    
    public Module findLoadedModule(String moduleName, String searchedVersion) {
        return super.findLoadedModule(moduleName, runtimeVersion(moduleName, searchedVersion));
    }
    
    @Override
    public RuntimeModelLoader getModelLoader() {
        return (RuntimeModelLoader) super.getModelLoader();
    }
    
    public Module findModuleForClass(java.lang.Class<?> klass){
        return getModelLoader().findModuleForClass(klass);
    }
    
    @SuppressWarnings("serial")
    private final LinkedHashMap<TypeDescriptor, Type> typeCache = 
    		new LinkedHashMap<TypeDescriptor, Type>(100, (float)0.75, true) {
    	@Override
    	protected boolean removeEldestEntry
    	        (Map.Entry<TypeDescriptor,Type> eldest) {
    		return size() > 100;
    	}
    };
    
    public Type getCachedType(TypeDescriptor td) {
        Type pt = typeCache.get(td);
        if (pt == null) {
            pt = td.toType(this);
            typeCache.put(td, pt);
        }
        return pt;
    }
    
    @SuppressWarnings("serial")
    private final LinkedHashMap<String, Boolean> isCache = 
    		new LinkedHashMap<String, Boolean>(100, (float)0.75, true) {
    	@Override
    	protected boolean removeEldestEntry
    	        (Map.Entry<String,Boolean> eldest) {
    		return size() > 100;
    	}
    };
    
    public boolean cachedIs(Object o, TypeDescriptor type) {
        TypeDescriptor instanceType = Metamodel.getTypeDescriptor(o);
        String key = instanceType+"<:"+type;
        Boolean cachedResult = isCache.get(key);
        if (cachedResult != null) {
            return cachedResult;
        }
        
        if(instanceType == null)
            return false;
        Type pt1 = getCachedType(instanceType);
        Type pt2  = getCachedType(type);
        boolean result = pt1.isSubtypeOf(pt2);
        isCache.put(key, result);
        return result;
    }
}
