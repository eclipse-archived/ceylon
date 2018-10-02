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
package org.eclipse.ceylon.compiler.java.test.fordebug;

import java.io.File;

import org.eclipse.ceylon.compiler.java.test.fordebug.Tracer.HandlerResult;
import org.eclipse.ceylon.compiler.java.test.fordebug.Tracer.MethodEntry;
import org.eclipse.ceylon.compiler.java.test.fordebug.Tracer.MethodExit;
import org.eclipse.ceylon.compiler.java.test.fordebug.Tracer.Step;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

public class TraceTests extends DebuggerTests {
    
    @BeforeClass
    public static void checkPreConditions() {
        Assume.assumeTrue(allowNetworkTests());
        Assume.assumeTrue("Runs on JDK8+", JDKUtils.jdk.providesVersion(JDKUtils.JDK.JDK8.version));
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-trace";
    }
    
    private void compileAndTrace(String mainClass, String ceylonSource) throws Exception {
        compareWithJavaSource(ceylonSource);
        trace(ceylonSource+".ceylon", mainClass);
    }

    private void trace(String ceylonSource, String mainClass) throws Exception {
        String sourceName = new File(ceylonSource).getName();
        String traceFile = ceylonSource.replaceAll(".ceylon$", ".trace");
        try (Tracer tracer = tracer(mainClass)) {
            tracer.start();
            // stop when we enter main()
            MethodEntry entry = tracer.methodEntry().classFilter(mainClass).methodFilter("main").result(HandlerResult.SUSPEND).enable();
            // resume until we hit that, then disable it
            tracer.resume();
            entry.disable();
            // now step into everything, logging only events which come from code in the given source file.
            Step step = tracer.step().within(sourceName).log().enable();
            // and also listen out for when we exit main()
            MethodExit exit = tracer.methodExit().classFilter(mainClass).methodFilter("main").result(HandlerResult.SUSPEND).enable();
            // once we've exited main() disable the step breakpoint
            tracer.resume();
            if (tracer.isVmAlive()) {
                step.disable();
                tracer.resume();
            }
            System.err.println(tracer.getTrace());
            assertSameTrace(tracer, traceFile);
        }
    }
    
    @Test
    public void testAssertFalse() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.assertFalse_", 
                "trace/AssertFalse"
                );
    }
    
    @Test
    public void testDefaultParameters() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.defaultedParametersMain_",
                "trace/DefaultedParameters"
                );
    }
    
    @Test
    public void testBug2043() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.bug2043_",
                "trace/Bug2043"
                );
    }
    
    @Test
    public void testBug2046() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.bug2046_",
                "trace/Bug2046"
                );
    }
    
    @Test
    public void testBug2047() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.bug2047_",
                "trace/Bug2047"
                );
    }
    
    @Test
    public void testSwitch() throws Exception {
        compileAndTrace(
                "org.eclipse.ceylon.compiler.java.test.fordebug.trace.swtch_",
                "trace/Switch"
                );
    }
}
