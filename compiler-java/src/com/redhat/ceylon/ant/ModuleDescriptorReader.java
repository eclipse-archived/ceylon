package com.redhat.ceylon.ant;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Module;

class ModuleDescriptorReader {
    
    private static final class NullLogger implements Logger {
        @Override
        public void error(String str) {
            // Don't care
        }

        @Override
        public void warning(String str) {
            // Don't care
        }

        @Override
        public void info(String str) {
            // Don't care
        }

        @Override
        public void debug(String str) {
            // Don't care
        }
    }
    
    private final Module moduleDescriptor;

    public ModuleDescriptorReader(com.redhat.ceylon.ant.Module module, File srcDir) {
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(new NullLogger());
        RepositoryManager repoManager = builder.buildRepository();
        VFS vfs = new VFS();
        Context context = new Context(repoManager, vfs);
        PhasedUnits pus = new PhasedUnits(context);
        pus.parseUnit(vfs.getFromFile(srcDir));
        for (PhasedUnit pu : pus.getPhasedUnits()) {
            pu.visitSrcModulePhase();
        }
        ModuleManager moduleManager = pus.getModuleManager();
        List<String> name = ModuleManager.splitModuleName(module.name);
        this.moduleDescriptor = moduleManager.getOrCreateModule(name, null);
    }
    
    /**
     * Gets the module version
     * @return The module version, or null if no version could be found
     */
    public String getModuleVersion() {
        return moduleDescriptor.getVersion();
    }
    
    /**
     * Gets the module name
     * @return The module version, or null if no version could be found
     */
    public String getModuleName() {
        return moduleDescriptor.getNameAsString();
    }
    
    /**
     * Gets the module license
     * @return The module version, or null if no version could be found
     */
    public String getModuleLicense() {
        return moduleDescriptor.getLicense();
    }
    
    /**
     * Gets the module license
     * @return The module version, or null if no version could be found
     */
    public List<String> getModuleAuthors() {
        return moduleDescriptor.getAuthors();
    }
}
