package com.redhat.ceylon.compiler.java.test.statement;

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
    public void testLopOptimArraySequenceIterationStaticBench(){
        compile("loop/optim/ArraySequenceIterationStaticBench.ceylon");
        long opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBench");
        opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBench");
        System.gc();
        long unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBenchDis");
        unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arraySequenceIterationStaticBenchDis");
        //System.gc();
        //long java = arraySequenceIterationStaticJava();
        //java = arraySequenceIterationStaticJava();
        
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
        //System.out.println("Java took " + java/1_000_000 + "ms");
    }
    
    @Test
    public void testLopOptimArrayIterationDynamic(){
        compareWithJavaSource("loop/optim/ArrayIterationDynamic");
    }
    
    @Test
    public void testLopOptimArrayIterationDynamicBench(){
        compile("loop/optim/ArrayIterationDynamicBench.ceylon");
        long optArray = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArray");
        optArray = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArray");
        System.gc();
        long optArraySequence = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArraySequence");
        optArraySequence = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedArraySequence");
        System.gc();
        long optRange = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedRange");
        optRange = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchOptimizedRange");
        System.gc();
        long unoptArray = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArray");
        unoptArray = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArray");
        System.gc();
        long unoptArraySequence = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArraySequence");
        unoptArraySequence = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedArraySequence");
        System.gc();
        long unoptRange = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedRange");
        unoptRange = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.arrayIterationDynamicBenchUnoptimizedRange");
        System.gc();
        //long java = arrayIterationStaticJava();
        //java = arrayIterationStaticJava();
        
        System.out.println("Optimized with Array argument took " + optArray/1_000_000 + "ms");
        System.out.println("Optimized with ArraySequence argument took " + optArraySequence/1_000_000 + "ms");
        System.out.println("Optimized with Range argument took " + optRange/1_000_000 + "ms");
        System.out.println("Unoptimized with Array argument took " + unoptArray/1_000_000 + "ms");
        System.out.println("Unoptimized with ArraySequence argument took " + unoptArraySequence/1_000_000 + "ms");
        System.out.println("Unoptimized with Range argument took " + unoptRange/1_000_000 + "ms");
        //System.out.println("Java took " + java/1_000_000 + "ms");
    }
    
    private final void arrayOfInts_object(final int[] array) {
        long sum = 0;
        long t0 = System.nanoTime();
        for (long ii = 0; ii < 10_000_000; ii++) {
            final java.lang.Object array$6 = array;
            final int length$7 = com.redhat.ceylon.compiler.java.Util.arrayLength(array$6);
            for (int i$8 = 0; i$8 < length$7; i$8++) {
                final long element = com.redhat.ceylon.compiler.java.Util.getIntegerArray(array$6, i$8);
                sum += element;
            }
        }
        long t1 = System.nanoTime();
        System.out.println(sum);
        System.out.println((t1-t0)/1_000_000 + "ms");
    }
    
    private final void arrayOfInts_array(final int[] array) {
        long sum = 0;
        long t0 = System.nanoTime();
        for (long ii = 0; ii < 10_000_000; ii++) {
            final int[] array$6 = array;
            for (int i$8 = 0; i$8 < array$6.length; i$8++) {
                final long element = array$6[i$8];
                sum += element;
            }
        }
        long t1 = System.nanoTime();
        System.out.println(sum);
        System.out.println((t1-t0)/1_000_000 + "ms");
    }
    
    @Test
    public void foo() {
        int [] ints = new int[1000];
        for (int ii = 0; ii < ints.length; ii++) {
            ints[ii] = ii;
        }
        arrayOfInts_array(ints);
        arrayOfInts_object(ints);
        arrayOfInts_array(ints);
        arrayOfInts_object(ints);
    }
    
    private final long bar_ints(int n) {
        System.out.println("ints(" + n + ")");
        long t0 = System.nanoTime();
        long sum = 0;
        for (int ii = 0; ii < n; ii++) {
            for (int jj = 0; jj < 1000; jj++) {
                sum = jj + sum/2;
            }
        }
        long t1 = System.nanoTime();
        System.out.println(sum);
        return t1-t0;
    }
    
    private final long bar_longs(long n) {
        System.out.println("longs(" + n + ")");
        long t0 = System.nanoTime();
        long sum = 0;
        for (long ii = 0; ii < n; ii++) {
            for (long jj = 0; jj < 1000L; jj++) {
                sum = jj + sum/2;
            }
        }
        long t1 = System.nanoTime();
        System.out.println(sum);
        return t1-t0;
    }
    
    @Test
    public void bar() {
        System.out.println(bar_ints(100_000)/1_000_000 + "ms for ints");
        System.out.println(bar_longs(100_000)/1_000_000 + "ms for longs");
        System.out.println(bar_ints(1_000_000)/1_000_000 + "ms for ints");
        System.out.println(bar_longs(1_000_000)/1_000_000 + "ms for longs");
        System.out.println(bar_ints(2_000_000)/1_000_000 + "ms for ints");
        System.out.println(bar_longs(2_000_000)/1_000_000 + "ms for longs");
        System.out.println(bar_ints(2_000_000)/1_000_000 + "ms for ints");
        System.out.println(bar_longs(2_000_000)/1_000_000 + "ms for longs");
    }
}
