package org.eclipse.ceylon.cmr.ceylon.loader;

import org.eclipse.ceylon.model.cmr.ModuleScope;

public interface ModuleLoader {
    ClassLoader loadModule(String name, String version) throws ModuleNotFoundException;
    ClassLoader loadModule(String name, String version, ModuleScope moduleScope) throws ModuleNotFoundException;
}
