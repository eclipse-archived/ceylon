package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

public class ParseError extends RecognitionError {
	
	private CeylonParser parser;
	private int code;
	
	public ParseError(CeylonParser parser, RecognitionException re, String[] tn) {
		this(parser, re, tn, -1);
	}
	
    public ParseError(CeylonParser parser, RecognitionException re, String[] tn, int code) {
        super(re, tn);
        this.parser = parser;
        this.code = code;
    }

	public String getToken() {
		return recognitionException.token.getText();
	}
	
	public String getHeader() {
		return parser.getErrorHeader(recognitionException);
	}
	
    @Override
    public int getCode() {
        return code;
    }
    
	@Override 
	public String getMessage() {
		return "incorrect syntax: " + parser.getErrorMessage(recognitionException, tokenNames);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
