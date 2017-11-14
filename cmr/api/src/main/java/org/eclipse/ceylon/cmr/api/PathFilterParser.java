/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import static javax.xml.stream.XMLStreamConstants.ATTRIBUTE;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.DTD;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.ENTITY_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.ENTITY_REFERENCE;
import static javax.xml.stream.XMLStreamConstants.NOTATION_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.PROCESSING_INSTRUCTION;
import static javax.xml.stream.XMLStreamConstants.SPACE;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.ceylon.model.cmr.PathFilter;
import org.w3c.dom.Node;

/**
 * Includes code from Ceylon Runtime and JBoss Modules ModuleXmlParser.
 * 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author Stef Epardaud
 */
public final class PathFilterParser {

    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    public static PathFilter parse(String filter) throws IOException {
        if(filter.startsWith("<exports>") || filter.startsWith("<filter>")){
            // JBoss modules wants a namespace for validation
            int end = filter.indexOf('>');
            filter = filter.substring(0, end) + " xmlns=\"urn:jboss:module:1.0\"" + filter.substring(end);
        }
        return parse(new ByteArrayInputStream(filter.getBytes()));
    }

    private static void setIfSupported(XMLInputFactory inputFactory, String property, Object value) {
        if (inputFactory.isPropertySupported(property)) {
            inputFactory.setProperty(property, value);
        }
    }

