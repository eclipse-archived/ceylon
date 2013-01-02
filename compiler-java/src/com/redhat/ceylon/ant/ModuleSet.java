package com.redhat.ceylon.ant;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.types.DataType;

/**
 * Runtime representation of a {@code <moduleset>} element, which can be 
 * defined at the top level of an ant project and referred to by other tasks.
 */
public class ModuleSet extends DataType {

    private final LinkedHashSet<Module> modules = new LinkedHashSet<Module>();
    
    public void addConfiguredModule(Module module) {
        this.modules.add(module);
    }
    
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.modules.addAll(sourceModules.getModules());
    }
    
    public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.modules.addAll(moduleset.getModules());
    }
    
    public Set<Module> getModules() {
        LinkedHashSet<Module> result = new LinkedHashSet<Module>();        
        result.addAll(this.modules);
        if (getRefid() != null) {
            ModuleSet referredModuleSet = (ModuleSet)getProject().getReference(getRefid().getRefId());    
            result.addAll(referredModuleSet.getModules());
        }
        return result;
    }

    
    
}
