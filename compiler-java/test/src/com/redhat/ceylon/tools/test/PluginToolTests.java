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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.plugin.CeylonPluginTool;

public class PluginToolTests extends AbstractToolTests {

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonPluginTool> model = pluginLoader.loadToolModel("plugin");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testBug2114 () throws Exception {
        ToolModel<CeylonPluginTool> model = pluginLoader.loadToolModel("plugin");
        Assert.assertNotNull(model);
        CeylonPluginTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--out", getOutPath(),
                        "--source", getPackagePath()+"/plugin/foo1/source", 
                        "--script", getPackagePath()+"/plugin/foo1/script",
                        "pack", "foo"));
        tool.run();
        File f = getOutputScriptFileName("foo", "1");
        System.err.println(f);
        Assert.assertTrue(f.exists());

        CeylonPluginTool tool2 = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--out", getOutPath(),
                        "--source", getPackagePath()+"/plugin/foo2/source", 
                        "--script", getPackagePath()+"/plugin/foo2/script",
                        "pack", "foo"));
        tool2.run();
        File f2 = getOutputScriptFileName("foo", "2");
        Assert.assertTrue(f2.exists());
    }

    private File getOutputScriptFileName(String module, String version) {
        return new File(getOutPath(), module.replace('.', '/') + '/' + version + '/' + module + '-' + version + ".scripts.zip");
    }
}
