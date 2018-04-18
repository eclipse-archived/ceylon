/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.parser;

import java.io.IOException;
import java.io.StringReader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;

/**
 * Responsible for re-lexing the stream of tokens produced
 * by the ANTLR grammar, scanning STRING_LITERAL tokens
 * for \(expression) style interpolated strings.
 */
public final class CeylonInterpolatingLexer implements TokenSource {
    
    private final CeylonLexer lexer;
    private int tokenIndex;
    private Token interpolatedStringToken;
    private int currentIndexInStringToken;
    private CeylonLexer interpolatedExpressionLexer;
    private int consumedTextLength;
    private int consumedTextLines;
    private int consumedTextCharsSinceLastLine;
    
    public CeylonInterpolatingLexer(CeylonLexer lexer) {
        this.lexer = lexer;
    }

    private int line() {
        return consumedTextLines + 1;
    }
    
    private int charPos() {
        return consumedTextCharsSinceLastLine;
    }
    
    private int token() {
        return tokenIndex++;
    }
    
    private void initToken(CommonToken token) {
        token.setTokenIndex(token());
        token.setLine(line());
        token.setCharPositionInLine(charPos());
        token.setStartIndex(consumedTextLength);
        consume(token);
        token.setStopIndex(consumedTextLength-1);
    }

    private void consume(CommonToken token) {
        String text = token.getText();

        int startMismatch = 
                token.getStartIndex()
                - consumedTextLength;
        if (startMismatch>0) {
            consumedTextLength += startMismatch;
            consumedTextCharsSinceLastLine += startMismatch;
        }
        
        for (int i=0, s=text.length(); i<s; i++) {
            char ch = text.charAt(i);
            consumedTextLength++;
            if (ch=='\n') {
                consumedTextLines++;
                consumedTextCharsSinceLastLine=0;
            }
            else if (ch=='\r' 
                        && i+1<s 
                        && text.charAt(i+1)=='\f') {
                consumedTextLines++;
                consumedTextCharsSinceLastLine=0;
                consumedTextLength++;
                i++;
            }
            else {
                consumedTextCharsSinceLastLine++;
            }
        }
        
        int endMismatch = 
                1 + token.getStopIndex()
                - consumedTextLength;
        if (endMismatch>0) {
            consumedTextLength += endMismatch;
            consumedTextCharsSinceLastLine += endMismatch;
        }

    }

    private int findOpeningParen(String text, int from) {
        for (int i=from, s=text.length()-1; i<s; i++) {
        	char ch = text.charAt(i);
            if (ch=='\\' && text.length()>i+2) {
                char nx = text.charAt(i+1);
                if (nx=='{') {
                	char fc = text.charAt(i+2);
                	if (fc!='#') {
                    	if (fc <'A' || fc>'Z') {
                    		return i;
                    	}
                		for (int j=i+2;j<text.length();j++) {
                			char nc = text.charAt(j);
                			if (nc=='}') {
                				break;
                			}
                			else if ((nc <'A' || nc>'Z')
                					&& (nc <'0' || nc>'9') 
                					&& nc!='-' 
                					&& nc!=' '
                					&& nc!='(' && nc!=')') {
                        		return i;
                			}
                		}
                	}
                }
                i++; //else skip the escaped char
            }
        }
        return -1;
    }

    private int findMatchingCloseParen(String text, int from) {
        if (from<0) return -1;
        int depth = 0;
        boolean quoted = false; //i.e. single quoted (can't nest double-quoted strings)
        for (int i=from+2, s=text.length(); i<s; i++) {
            char ch = text.charAt(i);
            if (!quoted) {
                if (ch=='{') depth++;
                if (ch=='}') depth--;
            }
            if (ch=='\\' && quoted) {
                i++; //ignore the escaped char inside quotes
            }
            if (ch=='\'') {
                quoted = !quoted;
            }
            if (depth<0) return i;
        }
        return -1;
    }

    @Override
    public Token nextToken() {
        if (interpolatedExpressionLexer != null) {
            Token token = interpolatedExpressionLexer.nextToken();
            if (token.getType()==CeylonLexer.EOF) {
                interpolatedExpressionLexer = null;
            }
            else {
                CommonToken t = new CommonToken(token);
                initToken(t);
                return t;
            }
        }
        
        if (interpolatedStringToken!=null) {
            String text = interpolatedStringToken.getText();
            int start = findOpeningParen(text, currentIndexInStringToken);
            int end = findMatchingCloseParen(text, start);
            if (start<0 || end<0) {
                CommonToken endToken = 
                        new CommonToken(CeylonLexer.STRING_END, 
                                text.substring(currentIndexInStringToken));
                initToken(endToken);
                interpolatedStringToken = null;
                return endToken;
            }
            else {
                CommonToken midToken = 
                        new CommonToken(CeylonLexer.STRING_MID, 
                                text.substring(currentIndexInStringToken, start+2));
                initToken(midToken);
                createInterpolatedLexer(text, start, end);
                return midToken;
            }
        }
        
        CommonToken token = (CommonToken) lexer.nextToken();
        token.setTokenIndex(token());
        
        if (token.getType()!=CeylonLexer.STRING_LITERAL) {
            consume(token);
            return token;
        }
        
        String text = token.getText();
        int start = findOpeningParen(text, 0);
        int end = findMatchingCloseParen(text, start);
        if (start<0 || end<0) {
            consume(token);
            return token;
        }
        
        CommonToken startToken = 
                new CommonToken(CeylonLexer.STRING_START, 
                        text.substring(0, start+2));
        initToken(startToken);
        interpolatedStringToken = token;
        createInterpolatedLexer(text, start, end);
        return startToken;
    }

    private void createInterpolatedLexer(String text, int start, int end) {
        String substring = text.substring(start+2, end);
        try {
            interpolatedExpressionLexer = 
                    new CeylonLexer(new ANTLRReaderStream(
                            new StringReader(substring)));
        } catch (IOException e) {
            interpolatedStringToken = null;
            interpolatedExpressionLexer = null;
        }
        currentIndexInStringToken = end;
    }

    @Override
    public String getSourceName() {
        return lexer.getSourceName();
    }
}