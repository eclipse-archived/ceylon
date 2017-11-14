/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.parser;

import org.antlr.runtime.RecognitionException;
import org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer;

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
