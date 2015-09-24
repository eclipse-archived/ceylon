package com.redhat.ceylon.compiler.java.test.statement;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class OptimizationTests extends CompilerTests {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-optim";
    }
    
    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main) {
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.statement.loop.optim", "1");
    }
    
    @Test
    public void testLopRangeOpIterationOptimization(){
        compareWithJavaSource("loop/optim/RangeOpIterationOptimization");
    }
    
    @Test 
    //@Ignore("https://github.com/ceylon/ceylon-compiler/issues/1647")
    public void testLopRangeOpIterationOptimizationCorrect(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.optim.rangeOpIterationOptimizationCorrect", 
                "loop/optim/RangeOpIterationOptimizationCorrect.ceylon",
                "loop/optim/ArrayBuilder.ceylon");
    }
    
    @Test
    public void testLopOptimSpanIteration() {
        compareWithJavaSource("loop/optim/SpanIteration");
    }
    
    @Test
    public void testLopOptimBug2130_Span() {
        compareWithJavaSource("loop/optim/Bug2130_Span");
        //compile("loop/optim/Bug2130_Span.ceylon");
        run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.bug2130Span");
    }
    
    @Test
    public void testLopOptimBug2130_Measure() {
        compareWithJavaSource("loop/optim/Bug2130_Measure");
        //compile("loop/optim/Bug2130_Measure.ceylon");
        run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.bug2130Measure");
    }
    
    @Test
    public void testLopSegmentOpIterationOptimization(){
        compareWithJavaSource("loop/optim/SegmentOpIterationOptimization");
    }
    
    @Test
    public void testLopSegmentOpIterationOptimizationCorrect(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.optim.segmentOpIterationOptimizationCorrect", 
                "loop/optim/SegmentOpIterationOptimizationCorrect.ceylon",
                "loop/optim/ArrayBuilder.ceylon");
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
    public void testLopOptimJavaArrayIterationStatic(){
        compareWithJavaSource("loop/optim/JavaArrayIterationStatic");
    }
    
    @Test
    @Ignore("For benchmarking only")
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
    @Ignore("For benchmarking only")
    public void testLopOptimTupleIterationStaticBench(){
        compile("loop/optim/TupleIterationStaticBench.ceylon");
        long opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBench");
        opt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBench");
        System.gc();
        long unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBenchDis");
        unopt = (Long)run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBenchDis");
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
    }
    
    @Test
    public void testLopOptimTupleIterationStatic(){
        compareWithJavaSource("loop/optim/TupleIterationStatic");
    }
    
    @Test
    public void testLopOptimArrayIterationDynamic(){
        compareWithJavaSource("loop/optim/ArrayIterationDynamic");
    }
    
    @Test
    public void testLopOptimCorrect(){
        compareWithJavaSource("loop/optim/Correct");
        //compile("loop/optim/Correct.ceylon");
        run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.Correct");
    }
    
    @Test
    public void testLopOptimBug1467(){
        compareWithJavaSource("loop/optim/Bug1467");
        run("com.redhat.ceylon.compiler.java.test.statement.loop.optim.bug1467");
    }
    
    @Test
    public void testLopOptimStringIterationStatic() {
        compareWithJavaSource("loop/optim/StringIterationStatic");
    }
    
    @Ignore("For benchmarking only")
    @Test
    public void testLopOptimDynamicIterationBench() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.optim.dynamicIterationBench_main",
                "loop/optim/DynamicIterationBench.ceylon");
    }
}
