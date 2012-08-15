package com.redhat.ceylon.tools.help;

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
import org.tautua.markdownpapers.ast.Paragraph;
import org.tautua.markdownpapers.ast.Quote;
import org.tautua.markdownpapers.ast.ResourceDefinition;
import org.tautua.markdownpapers.ast.Ruler;
import org.tautua.markdownpapers.ast.SimpleNode;
import org.tautua.markdownpapers.ast.Tag;
import org.tautua.markdownpapers.ast.Text;
import org.tautua.markdownpapers.ast.Visitor;

class AbstractMarkdownVisitor implements Visitor {

    @Override
    public void visit(SimpleNode that) {
        that.childrenAccept(this);
    }
    
    @Override
    public void visit(CharRef that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Code that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(CodeSpan that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(CodeText that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Comment that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Document that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Emphasis that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Header that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Image that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Line that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(LineBreak that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Link that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(List that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(InlineUrl that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Item that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Paragraph that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Quote that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(ResourceDefinition that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Ruler that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Tag that) {
        visit((SimpleNode)that);
    }

    @Override
    public void visit(Text that) {
        visit((SimpleNode)that);
    }

}
