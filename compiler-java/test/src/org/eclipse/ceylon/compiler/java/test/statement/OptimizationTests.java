/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.test.statement;

import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

public class OptimizationTests extends CompilerTests {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-optim";
    }
    
    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main) {
        Assume.assumeTrue(allowSdkTests());
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.statement.loop.optim", "1");
    }
    
    @Test
    public void testLopRangeOpIterationOptimization(){
        compareWithJavaSource("loop/optim/RangeOpIterationOptimization");
    }
    
    @Test 
    //@Ignore("https://github.com/ceylon/ceylon-compiler/issues/1647")
    public void testLopRangeOpIterationOptimizationCorrect(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.rangeOpIterationOptimizationCorrect", 
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
        run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.bug2130Span");
    }
    
    @Test
    public void testLopOptimBug2130_Measure() {
        compareWithJavaSource("loop/optim/Bug2130_Measure");
        //compile("loop/optim/Bug2130_Measure.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.bug2130Measure");
    }
    
    @Test
    public void testLopSegmentOpIterationOptimization(){
        compareWithJavaSource("loop/optim/SegmentOpIterationOptimization");
    }
    
    @Test
    public void testLopSegmentOpIterationOptimizationCorrect(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.segmentOpIterationOptimizationCorrect", 
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
        Assume.assumeTrue(allowSdkTests());
        compile("loop/optim/ArrayIterationStaticBench.ceylon");
        long java = arrayIterationStaticJava();
        java = arrayIterationStaticJava();
        System.gc();
        
        ModuleWithArtifact[] modules = new ModuleWithArtifact[]{
                getDestModuleWithArtifact("ignored"),
                new ModuleWithArtifact("ceylon.interop.java", Versions.CEYLON_VERSION_NUMBER, "../../ceylon-sdk/modules", "car")
        };
        long unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis", modules);
        unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBenchDis", modules);
        System.gc();
        long opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench", modules);
        opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationStaticBench", modules);
        System.gc();
        
        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
        System.out.println("Java took " + java/1_000_000 + "ms");
    }

    @Test
    @Ignore("For benchmarking only")
    public void testLopOptimArrayIterationBoxedStaticBench(){
        compile("loop/optim/ArrayIterationBoxedStaticBench.ceylon");
        long java = arrayIterationBoxedStaticJava();
        java = arrayIterationBoxedStaticJava();
        System.gc();

        ModuleWithArtifact[] modules = new ModuleWithArtifact[]{
                getDestModuleWithArtifact("ignored"),
                new ModuleWithArtifact("ceylon.interop.java", Versions.CEYLON_VERSION_NUMBER, "../../ceylon-sdk/modules", "car")
        };
        long unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationBoxedStaticBenchDis", modules);
        unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationBoxedStaticBenchDis", modules);
        System.gc();
        long opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationBoxedStaticBench", modules);
        opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.arrayIterationBoxedStaticBench", modules);
        System.gc();

        System.out.println("Optimized took " + opt/1_000_000 + "ms");
        System.out.println("Unoptimized took " + unopt/1_000_000 + "ms");
        System.out.println("Java took " + java/1_000_000 + "ms");
    }

    private long arrayIterationStaticJava() {
        int arrayIterationStaticN = 1_000_000;
        long[] arrayIterationStaticInts = new long[200];
        for (int i = 0; i < arrayIterationStaticInts.length; i++) {
            arrayIterationStaticInts[i] = i;
        }
        int i = arrayIterationStaticN;
        long sum = 0;
        long t0 = System.nanoTime();
        while (i > 0) {
            sum = 0;
            for (int ii = 0; ii < arrayIterationStaticInts.length; ii++) {
                long x = arrayIterationStaticInts[ii];
                sum += x;
            }
            i--;
        }
        long t1 = System.nanoTime();
        System.out.println("Java result " + sum);
        return t1-t0;
    }

    private long arrayIterationBoxedStaticJava() {
        int arrayIterationStaticN = 1_000_000;
        long[] arrayIterationStaticInts = new long[200];
        for (int i = 0; i < arrayIterationStaticInts.length; i++) {
            arrayIterationStaticInts[i] = i;
        }
        int i = arrayIterationStaticN;
        long sum = 0;
        long t0 = System.nanoTime();
        while (i > 0) {
            sum = 0;
            for (int ii = 0; ii < arrayIterationStaticInts.length; ii++) {
                long x = ceylon.language.Integer.instance(arrayIterationStaticInts[ii]).longValue();
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
        long opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBench");
        opt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBench");
        System.gc();
        long unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBenchDis");
        unopt = (Long)run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.tupleIterationStaticBenchDis");
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
        run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.Correct");
    }
    
    @Test
    public void testLopOptimBug1467(){
        compareWithJavaSource("loop/optim/Bug1467");
        run("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.bug1467");
    }
    
    @Test
    public void testLopOptimStringIterationStatic() {
        compareWithJavaSource("loop/optim/StringIterationStatic");
    }
    
    @Ignore("For benchmarking only")
    @Test
    public void testLopOptimDynamicIterationBench() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.loop.optim.dynamicIterationBench_main",
                "loop/optim/DynamicIterationBench.ceylon");
    }
    
    @Test
    public void testLopOptimPrimitiveArrayIteration() {
        compareWithJavaSource("loop/optim/PrimitiveArrayIteration");
    }
    
    @Test
    public void testLopOptimDestructureLoop() {
        compareWithJavaSource("loop/optim/DestructureLoop");
    }
    
    @Test
    public void testSmallIteration() {
        compareWithJavaSource("loop/optim/SmallIteration");
    }
}
