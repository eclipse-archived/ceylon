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
package org.eclipse.ceylon.tools.version;

import java.io.StringWriter;
import java.util.Arrays;

import org.eclipse.ceylon.common.tool.ToolFactory;
import org.eclipse.ceylon.common.tool.ToolLoader;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.common.tools.CeylonToolLoader;
import org.eclipse.ceylon.tools.test.AbstractToolTests;
import org.eclipse.ceylon.tools.version.CeylonVersionTool;
import org.junit.Assert;
import org.junit.Test;

public class VersionToolTests extends AbstractToolTests {
    
    @Test
    public void testAll() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--src", "test/src/org/eclipse/ceylon/tools/version/modules"));
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
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--src", "test/src/org/eclipse/ceylon/tools/version/modules",
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
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--src", "test/src/org/eclipse/ceylon/tools/version/modules", 
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
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--src", "test/src/org/eclipse/ceylon/tools/version/modules",
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
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--src", "test/src/org/eclipse/ceylon/tools/version/modules",
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
    
    @Test
    public void testAllCwd() throws Exception {
        ToolModel<CeylonVersionTool> model = pluginLoader.loadToolModel("version");
        CeylonVersionTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList(
                "--cwd", "test",
                "--src", "src/org/eclipse/ceylon/tools/version/modules"));
        StringWriter out = new StringWriter();
        tool.setOut(out);
        tool.run();
        Assert.assertEquals(
                "bar/3.1\n"+
                "baz/1.2\n"+
                "foo/1.0\n", 
                normalizeLineEndings(out.toString()));
    }
    
}
