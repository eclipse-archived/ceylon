package com.redhat.ceylon.module.loader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;

public class FlatpathModuleLoader extends BaseModuleLoaderImpl {
    final Map<String, String> extraModules;

    public FlatpathModuleLoader() {
        this(null, null);
    }

    /*
     * Used by reflection in com.redhat.ceylon.common.tool.ToolLoader
     */
    public FlatpathModuleLoader(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        this(repoManager, delegateClassLoader, null, false);
    }

    public FlatpathModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose) {
        super(repositoryManager, delegateClassLoader, verbose);
        this.extraModules = extraModules;
    }

    class FlatpathModuleLoaderContext extends ModuleLoaderContext {

        FlatpathModuleLoaderContext(String module, String version) throws ModuleNotFoundException {
            super(module, version);
        }

        @Override
        void initialise() throws ModuleNotFoundException {
            preloadModules();
            moduleClassLoader = setupClassLoader();
            initialiseMetamodel();
        }

        private void preloadModules() throws ModuleNotFoundException {
            try {
                loadModule(null, module, modver, false, false, null);
                if(extraModules != null){
                    for(Entry<String,String> entry : extraModules.entrySet()){
                        loadModule(null, entry.getKey(), entry.getValue(), false, false, null);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private ClassLoader setupClassLoader() {
            // make a Class loader for this module if required
            ModuleGraph.Module loadedModule = moduleGraph.findModule(module);
            if(loadedModule.inCurrentClassLoader)
                return delegateClassLoader;
            else
                return makeModuleClassLoader();
        }
        
        private ClassLoader makeModuleClassLoader() {
            final Set<ModuleGraph.Module> modulesNotInCurrentClassLoader = new HashSet<ModuleGraph.Module>();
            // we need to make a class loader for all the modules it requires which are not provided by the current class loader
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(!module.inCurrentClassLoader && module.artifact != null)
                        modulesNotInCurrentClassLoader.add(module);
                }
            });
            URL[] urls = new URL[modulesNotInCurrentClassLoader.size()];
            if(verbose)
                log("Making classpath with "+urls.length+" jars");
            int i=0;
            for(ModuleGraph.Module module : modulesNotInCurrentClassLoader){
                ArtifactResult artifact = module.artifact;
                try {
                    @SuppressWarnings("deprecation")
                    URL url = artifact.artifact().toURL();
                    if(verbose)
                        log(" cp["+i+"] = "+url);
                    urls[i++] = url;
                } catch (MalformedURLException | RepositoryException e) {
                    throw new RuntimeException("Failed to get a URL for module file for "+module, e);
                }
            }
            return new URLClassLoader(urls , delegateClassLoader);
        }
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) throws ModuleNotFoundException {
        return new FlatpathModuleLoaderContext(name, version);
    }
}
