package com.redhat.ceylon.module.loader;

public interface ModuleLoader {
    ClassLoader loadModule(String name, String version) throws ModuleNotFoundException;
}
