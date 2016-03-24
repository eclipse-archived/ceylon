package com.redhat.ceylon.compiler.java.runtime.tools;


public interface Runner {
    void cleanup();
    void run(String... arguments);
}