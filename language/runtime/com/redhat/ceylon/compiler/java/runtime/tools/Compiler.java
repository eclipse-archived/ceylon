package com.redhat.ceylon.compiler.java.runtime.tools;


public interface Compiler {
    boolean compile(CompilerOptions options, 
                    CompilationListener listener);
}