package com.redhat.ceylon.tools.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;

public class DocToolToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    private File dir = FileUtil.makeTempDir("DocToolToolTest");
    
    private void runDocTool(String toolName) throws IOException {
        PluginModel<DocToolTool> model = pluginLoader.loadToolModel("doc-tool");
        Assert.assertNotNull(model);
        DocToolTool tool = pluginFactory.bindArguments(model, Arrays.asList(toolName, "--output=" + dir.getAbsolutePath()));
        Assert.assertEquals(0, dir.listFiles().length);
        tool.run();
        List<File> files = Arrays.asList(dir.listFiles());
        Assert.assertFalse(files.isEmpty());
        Assert.assertTrue(files.contains(new File(dir, "doc-tool.css")));
        File file = new File(dir, toolName + ".html");
        Assert.assertTrue(files.contains(file));
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line = r.readLine();
            while (line != null) {
                System.out.println(line);
                line = r.readLine();
            }
        }
        
    }
    
    @Test
    public void testExample() throws Exception {
        runDocTool("example");
    }
    
    @Test
    public void testHelp() throws Exception {
        runDocTool("help");
    }
    
    @Test
    public void testCompiler() throws Exception {
        runDocTool("compile");
    }
    
    @Test
    public void testDoc() throws Exception {
        runDocTool("doc");
    }
    
    @Test
    public void testImportJar() throws Exception {
        runDocTool("import-jar");
    }
    
    @Test
    public void testDocTool() throws Exception {
        runDocTool("doc-tool");
    }

}
