package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

public class ParseError extends RecognitionError {
	
	private CeylonParser parser;
	
	public ParseError(CeylonParser parser, RecognitionException re, String[] tn) {
		super(re, tn);
		this.parser = parser;
	}

	public String getToken() {
		return recognitionException.token.getText();
	}
	
	public String getHeader() {
		return parser.getErrorHeader(recognitionException);
	}
	
    @Override
    public int getCode() {
        return -1;
    }
    
	@Override 
	public String getMessage() {
		return parser.getErrorMessage(recognitionException, tokenNames);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
