package com.redhat.ceylon.tools.version;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;

public class VersionToolTest {
    protected static String normalizeLineEndings(String txt) {
        String result = txt.replaceAll("\r\n", "\n"); // Windows
        result = result.replaceAll("\r", "\n"); // Mac (OS<=9)
        return result;
    }
    
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    @Test
    public void testAll() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--src", "test/src/com/redhat/ceylon/tools/version/modules"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "bar/3.1\n"+
                "baz/1.2\n"+
                "foo/1.0\n", 
                normalizeLineEndings(out.toString()));
    }
    @Test
    public void testAllAndDeps() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--src", "test/src/com/redhat/ceylon/tools/version/modules",
                "--dependencies"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "bar/3.1\n"+
                "baz/1.2\n"+
                "foo/1.0\n", 
                normalizeLineEndings(out.toString()));
    }
    @Test
    public void testFoo() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--src", "test/src/com/redhat/ceylon/tools/version/modules", 
                "foo"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "foo/1.0\n", 
                normalizeLineEndings(out.toString()));
    }
    @Test
    public void testFooAndDeps() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--src", "test/src/com/redhat/ceylon/tools/version/modules",
                "--dependencies",
                "foo"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "foo/1.0\n" +
                "bar/3.1 depends on foo/1.0\n" +
                "baz/1.2 depends on foo/1.0\n", 
                normalizeLineEndings(out.toString()));
    }
    @Test
    public void testBarAndDeps() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, Arrays.asList(
                "--src", "test/src/com/redhat/ceylon/tools/version/modules",
                "--dependencies",
                "bar"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "bar/3.1\n" +
                "baz/1.2 depends on bar/3.1\n", 
                normalizeLineEndings(out.toString()));
    }
    
}
