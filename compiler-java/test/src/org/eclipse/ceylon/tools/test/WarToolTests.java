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
package org.eclipse.ceylon.tools.test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.OptionArgumentException;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.tools.war.CeylonWarTool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WarToolTests extends AbstractToolTests {

    @Before
    public void setup(){
        FileUtil.mkdirs(new File(getOutPath()));
    }
    
    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonWarTool> model = pluginLoader.loadToolModel("war");
        Assert.assertNotNull(model);
        try {
            pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testDuplicates() throws Exception {
        ToolModel<CeylonWarTool> model = pluginLoader.loadToolModel("war");
        Assert.assertNotNull(model);
        CeylonWarTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", "../dist/dist/repo",
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "ceylon.collection/1.2.0",
                        "ceylon.collection/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        File out = tool.getJarFile();
        Assert.assertTrue(out.exists());
        // check the contents of the jar
        try(ZipFile zf = new ZipFile(out)){
            // single version
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.language-1.2.0.jar"));
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.collection-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.collection-1.2.0.jar"));
            // dependency
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/org.eclipse.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            // NO extra jar
            Assert.assertNull(zf.getEntry("WEB-INF/lib/org.eclipse.ceylon.war-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            // NO extra stuff
            Assert.assertNull(zf.getEntry("META-INF/libs.txt"));
            Assert.assertNull(zf.getEntry("META-INF/module.properties"));
            Assert.assertNull(zf.getEntry("WEB-INF/web.xml"));
        }
    }

    @Test
    public void testStaticMetamodel() throws Exception {
        ToolModel<CeylonWarTool> model = pluginLoader.loadToolModel("war");
        Assert.assertNotNull(model);
        CeylonWarTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", "../dist/dist/repo",
                        "--rep", getOutPath(),
                        "--out", getOutPath(),
                        "--static-metamodel",
                        "ceylon.collection/1.3.4-SNAPSHOT", // gets auto-upgraded to 1.3.1
                        "ceylon.html/1.3.4-SNAPSHOT", // stays but depends on collection/1.3.1
                        "ceylon.buffer/1.3.4-SNAPSHOT")); // stays
        tool.run();
        
        File out = tool.getJarFile();
        Assert.assertTrue(out.exists());
        // check the contents of the jar
        try(ZipFile zf = new ZipFile(out)){
            // single version
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.collection-1.3.4-SNAPSHOT.jar"));
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.buffer-1.3.4-SNAPSHOT.jar"));
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/ceylon.html-1.3.4-SNAPSHOT.jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.language-1.2.0.jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.language-1.2.2.jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.collection-1.2.0.jar"));
            Assert.assertNull(zf.getEntry("WEB-INF/lib/ceylon.collection-1.2.2.jar"));
            // dependency
            Assert.assertNotNull(zf.getEntry("WEB-INF/lib/org.eclipse.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            // no extra stuff
            Assert.assertNull(zf.getEntry("WEB-INF/lib/org.eclipse.ceylon.war-"+Versions.CEYLON_VERSION_NUMBER+".jar"));
            Assert.assertNull(zf.getEntry("META-INF/libs.txt"));
            Assert.assertNull(zf.getEntry("META-INF/module.properties"));
            // no web.xml
            Assert.assertNull(zf.getEntry("WEB-INF/web.xml"));
            // metamodel
            Assert.assertNotNull(zf.getEntry("META-INF/ceylon/metamodel"));
            
            String metamodel = read(zf, zf.getEntry("META-INF/ceylon/metamodel"));
            Assert.assertFalse(metamodel.contains("ceylon.language/1.3.1"));
            Assert.assertFalse(metamodel.contains("ceylon.collection/1.2.0"));
            Assert.assertFalse(metamodel.contains("ceylon.collection/1.2.2"));
        }
    }
}
