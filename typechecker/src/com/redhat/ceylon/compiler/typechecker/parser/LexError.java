package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

public class LexError extends RecognitionError {
	
	public LexError(RecognitionException re, String[] tn) {
		super(re, tn);	}

	public char getCharacter() {
		return (char) recognitionException.c;
	}
	
	public String getMessage(CeylonLexer lexer) {
		return lexer.getErrorHeader(recognitionException) + " - " +
		lexer.getErrorMessage(recognitionException, tokenNames);
	}
	
}
