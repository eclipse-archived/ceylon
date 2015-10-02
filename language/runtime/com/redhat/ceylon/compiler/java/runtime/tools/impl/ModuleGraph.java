package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.model.cmr.ArtifactResult;

public class ModuleGraph {
    
    Set<Module> roots = new HashSet<Module>();

    static interface Visitor {
        void visit(Module module);
    }
    
    class Module {
        String name;
        String version;
        Set<Module> dependencies = new HashSet<Module>();
        Set<Module> dependents = new HashSet<Module>();
        public ArtifactResult artifact;
        public boolean inCurrentClassLoader;
        boolean replaced;
        
        public Module(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public Module replace(Module replacement){
            // should not happen twice, but who knows
            if(replaced)
                return replacement;
            replaced = true;
            // update our dependents
            for(Module dependent : dependents){
                dependent.replaceDependency(this, replacement);
            }
            // nobody depends on us
            dependents.clear();
            // make sure we remove ourselves from the roots if we're there
            roots.remove(this);
            // also clear ourselves from our dependencies
            for(Module dependency : dependencies){
                dependency.dependents.remove(this);
            }
            dependencies.clear();
            // also 
            return replacement;
        }
        
        private void replaceDependency(Module from, Module to) {
            dependencies.remove(from);
            dependencies.add(to);
            to.dependents.add(this);
        }

        @Override
        public String toString() {
            return name+"/"+version;
        }
        
        @Override
        public int hashCode() {
            int hash = 17;
            hash = hash*31 + name.hashCode();
            hash = hash*31 + version.hashCode();
            return hash;
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj instanceof Module == false)
                return false;
            if(obj == this)
                return true;
            Module other = (Module) obj;
            return name.equals(other.name)
                    && version.equals(other.version);
        }

        public Module addDependency(String name, String version) {
            Module mod = new Module(name, version);
            dependencies.add(mod);
            mod.dependents.add(this);
            return mod;
        }

        public Module addDependency(Module mod) {
            dependencies.add(mod);
            mod.dependents.add(this);
            return mod;
        }
    }
    
    public Module addRoot(String module, String version){
        Module mod = new Module(module, version);
        roots.add(mod);
        return mod;
    }

    public Module addRoot(Module mod){
        roots.add(mod);
        return mod;
    }

    public Module findModule(String module){
        Set<String> visited = new HashSet<String>();
        return findModule(module, visited, roots);
    }

    private Module findModule(String module, Set<String> visited, Set<Module> modules) {
        for(Module mod : modules){
            if(!visited.add(mod.name))
                continue;
            if(mod.name.equals(module))
                return mod;
            Module found = findModule(module, visited, mod.dependencies);
            if(found != null)
                return found;
        }
        return null;
    }

    public Module findModule(String module, String version){
        Set<Module> visited = new HashSet<Module>();
        return findModule(module, version, visited, roots);
    }

    private Module findModule(String module, String version, Set<Module> visited, Set<Module> modules) {
        for(Module mod : modules){
            if(!visited.add(mod))
                continue;
            if(mod.name.equals(module)
                    && mod.version.equals(version))
                return mod;
            Module found = findModule(module, version, visited, mod.dependencies);
            if(found != null)
                return found;
        }
        return null;
    }
    
    public void visit(Visitor visitor){
        Set<Module> visited = new HashSet<Module>();
        visit(visitor, visited, roots);
    }

    private void visit(Visitor visitor, Set<Module> visited, Set<Module> modules) {
        for(Module mod : modules){
            if(!visited.add(mod))
                continue;
            visitor.visit(mod);
            visit(visitor, visited, mod.dependencies);
        }
    }

    public void clear() {
        roots.clear();
    }
}
