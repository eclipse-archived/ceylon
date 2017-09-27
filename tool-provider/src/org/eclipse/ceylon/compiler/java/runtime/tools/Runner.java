package org.eclipse.ceylon.compiler.java.runtime.tools;


public interface Runner {
    void cleanup();
    void run(String... arguments);
}