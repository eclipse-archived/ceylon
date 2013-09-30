package com.redhat.ceylon.compiler.java.test.fordebug;

import java.io.File;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.fordebug.Tracer.HandlerResult;
import com.redhat.ceylon.compiler.java.test.fordebug.Tracer.MethodEntry;
import com.redhat.ceylon.compiler.java.test.fordebug.Tracer.MethodExit;
import com.redhat.ceylon.compiler.java.test.fordebug.Tracer.Step;

public class TraceTests extends DebuggerTest {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-trace";
    }
    
    private void compileAndTrace(String mainClass, String ceylonSource) throws Exception {
        compile(ceylonSource);
        trace(ceylonSource, mainClass);
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
            // now log everything within DefaultedParameters.ceylon
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
                "com.redhat.ceylon.compiler.java.test.fordebug.trace.assertFalse_", 
                "trace/AssertFalse.ceylon"
                );
    }
    
    @Test
    public void testDefaultParameters() throws Exception {
        compileAndTrace(
                "com.redhat.ceylon.compiler.java.test.fordebug.trace.defaultedParametersMain_",
                "trace/DefaultedParameters.ceylon"
                );
    }
    
}
