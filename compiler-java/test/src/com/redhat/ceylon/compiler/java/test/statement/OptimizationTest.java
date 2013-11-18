package com.redhat.ceylon.compiler.java.test.statement;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
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
        compareWithJavaSource("loop/optim/RangeOpIterationOptimization");
    }
    
    @Test
    public void testLopRangeOpIterationOptimizationCorrect(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.optim.rangeOpIterationOptimizationCorrect", 
                "loop/optim/RangeOpIterationOptimizationCorrect.ceylon");
    }
    
    @Test
    public void testLopOptimArrayIterationStatic(){
        compareWithJavaSource("loop/optim/ArrayIterationStatic");
    }
    
    @Test
    public void testLopOptimArrayIterationStaticRequired(){
        assertErrors("loop/optim/ArrayIterationStaticRequired",
                new CompilerError(35, "@requireOptimization[\"ArrayIterationStatic\"] assertion failed: static type of iterable in for statement is not Array"),
                new CompilerError(39, "@requireOptimization[\"JavaArrayIterationStatic\"] assertion failed: iterable expression wasn't of form javaArray.array"));
    }
    
    @Test
    @Ignore
    public void testLopOptimArrayIterationStaticBench(){
        compile("loop/optim/ArrayIterationStaticBench.ceylon");
        long java = arrayIterationStaticJava();
        java = arrayIterationStaticJava();
        System.gc();
        long unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis");
        unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis");
        System.gc();
        long opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench");
        opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench");
        System.gc();
        
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
        System.out.println("Java took " + java/1_000_000 + "ms");
    }

    private long arrayIterationStaticJava() {
        int arrayIterationStaticN = 1_000_000;
        int[] arrayIterationStaticInts = new int[100];
        for (int i = 0; i < arrayIterationStaticInts.length; i++) {
            arrayIterationStaticInts[i] = i;
        }
        int i = arrayIterationStaticN;
        int sum = 0;
        long t0 = System.nanoTime();
        while (i > 0) {
            sum = 0;
            for (int ii = 0; ii < arrayIterationStaticInts.length; ii++) {
                int x = arrayIterationStaticInts[ii];
                sum += x;
            }
            i--;
        }
        long t1 = System.nanoTime();
        System.out.println("Java result " + sum);
        return t1-t0;
    }
    
    @Test
    public void testLopOptimArraySequenceIterationStatic(){
        compareWithJavaSource("loop/optim/ArraySequenceIterationStatic");
    }
    
    @Test
    @Ignore
    public void testLopOptimArraySequenceIterationStaticBench(){
        compile("loop/optim/ArraySequenceIterationStaticBench.ceylon");
        long opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBench");
        opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBench");
        System.gc();
        long unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBenchDis");
        unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBenchDis");
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
    }
    
    @Test
    public void testLopOptimArrayIterationDynamic(){
        compareWithJavaSource("loop/optim/ArrayIterationDynamic");
    }
    
    @Test
    @Ignore
    public void testLopOptimArrayIterationDynamicBench(){
        compile("loop/optim/ArrayIterationDynamicBench.ceylon");
        long optArray = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArray");
        /*
        long optArraySequence = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArraySequence");
        
        long optRange = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedRange");
        
        long unoptArray = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArray");
        
        long unoptArraySequence = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArraySequence");
        
        long unoptRange = (Long)runThrice("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedRange");
        
        //long java = arrayIterationStaticJava();
        //java = arrayIterationStaticJava();
        */
        System.out.println("Optimized with Array argument took " + optArray/1_000_000 + "ms");
        //System.out.println("Optimized with ArraySequence argument took " + optArraySequence/1_000_000 + "ms");
        //System.out.println("Optimized with Range argument took " + optRange/1_000_000 + "ms");
        //System.out.println("Unoptimized with Array argument took " + unoptArray/1_000_000 + "ms");
        //System.out.println("Unoptimized with ArraySequence argument took " + unoptArraySequence/1_000_000 + "ms");
        //System.out.println("Unoptimized with Range argument took " + unoptRange/1_000_000 + "ms");
        //System.out.println("Java took " + java/1_000_000 + "ms");
    }
    
}
