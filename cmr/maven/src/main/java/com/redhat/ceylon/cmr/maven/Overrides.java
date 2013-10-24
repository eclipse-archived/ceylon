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

package com.redhat.ceylon.cmr.maven;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Overrides {
    private Map<MavenCoordinate, ArtifactOverrides> overrides = new HashMap<>();

    public void addArtifactOverride(ArtifactOverrides ao) {
        overrides.put(ao.getOwner(), ao);
    }

    public ArtifactOverrides getArtifactOverrides(MavenCoordinate mc) {
        return overrides.get(mc);
    }

    static Overrides parse(InputStream is) throws Exception {
        try {
            Overrides result = new Overrides();
            Document document = parseXml(is);
            List<Element> artifacts = getChildren(document.getDocumentElement(), "artifact");
            for (Element artifact : artifacts) {
                MavenCoordinate mc = getMavenCoordinate(artifact);
                ArtifactOverrides ao = new ArtifactOverrides(mc);
                result.overrides.put(mc, ao);
                addOverrides(ao, artifact, DependencyOverride.Type.ADD);
                addOverrides(ao, artifact, DependencyOverride.Type.REMOVE);
                addOverrides(ao, artifact, DependencyOverride.Type.REPLACE);
            }
            return result;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    protected static MavenCoordinate getMavenCoordinate(Element element) {
        String groupId = getRequiredAttribute(element, "groupId");
        String artifactId = getRequiredAttribute(element, "artifactId");
        String version = getRequiredAttribute(element, "version");
        String packaging = getAttribute(element, "packaging");
        PackagingType pt = (packaging != null) ? PackagingType.of(packaging) : PackagingType.JAR;
        String classifier = getAttribute(element, "classifier");
        return MavenCoordinates.createCoordinate(groupId, artifactId, version, pt, classifier);
    }

    protected static void addOverrides(ArtifactOverrides ao, Element artifact, DependencyOverride.Type type) {
        List<Element> overrides = getChildren(artifact, type.name().toLowerCase());
        for (Element override : overrides) {
            MavenCoordinate dep = getMavenCoordinate(override);
            DependencyOverride doo = new DependencyOverride(dep, type);
            ao.addOverride(doo);
        }
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
}
