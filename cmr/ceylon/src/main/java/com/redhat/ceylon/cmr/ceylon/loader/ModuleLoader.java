package com.redhat.ceylon.cmr.ceylon.loader;

import com.redhat.ceylon.model.cmr.ModuleScope;

public interface ModuleLoader {
    ClassLoader loadModule(String name, String version) throws ModuleNotFoundException;
    ClassLoader loadModule(String name, String version, ModuleScope moduleScope) throws ModuleNotFoundException;
}
