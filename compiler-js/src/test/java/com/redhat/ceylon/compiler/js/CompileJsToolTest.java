package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
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
        ArrayList<String> result = new ArrayList<String>();
        result.add("--rep=build/runtime");
        result.add("--out=build/test-modules");
        result.addAll(Arrays.asList(args));
        return result;
    }
    
    @Test
    public void testLoad() {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        pluginFactory.bindArguments(tool, args());
    }

    @Test(expected=OptionArgumentException.ToolInitializationException.class)
    public void testDefaultSourceInvalidResource1() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/invalid.txt"));
        jsc.run();
    }

    @Test(expected=OptionArgumentException.ToolInitializationException.class)
    public void testDefaultSourceInvalidResource2() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/invalid",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
    }

    @Test(expected=OptionArgumentException.ToolInitializationException.class)
    public void testDefaultInvalidSourceValidResource1() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/invalid.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
    }

    @Test(expected=OptionArgumentException.ToolInitializationException.class)
    public void testDefaultInvalidSourceValidResource2() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/invalid",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
    }

    @Test
    public void testDefaultSourceValidResource() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
        checkCompilerResult("build/test-modules/default", "default");
        checkResources("build/test-modules/default/default-resources.zip", "test.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip", "m1res.txt",
                "m1/m1res.txt", "subdir/third.txt", "ROOT/inroot.txt", "ALTROOT/altroot.txt");
    }

    @Test
    public void testDefaultSourceNoResources() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon"));
        jsc.run();
        checkCompilerResult("build/test-modules/default", "default");
        checkNoResources("build/test-modules/default/default-resources.zip");
    }

    @Test
    public void testDefaultModule() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "default"));
        jsc.run();
        checkCompilerResult("build/test-modules/default", "default");
        checkResources("build/test-modules/default/default-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ROOT/inroot.txt", "ALTROOT/altroot.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip",
                "m1res.txt", "m1/m1res.txt", "m1/ROOT/m1root.txt", "ROOT/m1root.txt", "m1/ALTROOT/altrootm1.txt");
    }

    @Test
    public void testDefaultModuleWithAltRoot() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "--resource-root=ALTROOT",
                "default"));
        jsc.run();
        checkCompilerResult("build/test-modules/default", "default");
        checkResources("build/test-modules/default/default-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ALTROOT/altroot.txt", "ROOT/inroot.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip",
                "m1res.txt", "m1/m1res.txt", "m1/ROOT/m1root.txt", "ROOT/m1root.txt", "m1/ALTROOT/altrootm1.txt");
    }

    @Test(expected=OptionArgumentException.ToolInitializationException.class)
    public void testModuleSourceInvalidResource() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon",
                "src/test/resources/res_test/m1/invalid.txt"));
        jsc.run();
    }

    @Test
    public void testModuleSourceValidResource() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon",
                "src/test/resources/res_test/m1/m1res.txt"));
        jsc.run();
        checkCompilerResult("build/test-modules/m1/0.1", "m1-0.1");
        checkResources("build/test-modules/m1/0.1/m1-0.1-resources.zip", "m1/m1res.txt");
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "m1root.txt", 
                "ROOT/m1root.txt", "ROOT/inroot.txt", "ALTROOT/altroot.txt");
    }

    @Test
    public void testModuleFileDefaultResourceFile() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
        checkCompilerResult("build/test-modules/m1/0.1", "m1-0.1");
        checkNoResources("build/test-modules/m1/0.1/m1-0.1-resources.zip");
        checkResources("build/test-modules/default/default-resources.zip", "test.txt");
        checkExcludedResources("build/test-modules/default/default-resources.zip", "another_test.txt");
    }

    @Test
    public void testModuleFileNoResources() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "src/test/resources/loader/pass1/m1/test.ceylon",
                "src/test/resources/loader/pass1/m1/module.ceylon",
                "src/test/resources/loader/pass1/m1/package.ceylon"));
        jsc.run();
        checkCompilerResult("build/test-modules/m1/0.1", "m1-0.1");
        checkNoResources("build/test-modules/default/default-resources.zip");
    }

    @Test
    public void testModule() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "m1"));
        jsc.run();
        checkCompilerResult("build/test-modules/m1/0.1", "m1-0.1");
        checkResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "m1root.txt", "m1/m1res.txt", "m1/ALTROOT/altrootm1.txt");
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt",
                "ROOT/inroot.txt", "ALTROOT/altroot.txt");
    }

    @Test
    public void testModuleWithAltRoot() throws Exception {
        FileUtil.delete(new File("build/test-modules"));
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--source=src/test/resources/loader/pass1",
                "--resource=src/test/resources/res_test",
                "--resource-root=ALTROOT",
                "m1"));
        jsc.run();
        checkCompilerResult("build/test-modules/m1/0.1", "m1-0.1");
        checkResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "altrootm1.txt", "m1/m1res.txt");
        checkExcludedResources("build/test-modules/m1/0.1/m1-0.1-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt", "ALTROOT/altroot.txt", "ROOT/inroot.txt");
    }

    void checkCompilerResult(String path, String modVerName) throws IOException {
        String[] names = {
                modVerName + ".js",
                modVerName + ".js.sha1",
                modVerName + "-model.js",
                modVerName + "-model.js.sha1",
                modVerName + ".src",
                modVerName + ".src.sha1",
        };
        for (String name : names) {
            File out = new File(path, name);
            Assert.assertTrue("Missing compiler output file", out.exists());
        }
    }

    void checkNoResources(String path) throws IOException {
        File res = new File(path);
        Assert.assertFalse("No resource should exist", res.exists());
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
