package org.eclipse.ceylon.compiler.java.runtime.tools;


public interface JavaRunner extends Runner {
    ClassLoader getModuleClassLoader();
}