package com.redhat.ceylon.compiler.parser;

import org.antlr.runtime.RecognitionException;

public class ParseError extends RecognitionError {
	
	public ParseError(RecognitionException re, String[] tn) {
		super(re, tn);
	}

	public String getToken() {
		return recognitionException.token.getText();
	}
	
	public String getMessage(CeylonParser parser) {
		return parser.getErrorHeader(recognitionException) + " - " +
			parser.getErrorMessage(recognitionException, tokenNames);
	}
	
}
