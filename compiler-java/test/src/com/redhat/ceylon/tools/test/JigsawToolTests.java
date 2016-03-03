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

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.jigsaw.CeylonJigsawTool;

public class JigsawToolTests extends AbstractToolTests {

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonJigsawTool> model = pluginLoader.loadToolModel("jigsaw");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testCopy() throws Exception {
        ToolModel<CeylonJigsawTool> model = pluginLoader.loadToolModel("jigsaw");
        Assert.assertNotNull(model);
        CeylonJigsawTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                		"create-mlib",
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "ceylon.language/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        File out = new File(getOutPath());
        Assert.assertTrue(out.exists());
        Assert.assertTrue(new File(out, "ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
        Assert.assertTrue(new File(out, "com.redhat.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
    }

    @Test
    public void testCopyExcludes() throws Exception {
        File out = new File(getOutPath());
        Assert.assertFalse(new File(out, "com.redhat.ceylon.maven-support-"+Versions.DEPENDENCY_MAVEN_SUPPORT_VERSION+".jar").exists());

        ToolModel<CeylonJigsawTool> model = pluginLoader.loadToolModel("jigsaw");
        Assert.assertNotNull(model);
        CeylonJigsawTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                		"create-mlib",
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "--exclude-module", "com.redhat.ceylon.maven-support",
                        "ceylon.language/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        Assert.assertTrue(out.exists());
        Assert.assertTrue(new File(out, "ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
        Assert.assertTrue(new File(out, "com.redhat.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
        Assert.assertFalse(new File(out, "com.redhat.ceylon.maven-support-"+Versions.DEPENDENCY_MAVEN_SUPPORT_VERSION+".jar").exists());
    }

    /*
    @Test
    public void testJdkProvider() throws Exception {
        ToolModel<CeylonJigsawTool> model = pluginLoader.loadToolModel("jigsaw");
        Assert.assertNotNull(model);
        CeylonJigsawTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                		"create-mlib",
                        "--rep", getOutPath(),
                        "--rep", "../../../AndroidStudioProjects/TestJava/modules",
                        "--out", getOutPath(),
                        "--jdk-provider", "android/23.1.1",
                        "ceylon.language/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        File out = new File(getOutPath());
    }
    */
}
