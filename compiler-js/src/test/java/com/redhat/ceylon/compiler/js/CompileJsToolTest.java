package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ServiceToolLoader;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;

public class CompileJsToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new ServiceToolLoader(Tool.class) {
        
        @Override
        public String getToolName(String className) {
            return classNameToToolName(className);
        }
        
        
    };

    private List<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    @Test
    public void testLoad() {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        pluginFactory.bindArguments(tool, args());
    }

    @Test(expected=CompilerErrorException.class)
    public void testDefaultModuleInvalidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",
                "--out=build/test-modules",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/invalid.txt"));
        jsc.run();
    }

    @Test
    public void testDefaultModuleValidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
        checkResources("build/test-modules/default/default-resources.zip", "test.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip", "m1res.txt",
                "m1/m1res.txt", "subdir/third.txt", "ROOT/inroot.txt", "ALTROOT/altroot.txt");
    }

    @Test
    public void testDefaultModuleAllResources() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon"));
        jsc.run();
        checkResources("build/test-modules/default/default-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ROOT/inroot.txt", "ALTROOT/altroot.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip",
                "m1res.txt");
    }

    @Test
    public void testDefaultModuleWithAltRoot() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "--resource-root=ALTROOT",
                "src/test/resources/doc/calls.ceylon"));
        jsc.run();
        checkResources("build/test-modules/default/default-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ALTROOT/altroot.txt", "ROOT/inroot.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip",
                "m1res.txt");
    }

    @Test(expected=CompilerErrorException.class)
    public void testModuleInvalidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",
                "--out=build/test-modules",
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/*.ceylon",
                "src/test/resources/res_test/invalid.txt"));
        jsc.run();
    }

    @Test
    public void testModuleValidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip", "test.txt");
    }

    @Test
    public void testModuleAllResources() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon"));
        jsc.run();
        checkResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "m1root.txt", "m1res.txt", "ALTROOT/altrootm1.txt");
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ROOT/m1root.txt",
                "ROOT/inroot.txt", "ALTROOT/altroot.txt");
    }

    @Test
    public void testModuleWithAltRoot() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--out=build/test-modules",
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "--resource-root=ALTROOT",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon"));
        jsc.run();
        checkResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "altrootm1.txt", "m1res.txt");
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ALTROOT/altroot.txt", "ROOT/inroot.txt");
    }

    void checkResources(String path, String... paths) throws IOException {
        File res = new File(path);
        Assert.assertTrue("Resources file missing", res.exists() && res.isFile());
        try (ZipFile zip = new ZipFile(res)) {
            for (String r : paths) {
                ZipEntry e = zip.getEntry(r);
                Assert.assertTrue("Missing resource " + r, e != null);
            }
        } finally {
            //nothing
        }
    }

    void checkExcludedResources(String path, String... paths) throws IOException {
        File res = new File(path);
        Assert.assertTrue("Resources file missing", res.exists() && res.isFile());
        try (ZipFile zip = new ZipFile(res)) {
            for (String r : paths) {
                ZipEntry e = zip.getEntry(r);
                Assert.assertNull("Resource should NOT be in resources file: " + r, e);
            }
        } finally {
            //nothing
        }
    }

}