    public static PathFilter parse(InputStream inputStream) throws IOException {
        try {
            final XMLInputFactory inputFactory = INPUT_FACTORY;
            setIfSupported(inputFactory, XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
            final XMLStreamReader streamReader = inputFactory.createXMLStreamReader(inputStream);
            return parseFilter(streamReader);
        } catch (Exception e) {
            throw new IOException("Error parsing filter.", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {
            }
        }
    }

    private static PathFilter parseFilter(XMLStreamReader reader) throws Exception {
        reader.nextTag(); // just skip <filter> or <exports>

        ModulesPathFilter pathFilter = new ModulesPathFilter();
        parseFilterList(reader, pathFilter);

        return pathFilter;
    }

    private static class ModulesPathFilter implements PathFilter {

        List<FilterRule> rules = new LinkedList<FilterRule>();
        
        private ModulesPathFilter() {
        }
        
        void addFilter(FilterRule rule){
            rules.add(rule);
        }

        public boolean accept(String path) {
            for(FilterRule rule : rules){
                Boolean ret = rule.accept(path);
                if(ret != null)
                    return ret;
            }
            // default to accept
            return true;
        }
    }
    
    public static String convertNodeToString(Node node) throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }

    private static final String MODULE_1_0 = "urn:jboss:module:1.0";
    private static final String MODULE_1_1 = "urn:jboss:module:1.1";
    private static final String MODULE_1_2 = "urn:jboss:module:1.2";
    private static final String MODULE_1_3 = "urn:jboss:module:1.3";

    private static final String A_NAME = "name";
    private static final String A_PATH = "path";

    private static final String E_INCLUDE = "include";
    private static final String E_EXCLUDE = "exclude";
    private static final String E_INCLUDE_SET = "include-set";
    private static final String E_EXCLUDE_SET = "exclude-set";
    private static final String E_PATH = "path";

    private static void parseFilterList(final XMLStreamReader reader, final ModulesPathFilter pathFilter) throws IOException, XMLStreamException {
        assertNoAttributes(reader);
        // xsd:choice
        int eventType;
        for (;;) {
            eventType = reader.nextTag();
            switch (eventType) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    validateNamespace(reader);
                    switch (reader.getLocalName()) {
                        case E_INCLUDE: parsePath(reader, true, pathFilter); break;
                        case E_EXCLUDE: parsePath(reader, false, pathFilter); break;
                        case E_INCLUDE_SET: parseSet(reader, true, pathFilter); break;
                        case E_EXCLUDE_SET: parseSet(reader, false, pathFilter); break;
                        default: throw unexpectedContent(reader);
                    }
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
    }

    private static void parsePath(final XMLStreamReader reader, final boolean include, final ModulesPathFilter pathFilter) throws IOException, XMLStreamException {
        String path = null;
        final Set<String> required = new HashSet<>(Arrays.asList(A_PATH));
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            validateAttributeNamespace(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            required.remove(attribute);
            switch (attribute) {
                case A_PATH: path = reader.getAttributeValue(i); break;
                default: throw unknownAttribute(reader, i);
            }
        }
        if (! required.isEmpty()) {
            throw missingAttributes(reader, required);
        }

        final boolean literal = path.indexOf('*') == -1 && path.indexOf('?') == -1;
        if (literal) {
            if (path.charAt(path.length() - 1) == '/') {
                pathFilter.addFilter(new FilterRule.IsChildOfFilterRule(path, include));
            } else {
                pathFilter.addFilter(new FilterRule.IsFilterRule(path, include));
            }
        } else {
            pathFilter.addFilter(new FilterRule.MatchFilterRule(path, include));
        }

        // consume remainder of element
        parseNoContent(reader);
    }

    private static Set<String> parseSet(final XMLStreamReader reader) throws IOException, XMLStreamException {
        assertNoAttributes(reader);
        final Set<String> set = new HashSet<>();
        // xsd:choice
        int eventType;
        for (;;) {
            eventType = reader.nextTag();
            switch (eventType) {
                case END_ELEMENT: {
                    return set;
                }
                case START_ELEMENT: {
                    validateNamespace(reader);
                    switch (reader.getLocalName()) {
                        case E_PATH: parsePathName(reader, set); break;
                        default: throw unexpectedContent(reader);
                    }
                }
            }
        }
    }

    private static void parseSet(final XMLStreamReader reader, final boolean include, final ModulesPathFilter pathFilter) throws IOException, XMLStreamException {
        pathFilter.addFilter(new FilterRule.SetFilterRule(parseSet(reader), include));
    }

    private static void parsePathName(final XMLStreamReader reader, final Set<String> set) throws IOException, XMLStreamException {
        String name = null;
        final Set<String> required = new HashSet<>(Arrays.asList(A_NAME));
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i ++) {
            validateAttributeNamespace(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            required.remove(attribute);
            switch (attribute) {
                case A_NAME: name = reader.getAttributeValue(i); break;
                default: throw unknownAttribute(reader, i);
            }
        }
        if (! required.isEmpty()) {
            throw missingAttributes(reader, required);
        }
        set.add(name);

        // consume remainder of element
        parseNoContent(reader);
    }

    private static XMLStreamException unexpectedContent(final XMLStreamReader reader) {
        final String kind;
        switch (reader.getEventType()) {
            case ATTRIBUTE:
                kind = "attribute";
                break;
            case CDATA:
                kind = "cdata";
                break;
            case CHARACTERS:
                kind = "characters";
                break;
            case COMMENT:
                kind = "comment";
                break;
            case DTD:
                kind = "dtd";
                break;
            case END_DOCUMENT:
                kind = "document end";
                break;
            case END_ELEMENT:
                kind = "element end";
                break;
            case ENTITY_DECLARATION:
                kind = "entity declaration";
                break;
            case ENTITY_REFERENCE:
                kind = "entity ref";
                break;
            case XMLStreamConstants.NAMESPACE:
                kind = "namespace";
                break;
            case NOTATION_DECLARATION:
                kind = "notation declaration";
                break;
            case PROCESSING_INSTRUCTION:
                kind = "processing instruction";
                break;
            case SPACE:
                kind = "whitespace";
                break;
            case START_DOCUMENT:
                kind = "document start";
                break;
            case START_ELEMENT:
                kind = "element start";
                break;
            default:
                kind = "unknown";
                break;
        }
        final StringBuilder b = new StringBuilder("Unexpected content of type '").append(kind).append('\'');
        if (reader.hasName()) {
            b.append(" named '").append(reader.getName()).append('\'');
        }
        if (reader.hasText()) {
            b.append(", text is: '").append(reader.getText()).append('\'');
        }
        return new XMLStreamException(b.toString(), reader.getLocation());
    }

    private static XMLStreamException unknownAttribute(final XMLStreamReader parser, final int index) {
        final String namespace = parser.getAttributeNamespace(index);
        final String prefix = parser.getAttributePrefix(index);
        final String name = parser.getAttributeLocalName(index);
        final StringBuilder eb = new StringBuilder("Unknown attribute \"");
        if (prefix != null) eb.append(prefix).append(':');
        eb.append(name);
        if (namespace != null) eb.append("\" from namespace \"").append(namespace);
        eb.append('"');
        return new XMLStreamException(eb.toString(), parser.getLocation());
    }

    private static XMLStreamException missingAttributes(final XMLStreamReader parser, final Set<String> required) {
        final StringBuilder b = new StringBuilder("Missing one or more required attributes:");
        for (String attribute : required) {
            b.append(' ').append(attribute);
        }
        return new XMLStreamException(b.toString(), parser.getLocation());
    }

    private static void assertNoAttributes(final XMLStreamReader reader) throws XMLStreamException {
        final int attributeCount = reader.getAttributeCount();
        if (attributeCount > 0) {
            throw unknownAttribute(reader, 0);
        }
    }

    private static void validateAttributeNamespace(final XMLStreamReader reader, final int index) throws XMLStreamException {
        if (reader.getAttributeNamespace(index) != null) {
            throw unknownAttribute(reader, index);
        }
    }

    private static void parseNoContent(final XMLStreamReader reader) throws IOException, XMLStreamException {
        int eventType;
        for (;;) {
            eventType = reader.nextTag();
            switch (eventType) {
                case END_ELEMENT: {
                    return;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
    }

    private static void validateNamespace(final XMLStreamReader reader) throws XMLStreamException {
        switch (reader.getNamespaceURI()) {
            case MODULE_1_0:
            case MODULE_1_1:
            case MODULE_1_2:
            case MODULE_1_3:
                break;
            default: throw unexpectedContent(reader);
        }
    }
}
