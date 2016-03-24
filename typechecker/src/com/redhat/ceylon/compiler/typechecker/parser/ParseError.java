package com.redhat.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;

public class ParseError extends RecognitionError {
	
	private Parser parser;
	private int code;
	private int expecting;
	
	public ParseError(Parser parser, RecognitionException re, int expecting, String[] tn) {
		this(parser, re, tn, -1);
		this.expecting = expecting;
	}
	
    public ParseError(Parser parser, RecognitionException re, String[] tn, int code) {
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
	    String message = parser.getErrorMessage(recognitionException, tokenNames)
	            .replace("'<EOF>'", "end of file")
	            .replace("input", "token")
	            .replace("missing null", "error");
        String result = "incorrect syntax: " + message;
		if (expecting!=-1 && !result.contains("expecting")) {
			result += " expecting " + tokenNames[expecting];
		}
		return result;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
