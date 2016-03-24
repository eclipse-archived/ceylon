package com.redhat.ceylon.compiler.java.runtime.tools;

public class JavaRunnerOptions extends RunnerOptions {
    private ClassLoader delegateClassLoader;

    public ClassLoader getDelegateClassLoader() {
        return delegateClassLoader;
    }

    public void setDelegateClassLoader(ClassLoader delegateClassLoader) {
        this.delegateClassLoader = delegateClassLoader;
    }
}
