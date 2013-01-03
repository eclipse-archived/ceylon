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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.TestingToolLoader;

public class BashCompletionToolTest {
    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new TestingToolLoader(null, false);
    private PrintStream savedOut;
    private ByteArrayOutputStream out;
    
    Iterable<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    
    public void redirectStdout() {
        this.savedOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }
    
    public void restoreStdout() {
        System.setOut(this.savedOut);
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
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        ""));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "example \n" +
        		"", new String(out.toByteArray()));
    }
    
    @Test
    public void testToolNameCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=1",
                        "--",
                        "/path/to/ceylon",
                        "e"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals("example \n", new String(out.toByteArray()));
    }
    
    @Test
    public void testOptionNameCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "--file\\=\n" +
                "--list-option\\=\n"+
                "--long-name\n"+
                "--pure-option\n"+
                "--short-name\\=\n"+
                "--thread-state\\=\n"+
                "", new String(out.toByteArray()));
    }
    
    @Test
    public void testOptionNameCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--l"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        Assert.assertEquals(
                "--list-option\\=\n" +
                "--long-name\n" +
                "", new String(out.toByteArray()));
    }
    
    @Test
    public void testFileCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file="));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--file=src/\n"));
        Assert.assertTrue(files, files.contains("--file=test/\n"));
    }
    
    @Test
    public void testFileCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--file=s"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--file=src/"));
        Assert.assertFalse(files, files.contains("--file=test/ \n"));
    }
    
    @Test
    public void testEnumCompletion()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state="));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--thread-state=NEW\n"));
        Assert.assertTrue(files, files.contains("--thread-state=BLOCKED\n"));
        Assert.assertTrue(files, files.contains("--thread-state=RUNNABLE\n"));
    }
    
    @Test
    public void testEnumCompletion_partial()  throws Exception {
        ToolModel<CeylonBashCompletionTool> model = pluginLoader.loadToolModel("bash-completion");
        Assert.assertNotNull(model);
        CeylonBashCompletionTool tool = pluginFactory.bindArguments(model, 
                args("--cword=2",
                        "--",
                        "/path/to/ceylon",
                        "example",
                        "--thread-state=N"));
        try {
            redirectStdout();
            tool.run();
        } finally {
           restoreStdout();
        } 
        String files = new String(out.toByteArray());
        Assert.assertTrue(files, files.contains("--thread-state=NEW \n"));
        Assert.assertFalse(files, files.contains("--thread-state=BLOCKED \n"));
        Assert.assertFalse(files, files.contains("--thread-state=RUNNABLE \n"));
    }

}
