package com.redhat.ceylon.cmr.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.cmr.ModuleScope;

/**
 * Utility class which does not depend on anything Aether (optional dep), so can be safely public
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MavenUtils {
    
    public static String getDefaultMavenSettings() {
        String path = System.getProperty("maven.repo.local");
        if (path != null) {
            File file = new File(path, "settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getProperty("user.home");
        if (path != null) {
            File file = new File(path, ".m2/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        path = System.getenv("M2_HOME");
        if (path != null) {
            File file = new File(path, "conf/settings.xml");
            if (file.exists())
                return file.getAbsolutePath();
        }

        return "classpath:settings.xml";
    }

    public static ModuleInfo getDependencies(File file, String name, String version) throws IOException {
        try(InputStream is = new FileInputStream(file)){
            return getDependencies(is, name, version);
        }
    }

    public static ModuleInfo getDependencies(InputStream stream, String name, String version) throws IOException {
        Document doc;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(stream);
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } catch (SAXException e) {
            throw new IOException(e);
        }
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        
        String modGroupId = getText(root, "groupId");
        // can be null, inherited from parent
        if(modGroupId == null){
            Element parent = getFirstElement(root, "parent");
            if(parent != null)
                modGroupId = getText(parent, "groupId");
        }
        
        String modArtifactId = getText(root, "artifactId");
        
        String classifier = getText(root, "classifier");
        
        String modVersion = getText(root, "version");
        // can be null, inherited from parent
        if(modVersion == null){
            Element parent = getFirstElement(root, "parent");
            if(parent != null)
                modVersion = getText(parent, "version");
        }
        
        String modName = modGroupId + ":" + modArtifactId;
        if(name != null && !name.equals(modName))
            return null;
        if(version != null && !version.equals(modVersion))
            return null;
        Element deps = getFirstElement(root, "dependencies");
        Set<ModuleDependencyInfo> ret = new HashSet<>();
        if(deps != null){
            NodeList depList = deps.getElementsByTagName("dependency");
            if(depList != null){
                for(int i=0;i<depList.getLength();i++){
                    Element dep = (Element) depList.item(i);
                    String depGroupId = getText(dep, "groupId");
                    String depArtifactId = getText(dep, "artifactId");
                    String depClassifier = getText(dep, "classifier");
                    String depVersion = getText(dep, "version");
                    String depScope = getText(dep, "scope");
                    String depOptional = getText(dep, "optional");
                    ModuleScope scope;
                    
                    // keep compile, runtime, provided
                    if(depScope != null 
                            && (depScope.equals("system")
                                    || depScope.equals("test")))
                        continue;
                    if("provided".equals(depScope))
                        scope = ModuleScope.PROVIDED;
                    else if("runtime".equals(depScope))
                        scope = ModuleScope.RUNTIME;
                    else
                        scope = ModuleScope.COMPILE;

                    ret.add(new ModuleDependencyInfo(MavenRepository.NAMESPACE, 
                            moduleName(depGroupId, depArtifactId, depClassifier), 
                            depVersion, "true".equals(depOptional), false, Backends.JAVA, scope));
                }
            }
        }
        return new ModuleInfo(MavenRepository.NAMESPACE, modName, modVersion, modGroupId, modArtifactId, classifier, null, ret);
    }

    static String getText(Element element, String childName){
        Element child = getFirstElement(element, childName);
        if(child != null){
            return child.getTextContent();
        }
        return null;
    }

    static Element getFirstElement(Element element, String childName){
        NodeList children = element.getChildNodes();
        for(int i=0;i<children.getLength();i++){
            Node child = children.item(i);
            if(child instanceof Element
                    && ((Element) child).getTagName().equals(childName)){
                return (Element) child;
            }
        }
        return null;
    }

    static void collectText(Element element, StringBuilder builder, String... tags){
        for(String tag : tags){
            String desc = getText(element, tag);
            if(desc != null){
                if(builder.length() > 0)
                    builder.append("\n");
                builder.append(desc);
            }
        }
    }

    public static String moduleName(String groupId, String artifactId, String classifier) {
        return classifier==null || classifier.isEmpty() ? 
                groupId+":"+artifactId : 
                groupId+":"+artifactId+":"+classifier;
    }
}
