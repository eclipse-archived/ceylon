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

import com.redhat.ceylon.cmr.api.JDKUtils.JDK;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolError;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.classpath.CeylonClasspathTool;
import com.redhat.ceylon.tools.info.CeylonInfoTool;

public class ClassPathToolTests extends AbstractToolTests {

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
            Assert.assertEquals("Module conflict error prevented classpath generation: try running \"ceylon info --suggest-override io.cayla.web/0.3.0\" to display an override file you can use with \"ceylon classpath --overrides override.xml io.cayla.web/0.3.0\"", err.getMessage());
        }
    }

    @Test
    public void testRecursiveDependenciesOverride() throws Exception {
        ToolModel<CeylonClasspathTool> model = pluginLoader.loadToolModel("classpath");
        Assert.assertNotNull(model);
        CeylonClasspathTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--maven-overrides", getPackagePath()+"/overrides.xml", "io.cayla.web/0.3.0"));
        tool.run();
    }
}
