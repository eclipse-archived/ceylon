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
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.CeylonDocTool;
import com.redhat.ceylon.common.tool.ToolModel;

public class DocToolTests extends AbstractToolTests {
    
    protected List<String> toolOptions(String... opts) {
        List<String> ret = super.toolOptions("--out", destDir);
        ret.addAll(Arrays.asList(opts));
        return ret;
    }
    
    @Test
    public void testDoc()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocMultiple()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--src=test/src", "com.redhat.ceylon.tools.test.multiple.*"));
        tool.run();
    }
    
    @Test
    public void testDocNonShared()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--non-shared", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
    @Test
    public void testDocSourceCode()  throws Exception {
        ToolModel<CeylonDocTool> model = pluginLoader.loadToolModel("doc");
        Assert.assertNotNull(model);
        CeylonDocTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--source-code", "--src=test/src", "com.redhat.ceylon.tools.test.ceylon"));
        tool.run();
    }
    
}
