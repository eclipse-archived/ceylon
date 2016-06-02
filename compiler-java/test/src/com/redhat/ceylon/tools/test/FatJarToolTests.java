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
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.tools.fatjar.CeylonFatJarTool;

public class FatJarToolTests extends AbstractToolTests {

    public FatJarToolTests(String[] compilerArgs) {
        super(compilerArgs);
    }

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonFatJarTool> model = pluginLoader.loadToolModel("fat-jar");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testJar() throws Exception {
        ToolModel<CeylonFatJarTool> model = pluginLoader.loadToolModel("fat-jar");
        Assert.assertNotNull(model);
        File out = new File(getOutPath(), "fatjar.jar");
        CeylonFatJarTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", "../dist/dist/repo",
                        "--out", out.getAbsolutePath(),
                        "ceylon.language/"+Versions.CEYLON_VERSION_NUMBER,
                        "com.redhat.ceylon.module-resolver/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        Assert.assertTrue(out.exists());
        try(ZipFile zf = new ZipFile(out)){
            Assert.assertNotNull(zf.getEntry("ceylon/language/true_.class"));
            // dependency
            Assert.assertNotNull(zf.getEntry("com/redhat/ceylon/common/log/Logger.class"));
            // extra jar
            Assert.assertNotNull(zf.getEntry("com/redhat/ceylon/cmr/api/Overrides.class"));
            Assert.assertNull(zf.getEntry("META-INF/INDEX.LIST"));
            Assert.assertNull(zf.getEntry("META-INF/mapping.txt"));
            ZipEntry manifestEntry = zf.getEntry("META-INF/MANIFEST.MF");
            Assert.assertNotNull(manifestEntry);
            Manifest manifest = new Manifest(zf.getInputStream(manifestEntry));
            Attributes attributes = manifest.getMainAttributes();
            Assert.assertEquals("ceylon.language.run_", attributes.getValue("Main-Class"));
        }
    }

}
