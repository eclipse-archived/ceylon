package com.redhat.ceylon.compiler.typechecker.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisMessage;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private final CommonTree antlrTreeNode;
    private Scope scope;
    private Unit unit;
    private List<AnalysisMessage> errors = new ArrayList<AnalysisMessage>();
    
    protected Node(CommonTree antlrTreeNode) {
        this.antlrTreeNode = antlrTreeNode; 
        text = antlrTreeNode.getText();
        correctLineNumber(antlrTreeNode);
    }
    
    /**
     * The scope within which the node occurs. 
     */
    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
    
    /**
     * The compilation unit in which the node
     * occurs.
     */
    public Unit getUnit() {
        return unit;
    }
    
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    /**
     * The text of the corresponding ANTLR node.
     */
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * The text of the corresponding ANTLR tree node. Never null, 
     * since the two trees are isomorphic.
     */
    public CommonTree getAntlrTreeNode() {
        return antlrTreeNode;
    }
    
    
    /**
     * The compilation errors belonging to this node.
     */
    public List<AnalysisMessage> getErrors() {
        return errors;
    }
    
    public void addError(String message) {
        errors.add( new AnalysisError(this, message) );
    }
    
    public void addWarning(String message) {
        errors.add( new AnalysisWarning(this, message) );
    }
    
    public abstract void visitChildren(Visitor visitor);
    
    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        PrintVisitor pv = new PrintVisitor(w);
        pv.visitAny(this);
        return w.toString();
        //return getClass().getSimpleName() + "(" + text + ")"; 
    }

    public String getNodeType() {
        return getClass().getSimpleName();
    }
    
    public static void correctLineNumber(CommonTree node) {
        Token token = node.getToken();
        if (token!=null && !hasLocation(token)) {
            Token t = getFirstChildToken(node);
            if (t==null) {
                t = getParentToken(node);
            }
            if (t!=null) {
                copyLocation(t, token);
            }
        }
    }

    private static Token getFirstChildToken(CommonTree node) {
        @SuppressWarnings("rawtypes") 
        List children = node.getChildren();
        if (children!=null) {
            for (Object child: children) {
                if (child instanceof CommonTree) {
                    Token ct = ((CommonTree) child).getToken();
                    if (ct!=null && hasLocation(ct)) {
                        return ct;
                    }
                    else {
                        Token st = getFirstChildToken((CommonTree) child);
                        if (st!=null) return st;
                    }
                }
            }
        }
        return null;
    }

    private static Token getParentToken(CommonTree node) {
        org.antlr.runtime.tree.Tree parent = node.getParent();
        if (parent!=null && parent instanceof CommonTree) {
            Token pt = ((CommonTree) parent).getToken();
            if (pt!=null && hasLocation(pt)) {
                return pt;
            }
        }
        return null;
    }

    private static void copyLocation(Token from, Token to) {
        to.setLine(from.getLine());
        to.setCharPositionInLine(from.getCharPositionInLine());
    }

    private static boolean hasLocation(Token token) {
        return token.getLine()!=0 || 
                token.getCharPositionInLine()!=-1;
    }

}
