package com.redhat.ceylon.compiler.typechecker.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private final Token token;
    private Scope scope;
    private Unit unit;
    private List<Message> errors = new ArrayList<Message>();
    
    protected Node(Token token) {
        this.token = token;
        if (token==null) {
            text = "";
        }
        else {
            text = token.getText();
        }
        //correctLineNumber(antlrTreeNode);
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
    public Token getToken() {
        return token;
    }
    
    
    /**
     * The compilation errors belonging to this node.
     */
    public List<Message> getErrors() {
        return errors;
    }
    
    public void addError(String message) {
        errors.add( new AnalysisError(this, message) );
    }
    
    public void addUnexpectedError(String message) {
        errors.add( new UnexpectedError(this, message) );
    }
    
    public void addWarning(String message) {
        errors.add( new AnalysisWarning(this, message) );
    }
    
    public void addParseError(ParseError error) {
        errors.add(error);
    }
    
    public void addLexError(LexError error) {
        errors.add(error);
    }
    
    public abstract void visit(Visitor visitor);
    
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
            Token fc = getFirstChildToken(node);
            Token lc = getLastChildToken(node);
            Token pc = getParentToken(node);
            if (fc!=null && lc!=null) {
            	node.setTokenStartIndex(fc.getTokenIndex());
            	node.setTokenStopIndex(lc.getTokenIndex());
            }
            else if (pc!=null) {
            	node.setTokenStartIndex(pc.getTokenIndex());
            	node.setTokenStopIndex(pc.getTokenIndex());
            }
            if (fc!=null) {
                copyLocation(fc, token);
            }
            else if (pc!=null) {
                copyLocation(pc, token);
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

    private static Token getLastChildToken(CommonTree node) {
        @SuppressWarnings("rawtypes") 
        List children = node.getChildren();
        if (children!=null) {
            for (int i=children.size()-1; i>=0; i--) {
            	Object child = children.get(i); 
                if (child instanceof CommonTree) {
                    Token ct = ((CommonTree) child).getToken();
                    if (ct!=null && hasLocation(ct)) {
                        return ct;
                    }
                    else {
                        Token st = getLastChildToken((CommonTree) child);
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
    
    public void handleException(Exception e, Visitor visitor) {
	    addError(getMessage(e, visitor));
    }

    public String getMessage(Exception e, Visitor visitor) {
		return visitor.getClass().getSimpleName() +
	            " caused an exception visiting " + getNodeType() + 
	            " node: " + e + " at " + getLocationInfo(e);
	}

	private String getLocationInfo(Exception e) {
		return e.getStackTrace().length>0 ? 
				e.getStackTrace()[0].toString() : "unknown";
	}

}
