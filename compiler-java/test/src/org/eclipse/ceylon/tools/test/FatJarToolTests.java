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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.OptionArgumentException;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.tools.fatjar.CeylonFatJarTool;
import org.junit.Assert;
import org.junit.Test;

public class FatJarToolTests extends AbstractToolTests {

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
                        "org.eclipse.ceylon.module-resolver/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        Assert.assertTrue(out.exists());
        try(ZipFile zf = new ZipFile(out)){
            Assert.assertNotNull(zf.getEntry("ceylon/language/true_.class"));
            // dependency
            Assert.assertNotNull(zf.getEntry("org/eclipse/ceylon/common/log/Logger.class"));
            // extra jar
            Assert.assertNotNull(zf.getEntry("org/eclipse/ceylon/cmr/api/Overrides.class"));
            Assert.assertNull(zf.getEntry("META-INF/INDEX.LIST"));
            Assert.assertNull(zf.getEntry("META-INF/mapping.txt"));
            ZipEntry manifestEntry = zf.getEntry("META-INF/MANIFEST.MF");
            Assert.assertNotNull(manifestEntry);
            Manifest manifest = new Manifest(zf.getInputStream(manifestEntry));
            Attributes attributes = manifest.getMainAttributes();
            Assert.assertEquals("ceylon.language.run_", attributes.getValue("Main-Class"));
        }
    }

    @Test
    public void testDefaultJar() throws Exception {
        compile("fatjar/source/def/hello.ceylon");
        
        ToolModel<CeylonFatJarTool> model = pluginLoader.loadToolModel("fat-jar");
        Assert.assertNotNull(model);
        File out = new File(getOutPath(), "fatjar.jar");
        CeylonFatJarTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", "../dist/dist/repo",
                        "--rep", getOutPath(),
                        "--out", out.getAbsolutePath(),
                        "--run", "org.eclipse.ceylon.tools.test.fatjar.source.def::hello",
                        "default"));
        tool.run();
        
        Assert.assertTrue(out.exists());
        try(ZipFile zf = new ZipFile(out)){
            Assert.assertNotNull(zf.getEntry("ceylon/language/true_.class"));
            // dependency
            Assert.assertNotNull(zf.getEntry("org/eclipse/ceylon/common/log/Logger.class"));
            Assert.assertNull(zf.getEntry("META-INF/INDEX.LIST"));
            Assert.assertNull(zf.getEntry("META-INF/mapping.txt"));
            ZipEntry manifestEntry = zf.getEntry("META-INF/MANIFEST.MF");
            Assert.assertNotNull(manifestEntry);
            Manifest manifest = new Manifest(zf.getInputStream(manifestEntry));
            Attributes attributes = manifest.getMainAttributes();
            Assert.assertEquals("org.eclipse.ceylon.tools.test.fatjar.source.def.hello_", attributes.getValue("Main-Class"));
        }
    }

    @Test
    public void testResources() throws Throwable {
        compile(Arrays.asList("-out", getOutPath(), 
                "-src", getPackagePath()+"fatjar/source",
                "-res", getPackagePath()+"fatjar/resource"), 
                "fatjar/source/com/foo/module.ceylon",
                "fatjar/source/com/foo/run.ceylon",
                "fatjar/resource/com/foo/bar.txt");
        
        ToolModel<CeylonFatJarTool> model = pluginLoader.loadToolModel("fat-jar");
        Assert.assertNotNull(model);
        File out = new File(getOutPath(), "fatjar.jar");
        CeylonFatJarTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList(
                        "--rep", "../dist/dist/repo",
                        "--rep", getOutPath(),
                        "--out", out.getAbsolutePath(),
                        "com.foo/1"));
        tool.run();
        
        Assert.assertTrue(out.exists());
        
        URLClassLoader classLoader = new CleanupClassLoader(new URL[]{out.toURL()}, null);
        try{
            try{
                Class<?> klass = classLoader.loadClass("com.foo.run_");
                Method run = klass.getMethod("main", String[].class);
                run.invoke(null, new Object[]{new String[0]});
            }catch(InvocationTargetException x){
                Throwable cause = x.getCause();
                // make sure toString() loads all the classes it needs before we close the CL and rethrow
                cause.toString();
                throw cause;
            }
        }finally{
            classLoader.close();
        }
    }

}
