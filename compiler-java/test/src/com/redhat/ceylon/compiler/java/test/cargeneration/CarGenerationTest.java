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
package com.redhat.ceylon.compiler.java.test.cargeneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;

public class CarGenerationTest extends CompilerTest {
    
    @Test
    public void testCarResourceSimple() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/simple/source");
        options.add("-res");
        options.add(getPackagePath() + "resmodules/simple/resource");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("test.simple"));
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("test.simple", "1.0");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("test/simple/README.txt");
        assertNotNull(moduleClass);

        moduleClass = car.getEntry("test/simple/subdir/SUBDIR.txt");
        assertNotNull(moduleClass);

        moduleClass = car.getEntry("test/simple/module_.class");
        assertNotNull(moduleClass);
        car.close();
    }
    
    @Test
    public void testCarResourceFiles() throws IOException{
        testCarResourceFilesSub(false);
        testCarResourceFilesSub(true);
    }
    
    private void testCarResourceFilesSub(boolean alternative) throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/files/source");
        options.add("-res");
        options.add(getPackagePath() + "resmodules/files/resource");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task;
        if (alternative) {
            task = getCompilerTask(options, 
//                "resmodules/files/source/test/files/module.ceylon",
                "resmodules/files/resource/test/files/extrafile");
        } else {
            task = getCompilerTask(options,
                "resmodules/files/source/test/files/module.ceylon",
                "resmodules/files/resource/test/files/README.txt");
        }
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("test.files", "1.0");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("test/files/README.txt");
        assertNotNull(moduleClass);

        moduleClass = car.getEntry("test/files/extrafile");
        if (alternative) {
            assertNotNull(moduleClass);
        } else {
            assertNull(moduleClass);
        }

        moduleClass = car.getEntry("test/files/module_.class");
        assertNotNull(moduleClass);
        car.close();
    }
    
    @Test
    public void testCarResourceMultiple() throws IOException{
        assertEquals(testCarResourceMultipleSub(false), 40);
        assertEquals(testCarResourceMultipleSub(true), 108);
    }
    
    private long testCarResourceMultipleSub(boolean reverse) throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/multiple/source");
        if (reverse) {
            options.add("-res");
            options.add(getPackagePath() + "resmodules/multiple/resource2");
            options.add("-res");
            options.add(getPackagePath() + "resmodules/multiple/resource");
        } else {
            options.add("-res");
            options.add(getPackagePath() + "resmodules/multiple/resource");
            options.add("-res");
            options.add(getPackagePath() + "resmodules/multiple/resource2");
        }
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("test.multiple"));
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("test.multiple", "1.0");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("test/multiple/README.txt");
        long result = moduleClass.getSize();
        assertNotNull(moduleClass);

        moduleClass = car.getEntry("test/multiple/README2.txt");
        assertNotNull(moduleClass);

        moduleClass = car.getEntry("test/multiple/module_.class");
        assertNotNull(moduleClass);
        car.close();
        
        return result;
    }

    @Test
    public void testCarResourceDefault() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/default/source");
        options.add("-res");
        options.add(getPackagePath() + "resmodules/default/resource");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                "resmodules/default/resource/README.txt",
                "resmodules/default/resource/subdir/SUBDIR.txt");
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("README.txt");
        assertNotNull(moduleClass);
        
        moduleClass = car.getEntry("subdir/SUBDIR.txt");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testCarResourceRoot() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/rootdir/source");
        options.add("-res");
        options.add(getPackagePath() + "resmodules/rootdir/resource");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("test.rootdir"));
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("test.rootdir", "1.0");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry carEntry = car.getEntry("test/rootdir/README.txt");
        assertNotNull(carEntry);

        carEntry = car.getEntry("rootfile");
        assertNotNull(carEntry);

        carEntry = car.getEntry("rootdir/rootsubdirfile");
        assertNotNull(carEntry);

        carEntry = car.getEntry("test/rootdir/module_.class");
        assertNotNull(carEntry);
        car.close();
    }

    @Test
    public void testCarResourceAlternativeRoot() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath() + "resmodules/altrootdir/source");
        options.add("-res");
        options.add(getPackagePath() + "resmodules/altrootdir/resource");
        options.add("-resroot");
        options.add("ALTROOT");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("test.altrootdir"));
        Boolean ret = task.call();
        assertTrue(ret);
        
        File carFile = getModuleArchive("test.altrootdir", "1.0");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry carEntry = car.getEntry("test/altrootdir/README.txt");
        assertNotNull(carEntry);

        carEntry = car.getEntry("rootfile");
        assertNotNull(carEntry);

        carEntry = car.getEntry("test/altrootdir/module_.class");
        assertNotNull(carEntry);
        car.close();
    }
    
}
