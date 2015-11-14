/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package ceylon.modules.bootstrap.loader;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * A fast, validating module.xml parser.
 * Copied from JBoss Modules.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author thomas.diesler@jboss.com
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
final class ModuleXmlParser {

    private ModuleXmlParser() {
    }

    private static final String NAMESPACE = "urn:jboss:module:1.0";

    enum Element {
        MODULE,
        DEPENDENCIES,
        EXPORTS,
        IMPORTS,
        INCLUDE,
        INCLUDE_SET,
        EXCLUDE,
        EXCLUDE_SET,
        RESOURCES,
        MAIN_CLASS,
        RESOURCE_ROOT,
        PATH,
        FILTER,
        CONFIGURATION,
        LOADER,
        MODULE_PATH,
        IMPORT,

        // default unknown element
        UNKNOWN;

        private static final Map<QName, Element> elements;

        static {
            Map<QName, Element> elementsMap = new HashMap<QName, Element>();
            elementsMap.put(new QName(NAMESPACE, "module"), Element.MODULE);
            elementsMap.put(new QName(NAMESPACE, "dependencies"), Element.DEPENDENCIES);
            elementsMap.put(new QName(NAMESPACE, "resources"), Element.RESOURCES);
            elementsMap.put(new QName(NAMESPACE, "main-class"), Element.MAIN_CLASS);
            elementsMap.put(new QName(NAMESPACE, "resource-root"), Element.RESOURCE_ROOT);
            elementsMap.put(new QName(NAMESPACE, "path"), Element.PATH);
            elementsMap.put(new QName(NAMESPACE, "exports"), Element.EXPORTS);
            elementsMap.put(new QName(NAMESPACE, "imports"), Element.IMPORTS);
            elementsMap.put(new QName(NAMESPACE, "include"), Element.INCLUDE);
            elementsMap.put(new QName(NAMESPACE, "exclude"), Element.EXCLUDE);
            elementsMap.put(new QName(NAMESPACE, "include-set"), Element.INCLUDE_SET);
            elementsMap.put(new QName(NAMESPACE, "exclude-set"), Element.EXCLUDE_SET);
            elementsMap.put(new QName(NAMESPACE, "filter"), Element.FILTER);
            elementsMap.put(new QName(NAMESPACE, "configuration"), Element.CONFIGURATION);
            elementsMap.put(new QName(NAMESPACE, "loader"), Element.LOADER);
            elementsMap.put(new QName(NAMESPACE, "module-path"), Element.MODULE_PATH);
            elementsMap.put(new QName(NAMESPACE, "import"), Element.IMPORT);
            elements = elementsMap;
        }

        static Element of(QName qName) {
            final Element element = elements.get(qName);
            return element == null ? UNKNOWN : element;
        }
    }

    enum Attribute {
        NAME,
        SLOT,
        EXPORT,
        SERVICES,
        PATH,
        OPTIONAL,
        DEFAULT_LOADER,

        // default unknown attribute
        UNKNOWN;

        private static final Map<QName, Attribute> attributes;

        static {
            Map<QName, Attribute> attributesMap = new HashMap<QName, Attribute>();
            attributesMap.put(new QName("name"), NAME);
            attributesMap.put(new QName("slot"), SLOT);
            attributesMap.put(new QName("export"), EXPORT);
            attributesMap.put(new QName("services"), SERVICES);
            attributesMap.put(new QName("path"), PATH);
            attributesMap.put(new QName("optional"), OPTIONAL);
            attributesMap.put(new QName("default-loader"), DEFAULT_LOADER);
            attributes = attributesMap;
        }

        static Attribute of(QName qName) {
            final Attribute attribute = attributes.get(qName);
            return attribute == null ? UNKNOWN : attribute;
        }
    }

    enum Disposition {
        NONE("none"),
        IMPORT("import"),
        EXPORT("export"),;

        private static final Map<String, Disposition> values;

        static {
            final Map<String, Disposition> map = new HashMap<String, Disposition>();
            for (Disposition d : values()) {
                map.put(d.value, d);
            }
            values = map;
        }

        private final String value;

        Disposition(String value) {
            this.value = value;
        }

        static Disposition of(String value) {
            final Disposition disposition = values.get(value);
            return disposition == null ? NONE : disposition;
        }
    }

