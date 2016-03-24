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

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.OptionArgumentException;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.model.cmr.JDKUtils.JDK;
import com.redhat.ceylon.tools.info.CeylonInfoTool;

public class InfoToolTests extends AbstractToolTests {

    @Test
    public void testNoArgs() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        try {
            CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>emptyList());
            Assert.fail();
        } catch (OptionArgumentException e) {
            // asserting this is thrown
        }
    }
    
    @Test
    public void testModuleVersion() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("ceylon.language/"+Versions.CEYLON_VERSION_NUMBER));
    }
    
    @Test
    public void testModuleOnly() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("ceylon.language"));
        tool.run();
    }

    @Test
    public void testModuleFromSource() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.<String>asList("--src", "test/src", "com.redhat.ceylon.tools.test.info/1"));

        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();

        Assert.assertTrue(b.toString().contains(
                "Name:        com.redhat.ceylon.tools.test.info\n"+
                "Version:     1\n"+
                "Available:   On local system\n"+
                "Origin:      Local source folder\n"+
                "Dependency Tree (up to depth 1):\n"+
                "  shared java.base/7\n"+
                "  optional java.desktop/7\n"
        ));
    }

    @Test
    public void testJdkModule() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("java.base/"+JDK.JDK7.version));

        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        
        Assert.assertTrue(b.toString().contains(
        "Name:        java.base\n"+
        "Version:     7\n"+
        "Artifacts:   JVM (legacy)\n"+
        "Available:   On local system\n"+
        "Origin:      Java Runtime\n"+
        "Description: JDK module java.base\n"
        ));
    }

    @Test
    public void testAetherModule() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.asList("--dependency-depth=-1",
                "com.sparkjava:spark-core/2.1"
                ));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        
        Assert.assertTrue(b.toString().contains(
        "Dependencies (up to depth ∞):\n"+
        "  org.eclipse.jetty.orbit:javax.servlet/3.0.0.v201112011016\n"+
        "  org.eclipse.jetty:jetty-http/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-io/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-security/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-server/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-servlet/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-util/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-webapp/9.0.2.v20130417\n"+
        "  org.eclipse.jetty:jetty-xml/9.0.2.v20130417\n"+
        "  org.slf4j:slf4j-api/1.7.7\n"+
        "  org.slf4j:slf4j-simple/1.7.7\n"
        ));

    }

    @Test
    public void testAetherModuleVersions() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("asm:asm-commons"));
        
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        
        Assert.assertTrue(b.toString().contains(
                "asm:asm-commons/2.0\n"+
                "asm:asm-commons/2.1\n"+
                "asm:asm-commons/2.2\n"+
                "asm:asm-commons/2.2.1\n"+
                "asm:asm-commons/3.0\n"+
                "asm:asm-commons/3.1\n"+
                "asm:asm-commons/3.2\n"+
                "asm:asm-commons/3.3\n"+
                "asm:asm-commons/3.3.1"
                ));

    }

    @Test
    public void testAetherModuleVersionSearch() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Collections.<String>singletonList("asm:asm-commons/2"));

        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        
        Assert.assertTrue(b.toString().contains(
                "asm:asm-commons/2.0\n"+
                "asm:asm-commons/2.1\n"+
                "asm:asm-commons/2.2\n"+
                "asm:asm-commons/2.2.1"
                ));
    }

    @Test
    public void testRecursiveDependencies() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--dependency-depth=-1", "--show-incompatible=yes", "io.cayla.web/0.3.0"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertTrue(b.toString().contains("Dependencies version conflicts (up to depth ∞):\n"
                +"  org.jboss.logging: 3.1.2.GA, 3.1.3.GA\n"));
    }

    @Test
    public void testRecursiveDependenciesOverride() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--dependency-depth=-1", "--overrides", getPackagePath()+"/overrides.xml", "--show-incompatible=yes", "io.cayla.web/0.3.0"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertFalse(b.toString().contains("Dependencies version conflicts"));
    }

    @Test
    public void testSuggestOverride() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                Arrays.asList("--dependency-depth=-1", "--show-incompatible=yes", "--print-overrides", "io.cayla.web/0.3.0"));
        StringBuilder b = new StringBuilder();
        tool.setOut(b);
        tool.run();
        Assert.assertTrue(b.toString().contains("<overrides>\n"
                +" <set module=\"org.jboss.logging\" version=\"3.1.3.GA\"/>\n"
                +"</overrides>\n"));
    }

    @Test
    public void testOffline() throws Exception {
        ToolModel<CeylonInfoTool> model = pluginLoader.loadToolModel("info");
        Assert.assertNotNull(model);
        CeylonInfoTool tool = pluginFactory.bindArguments(model, getMainTool(), Arrays.<String>asList("--offline", "ceylon.language"));
    }
}
