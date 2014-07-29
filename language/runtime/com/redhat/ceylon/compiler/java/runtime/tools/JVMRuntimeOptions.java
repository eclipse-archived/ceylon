package com.redhat.ceylon.compiler.java.runtime.tools;

public class JVMRuntimeOptions extends Options {
    private ClassLoader delegateClassLoader;

    public ClassLoader getDelegateClassLoader() {
        return delegateClassLoader;
    }

    public void setDelegateClassLoader(ClassLoader delegateClassLoader) {
        this.delegateClassLoader = delegateClassLoader;
    }
}