    private static void setIfSupported(XMLInputFactory inputFactory, String property, Object value) {
        if (inputFactory.isPropertySupported(property)) {
            inputFactory.setProperty(property, value);
        }
    }

    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    static List<String> parseResourcePaths(final InputStream source, ModuleIdentifier mi) throws ModuleLoadException {
        try {
            final XMLInputFactory inputFactory = INPUT_FACTORY;
            setIfSupported(inputFactory, XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
            setIfSupported(inputFactory, XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            final XMLStreamReader streamReader = inputFactory.createXMLStreamReader(source);
            try {
                List<String> resourcePaths = new ArrayList<String>();
                parseResourcePaths(streamReader, resourcePaths);
                return resourcePaths;
            } finally {
                safeClose(streamReader);
            }
        } catch (XMLStreamException e) {
            throw new ModuleLoadException("Error reading resource paths: " + mi, e);
        } finally {
            safeClose(source);
        }
    }

    private static void safeClose(final Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    private static void safeClose(final XMLStreamReader streamReader) {
        if (streamReader != null) try {
            streamReader.close();
        } catch (XMLStreamException e) {
            // ignore
        }
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

    private static XMLStreamException endOfDocument(final Location location) {
        return new XMLStreamException("Unexpected end of document", location);
    }

    private static XMLStreamException missingAttributes(final Location location, final Set<Attribute> required) {
        final StringBuilder b = new StringBuilder("Missing one or more required attributes:");
        for (Attribute attribute : required) {
            b.append(' ').append(attribute);
        }
        return new XMLStreamException(b.toString(), location);
    }

    private static void parseResourcePaths(final XMLStreamReader reader, List<String> resourcePaths) throws XMLStreamException {
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case START_ELEMENT: {
                    if (Element.of(reader.getName()) != Element.MODULE) {
                        throw unexpectedContent(reader);
                    }
                    parseModuleContents(reader, resourcePaths);
                    parseEndDocument(reader);
                    return;
                }
                case END_ELEMENT:
                    break;
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
        throw endOfDocument(reader.getLocation());
    }

    private static void parseModuleContents(final XMLStreamReader reader, final List<String> resourcePaths) throws XMLStreamException {
        Set<Element> visited = EnumSet.noneOf(Element.class);
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    final Element element = Element.of(reader.getName());
                    if (visited.contains(element)) {
                        throw unexpectedContent(reader);
                    }
                    visited.add(element);
                    switch (element) {
                        case EXPORTS:
                            skipElement(reader);
                            break;
                        case DEPENDENCIES:
                            skipElement(reader);
                            break;
                        case MAIN_CLASS:
                            skipElement(reader);
                            break;
                        case RESOURCES:
                            parseResources(reader, resourcePaths);
                            break;
                        default:
                            throw unexpectedContent(reader);
                    }
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
        throw endOfDocument(reader.getLocation());
    }

    private static void skipElement(final XMLStreamReader reader) throws XMLStreamException {
        String name = reader.getLocalName();
        // xsd:choice
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    if (name != null && name.equals(reader.getLocalName()))
                        return;
                    else
                        break;
                }
                case START_ELEMENT: {
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
        throw endOfDocument(reader.getLocation());
    }

    private static void parseResources(final XMLStreamReader reader, final List<String> resourcePaths) throws XMLStreamException {
        // xsd:choice
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    return;
                }
                case START_ELEMENT: {
                    switch (Element.of(reader.getName())) {
                        case RESOURCE_ROOT: {
                            parseResourceRoot(reader, resourcePaths);
                            break;
                        }
                        default:
                            throw unexpectedContent(reader);
                    }
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
        throw endOfDocument(reader.getLocation());
    }

    private static void parseResourceRoot(final XMLStreamReader reader, final List<String> resourcePaths) throws XMLStreamException {
        String path = null;
        final Set<Attribute> required = EnumSet.of(Attribute.PATH);
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final Attribute attribute = Attribute.of(reader.getAttributeName(i));
            required.remove(attribute);
            switch (attribute) {
                case NAME:
                    break;
                case PATH:
                    path = reader.getAttributeValue(i);
                    break;
                default:
                    throw unexpectedContent(reader);
            }
        }
        if (!required.isEmpty()) {
            throw missingAttributes(reader.getLocation(), required);
        }

        final Set<Element> encountered = EnumSet.noneOf(Element.class);
        while (reader.hasNext()) {
            switch (reader.nextTag()) {
                case END_ELEMENT: {
                    resourcePaths.add(path);
                    return;
                }
                case START_ELEMENT: {
                    final Element element = Element.of(reader.getName());
                    if (!encountered.add(element)) throw unexpectedContent(reader);
                    switch (element) {
                        case FILTER:
                            skipElement(reader);
                            break;
                        default:
                            throw unexpectedContent(reader);
                    }
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
    }

    private static void parseEndDocument(final XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            switch (reader.next()) {
                case END_DOCUMENT: {
                    return;
                }
                case CHARACTERS: {
                    if (!reader.isWhiteSpace()) {
                        throw unexpectedContent(reader);
                    }
                    // ignore
                    break;
                }
                case COMMENT:
                case SPACE: {
                    // ignore
                    break;
                }
                default: {
                    throw unexpectedContent(reader);
                }
            }
        }
    }
}
