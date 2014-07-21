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
    public void testInvalidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/invalid.txt"));
        jsc.run();
    }

    @Test
    public void testValidResource() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon",
                "src/test/resources/res_test/test.txt"));
        jsc.run();
        checkResources("modules/default/default-resources.zip", "test.txt");
    }

    @Test
    public void testAllResources() throws Exception {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        CeylonCompileJsTool jsc = pluginFactory.bindArguments(tool, args(
                "--rep=build/runtime",// "--verbose",
                "--source=src/test/resources/doc",
                "--resource=src/test/resources/res_test",
                "src/test/resources/doc/calls.ceylon"));
        jsc.run();
        checkResources("modules/default/default-resources.zip",
                "test.txt", "another_test.txt", "subdir/third.txt");
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

}
