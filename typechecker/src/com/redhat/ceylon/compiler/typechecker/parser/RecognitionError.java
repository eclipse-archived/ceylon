package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;

public class RecognitionError {
	
	RecognitionException recognitionException;
	String[] tokenNames;
	
	public RecognitionError(RecognitionException re, String[] tn) {
		recognitionException = re;
		tokenNames = tn;
	}
	
	public String[] getTokenNames() {
		return tokenNames;
	}
	
	public RecognitionException getRecognitionException() {
		return recognitionException;
	}
	
	public int getLine() {
		return recognitionException.line;
	}
	
	public int getCharacterInLine() {
		return recognitionException.charPositionInLine;
	}
	
}
