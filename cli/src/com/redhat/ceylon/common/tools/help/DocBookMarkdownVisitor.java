/*
 * This class is heavily based on org.tautua.markdownpapers.HtmlEmitter
 * which comes with the following header:  
 */

/*
 * Copyright 2011, TAUTUA
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
package com.redhat.ceylon.common.tools.help;

import static org.tautua.markdownpapers.util.Utils.EOL;
import static org.tautua.markdownpapers.util.Utils.SPACE;
import static org.tautua.markdownpapers.util.Utils.escape;

import java.io.IOException;

import org.tautua.markdownpapers.ast.CharRef;
import org.tautua.markdownpapers.ast.Code;
import org.tautua.markdownpapers.ast.CodeSpan;
import org.tautua.markdownpapers.ast.CodeText;
import org.tautua.markdownpapers.ast.Comment;
import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.ast.Emphasis;
import org.tautua.markdownpapers.ast.Header;
import org.tautua.markdownpapers.ast.Image;
import org.tautua.markdownpapers.ast.InlineUrl;
import org.tautua.markdownpapers.ast.Item;
import org.tautua.markdownpapers.ast.Line;
import org.tautua.markdownpapers.ast.LineBreak;
import org.tautua.markdownpapers.ast.Link;
import org.tautua.markdownpapers.ast.List;
import org.tautua.markdownpapers.ast.Node;
import org.tautua.markdownpapers.ast.Paragraph;
import org.tautua.markdownpapers.ast.Quote;
import org.tautua.markdownpapers.ast.Resource;
import org.tautua.markdownpapers.ast.ResourceDefinition;
import org.tautua.markdownpapers.ast.Ruler;
import org.tautua.markdownpapers.ast.SimpleNode;
import org.tautua.markdownpapers.ast.Tag;
import org.tautua.markdownpapers.ast.TagAttribute;
import org.tautua.markdownpapers.ast.Text;
import org.tautua.markdownpapers.ast.Visitor;

public class DocBookMarkdownVisitor implements Visitor {
    private Appendable buffer;

    public DocBookMarkdownVisitor(Appendable buffer) {
        this.buffer = buffer;
    }

    public void visit(CharRef node) {
        append(node.getValue());
    }

    public void visit(Code node) {
        append("<programlisting>");
        visitChildrenAndAppendSeparator(node, EOL);
        append("</programlisting>");
        append(EOL);
    }

    public void visit(CodeSpan node) {
        append("<literal>");
        escapeAndAppend(node.getText());
        append("</literal>");
    }

    public void visit(CodeText node) {
        escapeAndAppend(node.getValue());
    }

    public void visit(Comment node) {
        append("<!--");
        append(node.getText());
        append("-->");
    }

    public void visit(Document node) {
        visitChildrenAndAppendSeparator(node, EOL);
    }

    public void visit(Emphasis node) {
        switch (node.getType()) {
            case ITALIC:
                append("<emphasis>");
                append(node.getText());
                append("</emphasis>");
                break;
            case BOLD:
                append("<emphasis role='bold'>");
                append(node.getText());
                append("</emphasis>");
                break;
            case ITALIC_AND_BOLD:
                append("<emphasis><emphasis role='bold'>");
                append(node.getText());
                append("</emphasis></emphasis>");
                break;
        }
    }

    public void visit(Header node) {
        append("<title>");
        node.childrenAccept(this);
        append("</title>");
        append(EOL);
    }

    public void visit(Image node) {
        Resource resource = node.getResource();
        
        append("<informalfigure>");
        append("<mediaobject>");
        if (node.getText() != null) {
            append("<caption>");
            escapeAndAppend(node.getText());
            append("</caption>");
        }
        if (resource != null) {
            append("<imageobject");
            append(" fileref=\"");
            escapeAndAppend(resource.getLocation());
            append("\"/>");
        }
        if (resource != null) {
            append("<textobject>");
            append("<phrase>");
            escapeAndAppend(resource.getHint());
            append("</phrase>");
            append("</textobject>");
        }
        append("<caption>");
        escapeAndAppend(node.getText());
        append("</caption>");
        append("</mediaobject>");
        append("</informalfigure>");
    }

    public void visit(InlineUrl node) {
        append("<a href=\"");
        escapeAndAppend(node.getUrl());
        append("\">");
        escapeAndAppend(node.getUrl());
        append("</a>");
    }

    public void visit(Item node) {
        append("<listitem><para>");// XXX <para> is only correct when the nod contains text, not if it contains a nested list
        node.childrenAccept(this);
        append("</para></listitem>");
        append(EOL);
    }

    public void visit(Line node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(LineBreak node) {
        Line l = (Line) node.jjtGetParent();
        if(!l.isEnding()) {
            append("<br/>");
        }
    }

    public void visit(Link node) {
        Resource resource = node.getResource();
        if (resource == null) {
            if (node.isReferenced()) {
                append("[");
                node.childrenAccept(this);
                append("]");
                if (node.getReference() != null) {
                    if (node.hasWhitespaceAtMiddle()) {
                        append(' ');
                    }
                    append("[");
                    append(node.getReference());
                    append("]");
                }
            } else {
                append("<a href=\"\">");
                node.childrenAccept(this);
                append("</a>");
            }
        } else {
            append("<a");
            append(" href=\"");
            escapeAndAppend(resource.getLocation());
            if (resource.getHint() != null) {
                append("\" title=\"");
                escapeAndAppend(resource.getHint());
            }
            append("\">");
            node.childrenAccept(this);
            append("</a>");
        }
    }

    public void visit(ResourceDefinition node) {
        // do nothing
    }

    public void visit(List node) {
        if (node.isOrdered()) {
            append("<orderedlist>");
            append(EOL);
            node.childrenAccept(this);
            append("</orderedlist>");
        } else {
            append("<itemizedlist>");
            append(EOL);
            node.childrenAccept(this);
            append("</itemizedlist>");
        }
        append(EOL);
    }

    public void visit(Paragraph node) {
        Node parent = node.jjtGetParent();
        if(parent instanceof Item) {
            if (!((Item)parent).isLoose()) {
                visitChildrenAndAppendSeparator(node, EOL);
                return;
            }
        }
        append("<para>");
        visitChildrenAndAppendSeparator(node, EOL);
        append("</para>");
        append(EOL);
    }

    public void visit(Ruler node) {
        append("<hr/>");
        append(EOL);
    }

    public void visit(Quote node) {
        append("<blockquote>");
        append(EOL);
        node.childrenAccept(this);
        append("</blockquote>");
        append(EOL);
    }

    public void visit(SimpleNode node) {
        throw new IllegalArgumentException("can not process this element");
    }

    public void visit(Tag node) {
        append("<");
        append(node.getName());
        for (TagAttribute attribute : node.getAttributes()) {
            append(SPACE);
            append(attribute.getName());
            append("=\"");
            append(attribute.getValue());
            append("\"");
        }

        if(node.jjtGetNumChildren() == 0) {
            append("/>");
        } else {
            append(">");
            node.childrenAccept(this);
            append("</");
            append(node.getName());
            append(">");
        }
    }

    public void visit(Text node) {
        if(node.jjtGetParent() instanceof Tag) {
            append(node.getValue());
        } else {
            escapeAndAppend(node.getValue());
        }
    }

    void visitChildrenAndAppendSeparator(Node node, char separator){
        int count = node.jjtGetNumChildren();
        for(int i = 0; i < count; i++) {
            node.jjtGetChild(i).accept(this);
            if(i < count - 1) {
                append(separator);
            }
        }
    }

    void escapeAndAppend(String val) {
        for(char character : val.toCharArray()) {
            append(escape(character));
        }
    }

    void append(String val) {
        try {
            buffer.append(val);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void append(char val) {
        try {
            buffer.append(val);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
