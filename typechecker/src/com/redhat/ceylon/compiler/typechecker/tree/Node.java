package com.redhat.ceylon.compiler.typechecker.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private Token token;
    private Scope scope;
    private Unit unit;
    private List<Message> errors = new ArrayList<Message>();
    private List<Node> children = new ArrayList<Node>();
    private Node parent;
    
    protected Node(Token token) {
        this.token = token;
        if (token==null) {
            text = "";
        }
        else {
            text = token.getText();
        }
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
    	Token ct = getFirstChildToken();
    	if (ct!=null) return ct;
    	Token pt = getParentToken();
    	if (pt!=null) return pt;
    	return null;
    }
    
    private Token getParentToken() {
    	if (token!=null) {
    		return token;
    	}
    	else if (parent!=null) {
    		return parent.getParentToken();
    	}
    	else {
    		return null;
    	}
    }
    
    private Token getFirstChildToken() {
    	if (token!=null) {
    		return token;
    	}
    	else {
    		Token token=null;
    		for (Node child: children) {
    			Token tok = child.getFirstChildToken();
    			if (tok!=null && ( token==null || 
    					tok.getTokenIndex()<token.getTokenIndex() )) {
    				token=tok;
    			}
    		}
			return token;
    	}
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
	
	public void connect(Node child) {
		if (child!=null) {
			children.add(child);
			child.parent=this;
		}
	}

}
