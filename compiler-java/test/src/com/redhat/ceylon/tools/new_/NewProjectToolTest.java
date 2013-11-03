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
package com.redhat.ceylon.tools.new_;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;

public class NewProjectToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    private List<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    private void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }
    
    Map<String, String> setProperties(Map<String, String> props) {
        HashMap<String, String> oldMap = new HashMap<>(props.size());
        for (Map.Entry<String, String> entry : props.entrySet()) {
            String oldValue = System.getProperty(entry.getKey());
            System.setProperty(entry.getKey(), entry.getValue());
            oldMap.put(entry.getKey(), oldValue);
        }
        return oldMap;
    }
    
    void restoreProperties(Map<String, String> oldMap) {
        Properties properties = System.getProperties();
        for (Map.Entry<String, String> entry : oldMap.entrySet()) {
            if (entry.getValue() == null) {
                properties.remove(entry.getKey());
            } else {
                properties.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    private void assertFile(File file) {
        Assert.assertTrue(file + " should be a file", file.isFile());
    }
    
    private void assertDir(File dir) {
        Assert.assertTrue(dir + " should be a directory", dir.isDirectory());
    }
    
    private void assertNotExists(File file) {
        Assert.assertTrue(file + " should not exist", !file.exists());
    }

    private void runTool(CeylonNewTool tool) throws Exception {
        // In an attempt for a little test isolation, we restore the system properties 
        // we set
        Map<String, String> oldMap = setProperties(Collections.singletonMap("ceylon.home", "foo"));
        try {
            tool.run();
        } finally {
            restoreProperties(oldMap);
        }
    }
    
    @Test
    public void testHelloWorld() throws Exception {
        ToolModel<CeylonNewTool> model = pluginLoader.loadToolModel("new");
        Assert.assertTrue(model.isPorcelain());
        Assert.assertNotNull(model);
        Path tmpPath = Files.createTempDirectory("ceylon-new");
        File tmpDir = tmpPath.toFile();
        try {
            CeylonNewTool tool = pluginFactory.bindArguments(model, 
                    args("--from=../ceylon-dist/templates", 
                            "hello-world",
                            "--module-name=org.example.hello",
                            "--module-version=1",
                            "--ant=true",
                            "--eclipse=true",
                            "--eclipse-project-name=hello",
                            tmpDir.getAbsolutePath()));
            runTool(tool);
            assertFile(new File(tmpDir, "source/org/example/hello/module.ceylon"));
            assertFile(new File(tmpDir, "source/org/example/hello/package.ceylon"));
            assertFile(new File(tmpDir, "source/org/example/hello/run.ceylon"));
            assertFile(new File(tmpDir, "build.xml"));
            assertFile(new File(tmpDir, ".classpath"));
            assertFile(new File(tmpDir, ".project"));
            assertDir(new File(tmpDir, ".settings"));
        } finally {
            delete(tmpDir);
        }
    }
    
    @Test
    public void testHelloWorldNoAntNoEclipse() throws Exception {
        ToolModel<CeylonNewTool> model = pluginLoader.loadToolModel("new");
        Assert.assertTrue(model.isPorcelain());
        Assert.assertNotNull(model);
        Path tmpPath = Files.createTempDirectory("ceylon-new");
        File tmpDir = tmpPath.toFile();
        try {
            CeylonNewTool tool = pluginFactory.bindArguments(model, 
                    args("--from=../ceylon-dist/templates", 
                            "hello-world",
                            "--module-name=org.example.hello",
                            "--module-version=1",
                            "--ant=false",
                            "--eclipse=false",
                            tmpDir.getAbsolutePath()));
            runTool(tool);
            assertFile(new File(tmpDir, "source/org/example/hello/module.ceylon"));
            assertFile(new File(tmpDir, "source/org/example/hello/package.ceylon"));
            assertFile(new File(tmpDir, "source/org/example/hello/run.ceylon"));
            assertNotExists(new File(tmpDir, "build.xml"));
            assertNotExists(new File(tmpDir, ".classpath"));
            assertNotExists(new File(tmpDir, ".project"));
            assertNotExists(new File(tmpDir, ".settings"));
        } finally {
            delete(tmpDir);
        }
    }

    
}
