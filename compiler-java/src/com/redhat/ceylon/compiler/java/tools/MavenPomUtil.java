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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MavenPomUtil {

    private static String[] getMavenCoordinates(String moduleName){
        int lastDot = moduleName.lastIndexOf(".");
        String groupId;
        String artifactId;
        if(lastDot != -1){
            groupId = moduleName.substring(0, lastDot);
            artifactId = moduleName.substring(lastDot+1);
        }else{
            groupId = artifactId = moduleName;
        }
        return new String[]{groupId, artifactId};
    }
    
    public static void writeMavenManifest(JarOutputStream jarOutputStream, Module module, Set<String> folders) {
        String moduleName = module.getNameAsString();
        String[] mavenCoordinates = getMavenCoordinates(moduleName);
        String groupId = mavenCoordinates[0];
        String artifactId = mavenCoordinates[1];

        folders.add("META-INF/");
        folders.add("META-INF/maven/");
        folders.add("META-INF/maven/"+groupId+"/");
        String path = "META-INF/maven/"+groupId+"/"+artifactId+"/";
        folders.add(path);
        writePomXml(jarOutputStream, path, groupId, artifactId, module);
        writePomProperties(jarOutputStream, path, groupId, artifactId, module.getVersion());
    }

    private static void writePomXml(JarOutputStream jarOutputStream, String path, String groupId, String artifactId, Module module) {
        try {
            jarOutputStream.putNextEntry(new ZipEntry(path+"pom.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(jarOutputStream, "utf-8"));

            out.writeStartDocument();
            out.writeCharacters("\n");
            
            // FIXME: what to do with the default module?
            
            out.writeStartElement("project");
            out.writeAttribute("xmlns", "http://maven.apache.org/POM/4.0.0");
            out.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            out.writeAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");
            
            out.writeCharacters("\n ");
            out.writeStartElement("modelVersion");
            out.writeCharacters("4.0.0");
            out.writeEndElement();

            out.writeCharacters("\n ");
            out.writeStartElement("groupId");
            out.writeCharacters(groupId);
            out.writeEndElement();

            out.writeCharacters("\n ");
            out.writeStartElement("artifactId");
            out.writeCharacters(artifactId);
            out.writeEndElement();

            out.writeCharacters("\n ");
            out.writeStartElement("version");
            out.writeCharacters(module.getVersion());
            out.writeEndElement();

            out.writeCharacters("\n ");
            out.writeStartElement("name");
            out.writeCharacters(module.getNameAsString());
            out.writeEndElement();

            List<ModuleImport> imports = module.getImports();
            if(!imports.isEmpty()){
                out.writeCharacters("\n ");
                out.writeStartElement("dependencies");

                for(ModuleImport dep : imports){
                    Module moduleDependency = dep.getModule();
                    
                    String dependencyName = moduleDependency.getNameAsString();
                    
                    // skip c.l and jdk
                    if(dependencyName.equals(Module.LANGUAGE_MODULE_NAME)
                            || JDKUtils.isJDKModule(dependencyName)
                            || JDKUtils.isOracleJDKModule(dependencyName))
                        continue;
                    
                    String[] mavenCoordinates = getMavenCoordinates(moduleDependency.getNameAsString());
                    out.writeCharacters("\n  ");
                    out.writeStartElement("dependency");
                    
                    out.writeCharacters("\n    ");
                    out.writeStartElement("groupId");
                    out.writeCharacters(mavenCoordinates[0]);
                    out.writeEndElement();

                    out.writeCharacters("\n    ");
                    out.writeStartElement("artifactId");
                    out.writeCharacters(mavenCoordinates[1]);
                    out.writeEndElement();

                    out.writeCharacters("\n    ");
                    out.writeStartElement("version");
                    out.writeCharacters(moduleDependency.getVersion());
                    out.writeEndElement();
                    
                    if(dep.isOptional()){
                        out.writeCharacters("\n    ");
                        out.writeStartElement("optional");
                        out.writeCharacters("true");
                        out.writeEndElement();
                    }
                    
                    out.writeCharacters("\n  ");
                    out.writeEndElement();
                }

                out.writeCharacters("\n ");
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
        finally {
            try {
                jarOutputStream.closeEntry();
            }
            catch (IOException ignore) {
            }
        }
    }

    private static void writePomProperties(JarOutputStream jarOutputStream, String path, String groupId, String artifactId, String version) {
        try {
            jarOutputStream.putNextEntry(new ZipEntry(path+"pom.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Properties properties = new Properties();
            properties.put("version", version);
            properties.put("groupId", groupId);
            properties.put("artifactId", artifactId);
            properties.store(jarOutputStream, "Generated by Ceylon");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                jarOutputStream.closeEntry();
            }
            catch (IOException ignore) {
            }
        }
    }

    public static boolean isMavenDescriptor(String entryFullName, Module module) {
        String moduleName = module.getNameAsString();
        String[] mavenCoordinates = getMavenCoordinates(moduleName);
        String groupId = mavenCoordinates[0];
        String artifactId = mavenCoordinates[1];

        String path = "META-INF/maven/"+groupId+"/"+artifactId+"/";
        return entryFullName.equals(path+"pom.xml")
                || entryFullName.equals(path+"pom.properties");
    }

}
