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

import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.help.CeylonHelpTool;

public class HelpToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    @Test
    public void testHelp() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertTrue(model.isPorcelain());
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
        tool.run();
    }
    
    @Test
    public void testHelpExample() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, Arrays.asList("example"));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("Invalid value 'example' given for argument 'tool'"));
        }
    }
    
    @Test
    public void testHelpHelp() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("help"));
        tool.run();
    }
    
    @Test
    public void testHelpCompiler() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("compile"));
        tool.setToolLoader(pluginLoader);
        tool.run();
        
        try {
            pluginFactory.bindArguments(model, Arrays.asList("--", "compile", "--javac="));
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Unexpected argument '--javac=' to command 'help'", e.getMessage());
        }
    }
    
    @Test
    public void testHelpDoc() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("doc"));
        tool.run();
    }
    
    @Test
    public void testHelpImportJar() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("import-jar"));
        tool.run();
    }
    
    @Test
    public void testHelpDocTool() {
        ToolModel<CeylonHelpTool> model = pluginLoader.loadToolModel("help");
        Assert.assertNotNull(model);
        CeylonHelpTool tool = pluginFactory.bindArguments(model, Arrays.asList("doc-tool"));
        tool.run();
    }

}
