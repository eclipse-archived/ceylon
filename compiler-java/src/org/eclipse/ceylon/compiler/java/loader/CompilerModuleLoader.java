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
package org.eclipse.ceylon.compiler.java.loader;

import java.util.Map;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.loader.BaseModuleLoaderImpl;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.StatusPrinter;
import org.eclipse.ceylon.compiler.java.tools.CeylonLog;
import org.eclipse.ceylon.compiler.java.tools.StatusPrinterAptProgressListener;
import org.eclipse.ceylon.langtools.tools.javac.util.Log.WriterKind;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ModuleScope;

public class CompilerModuleLoader extends BaseModuleLoaderImpl {

    private StatusPrinter statusPrinter;
    private CeylonLog log;

    public CompilerModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose, 
            StatusPrinter statusPrinter, CeylonLog log) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
        this.statusPrinter = statusPrinter;
        this.log = log;
    }

    @Override
    public void log(String string) {
        log.printRawLines(WriterKind.NOTICE, "["+string+"]");
    }
    
    public class CompilerModuleLoaderContext extends ModuleLoaderContext {

        private StatusPrinterAptProgressListener progressListener;

        private final String[] artifactSuffixes = new String[] { ArtifactContext.CAR, ArtifactContext.JAR };
        
        CompilerModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
            if(statusPrinter != null){
                progressListener = new StatusPrinterAptProgressListener(statusPrinter){
                    @Override
                    protected long getNumberOfModulesResolved() {
                        return getModuleCount();
                    }
                };
            }
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            preloadModules();
        }

        @Override
        protected String[] getArtifactSuffixes() {
            return artifactSuffixes;
        }

        @Override
        protected void resolvingSuccess(ArtifactResult result) {
            if(verbose)
                log("Pre-resolved module: "+result);
            if(progressListener != null)
                progressListener.retrievingModuleArtifactSuccess(toModuleSpec(result), result);
        }

        private ModuleSpec toModuleSpec(ArtifactResult result) {
            return new ModuleSpec(result.namespace(), result.name(), result.version());
        }

        @Override
        protected void resolvingFailed(ArtifactContext artifactContext) {
            if(verbose)
                log("Pre-resolving module failed for: "+artifactContext);
            if(progressListener != null)
                progressListener.retrievingModuleArtifactFailed(toModuleSpec(artifactContext), artifactContext);
        }

        @Override
        protected void prepareContext(ArtifactContext artifactContext) {
            if(verbose)
                log("Pre-resolving module: "+artifactContext);
            if(progressListener != null)
                progressListener.retrievingModuleArtifact(toModuleSpec(artifactContext), artifactContext);
        }
        
        private ModuleSpec toModuleSpec(ArtifactContext artifactContext) {
            return new ModuleSpec(artifactContext.getNamespace(), artifactContext.getName(), artifactContext.getVersion());
        }

        public String getModuleVersion(String name) {
            ModuleGraph.Module module = moduleGraph.findModule(name);
            return module != null ? module.version : null;
        }
    }

    @Override
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return new CompilerModuleLoaderContext(name, version, lookupScope);
    }

    private CompilerModuleLoaderContext getContext(){
        if(contexts.size() == 1){
            return (CompilerModuleLoaderContext) contexts.values().iterator().next();
        }
        throw new RuntimeException("No context found");
    }
    
    public String getModuleVersion(String name) {
        CompilerModuleLoaderContext context = getContext();
        return context.getModuleVersion(ModuleUtil.getModuleNameFromUri(name));
    }

    public void setupOverrides(Overrides overrides) {
        CompilerModuleLoaderContext context = getContext();
        context.fillOverrides(overrides);
    }

    public int getModuleCount() {
        CompilerModuleLoaderContext context = getContext();
        return context.getModuleCount();
    }
}
