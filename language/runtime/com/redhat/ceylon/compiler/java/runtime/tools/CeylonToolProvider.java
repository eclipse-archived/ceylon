package com.redhat.ceylon.compiler.java.runtime.tools;

import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaCompiler;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaRunner;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaScriptCompiler;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaScriptRunner;


public class CeylonToolProvider {
    
    public static Compiler getCompiler(Backend backend){
        switch(backend){
        case Java:
            return new JavaCompiler();
        case JavaScript:
            return new JavaScriptCompiler();
        default:
            throw new RuntimeException("Compiler for backend not supported yet: "+backend);
        }
    }

    public static Runner getRunner(Backend backend, Options options, String module, String version) {
        switch(backend){
        case JavaScript:
            return new JavaScriptRunner(options, module, version);
        case Java:
            return new JavaRunner(options, module, version);
        default:
            throw new RuntimeException("Runner for backend not supported yet: "+backend);
        }
    }
}
