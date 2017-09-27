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
import java.util.List;

import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.OptionArgumentException;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.tools.maven.export.CeylonMavenExportTool;
import org.junit.Assert;
import org.junit.Test;

public class MavenExportToolTests extends AbstractToolTests {

    private final File destDir;
    private final File cacheDir;
    
    public MavenExportToolTests() {
        String destDirGeneral = "build/test-cars";
        String cacheDirGeneral = "build/test-cache";
        
        Package pakage = getClass().getPackage();
        String moduleName = pakage.getName();
        String destDir = destDirGeneral + File.separator;
        String cacheDir = cacheDirGeneral + File.separator;
        
        int lastDot = moduleName.lastIndexOf('.');
        if (lastDot == -1) {
            destDir += moduleName;
            cacheDir += moduleName;
        } else {
            destDir += moduleName.substring(lastDot+1);
            cacheDir += moduleName.substring(lastDot+1);
        }
        this.destDir = new File(destDir);
        this.cacheDir = new File(cacheDir);
    }

    protected List<String> toolOptions(String... opts) {
        List<String> ret = super.toolOptions("--out", destDir.getPath());
        ret.addAll(Arrays.asList(opts));
        return ret;
    }
    
    
    private File destFile(String f) {
        return new File(destDir, f);
    }
    
    private File cacheFile(String f) {
        return new File(cacheDir, f);
    }
    
    @Test
    public void testNoArgs() {
        ToolModel<CeylonMavenExportTool> model = pluginLoader.loadToolModel("maven-export");
        Assert.assertNotNull(model);
        try {
            CeylonMavenExportTool tool = pluginFactory.bindArguments(model, getMainTool(), toolOptions());
            Assert.fail();
        } catch (OptionArgumentException e) {
            Assert.assertEquals("Argument 'module' to command 'maven-export' should appear at least 1 time(s)", e.getMessage());
        }
    }
    
