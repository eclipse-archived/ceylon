package com.redhat.ceylon.cmr.ceylon.loader;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.Exclusion;

public class ModuleGraph {
    
    Set<Module> roots = new HashSet<Module>();
    private int count;

    public static interface DependencySelector {
        boolean selectDependency(ArtifactResult dep);
        boolean canExclude(Module mod);
    }
    
    public static interface Visitor {
        void visit(Module module);
    }
    
    public class Module {
        public final String name;
        public final String version;
        public final Set<Module> dependencies = new HashSet<Module>();
        public final Set<Module> dependents = new HashSet<Module>();
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
            count--;
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
            count++;
            return mod;
        }

        public Module addDependency(Module mod) {
            dependencies.add(mod);
            mod.dependents.add(this);
            return mod;
        }

        public void remove() {
            // update our dependents
            for(Module dependent : dependents){
                dependent.dependencies.remove(this);
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
            count--;
        }
    }
    
    public Module addRoot(String module, String version){
        Module mod = new Module(module, version);
        roots.add(mod);
        count++;
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
        count = 0;
    }

    public void pruneExclusions(final DependencySelector selector) {
        final Set<Exclusion> exclusions = new HashSet<>();
        visit(new ModuleGraph.Visitor(){
            @Override
            public void visit(ModuleGraph.Module module) {
                if(module.artifact != null){
                    for (ArtifactResult dep : module.artifact.dependencies()) {
                        // only visit selected deps
                        if(!selector.selectDependency(dep))
                            continue;
                        if(dep.getExclusions() != null){
                            exclusions.addAll(dep.getExclusions());
                        }
                    }
                }
            }
        });
        final List<Module> removedModules = new LinkedList<>();
        visit(new ModuleGraph.Visitor(){
            @Override
            public void visit(ModuleGraph.Module module) {
                if(module.artifact != null){
                    if(selector.canExclude(module)
                            && ModuleUtil.isMavenModule(module.name)){
                        int sep = module.name.indexOf(':');
                        String groupId = module.name.substring(0, sep);
                        String artifactId = module.name.substring(sep+1);
                        for (Exclusion exclusion : exclusions) {
                            if(exclusion.getGroupId().equals(groupId)
                                    && exclusion.getArtifactId().equals(artifactId)){
                                // remove that module
                                removedModules.add(module);
                                break;
                            }
                        }
                    }
                }
            }
        });
        for (Module module : removedModules) {
            module.remove();
        }
    }

    public void dump(){
        dump(true);
    }
    
    public void dump(boolean printDuplicates){
        Set<Module> visited = new HashSet<Module>();
        dump(0, visited, roots, printDuplicates);
    }

    private void dump(int level, Set<Module> visited, Set<Module> modules, boolean printDuplicates) {
        for(Module mod : modules){
            boolean dupe = !visited.add(mod);
            if(!dupe || printDuplicates){
                for (int i = 0; i < level; i++) {
                    System.err.print("|    ");
                };
                System.err.print("+--- ");
                System.err.print(mod.name);
                System.err.print("/");
                System.err.print(mod.version);
            }
            if(dupe){
                if(printDuplicates)
                    System.err.println(" (*)");
                continue;
            }
            System.err.println();
            dump(level+1, visited, mod.dependencies, printDuplicates);
        }
    }
    
    public int getCount() {
        return count;
    }
}
