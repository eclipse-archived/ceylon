package com.redhat.ceylon.compiler.typechecker.analyzer;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class LineNumberVisitor extends Visitor {
    
    @Override
    public void visitAny(Node that) {
        Token t = that.getAntlrTreeNode().getToken();
        if (t.getLine()==0 && 
                t.getCharPositionInLine()==-1) {
            org.antlr.runtime.tree.Tree tr = that.getAntlrTreeNode().getParent();
            if (tr!=null && tr instanceof CommonTree) {
                Token o = ((CommonTree) tr).getToken();
                t.setLine(o.getLine());
                t.setCharPositionInLine(o.getCharPositionInLine());
            }
        }
        super.visitAny(that);
    }

}
