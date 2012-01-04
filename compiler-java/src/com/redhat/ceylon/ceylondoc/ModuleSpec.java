package com.redhat.ceylon.ceylondoc;

import java.util.LinkedList;
import java.util.List;

public class ModuleSpec {
    public String name, version;
    
    public ModuleSpec(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public static List<ModuleSpec> parse(List<String> moduleSpecs){
        List<ModuleSpec> modules = new LinkedList<ModuleSpec>();
        for(String moduleSpec : moduleSpecs){
            int sep = moduleSpec.indexOf("/");
            String name = sep != -1 ? moduleSpec.substring(0, sep) : moduleSpec;
            String version = sep != -1 ? moduleSpec.substring(sep+1) : null;
            modules.add(new ModuleSpec(name, version));
        }
        return modules;
    }
}
