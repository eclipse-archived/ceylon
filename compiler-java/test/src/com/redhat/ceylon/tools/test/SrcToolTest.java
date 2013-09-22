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

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.CeylonToolLoader;
import com.redhat.ceylon.tools.src.CeylonSrcTool;

public class SrcToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonSrcTool> model = pluginLoader.loadToolModel("src");
        Assert.assertNotNull(model);
        try {
            CeylonSrcTool tool = pluginFactory.bindArguments(model, Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testModuleVersion() throws Exception {
        ToolModel<CeylonSrcTool> model = pluginLoader.loadToolModel("src");
        Assert.assertNotNull(model);
        CeylonSrcTool tool = pluginFactory.bindArguments(model, Collections.<String>singletonList("ceylon.language/"+Versions.CEYLON_VERSION_NUMBER));
    }
    
    @Test
    public void testModuleOnly() throws Exception {
        ToolModel<CeylonSrcTool> model = pluginLoader.loadToolModel("src");
        Assert.assertNotNull(model);
        CeylonSrcTool tool = pluginFactory.bindArguments(model, Collections.<String>singletonList("ceylon.language"));
    }
    
    @Test
    public void testOffline() throws Exception {
        ToolModel<CeylonSrcTool> model = pluginLoader.loadToolModel("src");
        Assert.assertNotNull(model);
        CeylonSrcTool tool = pluginFactory.bindArguments(model, Arrays.<String>asList("--offline", "ceylon.language"));
        tool.run();
    }
}
