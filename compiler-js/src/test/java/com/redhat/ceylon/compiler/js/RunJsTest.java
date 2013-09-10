package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/** To run this you need to execute the nodetest ant target first. */
public class RunJsTest {

    static File tmpModules;

    @BeforeClass
    public static void setup() throws IOException {
        tmpModules = File.createTempFile("ceylon", "runjstest");
        tmpModules.delete();
        tmpModules.mkdir();
        File sub = new File(tmpModules, "check/0.1");
        sub.mkdirs();
        File src = new File("build/test/modules/check/0.1");
        for (File f : src.listFiles()) {
            Stitcher.copyFile(f, new File(sub, f.getName()));
        }
    }

    @Test
    public void testModuleLoading() throws Exception {
        CeylonRunJsTool runner = new CeylonRunJsTool();
        runner.setModuleVersion("misc/0.1");
        runner.setRun("test");
        runner.setRepository(Arrays.asList(new URI(tmpModules.getAbsolutePath()), new URI("build/runtime"), new URI("build/test/modules")));
        runner.run();
    }

    @AfterClass
    public static void cleanup() {
        tmpModules.delete();
    }
}
