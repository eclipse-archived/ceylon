package com.redhat.ceylon.compiler.java.runtime.tools;


public interface JavaRunner extends Runner {
    ClassLoader getModuleClassLoader();
}