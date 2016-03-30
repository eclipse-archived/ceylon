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
package com.redhat.ceylon.common.tools.help;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.ast.Header;
import org.tautua.markdownpapers.ast.Node;
import org.tautua.markdownpapers.ast.ResourceDefinition;
import org.tautua.markdownpapers.ast.SimpleNode;
import org.tautua.markdownpapers.ast.Text;
import org.tautua.markdownpapers.parser.ParseException;
import org.tautua.markdownpapers.parser.Parser;

public class Markdown {

    /** Parses the given string as Markdown */
    public static Document markdown(String markdown) {
        Parser parser = new Parser(new StringReader(markdown.replaceAll("\\s+$", "")));
        try {
            return parser.parse();
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }
    
    /** Constructs a Header */
    static Header header(int level, String text) {
        Header header = new Header(Parser.JJTHEADER);
        header.setLevel(level);
        Text t = new Text(Parser.JJTTEXT);
        t.jjtSetValue(text);
        header.jjtAddChild(t, 0);
        return header;
    }
    
    static Node getFirstSibling(Node node) {
        return node.jjtGetParent().jjtGetChild(0);
    }
    
    static Node getLastSibling(Node node) {
        Node parent = node.jjtGetParent();
        return parent.jjtGetChild(parent.jjtGetNumChildren() - 1);
    }
    
    static int getIndexInParent(Node node) {
        Node parent = node.jjtGetParent();
        for (int ii = 0; ii < parent.jjtGetNumChildren(); ii++) {
            if (parent.jjtGetChild(ii) == node) {
                return ii;
            }
        }
        throw new IllegalStateException();   
    }
    
    static Node getPrev(Node node) {
        Node parent = node.jjtGetParent();
        int ii = getIndexInParent(node);
        return ii > 0 ? parent.jjtGetChild(ii - 1) : null;
    }
    
    static Node getNext(Node node) {
        Node parent = node.jjtGetParent();
        int ii = getIndexInParent(node);
        return ii < parent.jjtGetNumChildren() - 1 ? parent.jjtGetChild(ii + 1) : null;
    }

    private static class SectionsMarkdownVisitor extends AbstractMarkdownVisitor {
        
        private Map<Integer, List<Header>> sections = new HashMap<Integer, List<Header>>();
        
        private List<ResourceDefinition> resourceDefinitions = new ArrayList<>();
    
        @Override
        public void visit(Header arg0) {
            List<Header> headers = sections.get(arg0.getLevel());
            if (headers == null) {
                headers = new ArrayList<>();
                sections.put(arg0.getLevel(), headers);
            }
            headers.add(arg0);
        }

        @Override
        public void visit(ResourceDefinition node) {
            resourceDefinitions.add(node); 
        }
        
        @Override
        public void visit(SimpleNode that) {
            if (sections.isEmpty()) {
                if (!(that instanceof Header
                        || that instanceof ResourceDefinition)) {
                    // TODO Issue a warning
                }
            }
            super.visit(that);
        }
    }
    
    public static class Section {
        private Header heading;
        private Document doc;
        public Section(Header heading, Document doc) {
            super();
            this.heading = heading;
            this.doc = doc;
        }
        public Header getHeading() {
            return heading;
        }
        public Document getDoc() {
            return doc;
        }
        
    }
    
    /**
     * Nasty method for extracting doc sections from a markdown document.
     * <ol>
     * <li>The most prominent heading(s) are identified
     * <li>The document is split into a list of sections corresponding to the 
     * tree following headings of that level. If the document didn't begin 
     * with a heading of that level, the first Section in the result list will 
     * have a null heading. 
     * </ol>
     * @param document
     * @return
     */
    public static List<Section> extractSections(Document document) {
        SectionsMarkdownVisitor v = new SectionsMarkdownVisitor();
        document.accept(v);
        List<Section> result = new ArrayList<>();
        for (int levelIndex = 1; levelIndex <= 6; levelIndex++) {
            List<Header> sections = v.sections.get(levelIndex);
            if (sections != null) {
                Node parent;
                {
                    Header header = sections.get(0);
                    parent = header.jjtGetParent();
                    Document doc = extractBetween(v, parent, null, header);
                    result.add(new Section(null, doc));
                }
                for (int sectionIndex = 0; sectionIndex < sections.size(); sectionIndex++) {
                    Header header = sections.get(sectionIndex);
                    Header next = sectionIndex <  sections.size() - 1 ? sections.get(sectionIndex+1) : null;
                    Document doc = extractBetween(v, parent, header, next);
                    result.add(new Section(header, doc));
                }
                
                break;
            }
        }
        return result;
    }

    private static Document extractBetween(SectionsMarkdownVisitor v, Node parent, Header header1,
            Header header2) {
        /*Node parent1 = header1 != null ? header1.jjtGetParent() : null;
        Node parent2 = header2 != null ? header2.jjtGetParent() : null;
        /*if (parent1 != null
                && parent2 != null
                && parent1 != parent2) {
            throw new RuntimeException();
        }* /
        Node parent = parent1 != null ? parent1 : parent2;*/
        
        Document doc = new Document(Parser.JJTDOCUMENT);    
        final int numChildren = parent.jjtGetNumChildren();
        final int start = header1 != null ? Markdown.getIndexInParent(header1) + 1: 0;
        final int end = header2 != null ? Markdown.getIndexInParent(header2) -1 : numChildren - 1;
        for (int nodeIndex = start; nodeIndex <= end; nodeIndex++) {
            Node child = parent.jjtGetChild(nodeIndex);
            child.jjtSetParent(doc);
            doc.jjtAddChild(child, nodeIndex-start);
        }
        // Add a copy of all the resource definitions
        // to each document
        for (ResourceDefinition rd : v.resourceDefinitions) {
            ResourceDefinition copy = copy(rd);
            copy.jjtSetParent(doc);
            doc.jjtAddChild(copy, doc.jjtGetNumChildren());
        }
        return doc;
    }
    
    private static ResourceDefinition copy(ResourceDefinition rd) {
        ResourceDefinition copy = new ResourceDefinition(Parser.JJTRESOURCEDEFINITION);
        copy.setId(rd.getId());
        copy.setResource(rd.getResource());
        return copy;
    }
    
    /** Adjusts the heading levels in the document */
    public static void adjustHeadings(Document document, final int increment) {
        // adjust heading levels to h2 max
       document.accept(new AbstractMarkdownVisitor() {
           @Override
           public void visit(Header header) {
               header.setLevel(header.getLevel()+increment);
           }
       });
   }
    
}
