package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

import com.redhat.ceylon.compiler.typechecker.tree.Message;

public class LexError extends RecognitionError implements Message {
	
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
	
	public String getMessage() {
		return lexer.getErrorMessage(recognitionException, tokenNames);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
