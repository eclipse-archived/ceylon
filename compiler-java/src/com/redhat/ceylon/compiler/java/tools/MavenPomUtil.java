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

package com.redhat.ceylon.compiler.java.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MavenPomUtil {

    public static void writeMavenManifest2(File outputFolder, Module module,  
    		JdkProvider jdkProvider) {
        String groupId;
        String artifactId;
        if(module.getGroupId() != null){
            groupId = module.getGroupId();
            artifactId = artifactId(module);
        }else{
            String moduleName = module.getNameAsString();
            String[] mavenCoordinates = ModuleUtil.getMavenCoordinates(moduleName);
            groupId = mavenCoordinates[0];
            artifactId = mavenCoordinates[1];
        }
        String path = mavenPath(groupId, artifactId);
        File destinationPath = new File(outputFolder, path);
        FileUtil.mkdirs(destinationPath);
        writePomXml(destinationPath, groupId, artifactId, module, jdkProvider);
        writePomProperties(destinationPath, groupId, artifactId, module.getVersion());
    }

    private static void writePomXml(File outputFolder, String groupId, String artifactId, 
    		Module module, JdkProvider jdkProvider) {
        try (OutputStream os = new FileOutputStream(new File(outputFolder,"pom.xml"))){
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(os, "utf-8"));

            out.writeStartDocument();
            out.writeCharacters("\n");
            
            // FIXME: what to do with the default module?
            
            out.writeStartElement("project");
            out.writeAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
            out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            out.writeAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");
            
            out.writeCharacters("\n  ");
            out.writeStartElement("modelVersion");
            out.writeCharacters("4.0.0");
            out.writeEndElement();

            out.writeCharacters("\n  ");
            out.writeStartElement("groupId");
            out.writeCharacters(groupId);
            out.writeEndElement();

            out.writeCharacters("\n  ");
            out.writeStartElement("artifactId");
            out.writeCharacters(artifactId);
            out.writeEndElement();

            out.writeCharacters("\n  ");
            out.writeStartElement("version");
            out.writeCharacters(module.getVersion());
            out.writeEndElement();

            out.writeCharacters("\n  ");
            out.writeStartElement("name");
            out.writeCharacters(module.getNameAsString());
            out.writeEndElement();

            List<ModuleImport> imports = module.getImports();
            if(!imports.isEmpty()){
                out.writeCharacters("\n  ");
                out.writeStartElement("dependencies");

                for(ModuleImport dep : imports){
                    if (!ModelUtil.isForBackend(dep.getNativeBackends(), Backend.Java)) {
                        continue;
                    }
                    Module moduleDependency = dep.getModule();
                    
                    final String dependencyName = moduleDependency.getNameAsString();
                    
                    // skip c.l and jdk
                    if(dependencyName.equals(Module.LANGUAGE_MODULE_NAME)
                            || jdkProvider.isJDKModule(dependencyName))
                        continue;
                    
                    String depGroupId;
                    String depArtifactId;
                    String depClassifier;
                    if(moduleDependency.getGroupId() != null){
                        depGroupId = moduleDependency.getGroupId();
                        depArtifactId = artifactId(moduleDependency);
                        depClassifier = moduleDependency.getClassifier();
                    }else{
                        String[] mavenCoordinates = ModuleUtil.getMavenCoordinates(dependencyName);
                        depGroupId = mavenCoordinates[0];
                        depArtifactId = mavenCoordinates[1];
                        depClassifier = mavenCoordinates[2];
                    }

                    out.writeCharacters("\n    ");
                    out.writeStartElement("dependency");
                    
                    out.writeCharacters("\n      ");
                    out.writeStartElement("groupId");
                    out.writeCharacters(depGroupId);
                    out.writeEndElement();

                    out.writeCharacters("\n      ");
                    out.writeStartElement("artifactId");
                    out.writeCharacters(depArtifactId);
                    out.writeEndElement();
                    
                    if (depClassifier!=null) {
                        out.writeCharacters("\n      ");
                        out.writeStartElement("classifier");
                        out.writeCharacters(depClassifier);
                        out.writeEndElement();
                    }

                    out.writeCharacters("\n      ");
                    out.writeStartElement("version");
                    out.writeCharacters(moduleDependency.getVersion());
                    out.writeEndElement();
                    
                    if(dep.isOptional()){
                        out.writeCharacters("\n      ");
                        out.writeStartElement("optional");
                        out.writeCharacters("true");
                        out.writeEndElement();
                    }
                    
                    out.writeCharacters("\n    ");
                    out.writeEndElement();
                }

                out.writeCharacters("\n  ");
                out.writeEndElement();
            }

            
            out.writeCharacters("\n");
            out.writeEndElement();
            out.writeEndDocument();
            
            out.flush();
        }
        catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private static String artifactId(Module moduleDependency) {
        return moduleDependency.getArtifactId() != null ? 
                moduleDependency.getArtifactId() : 
                moduleDependency.getNameAsString();
    }

    private static void writePomProperties(File outputFolder, String groupId, String artifactId, String version) {
        try (OutputStream os = new FileOutputStream(new File(outputFolder,"pom.properties"))){
            Properties properties = new Properties();
            properties.put("version", version);
            properties.put("groupId", groupId);
            properties.put("artifactId", artifactId);
            properties.store(os, "Generated by Ceylon");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isMavenDescriptor(String entryFullName, Module module) {
        String moduleName = module.getNameAsString();
        String[] mavenCoordinates = ModuleUtil.getMavenCoordinates(moduleName);
        String groupId = mavenCoordinates[0];
        String artifactId = mavenCoordinates[1];

        String path = mavenPath(groupId, artifactId);
        return entryFullName.equals(path+"pom.xml")
            || entryFullName.equals(path+"pom.properties");
    }

    private static String mavenPath(String groupId, String artifactId) {
        return "META-INF/maven/"+groupId+"/"+artifactId+"/";
    }

}
