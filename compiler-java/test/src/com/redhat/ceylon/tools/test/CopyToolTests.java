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
import com.redhat.ceylon.tools.copy.CeylonCopyTool;
import com.redhat.ceylon.tools.plugin.CeylonPluginTool;

public class CopyToolTests extends AbstractToolTests {

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonCopyTool> model = pluginLoader.loadToolModel("copy");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testBug2115() throws Exception {
        ToolModel<CeylonCopyTool> model = pluginLoader.loadToolModel("copy");
        Assert.assertNotNull(model);
        CeylonCopyTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "ceylon.openshift/1.1.1"));
        tool.run();

        CeylonCopyTool tool2 = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "ceylon.openshift/1.1.1"));
        tool2.run();

        File f = getModuleArchive("ceylon.openshift", "1.1.1");
        Assert.assertTrue(f.exists());
        Assert.assertTrue(f.length() != 0);
    }

    @Test
    public void testBug2115InCache() throws Exception {
        ToolModel<CeylonCopyTool> model = pluginLoader.loadToolModel("copy");
        Assert.assertNotNull(model);
        CeylonCopyTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "--cacherep", getOutPath(),
                        "ceylon.openshift/1.1.1"));
        tool.run();

        CeylonCopyTool tool2 = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "--cacherep", getOutPath(),
                        "ceylon.openshift/1.1.1"));
        tool2.run();

        File f = getModuleArchive("ceylon.openshift", "1.1.1");
        // if the module was found locally it will be non-zero
        // if it is remote, we can't cache it and so we will not find it as remote repos will be skipped
        if(f.exists()){
            Assert.assertTrue(f.length() != 0);
        }
    }
}
