package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;

/** To run this you need to execute the nodetest ant target first. */
public class RunJsTest {

    static File tmpModules;

    @BeforeClass
    public static void setup() throws IOException {
        tmpModules = Files.createTempDirectory("ceylon-runjstest-").toFile();
        tmpModules.delete();
        tmpModules.mkdir();
        File sub = new File(tmpModules, "check/0.1");
        sub.mkdirs();
        File src = new File("build/test/proto/check/0.1");
        if (src.isDirectory()) {
            for (File f : src.listFiles()) {
                Files.copy(f.toPath(), new File(sub, f.getName()).toPath());
            }
        }
    }

    @Test
    public void testModuleLoading() throws Exception {
        CeylonRunJsTool runner = new CeylonRunJsTool();
        runner.setModuleVersion("misc/0.1");
        runner.setRun("test");
        runner.setRepository(Arrays.asList(new URI(tmpModules.getAbsolutePath()), new URI("build/runtime"), new URI("build/test/proto")));
        runner.run();
    }

    @AfterClass
    public static void cleanup() {
        FileUtil.deleteQuietly(tmpModules);
    }

    @Test
    public void testResources() throws Exception {
        //Compile a module with resources
        CeylonCompileJsTool compiler = new CeylonCompileJsTool();
        compiler.setRepositoryAsStrings(Arrays.asList("build/runtime"));
        compiler.setSource(Arrays.asList(new File("src/test/resources/doc")));
        compiler.setSkipSrcArchive(true);
        compiler.setResource(Arrays.asList(new File("src/test/resources/res_test")));
        compiler.setModule(Arrays.asList("default"));
        compiler.setOut("build/modules");
        compiler.run();
        //Run it, just to make sure the resources were exploded
        CeylonRunJsTool runner = new CeylonRunJsTool();
        runner.setModuleVersion("default");
        runner.setRun("run");
        runner.setRepositoryAsStrings(Arrays.asList("build/runtime", "build/modules"));
        runner.run();
        Assert.assertTrue("test.txt is missing", new File("build/modules/default/module-resources/test.txt").exists());
        Assert.assertTrue("another_test.txt is missing", new File("build/modules/default/module-resources/another_test.txt").exists());
        Assert.assertTrue("third.txt is missing", new File("build/modules/default/module-resources/subdir/third.txt").exists());
    }

}
