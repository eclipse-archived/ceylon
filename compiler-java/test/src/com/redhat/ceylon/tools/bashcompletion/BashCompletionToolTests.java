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
package com.redhat.ceylon.tools.bashcompletion;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.tools.TestingToolLoader;

public class BashCompletionToolTests {
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new TestingToolLoader(null, false);
    
    Iterable<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    private CeylonTool getMainTool() {
        return pluginLoader.instance("", null);
    }
    
    @Test
    public void testPlumbing()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertTrue(model.isPlumbing());
    }
    
    @Test
    public void testToolNameCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        ""));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertEquals(
                "example \n" +
        		"", b.toString().replace("\r\n", "\n"));
    }
    
    @Test
    public void testToolNameCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        "e"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertEquals("example \n", b.toString().replace("\r\n", "\n"));
    }
    
    @Test
    public void testOptionNameCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertEquals(
                "--file\\=\n" +
                "--list-option\\=\n"+
                "--long-name\n"+
                "--pure-option\n"+
                "--short-name\\=\n"+
                "--thread-state\\=\n"+
                "", b.toString().replace("\r\n", "\n"));
    }
    
    @Test
    public void testOptionNameCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--l"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertEquals(
                "--list-option\\=\n" +
                "--long-name\n" +
                "", b.toString().replace("\r\n", "\n"));
    }
    
    @Test
    public void testFileCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file="));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String files = b.toString().replace("\r\n", "\n");
        Assert.assertTrue(files, files.contains("--file=src/\n"));
        Assert.assertTrue(files, files.contains("--file=test/\n"));
    }
    
    @Test
    public void testFileCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file=s"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String files = b.toString().replace("\r\n", "\n");
        Assert.assertTrue(files, files.contains("--file=src/"));
        Assert.assertFalse(files, files.contains("--file=test/ \n"));
    }
    
    @Test
    public void testEnumCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state="));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String files = b.toString().replace("\r\n", "\n");
        Assert.assertTrue(files, files.contains("--thread-state=NEW\n"));
        Assert.assertTrue(files, files.contains("--thread-state=BLOCKED\n"));
        Assert.assertTrue(files, files.contains("--thread-state=RUNNABLE\n"));
    }
    
    @Test
    public void testEnumCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, getMainTool(),
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state=N"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String files = b.toString().replace("\r\n", "\n");
        Assert.assertTrue(files, files.contains("--thread-state=NEW \n"));
        Assert.assertFalse(files, files.contains("--thread-state=BLOCKED \n"));
        Assert.assertFalse(files, files.contains("--thread-state=RUNNABLE \n"));
    }

}
