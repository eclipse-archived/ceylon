package com.redhat.ceylon.tools.help;

import java.util.TreeMap;

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
import org.tautua.markdownpapers.ast.Text;
import org.tautua.markdownpapers.ast.Visitor;

import com.redhat.ceylon.common.tool.WordWrap;

public class PlaintextMarkdownVisitor implements Visitor {

    private WordWrap out;
    private TreeMap<String, Resource> refLinks;

    public PlaintextMarkdownVisitor(WordWrap out) {
        this.out = out;
    }

    @Override
    public void visit(Document node) {
        PreVisitor preVisitor = new PreVisitor();
        node.accept(preVisitor);
        this.refLinks = preVisitor.refLinks;
        node.childrenAccept(this);
    }
    
    @Override
    public void visit(Comment node) {
    }
    
    @Override
    public void visit(Header node) {
        final Node parent = node.jjtGetParent();
        for (int ii = 0; ii < parent.jjtGetNumChildren(); ii++) {
            if (parent.jjtGetChild(ii) == node) {
                if (ii == 0) {
                    continue;
                }
                Node prev = parent.jjtGetChild(ii-1);
                if (prev instanceof Paragraph
                        || prev instanceof List) {
                    out.newline();
                }
            }
        }
        switch (node.getLevel()) {
        case 1:
            int col = out.getColumn();
            node.childrenAccept(this);
            int num = out.getColumn() - col;
            out.newline();
            for (int ii = 0; ii < num; ii++) {
                out.append("=");
            }
            out.newline();
            break;
        case 2:
            col = out.getColumn();
            out.append("#### ");
            node.childrenAccept(this);
            out.append("####");
            num = out.getColumn() - col;
            out.newline();
            for (int ii = 0; ii < num; ii++) {
                out.append("-");
            }
            out.newline();
            break;
        case 3:
            out.append("#### ");
            node.childrenAccept(this);
            out.append("####").newline();
            break;
        case 4:
            out.append("### ");
            node.childrenAccept(this);
            out.append("###").newline();
            break;
        case 5:
            out.append("## ");
            node.childrenAccept(this);
            out.append("##").newline();
            break;
        case 6:
            out.append("# ");
            node.childrenAccept(this);
            out.append("#").newline();
            break;
        }
        out.newline();
    }

    @Override
    public void visit(List node) {
        node.childrenAccept(this);
    }
    
    @Override
    public void visit(Item node) {
        int rest = out.getIndentRestLines();
        if (((List)node.jjtGetParent()).isOrdered()) {
            for (int ii = 0; ii < node.jjtGetParent().jjtGetNumChildren(); ii++) {
                if (node.jjtGetParent().jjtGetChild(ii) == node) {
                    out.append((ii+1)+". ");
                    out.setIndentRestLines(rest + 3);
                    break;
                }
            }
        } else {
            out.append("* ");
            out.setIndentRestLines(rest + 2);
        }
        node.childrenAccept(this);
        out.setIndentRestLines(rest);
        out.newline().newline();
    }

    @Override
    public void visit(Paragraph node) {
        node.childrenAccept(this);
        if (!(node.jjtGetParent() instanceof Item)) {
            out.newline().newline();
        }
    }

    @Override
    public void visit(Quote node) {
        out.setPrefix("> ");
        node.childrenAccept(this);
        out.setPrefix(null);
    }

    
    @Override
    public void visit(CodeText node) {
        int first = out.getIndentFirstLine();
        int rest = out.getIndentRestLines();
        out.setIndent(rest+4);
        out.append(node.getValue());
        out.setIndentFirstLine(first);
        out.setIndentRestLines(rest);
        out.newline().newline();
    }
    
    @Override
    public void visit(Code node) {
        out.setIndent(4);
        node.childrenAccept(this);
        out.setIndent(0);
    }
    
    @Override
    public void visit(Ruler node) {
        out.newline();
        out.column(out.getWidth()/2-2);
        out.append("* * *").newline().newline();
    }
    
    @Override
    public void visit(Tag node) {
        out.append("<").append(node.getName()).append(">");
        node.childrenAccept(this);
        out.append("</").append(node.getName()).append(">");
    }
    
    @Override
    public void visit(CharRef node) {
        out.append(node.getValue());
    }

    @Override
    public void visit(CodeSpan node) {
        out.append("`").append(node.getText()).append("`");
    }

    @Override
    public void visit(Emphasis node) {
        switch (node.getType()) {
        case ITALIC_AND_BOLD:
        case BOLD:
            out.append(node.getText().toUpperCase());
            break;
        case ITALIC:
            out.append(node.getText());
        }
    }

    @Override
    public void visit(Image node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Line node) {
        node.childrenAccept(this);
        if (!node.isEmpty() && !node.isEnding()) {
            out.append(" ");
        }
    }

    @Override
    public void visit(LineBreak node) {
        out.append(" ");
        node.childrenAccept(this);
    }

    @Override
    public void visit(Link node) {
        Resource resource = node.getResource();
        if (resource == null) {
            resource = refLinks.get(node.getReference());
        }
        if (resource != null) {
            out.append(node.getText()).append(" (").append(resource.getLocation()).append(")");
        } else {
            out.append(node.getText());
        }
    }

    @Override
    public void visit(InlineUrl node) {
        out.append(node.getUrl());
    }

    @Override
    public void visit(ResourceDefinition node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(SimpleNode node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(final Text node) {
        if (node.isWhitespace()) {
            final Node parent = node.jjtGetParent();
            Node child = node;
            Node ancestor = parent;
            while (ancestor != null) {
                if (ancestor instanceof Header
                        && parent.jjtGetChild(0) == node) {
                    // filter whitespace at the start of headers
                    return;
                }
                child = ancestor;
                ancestor = ancestor.jjtGetParent();
            }
        }
        out.append(node.getValue());
    }

}


class PreVisitor implements Visitor {

    TreeMap<String, Resource> refLinks = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    
    PreVisitor() {
    }
    
    @Override
    public void visit(CharRef node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Code node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(CodeSpan node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(CodeText node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Comment node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Document node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Emphasis node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Header node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Image node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Line node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(LineBreak node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Link node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(List node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(InlineUrl node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Item node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Paragraph node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Quote node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(ResourceDefinition node) {
        String id = node.getId();
        refLinks.put(id, node.getResource());    
    }

    @Override
    public void visit(Ruler node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(SimpleNode node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Tag node) {
        node.childrenAccept(this);
    }

    @Override
    public void visit(Text node) {
        node.childrenAccept(this);
    }

}
