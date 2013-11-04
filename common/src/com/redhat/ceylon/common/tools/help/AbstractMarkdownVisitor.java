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
