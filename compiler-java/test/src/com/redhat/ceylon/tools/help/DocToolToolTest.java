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
package com.redhat.ceylon.tools.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;
import com.redhat.ceylon.common.tools.help.CeylonDocToolTool;

public class DocToolToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    private File dir;
    
    @Before
    public void setup(){
        dir = FileUtil.makeTempDir("DocToolToolTest");
    }

    @After
    public void cleanup(){
        FileUtil.delete(dir);
    }

    private void runDocTool(String toolName, String... otherArgs) throws IOException {
        ToolModel<CeylonDocToolTool> model = pluginLoader.loadToolModel("doc-tool");
        Assert.assertTrue(model.isPorcelain());
        Assert.assertNotNull(model);
        List<String> toolArgs = new ArrayList<>();
        toolArgs.addAll(Arrays.asList(toolName, "--output=" + dir.getAbsolutePath()));
        toolArgs.addAll(Arrays.asList(otherArgs));
        CeylonDocToolTool tool = pluginFactory.bindArguments(model, toolArgs);
        Assert.assertEquals(0, dir.listFiles().length);
        tool.run();
    }

    private void assertHtmlFiles(String toolName) throws IOException {
        List<File> files = Arrays.asList(dir.listFiles());
        Assert.assertFalse(files.isEmpty());
        Assert.assertTrue(files.contains(new File(dir, "doc-tool.css")));
        File file = new File(dir, "ceylon-" + toolName + ".html");
        Assert.assertTrue(file+" not in output", files.contains(file));
        dump(file);
    }
    
    private void assertTxtFiles(String toolName) throws IOException {
        List<File> files = Arrays.asList(dir.listFiles());
        Assert.assertFalse(files.isEmpty());
        String name;
        if(!toolName.isEmpty())
            name = "ceylon-" + toolName + ".txt";
        else
            name = "ceylon.txt";    
        File file = new File(dir, name);
        Assert.assertTrue(file+" not in output", files.contains(file));
        dump(file);
    }
        

    private void dump(File file) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line = r.readLine();
            while (line != null) {
                System.out.println(line);
                line = r.readLine();
            }
        }
    }
    
    @Test
    public void testHelp() throws Exception {
        runDocTool("help");
        assertHtmlFiles("help");
    }
    
    @Test
    public void testHelpTxt() throws Exception {
        runDocTool("help", "--format=txt");
        assertTxtFiles("help");
    }
    
    @Test
    public void testCompiler() throws Exception {
        runDocTool("compile");
        assertHtmlFiles("compile");
    }
    
    @Test
    public void testCompilerTxt() throws Exception {
        runDocTool("compile", "--format=txt");
        assertTxtFiles("compile");
    }
    
    @Test
    public void testDoc() throws Exception {
        runDocTool("doc");
        assertHtmlFiles("doc");
    }
    
    @Test
    public void testDocTxt() throws Exception {
        runDocTool("doc", "--format=txt");
        assertTxtFiles("doc");
    }
    
    @Test
    public void testImportJar() throws Exception {
        runDocTool("import-jar");
        assertHtmlFiles("import-jar");
    }
    
    @Test
    public void testImportJarTxt() throws Exception {
        runDocTool("import-jar", "--format=txt");
        assertTxtFiles("import-jar");
    }
    
    @Test
    public void testDocTool() throws Exception {
        runDocTool("doc-tool");
        assertHtmlFiles("doc-tool");
    }
    
    @Test
    public void testDocToolTxt() throws Exception {
        runDocTool("doc-tool", "--format=txt");
        assertTxtFiles("doc-tool");
    }
    
    @Test
    public void testWibbleTxt() throws Exception {
        try {
            runDocTool("wibble", "--format=txt");
        } catch (OptionArgumentException e) {
            String s = e.getMessage();
            Assert.assertTrue(s, s.startsWith("Invalid value 'wibble' given for argument 'tool'"));
        }
    }
    
    @Test
    public void testAll() throws Exception {
        runDocTool("--all-porcelain", "--index");
        assertHtmlFiles("compile");
        assertHtmlFiles("doc");
        assertHtmlFiles("import-jar");
        assertHtmlFiles("help");
        assertHtmlFiles("doc-tool");
        Assert.assertTrue(Arrays.asList(dir.listFiles()).contains(new File(dir, "index.html")));
    }
    
    @Test
    public void testAllTxt() throws Exception {
        runDocTool("--all-porcelain", "--format=txt");
        assertTxtFiles("compile");
        assertTxtFiles("doc");
        assertTxtFiles("import-jar");
        assertTxtFiles("help");
        assertTxtFiles("doc-tool");
    }

    @Test
    public void testTopLevelTxt() throws Exception {
        runDocTool("", "--format=txt");
        assertTxtFiles("");
    }
    

}
