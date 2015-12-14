package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaCompilerImpl;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaRunnerImpl;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaScriptCompilerImpl;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaScriptRunnerImpl;


public class CeylonToolProvider {
    
    public static Compiler getCompiler(Backend backend){
        switch(backend){
        case Java:
            return new JavaCompilerImpl();
        case JavaScript:
            return new JavaScriptCompilerImpl();
        default:
            throw new RuntimeException("Compiler for backend not supported yet: "+backend);
        }
    }

    /**
     * Gets a runner for running the given module
     * @param backend The backend to run the module on
     * @param options The runner options
     * @param module The module
     * @param version The module version
     * @return The runner
     * @throws ModuleNotFoundException If the module, or one of its 
     * non-optional dependencies could not be found
     */
    public static Runner getRunner(Backend backend, RunnerOptions options, String module, String version) {
        switch(backend){
        case JavaScript:
            return new JavaScriptRunnerImpl(options, module, version);
        case Java:
            return new JavaRunnerImpl(options, module, version);
        default:
            throw new RuntimeException("Runner for backend not supported yet: "+backend);
        }
    }
}
