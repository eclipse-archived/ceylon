package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

public class LexError extends RecognitionError {
	
	private CeylonLexer lexer;
	
	public LexError(CeylonLexer lexer, RecognitionException re, String[] tn) {
		super(re, tn);	
		this.lexer = lexer;
	}

	public char getCharacter() {
		return (char) recognitionException.c;
	}
    
	public String getHeader() {
		return lexer.getErrorHeader(recognitionException);
	}
	
    @Override
    public int getCode() {
        return -1;
    }
    
    @Override
	public String getMessage() {
		return "incorrect syntax: " + lexer.getErrorMessage(recognitionException, tokenNames);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
