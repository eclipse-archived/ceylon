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
package com.redhat.ceylon.tools.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolError;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.tools.classpath.CeylonClasspathTool;

public class ClasspathToolTests extends AbstractToolTests {

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        try {
            CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testRecursiveDependencies() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("io.cayla.web/0.3.0"));
        try{
            tool.run();
        }catch(ToolError err){
            Assert.assertEquals("Module conflict error prevented classpath generation: try running \"ceylon info --suggest-override io.cayla.web/0.3.0\" to display an override file you can use with \"ceylon classpath --overrides override.xml io.cayla.web/0.3.0\" or try with \"ceylon classpath --force io.cayla.web/0.3.0\" to select the latest versions", err.getMessage());
        }
    }

    @Test
    public void testRecursiveDependenciesOverride() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--overrides", getPackagePath()+"/overrides.xml", "io.cayla.web/0.3.0"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String cp = b.toString();
        Assert.assertTrue(cp.contains("org.jboss.logging-3.1.3.GA.jar"));
        Assert.assertFalse(cp.contains("org.jboss.logging-3.1.2.GA.jar"));
    }

    @Test
    public void testRecursiveDependenciesForce() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--force", "io.cayla.web/0.3.0"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String cp = b.toString();
        Assert.assertTrue(cp.contains("org.jboss.logging-3.1.3.GA.jar"));
        Assert.assertFalse(cp.contains("org.jboss.logging-3.1.2.GA.jar"));
    }

    @Test
    public void testMissingModule() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("naskduhqwedmansd"));
        try{
            tool.run();
            Assert.fail();
        }catch(ToolUsageError x){
            Assert.assertTrue(x.getMessage().contains("Module naskduhqwedmansd not found"));
        }
    }

    @Test
    public void testModuleNameAlone() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("ceylon.language"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        String cp = b.toString();
        Assert.assertTrue(cp.contains("ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".car"));
    }

    @Test
    public void testModuleNameWithBadVersion() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("ceylon.language/666"));
        try{
            tool.run();
        }catch(ToolUsageError x){
            Assert.assertTrue(x.getMessage().contains("Version 666 not found for module ceylon.language"));
        }
    }
}