    @Test
    public void testExportDistrib() throws Exception {
        ToolModel<CeylonMavenExportTool> model = pluginLoader.loadToolModel("maven-export");
        Assert.assertNotNull(model);
        CeylonMavenExportTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("ceylon.language/"+Versions.CEYLON_VERSION_NUMBER, 
                        "org.eclipse.ceylon.compiler.java/"+Versions.CEYLON_VERSION_NUMBER,
                        "org.eclipse.ceylon.java.main/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        Assert.assertTrue(new File(destDir, "org/ceylon-lang/ceylon.language/"+
                Versions.CEYLON_VERSION_NUMBER+"/ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
        File languagePomFile = new File(destDir, "org/ceylon-lang/ceylon.language/"+
                Versions.CEYLON_VERSION_NUMBER+"/ceylon.language-"+Versions.CEYLON_VERSION_NUMBER+".pom");
        Assert.assertTrue(languagePomFile.exists());

        // declare
        assertFileContainsLine(languagePomFile, "  <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(languagePomFile, "  <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(languagePomFile, "  <artifactId>ceylon.language</artifactId>");

        // depends
        assertFileContainsLine(languagePomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(languagePomFile, "      <artifactId>org.eclipse.ceylon.model</artifactId>");

        Assert.assertTrue(new File(destDir, "org/ceylon-lang/org.eclipse.ceylon.model/"+
                Versions.CEYLON_VERSION_NUMBER+"/org.eclipse.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".jar").exists());
        File modelPomFile = new File(destDir, "org/ceylon-lang/org.eclipse.ceylon.model/"+
                Versions.CEYLON_VERSION_NUMBER+"/org.eclipse.ceylon.model-"+Versions.CEYLON_VERSION_NUMBER+".pom");
        Assert.assertTrue(modelPomFile.exists());

        // declare
        assertFileContainsLine(modelPomFile, "  <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(modelPomFile, "  <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(modelPomFile, "  <artifactId>org.eclipse.ceylon.model</artifactId>");

        // depends
        assertFileContainsLine(modelPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(modelPomFile, "      <artifactId>org.eclipse.ceylon.common</artifactId>");
    }

    @Test
    public void testExportDistribForImport() throws Exception {
        ToolModel<CeylonMavenExportTool> model = pluginLoader.loadToolModel("maven-export");
        Assert.assertNotNull(model);
        CeylonMavenExportTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--for-import",
                        "ceylon.language/"+Versions.CEYLON_VERSION_NUMBER, 
                        "org.eclipse.ceylon.compiler.java/"+Versions.CEYLON_VERSION_NUMBER,
                        "org.eclipse.ceylon.java.main/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        File languagePomFile = new File(destDir, "ceylon.language/pom.xml");
        Assert.assertTrue(languagePomFile.exists());

        // declare
        assertFileContainsLine(languagePomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(languagePomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(languagePomFile, "  <artifactId>ceylon.language</artifactId>");

        // depends
        assertFileContainsLine(languagePomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(languagePomFile, "      <artifactId>org.eclipse.ceylon.model</artifactId>");

        // model
        File modelPomFile = new File(destDir, "org.eclipse.ceylon.model/pom.xml");
        Assert.assertTrue(modelPomFile.exists());

        // declare
        assertFileContainsLine(modelPomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(modelPomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(modelPomFile, "  <artifactId>org.eclipse.ceylon.model</artifactId>");

        // depends
        assertFileContainsLine(modelPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(modelPomFile, "      <artifactId>org.eclipse.ceylon.common</artifactId>");

        // all
        File allPomFile = new File(destDir, "ceylon-all/pom.xml");
        Assert.assertTrue(allPomFile.exists());

        // declare
        assertFileContainsLine(allPomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(allPomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(allPomFile, "  <artifactId>ceylon-all</artifactId>");

        // depends
        assertFileContainsLine(allPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(allPomFile, "      <artifactId>org.eclipse.ceylon.model</artifactId>");
        assertFileContainsLine(allPomFile, "      <artifactId>ceylon.language</artifactId>");

        // system
        File systemPomFile = new File(destDir, "ceylon-system/pom.xml");
        Assert.assertTrue(systemPomFile.exists());

        // declare
        assertFileContainsLine(systemPomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(systemPomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(systemPomFile, "  <artifactId>ceylon-system</artifactId>");
        assertFileContainsLine(systemPomFile, "  <packaging>pom</packaging>");

        // depends
        assertFileContainsLine(systemPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(systemPomFile, "      <artifactId>org.eclipse.ceylon.model</artifactId>");
        assertFileContainsLine(systemPomFile, "      <artifactId>ceylon.language</artifactId>");
        
        // complete
        File completePomFile = new File(destDir, "ceylon-complete/pom.xml");
        Assert.assertTrue(completePomFile.exists());

        // declare
        assertFileContainsLine(completePomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(completePomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(completePomFile, "  <artifactId>ceylon-complete</artifactId>");

        // depends
        assertFileContainsLine(completePomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(completePomFile, "      <artifactId>ceylon-all</artifactId>");
        // shade
        assertFileContainsLine(completePomFile, "                  <include>org.ceylon-lang:*</include>");
        assertFileContainsLine(completePomFile, "                  <include>org.antlr:antlr-runtime</include>");
    }

    @Test
    public void testExportDistribForSdkImport() throws Exception {
        ToolModel<CeylonMavenExportTool> model = pluginLoader.loadToolModel("maven-export");
        Assert.assertNotNull(model);
        CeylonMavenExportTool tool = pluginFactory.bindArguments(model, getMainTool(), 
                toolOptions("--for-sdk-import",
                        "--rep", "../../ceylon-sdk/modules",
                        "ceylon.test/"+Versions.CEYLON_VERSION_NUMBER,
                        "ceylon.collection/"+Versions.CEYLON_VERSION_NUMBER,
                        "ceylon.file/"+Versions.CEYLON_VERSION_NUMBER,
                        "ceylon.http.server/"+Versions.CEYLON_VERSION_NUMBER));
        tool.run();
        
        File testPomFile = new File(destDir, "ceylon.test/pom.xml");
        Assert.assertTrue(testPomFile.exists());

        // declare
        assertFileContainsLine(testPomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(testPomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(testPomFile, "  <artifactId>ceylon.test</artifactId>");

        // depends
        assertFileContainsLine(testPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(testPomFile, "      <artifactId>jboss-modules</artifactId>");
        assertFileContainsLine(testPomFile, "      <artifactId>ceylon.language</artifactId>");

        // http server
        File httpServerPomFile = new File(destDir, "ceylon.http.server/pom.xml");
        Assert.assertTrue(httpServerPomFile.exists());

        // depends
        assertFileContainsLine(httpServerPomFile, "      <groupId>io.undertow</groupId>");
        assertFileContainsLine(httpServerPomFile, "      <artifactId>undertow-core</artifactId>");
        assertFileContainsLine(httpServerPomFile, "      <groupId>org.jboss.xnio</groupId>");
        assertFileContainsLine(httpServerPomFile, "      <artifactId>xnio-nio</artifactId>");

        // sdk
        File sdkPomFile = new File(destDir, "ceylon-sdk/pom.xml");
        Assert.assertTrue(sdkPomFile.exists());

        // declare
        assertFileContainsLine(sdkPomFile, "    <groupId>org.ceylon-lang</groupId>");
        assertFileContainsLine(sdkPomFile, "    <version>"+Versions.CEYLON_VERSION_NUMBER+"</version>");
        assertFileContainsLine(sdkPomFile, "  <artifactId>ceylon-sdk</artifactId>");

        // depends
        assertFileContainsLine(sdkPomFile, "      <groupId>org.ceylon-lang</groupId>");
        assertNotFileContainsLine(sdkPomFile, "      <artifactId>org.eclipse.ceylon.model</artifactId>");
        assertNotFileContainsLine(sdkPomFile, "      <artifactId>ceylon.language</artifactId>");
        assertFileContainsLine(sdkPomFile, "      <artifactId>ceylon.test</artifactId>");
        assertFileContainsLine(sdkPomFile, "      <artifactId>ceylon.collection</artifactId>");
        assertFileContainsLine(sdkPomFile, "      <artifactId>ceylon.file</artifactId>");
    }
}
