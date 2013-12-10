package com.redhat.ceylon.compiler.typechecker.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UnsupportedError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private Token token;
    private Token endToken;
    private Scope scope;
    private Unit unit;
    private List<Message> errors = null;
    private List<Node> children = new ArrayList<Node>(4);
    
    protected Node(Token token) {
        this.token = token;
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
    	if (text!=null) {
    		return text;
    	}
    	else if (token==null) {
    		return "";
    	}
    	else if (endToken==null) {
    		return token.getText();
    	}
    	else {
    		return token.getText() + endToken.getText();
    	}
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * The text of the corresponding ANTLR tree node. Never null, 
     * since the two trees are isomorphic.
     */
    public Token getToken() {
    	return getFirstChildToken();
    }
    
    public Token getMainToken() {
        return token;
    }
    
    public Token getMainEndToken() {
        return endToken;
    }
    
    public String getLocation() {
    	Token token = getToken();
    	Token endToken = getEndToken();
		if (token==null) {
    		return "unknown location";
    	}
    	else if (endToken==null) {
    		return toLocation(token);
    	}
    	else {
    		return toLocation(token) + "-" + 
    				toEndLocation(endToken);
    	}
    }
    
    public Integer getStartIndex() {
    	Token token = getToken();
    	if (token==null) {
    		return null;
    	}
    	else {
    		return ((CommonToken) token).getStartIndex();
    	}
    }

    public Integer getStopIndex() {
    	Token token = getEndToken();
    	if (token==null) {
    		token = getToken();
    	}
    	if (token==null) {
    		return null;
    	}
    	else {
    		return ((CommonToken) token).getStopIndex();
    	}
    }

	private static String toLocation(Token token) {
		return token.getLine() + ":" + 
				token.getCharPositionInLine();
	}
    
	private static String toEndLocation(Token token) {
		return token.getLine() + ":" + 
				(token.getCharPositionInLine()
				+ token.getText().length()-1);
	}
    
    private boolean isMissingToken(Token t) {
        return t.getText().startsWith("<missing ");
    }
    
    private Token getFirstChildToken() {
        Token token=this.token==null || 
                //the tokens ANTLR inserts to represent missing tokens
                //don't come with useful offset information
                isMissingToken(this.token) ?
                null : this.token;
        List<Node> children = getChildren();
		for (int i=0;i<children.size();i++) {
		    Node child = children.get(i);
			Token tok = child.getFirstChildToken();
			if (tok!=null && (token==null || 
					tok.getTokenIndex()<token.getTokenIndex())) {
				token=tok;
			}
		}
		return token;
    }

    private Token getLastChildToken() {
		Token token=this.endToken==null || 
		        //the tokens ANTLR inserts to represent missing tokens
		        //don't come with useful offset information
		        isMissingToken(endToken) ?
				this.token : this.endToken;
		for (Node child: getChildren()) {
			Token tok = child.getLastChildToken();
			if (tok!=null && (token==null || 
			        tok.getTokenIndex()>token.getTokenIndex())) {
				token=tok;
			}
		}
		return token;
    }
    
    public Token getEndToken() {
    	return getLastChildToken();
	}
    
    public void setEndToken(Token endToken) {
        //the tokens ANTLR inserts to represent missing tokens
        //don't come with useful offset information
        if (endToken==null || !isMissingToken(endToken)) {
            this.endToken = endToken;
        }
	}
    
    /**
     * The compilation errors belonging to this node.
     */
    public List<Message> getErrors() {
        return errors != null ? errors : Collections.<Message>emptyList();
    }
    
    private void addError(Message error){
        if(errors == null)
            errors = new ArrayList<Message>(2);
        errors.add(error);
    }
    
    public void addError(String message) {
        addError( new AnalysisError(this, message) );
    }
    
    public void addError(String message, int code) {
        addError( new AnalysisError(this, message, code) );
    }
    
    public void addUnexpectedError(String message) {
        addError( new UnexpectedError(this, message) );
    }
    
    public void addUnsupportedError(String message) {
        addError( new UnsupportedError(this, message) );
    }

    public void addUsageWarning(String message) {
        addError( new UsageWarning(this, message) );
    }
    
    public void addParseError(ParseError error) {
        addError(error);
    }
    
    public void addLexError(LexError error) {
        addError(error);
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
		}
	}

	public List<Node> getChildren() {
		return children;
	}

}
