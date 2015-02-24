/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.redhat.ceylon.cmr.api.DependencyOverride.Type;
import com.redhat.ceylon.cmr.util.PathFilterParser;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Overrides {
    
    private Map<ArtifactContext, ArtifactOverrides> overrides = new HashMap<>();
    private Map<String, ArtifactOverrides> overridesNoVersion = new HashMap<>();
    private Set<DependencyOverride> removed = new HashSet<DependencyOverride>();

    public void addArtifactOverride(ArtifactOverrides ao) {
        overrides.put(ao.getOwner(), ao);
        if(ao.getOwner().getVersion() == null)
            overridesNoVersion.put(ao.getOwner().getName(), ao);
    }

    public ArtifactOverrides getArtifactOverrides(ArtifactContext mc) {
        ArtifactOverrides ao = overrides.get(mc);
        if(ao == null){
            // fall-back to overrides with no version specified
            return overridesNoVersion.get(mc.getName());
        }
        return ao;
    }

    private void addRemovedArtifact(DependencyOverride context) {
        removed.add(context);
    }

    public boolean isRemoved(ArtifactContext context){
        for(DependencyOverride ro : removed){
            if(ro.matches(context))
                return true;
        }
        return false;
    }
    
    static Overrides parse(InputStream is) throws Exception {
        try {
            Overrides result = new Overrides();
            Document document = parseXml(is);
            List<Element> artifacts = getChildren(document.getDocumentElement(), "artifact");
            for (Element artifact : artifacts) {
                ArtifactContext mc = getArtifactContext(artifact, true); // version is optional
                ArtifactOverrides ao = new ArtifactOverrides(mc);
                result.addArtifactOverride(ao);
                addOverrides(ao, artifact, DependencyOverride.Type.ADD);
                addOverrides(ao, artifact, DependencyOverride.Type.REMOVE);
                addOverrides(ao, artifact, DependencyOverride.Type.REPLACE);
                // filter
                NodeList filterNode = artifact.getElementsByTagName("filter");
                if (filterNode != null && filterNode.getLength() > 0) {
                    Node node = filterNode.item(0);
                    ao.setFilter(PathFilterParser.convertNodeToString(node));
                }
            }
            List<Element> removedArtifacts = getChildren(document.getDocumentElement(), "remove");
            for (Element artifact : removedArtifacts) {
                ArtifactContext context = getArtifactContext(artifact, true);
                DependencyOverride doo = new DependencyOverride(context, Type.REMOVE, false, false);
                result.addRemovedArtifact(doo);
            }
            return result;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    protected static ArtifactContext getArtifactContext(Element element, boolean optionalVersion) {
        String groupId = getAttribute(element, "groupId");
        if(groupId != null){
            String artifactId = getRequiredAttribute(element, "artifactId");
            String version = optionalVersion ? getAttribute(element, "version") : getRequiredAttribute(element, "version");
            String packaging = getAttribute(element, "packaging");
            String classifier = getAttribute(element, "classifier");
            return createMavenArtifactContext(groupId, artifactId, version, packaging, classifier);
        }else{
            String module = getRequiredAttribute(element, "module");
            String version = optionalVersion ? getAttribute(element, "version") : getRequiredAttribute(element, "version");
            return new ArtifactContext(module, version);
        }
    }

    protected static void addOverrides(ArtifactOverrides ao, Element artifact, DependencyOverride.Type type) {
        List<Element> overrides = getChildren(artifact, type.name().toLowerCase());
        for (Element override : overrides) {
            ArtifactContext dep = getArtifactContext(override, type == Type.REMOVE);
            boolean shared = getBooleanAttribute(override, "shared");
            boolean optional = getBooleanAttribute(override, "optional");
            DependencyOverride doo = new DependencyOverride(dep, type, shared, optional);
            ao.addOverride(doo);
        }
    }

    protected static boolean getBooleanAttribute(Element element, String name) {
        String val = getAttribute(element, name);
        return val != null && val.toLowerCase().equals("true");
    }

    protected static String getAttribute(Element element, String name) {
        String value = element.getAttribute(name);
        return (value == null || value.length() == 0) ? null : value;
    }

    protected static String getRequiredAttribute(Element element, String name) {
        String value = getAttribute(element, name);
        if (value == null) {
            throw new IllegalArgumentException(String.format("Missing '%s' attribute in element %s.", name, element));
        }
        return value;
    }

    protected static Document parseXml(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    protected static List<Element> getChildren(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            elements.add((Element) nodes.item(i));
        }
        return elements;
    }

    public static ArtifactContext createMavenArtifactContext(String groupId, String artifactId, String version, 
                                                             String packaging, String classifier) {
        return new MavenArtifactContext(groupId, artifactId, version, packaging, classifier);
    }
}
