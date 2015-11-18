package com.redhat.ceylon.compiler.java.test.compat;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class CompatTests extends CompilerTests {

    /**
     * Tests we can run a module that was compiled using the 1.2.0 compiler in 
     * a 1.2.1 runtime. 
     * 
     * The source is in
     * <pre>
     *   /ceylon-compiler/test/src/com/redhat/ceylon/compiler/java/test/compat/source/source/compat120
     * </pre>
     * but the the module we execute,
     * <pre>  
     *   /ceylon-compiler/test/src/com/redhat/ceylon/compiler/java/test/compat/modules/compat120/1.0.0/compat120-1.0.0.car
     * </pre>
     * was compiled with the real 1.2.0 compiler
     */
    @Test
    public void run120CarIn121() throws Throwable {
        runInJBossModules("run", "compat120", Arrays.asList("--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules"));
    }
    
}
