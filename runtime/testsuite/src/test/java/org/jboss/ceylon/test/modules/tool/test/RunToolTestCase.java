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
package org.jboss.ceylon.test.modules.tool.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ceylon.modules.bootstrap.CeylonRunTool;

import com.redhat.ceylon.common.tool.AbstractToolTest;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tools.CeylonToolLoader;

public class RunToolTestCase extends AbstractToolTest {

    protected final static String OUT_EXPECTED_DEFAULT = "Hello, world";
    protected final static String OUT_EXPECTED_WITH_ARG = "Hello, Tako";
    
    protected final static String dir = "testsuite/src";
    protected final static String destDirGeneral = "build/test-cars";
    protected final static String cacheDirGeneral = "build/test-cache";
    protected final String destDir;
    protected final String cacheDir;
    
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    public RunToolTestCase() {
        Package pakage = getClass().getPackage();
        String moduleName = pakage.getName();
        int lastDot = moduleName.lastIndexOf('.');
        if(lastDot == -1){
            destDir = destDirGeneral + File.separator + moduleName;
        } else {
            destDir = destDirGeneral + File.separator + moduleName.substring(lastDot+1);
        }
        if(lastDot == -1){
            cacheDir = cacheDirGeneral + File.separator + moduleName;
        } else {
            cacheDir = cacheDirGeneral + File.separator + moduleName.substring(lastDot+1);
        }
    }
    
    private List<String> options(String... strings){
        List<String> ret = new ArrayList<String>(strings.length+2);
        ret.add("--sysrep");
        ret.add("../ceylon/dist/dist/repo");
        ret.add("--cacherep");
        ret.add(cacheDir);
        ret.add("--rep");
        ret.add("testsuite/src/test/resources/repo");
        for(String s : strings)
            ret.add(s);
        return ret;
    }
    
    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        try {
            CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testModuleVersion() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("hello/1.0.0"));
        assertOutput(tool, OUT_EXPECTED_DEFAULT);
    }
    
    @Test
    public void testModuleVersionArgs() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("hello/1.0.0", "Tako"));
        assertOutput(tool, OUT_EXPECTED_WITH_ARG);
    }
    
    @Test
    public void testModuleNoVersion() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("hello"));
        assertOutput(tool, OUT_EXPECTED_DEFAULT);
    }
    
    @Test
    public void testModuleNoVersionArgs() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("hello", "Tako"));
        assertOutput(tool, OUT_EXPECTED_WITH_ARG);
    }
    
    @Test
    public void testModuleVersionFunction() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--run=hello.hello", "hello/1.0.0"));
        assertOutput(tool, OUT_EXPECTED_DEFAULT);
    }
    
    @Test
    public void testDefault() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("default"));
        assertOutput(tool, OUT_EXPECTED_DEFAULT);
    }
    
    @Test
    public void testDefaultArg() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("default", "Tako"));
        assertOutput(tool, OUT_EXPECTED_WITH_ARG);
    }
    
    @Test
    public void testDefaultFunction() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--run=hello", "default"));
        assertOutput(tool, OUT_EXPECTED_DEFAULT);
    }
    
    @Test
    public void testQuotedModuleName() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("foo.long.module/1.0.0"));
        assertOutput(tool, "Hello, World!");
    }

    @Test
    public void testQuotedModuleNameKeywordFunction() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--run", "foo.long.module::do", "foo.long.module/1.0.0"));
        assertOutput(tool, "Hello, World!");
    }

    @Test
    public void testQuotedModuleNameNoVersion() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("foo.long.module"));
        assertOutput(tool, "Hello, World!");
    }

    @Test
    public void testQuotedModuleNameVersion() throws Exception {
        ToolModel<CeylonRunTool> model = pluginLoader.loadToolModel("run");
        Assert.assertNotNull(model);
        CeylonRunTool tool = pluginFactory.bindArguments(model, getMainTool(), options("--run=foo.long.module::run", "foo.long.module/1.0.0"));
        assertOutput(tool, "Hello, World!");
    }
    private void assertOutput(CeylonRunTool tool, String txt) throws IOException {
        PrintStream oldout = System.out;
        PrintStream out = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            out = new PrintStream(baos);
            System.setOut(out);
            tool.run();
            String output = baos.toString();
            Assert.assertTrue(output.contains(txt));
        } finally {
            System.setOut(oldout);
            if (out != null) {
                out.close();
            }
        }
    }
}
