package com.redhat.ceylon.compiler.typechecker.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the set of modules involved in the compilation
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Modules {
    private Module languageModule;
    private Set<Module> modules = new TreeSet<Module>();
    private Module defaultModule;

    public Module getLanguageModule() {
        return languageModule;
    }

    public void setLanguageModule(Module languageModule) {
        this.languageModule = languageModule;
    }

    public void setDefaultModule(Module defaultModule) {
        this.defaultModule = defaultModule;
    }

    public Module getDefaultModule() {
        return defaultModule;
    }

    public Set<Module> getListOfModules() {
        return modules;
    }
}
