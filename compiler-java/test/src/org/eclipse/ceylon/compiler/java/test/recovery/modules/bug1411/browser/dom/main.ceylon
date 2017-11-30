shared interface Node {
    shared formal String nodeName;
    shared formal String nodeValue;
    shared formal Integer nodeType;
    shared formal Node parentNode;
    shared formal NodeList childNodes;
    shared formal Node firstChild;
    shared formal Node lastChild;
    shared formal Node previousSibling;
    shared formal Node nextSibling;
    shared formal NamedNodeMap attributes;
    shared formal Document ownerDocument;
    shared formal Node insertBefore(Node node0, Node node1);
    shared formal Node replaceChild(Node node0, Node node1);
    shared formal Node removeChild(Node node0);
    shared formal Node appendChild(Node node0);
    shared formal Boolean hasChildNodes();
    shared formal Node cloneNode(Boolean boolean0);
    shared formal void normalize();
    shared formal Boolean isSupported(String string0, String string1);
    shared formal String namespaceURI;
    shared formal String prefix;
    shared formal String localName;
    shared formal Boolean hasAttributes();
    shared formal String baseURI;
    shared formal Integer compareDocumentPosition(Node node0);
    shared formal String textContent;
    shared formal Boolean isSameNode(Node node0);
    shared formal String lookupPrefix(String string0);
    shared formal Boolean isDefaultNamespace(String string0);
    shared formal String lookupNamespaceURI(String string0);
    shared formal Boolean isEqualNode(Node node0);
    shared formal Object getFeature(String string0, String string1);
    shared formal Object setUserData(String string0, Object object1, UserDataHandler userDataHandler2);
    shared formal Object getUserData(String string0);
    /*
    shared formal Integer ELEMENT_NODE;
    shared formal Integer ATTRIBUTE_NODE;
    shared formal Integer TEXXXT_NODE;
    shared formal Integer CDATA_SECTION_NODE;
    shared formal Integer ENTITY_REFERENCE_NODE;
    shared formal Integer ENTITY_NODE;
    shared formal Integer PROCESSING_INSTRUCTION_NODE;
    shared formal Integer COMMENT_NODE;
    shared formal Integer DOCUMENT_NODE;
    shared formal Integer DOCUMENT_TYPE_NODE;
    shared formal Integer DOCUMENT_FRAGMENT_NODE;
    shared formal Integer NOTATION_NODE;
    shared formal Integer DOCUMENT_POSITION_DISCONNECTED;
    shared formal Integer DOCUMENT_POSITION_PRECEDING;
    shared formal Integer DOCUMENT_POSITION_FOLLOWING;
    shared formal Integer DOCUMENT_POSITION_CONTAINS;
    shared formal Integer DOCUMENT_POSITION_CONTAINED_BY;
    shared formal Integer DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;
    */
}
shared interface Element satisfies Node {
    shared formal String tagName;
    shared formal String getAttribute(String string0);
    shared formal void setAttribute(String string0, String string1);
    shared formal void removeAttribute(String string0);
    shared formal Attr getAttributeNode(String string0);
    shared formal Attr setAttributeNode(Attr attr0);
    shared formal Attr removeAttributeNode(Attr attr0);
    shared formal NodeList getElementsByTagName(String string0);
    shared formal String getAttributeNS(String string0, String string1);
    shared formal void setAttributeNS(String string0, String string1, String string2);
    shared formal void removeAttributeNS(String string0, String string1);
    shared formal Attr getAttributeNodeNS(String string0, String string1);
    shared formal Attr setAttributeNodeNS(Attr attr0);
    shared formal NodeList getElementsByTagNameNS(String string0, String string1);
    shared formal Boolean hasAttribute(String string0);
    shared formal Boolean hasAttributeNS(String string0, String string1);
    shared formal TypeInfo schemaTypeInfo;
    shared formal void setIdAttribute(String string0, Boolean boolean1);
    shared formal void setIdAttributeNS(String string0, String string1, Boolean boolean2);
    shared formal void setIdAttributeNode(Attr attr0, Boolean boolean1);
}
shared interface TypeInfo {
    shared formal String typeName;
    shared formal String typeNamespace;
    shared formal Boolean isDerivedFrom(String string0, String string1, Integer integer2);
    /*
    shared formal Integer DERIVATION_RESTRICTION;
    shared formal Integer DERIVATION_EXXXTENSION;
    shared formal Integer DERIVATION_UNION;
    shared formal Integer DERIVATION_LIST;
    */
}
shared interface Attr satisfies Node {
    shared formal String name;
    shared formal Boolean specified;
    shared formal String \ivalue;
    shared formal Element ownerElement;
    shared formal TypeInfo schemaTypeInfo;
    shared formal Boolean id;
}
shared interface CharacterData satisfies Node {
    shared formal String data;
    shared formal Integer length;
    shared formal String substringData(Integer integer0, Integer integer1);
    shared formal void appendData(String string0);
    shared formal void insertData(Integer integer0, String string1);
    shared formal void deleteData(Integer integer0, Integer integer1);
    shared formal void replaceData(Integer integer0, Integer integer1, String string2);
}
shared interface Text satisfies CharacterData {
    shared formal Text splitText(Integer integer0);
    shared formal Boolean elementContentWhitespace;
    shared formal String wholeText;
    shared formal Text replaceWholeText(String string0);
}
shared interface CDATASection satisfies Text {
}
shared interface Comment satisfies CharacterData {
}
shared interface DOMConfiguration {
    shared formal void setParameter(String string0, Object object1);
    shared formal Object getParameter(String string0);
    shared formal Boolean canSetParameter(String string0, Object object1);
    shared formal DOMStringList parameterNames;
}
shared interface DOMError {
    shared formal Integer severity;
    shared formal String message;
    shared formal String type;
    shared formal Object relatedException;
    shared formal Object relatedData;
    shared formal DOMLocator location;
    /*
    shared formal Integer SEVERITY_WARNING;
    shared formal Integer SEVERITY_ERROR;
    shared formal Integer SEVERITY_FATAL_ERROR;
    */
}
shared interface DOMErrorHandler {
    shared formal Boolean handleError(DOMError dOMError0);
}
shared interface DOMException {
    shared formal Integer code;
    /*
    shared formal Integer INDEXXX_SIZE_ERR;
    shared formal Integer DOMSTRING_SIZE_ERR;
    shared formal Integer HIERARCHY_REQUEST_ERR;
    shared formal Integer WRONG_DOCUMENT_ERR;
    shared formal Integer INVALID_CHARACTER_ERR;
    shared formal Integer NO_DATA_ALLOWED_ERR;
    shared formal Integer NO_MODIFICATION_ALLOWED_ERR;
    shared formal Integer NOT_FOUND_ERR;
    shared formal Integer NOT_SUPPORTED_ERR;
    shared formal Integer INUSE_ATTRIBUTE_ERR;
    shared formal Integer INVALID_STATE_ERR;
    shared formal Integer SYNTAXXX_ERR;
    shared formal Integer INVALID_MODIFICATION_ERR;
    shared formal Integer NAMESPACE_ERR;
    shared formal Integer INVALID_ACCESS_ERR;
    shared formal Integer VALIDATION_ERR;
    shared formal Integer TYPE_MISMATCH_ERR;
    */
}
shared interface DOMImplementation {
    shared formal Boolean hasFeature(String string0, String string1);
    shared formal DocumentType createDocumentType(String string0, String string1, String string2);
    shared formal Document createDocument(String string0, String string1, DocumentType documentType2);
    shared formal Object getFeature(String string0, String string1);
}
shared interface DOMImplementationList {
    shared formal DOMImplementation item(Integer integer0);
    shared formal Integer length;
}
shared interface DOMImplementationSource {
    shared formal DOMImplementation getDOMImplementation(String string0);
    shared formal DOMImplementationList getDOMImplementationList(String string0);
}
shared interface DOMLocator {
    shared formal Integer lineNumber;
    shared formal Integer columnNumber;
    shared formal Integer byteOffset;
    shared formal Integer utf16Offset;
    shared formal Node relatedNode;
    shared formal String uri;
}
shared interface DOMStringList {
    shared formal String item(Integer integer0);
    shared formal Integer length;
    shared formal Boolean contains(String string0);
}
shared interface DocumentType satisfies Node {
    shared formal String name;
    shared formal NamedNodeMap entities;
    shared formal NamedNodeMap notations;
    shared formal String publicId;
    shared formal String systemId;
    shared formal String internalSubset;
}
shared interface DocumentFragment satisfies Node {
}
shared interface ProcessingInstruction satisfies Node {
    shared formal String target;
    shared formal String data;
}
shared interface EntityReference satisfies Node {
}
shared interface NodeList {
    shared formal Node item(Integer integer0);
    shared formal Integer length;
}
shared interface Document satisfies Node {
    shared formal DocumentType doctype;
    shared formal DOMImplementation implementation;
    shared formal Element documentElement;
    shared formal Element createElement(String string0);
    shared formal DocumentFragment createDocumentFragment();
    shared formal Text createTextNode(String string0);
    shared formal Comment createComment(String string0);
    shared formal CDATASection createCDATASection(String string0);
    shared formal ProcessingInstruction createProcessingInstruction(String string0, String string1);
    shared formal Attr createAttribute(String string0);
    shared formal EntityReference createEntityReference(String string0);
    shared formal NodeList getElementsByTagName(String string0);
    shared formal Node importNode(Node node0, Boolean boolean1);
    shared formal Element createElementNS(String string0, String string1);
    shared formal Attr createAttributeNS(String string0, String string1);
    shared formal NodeList getElementsByTagNameNS(String string0, String string1);
    shared formal Element getElementById(String string0);
    shared formal String inputEncoding;
    shared formal String xmlEncoding;
    shared formal Boolean xmlStandalone;
    shared formal String xmlVersion;
    shared formal Boolean strictErrorChecking;
    shared formal String documentURI;
    shared formal Node adoptNode(Node node0);
    shared formal DOMConfiguration domConfig;
    shared formal void normalizeDocument();
    shared formal Node renameNode(Node node0, String string1, String string2);
}
shared interface NamedNodeMap {
    shared formal Node getNamedItem(String string0);
    shared formal Node setNamedItem(Node node0);
    shared formal Node removeNamedItem(String string0);
    shared formal Node item(Integer integer0);
    shared formal Integer length;
    shared formal Node getNamedItemNS(String string0, String string1);
    shared formal Node setNamedItemNS(Node node0);
    shared formal Node removeNamedItemNS(String string0, String string1);
}
shared interface Entity satisfies Node {
    shared formal String publicId;
    shared formal String systemId;
    shared formal String notationName;
    shared formal String inputEncoding;
    shared formal String xmlEncoding;
    shared formal String xmlVersion;
}
shared interface NameList {
    shared formal String getName(Integer integer0);
    shared formal String getNamespaceURI(Integer integer0);
    shared formal Integer length;
    shared formal Boolean contains(String string0);
    shared formal Boolean containsNS(String string0, String string1);
}
shared interface Notation satisfies Node {
    shared formal String publicId;
    shared formal String systemId;
}
shared interface UserDataHandler {
    shared formal void handle(Integer integer0, String string1, Object object2, Node node3, Node node4);
    /*
    shared formal Integer NODE_CLONED;
    shared formal Integer NODE_IMPORTED;
    shared formal Integer NODE_DELETED;
    shared formal Integer NODE_RENAMED;
    shared formal Integer NODE_ADOPTED;
    */
}
shared interface HTMLElement satisfies Element {
    shared formal String id;
    shared formal String title;
    shared formal String lang;
    shared formal String dir;
    shared formal String className;
}
shared interface HTMLAnchorElement satisfies HTMLElement {
    shared formal String accessKey;
    shared formal String charset;
    shared formal String coords;
    shared formal String href;
    shared formal String hreflang;
    shared formal String name;
    shared formal String rel;
    shared formal String rev;
    shared formal String shape;
    shared formal Integer tabIndex;
    shared formal String target;
    shared formal String type;
    shared formal void blur();
    shared formal void focus();
}
shared interface HTMLAppletElement satisfies HTMLElement {
    shared formal String align;
    shared formal String alt;
    shared formal String archive;
    shared formal String code;
    shared formal String codeBase;
    shared formal String height;
    shared formal String hspace;
    shared formal String name;
    shared formal String \iobject;
    shared formal String vspace;
    shared formal String width;
}
shared interface HTMLAreaElement satisfies HTMLElement {
    shared formal String accessKey;
    shared formal String alt;
    shared formal String coords;
    shared formal String href;
    shared formal Boolean noHref;
    shared formal String shape;
    shared formal Integer tabIndex;
    shared formal String target;
}
shared interface HTMLBRElement satisfies HTMLElement {
    shared formal String clear;
}
shared interface HTMLBaseElement satisfies HTMLElement {
    shared formal String href;
    shared formal String target;
}
shared interface HTMLBaseFontElement satisfies HTMLElement {
    shared formal String color;
    shared formal String face;
    shared formal String size;
}
shared interface HTMLBodyElement satisfies HTMLElement {
    shared formal String aLink;
    shared formal String background;
    shared formal String bgColor;
    shared formal String link;
    shared formal String text;
    shared formal String vLink;
}
shared interface HTMLFormElement satisfies HTMLElement {
    shared formal HTMLCollection elements;
    shared formal Integer length;
    shared formal String name;
    shared formal String acceptCharset;
    shared formal String action;
    shared formal String enctype;
    shared formal String method;
    shared formal String target;
    shared formal void submit();
    shared formal void reset();
}
shared interface HTMLButtonElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal String accessKey;
    shared formal Boolean disabled;
    shared formal String name;
    shared formal Integer tabIndex;
    shared formal String type;
    shared formal String \ivalue;
}
shared interface HTMLCollection {
    shared formal Integer length;
    shared formal Node item(Integer integer0);
    shared formal Node namedItem(String string0);
}
shared interface HTMLDListElement satisfies HTMLElement {
    shared formal Boolean compact;
}
shared interface HTMLDocument satisfies Document {
    shared formal String title;
    shared formal String referrer;
    shared formal String domain;
    shared formal String url;
    shared formal HTMLElement body;
    shared formal HTMLCollection images;
    shared formal HTMLCollection applets;
    shared formal HTMLCollection links;
    shared formal HTMLCollection forms;
    shared formal HTMLCollection anchors;
    shared formal String cookie;
    shared formal void open();
    shared formal void close();
    shared formal void write(String string0);
    shared formal void writeln(String string0);
    shared formal NodeList getElementsByName(String string0);
}
shared interface HTMLDOMImplementation satisfies DOMImplementation {
    shared formal HTMLDocument createHTMLDocument(String string0);
}
shared interface HTMLDirectoryElement satisfies HTMLElement {
    shared formal Boolean compact;
}
shared interface HTMLDivElement satisfies HTMLElement {
    shared formal String align;
}
shared interface HTMLFieldSetElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
}
shared interface HTMLFontElement satisfies HTMLElement {
    shared formal String color;
    shared formal String face;
    shared formal String size;
}
shared interface HTMLFrameElement satisfies HTMLElement {
    shared formal String frameBorder;
    shared formal String longDesc;
    shared formal String marginHeight;
    shared formal String marginWidth;
    shared formal String name;
    shared formal Boolean noResize;
    shared formal String scrolling;
    shared formal String src;
    shared formal Document contentDocument;
}
shared interface HTMLFrameSetElement satisfies HTMLElement {
    shared formal String cols;
    shared formal String rows;
}
shared interface HTMLHRElement satisfies HTMLElement {
    shared formal String align;
    shared formal Boolean noShade;
    shared formal String size;
    shared formal String width;
}
shared interface HTMLHeadElement satisfies HTMLElement {
    shared formal String profile;
}
shared interface HTMLHeadingElement satisfies HTMLElement {
    shared formal String align;
}
shared interface HTMLHtmlElement satisfies HTMLElement {
    shared formal String version;
}
shared interface HTMLIFrameElement satisfies HTMLElement {
    shared formal String align;
    shared formal String frameBorder;
    shared formal String height;
    shared formal String longDesc;
    shared formal String marginHeight;
    shared formal String marginWidth;
    shared formal String name;
    shared formal String scrolling;
    shared formal String src;
    shared formal String width;
    shared formal Document contentDocument;
}
shared interface HTMLImageElement satisfies HTMLElement {
    shared formal String lowSrc;
    shared formal String name;
    shared formal String align;
    shared formal String alt;
    shared formal String border;
    shared formal String height;
    shared formal String hspace;
    shared formal Boolean isMap;
    shared formal String longDesc;
    shared formal String src;
    shared formal String useMap;
    shared formal String vspace;
    shared formal String width;
}
shared interface HTMLInputElement satisfies HTMLElement {
    shared formal String defaultValue;
    shared formal Boolean defaultChecked;
    shared formal HTMLFormElement form;
    shared formal String accept;
    shared formal String accessKey;
    shared formal String align;
    shared formal String alt;
    shared formal Boolean checked;
    shared formal Boolean disabled;
    shared formal Integer maxLength;
    shared formal String name;
    shared formal Boolean readOnly;
    shared formal String size;
    shared formal String src;
    shared formal Integer tabIndex;
    shared formal String type;
    shared formal String useMap;
    shared formal String \ivalue;
    shared formal void blur();
    shared formal void focus();
    shared formal void select();
    shared formal void click();
}
shared interface HTMLIsIndexElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal String prompt;
}
shared interface HTMLLIElement satisfies HTMLElement {
    shared formal String type;
    shared formal Integer \ivalue;
}
shared interface HTMLLabelElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal String accessKey;
    shared formal String htmlFor;
}
shared interface HTMLLegendElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal String accessKey;
    shared formal String align;
}
shared interface HTMLLinkElement satisfies HTMLElement {
    shared formal Boolean disabled;
    shared formal String charset;
    shared formal String href;
    shared formal String hreflang;
    shared formal String media;
    shared formal String rel;
    shared formal String rev;
    shared formal String target;
    shared formal String type;
}
shared interface HTMLMapElement satisfies HTMLElement {
    shared formal HTMLCollection areas;
    shared formal String name;
}
shared interface HTMLMenuElement satisfies HTMLElement {
    shared formal Boolean compact;
}
shared interface HTMLMetaElement satisfies HTMLElement {
    shared formal String content;
    shared formal String httpEquiv;
    shared formal String name;
    shared formal String scheme;
}
shared interface HTMLModElement satisfies HTMLElement {
    shared formal String cite;
    shared formal String dateTime;
}
shared interface HTMLOListElement satisfies HTMLElement {
    shared formal Boolean compact;
    shared formal Integer start;
    shared formal String type;
}
shared interface HTMLObjectElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal String code;
    shared formal String align;
    shared formal String archive;
    shared formal String border;
    shared formal String codeBase;
    shared formal String codeType;
    shared formal String data;
    shared formal Boolean declare;
    shared formal String height;
    shared formal String hspace;
    shared formal String name;
    shared formal String standby;
    shared formal Integer tabIndex;
    shared formal String type;
    shared formal String useMap;
    shared formal String vspace;
    shared formal String width;
    shared formal Document contentDocument;
}
shared interface HTMLOptGroupElement satisfies HTMLElement {
    shared formal Boolean disabled;
    shared formal String label;
}
shared interface HTMLOptionElement satisfies HTMLElement {
    shared formal HTMLFormElement form;
    shared formal Boolean defaultSelected;
    shared formal String text;
    shared formal Integer index;
    shared formal Boolean disabled;
    shared formal String label;
    shared formal Boolean selected;
    shared formal String \ivalue;
}
shared interface HTMLParagraphElement satisfies HTMLElement {
    shared formal String align;
}
shared interface HTMLParamElement satisfies HTMLElement {
    shared formal String name;
    shared formal String type;
    shared formal String \ivalue;
    shared formal String valueType;
}
shared interface HTMLPreElement satisfies HTMLElement {
    shared formal Integer width;
}
shared interface HTMLQuoteElement satisfies HTMLElement {
    shared formal String cite;
}
shared interface HTMLScriptElement satisfies HTMLElement {
    shared formal String text;
    shared formal String htmlFor;
    shared formal String event;
    shared formal String charset;
    shared formal Boolean defer;
    shared formal String src;
    shared formal String type;
}
shared interface HTMLSelectElement satisfies HTMLElement {
    shared formal String type;
    shared formal Integer selectedIndex;
    shared formal String \ivalue;
    shared formal Integer length;
    shared formal HTMLFormElement form;
    shared formal HTMLCollection options;
    shared formal Boolean disabled;
    shared formal Boolean multiple;
    shared formal String name;
    shared formal Integer size;
    shared formal Integer tabIndex;
    shared formal void add(HTMLElement hTMLElement0, HTMLElement hTMLElement1);
    shared formal void remove(Integer integer0);
    shared formal void blur();
    shared formal void focus();
}
shared interface HTMLStyleElement satisfies HTMLElement {
    shared formal Boolean disabled;
    shared formal String media;
    shared formal String type;
}
shared interface HTMLTableCaptionElement satisfies HTMLElement {
    shared formal String align;
}
shared interface HTMLTableCellElement satisfies HTMLElement {
    shared formal Integer cellIndex;
    shared formal String abbr;
    shared formal String align;
    shared formal String axis;
    shared formal String bgColor;
    shared formal String ch;
    shared formal String chOff;
    shared formal Integer colSpan;
    shared formal String headers;
    shared formal String height;
    shared formal Boolean noWrap;
    shared formal Integer rowSpan;
    shared formal String scope;
    shared formal String vAlign;
    shared formal String width;
}
shared interface HTMLTableColElement satisfies HTMLElement {
    shared formal String align;
    shared formal String ch;
    shared formal String chOff;
    shared formal Integer span;
    shared formal String vAlign;
    shared formal String width;
}
shared interface HTMLTableSectionElement satisfies HTMLElement {
    shared formal String align;
    shared formal String ch;
    shared formal String chOff;
    shared formal String vAlign;
    shared formal HTMLCollection rows;
    shared formal HTMLElement insertRow(Integer integer0);
    shared formal void deleteRow(Integer integer0);
}
shared interface HTMLTableElement satisfies HTMLElement {
    shared formal HTMLTableCaptionElement caption;
    shared formal HTMLTableSectionElement tHead;
    shared formal HTMLTableSectionElement tFoot;
    shared formal HTMLCollection rows;
    shared formal HTMLCollection tBodies;
    shared formal String align;
    shared formal String bgColor;
    shared formal String border;
    shared formal String cellPadding;
    shared formal String cellSpacing;
    shared formal String frame;
    shared formal String rules;
    shared formal String summary;
    shared formal String width;
    shared formal HTMLElement createTHead();
    shared formal void deleteTHead();
    shared formal HTMLElement createTFoot();
    shared formal void deleteTFoot();
    shared formal HTMLElement createCaption();
    shared formal void deleteCaption();
    shared formal HTMLElement insertRow(Integer integer0);
    shared formal void deleteRow(Integer integer0);
}
shared interface HTMLTableRowElement satisfies HTMLElement {
    shared formal Integer rowIndex;
    shared formal Integer sectionRowIndex;
    shared formal HTMLCollection cells;
    shared formal String align;
    shared formal String bgColor;
    shared formal String ch;
    shared formal String chOff;
    shared formal String vAlign;
    shared formal HTMLElement insertCell(Integer integer0);
    shared formal void deleteCell(Integer integer0);
}
shared interface HTMLTextAreaElement satisfies HTMLElement {
    shared formal String defaultValue;
    shared formal HTMLFormElement form;
    shared formal String accessKey;
    shared formal Integer cols;
    shared formal Boolean disabled;
    shared formal String name;
    shared formal Boolean readOnly;
    shared formal Integer rows;
    shared formal Integer tabIndex;
    shared formal String type;
    shared formal String \ivalue;
    shared formal void blur();
    shared formal void focus();
    shared formal void select();
}
shared interface HTMLTitleElement satisfies HTMLElement {
    shared formal String text;
}
shared interface HTMLUListElement satisfies HTMLElement {
    shared formal Boolean compact;
    shared formal String type;
}
