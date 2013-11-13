package com.redhat.ceylon.compiler.java.test.statement;

import java.lang.reflect.Array;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class OptimizationTest extends CompilerTest {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-optim";
    }
    
    protected ModuleWithArtifact getDestModuleWithArtifact() {
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.statement.loop.optim", "1");
    }
    
    @Test
    public void testLopRangeOpIterationOptimization(){
        compareWithJavaSource("loop/RangeOpIterationOptimization");
    }
    
    @Test
    public void testLopRangeOpIterationOptimizationCorrect(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.rangeOpIterationOptimizationCorrect", 
                "loop/RangeOpIterationOptimizationCorrect.ceylon");
    }
    
    @Test
    public void testLopOptimArrayIterationStatic(){
        compareWithJavaSource("loop/optim/ArrayIterationStatic");
    }
    
    @Test
    public void testLopOptimArrayIterationStaticBench(){
        compile("loop/optim/ArrayIterationStaticBench.ceylon");
        long opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench");
        opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench");
        System.gc();
        long unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis");
        unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis");
        System.gc();
        long java = arrayIterationStaticJava();
        java = arrayIterationStaticJava();
        
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
        System.out.println("Java took " + java/1_000_000 + "ms");
        
    }

    private long arrayIterationStaticJava() {
        long java;
        {
            long arrayIterationStaticN = 1_000_000;
            long[] arrayIterationStaticInts = new long[100];
            for (int i = 0; i < arrayIterationStaticInts.length; i++) {
                arrayIterationStaticInts[i] = i;
            }
            long i = arrayIterationStaticN;
            long sum = 0;
            long t0 = System.nanoTime();
            while (i > 0) {
                int length = arrayIterationStaticInts.length;
                for (int ii = 0; ii < length; ii++) {
                    long x = arrayIterationStaticInts[ii];
                    sum += x;
                }
                i--;
            }
            long t1 = System.nanoTime();
            System.out.println("Java result " + sum);
            java = t1-t0;
        }
        return java;
    }
    
    int length(Object o) {
        return //o instanceof Object[] ? ((Object[])o).length
                //: o instanceof boolean[] ? ((boolean[])o).length 
                //: o instanceof float[] ? ((float[])o).length
                //: o instanceof double[] ? ((double[])o).length
                /*:*/ o instanceof byte[] ? ((byte[])o).length
                : o instanceof short[] ? ((short[])o).length
                : o instanceof int[] ? ((int[])o).length
                : o instanceof long[] ? ((long[])o).length
                : die(1);
    }
    
    long get(Object o, int ii) {
        return //o instanceof Object[] ? ((Object[])o)[ii] 
                //: o instanceof boolean[] ? ((boolean[])o)[ii] 
                //: o instanceof float[] ? ((float[])o)[ii]
                //: o instanceof double[] ? ((double[])o)[ii]
                /*:*/ o instanceof byte[] ? ((byte[])o)[ii]
                : o instanceof short[] ? ((short[])o)[ii]
                : o instanceof int[] ? ((int[])o)[ii]
                : o instanceof long[] ? ((long[])o)[ii]
                : die(1L);
    }
    
    <T> T die(T t) {
        throw new RuntimeException();
    }
}
