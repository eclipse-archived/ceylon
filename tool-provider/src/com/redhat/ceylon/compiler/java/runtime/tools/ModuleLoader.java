package com.redhat.ceylon.compiler.java.runtime.tools;

public interface ModuleLoader {
    ClassLoader loadModule(String name, String version);
}
