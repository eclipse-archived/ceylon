package com.redhat.ceylon.compiler.typechecker.parser;

import static com.redhat.ceylon.compiler.typechecker.parser.CeylonInterpolatingLexer.findMatchingCloseParen;
import static com.redhat.ceylon.compiler.typechecker.parser.CeylonInterpolatingLexer.findOpeningParen;

import java.io.BufferedReader;
import java.io.IOException;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;

public class TemplateScanner implements TokenSource {
    
    private String text;
    private int consumedTextLength = 0;
    private CharStream stream;
    private CeylonLexer lexer;
    private int tokenIndex = 0;
    private int consumedTextLines = 1;
    private int consumedTextCharsSinceLastLine = 0;
    private boolean close;
    
    private int token() {
        return tokenIndex++;
    }
    
    private int line() {
        return consumedTextLines;
    }
    
    private int charPos() {
        return consumedTextCharsSinceLastLine;
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
    
    public TemplateScanner(String text) {
        this.text = text;
        stream = new ANTLRStringStream(text);
    }

    public TemplateScanner(BufferedReader reader) {
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while (reader.ready() 
                    && (line = reader.readLine())!=null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.text = builder.toString();
        try {
            stream = new ANTLRReaderStream(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSourceName() {
        return "";
    }
    
    private void initToken(CommonToken token) {
        token.setTokenIndex(token());
        token.setLine(line());
        token.setCharPositionInLine(charPos());
        token.setStartIndex(consumedTextLength);
        consume(token);
        token.setStopIndex(consumedTextLength-1);
    }

    @Override
    public Token nextToken() {
        if (lexer!=null) {
            Token token = lexer.nextToken();
            if (token.getType()==CeylonLexer.EOF) {
                lexer = null;
            }
            else {
                CommonToken t = new CommonToken(token);
                initToken(t);
                return t;
            }
        }
        if (consumedTextLength==text.length()) {
            CommonToken tok = new CommonToken(stream, 
                    CeylonLexer.EOF, 
                    Token.DEFAULT_CHANNEL, 
                    consumedTextLength, 
                    consumedTextLength);
            tok.setTokenIndex(token());
            return tok;
        }
        char ch = text.charAt(consumedTextLength);
        if (ch=='\n') {
            CommonToken tok = new CommonToken(stream, 
                    CeylonLexer.WS, 
                    Token.HIDDEN_CHANNEL, 
                    consumedTextLength, 
                    consumedTextLength);
            tok.setTokenIndex(token());
            tok.setLine(line());
            tok.setCharPositionInLine(charPos());
            tok.setText(text.substring(consumedTextLength, consumedTextLength+1));
            consumedTextLength++;
            consumedTextLines++;
            consumedTextCharsSinceLastLine = 0;
            return tok;
        }
        else if (consumedTextCharsSinceLastLine==0 
                    && ch=='$') {
            consumedTextLength++;
            consumedTextCharsSinceLastLine++;
            int index = text.indexOf('\n', consumedTextLength);
            lexer = new CeylonLexer(new ANTLRStringStream(
                    text.substring(consumedTextLength, index)));
            Token token = lexer.nextToken();
            CommonToken t = new CommonToken(token);
            initToken(t);
            return t;
        }
        else {
            if (close) {
                consumedTextLength++;
                consumedTextCharsSinceLastLine++;
                close=false;
            }
            int index = text.indexOf('\n', consumedTextLength)+1;
            int start = findOpeningParen(text, consumedTextLength); //TODO!!
            int stop = findMatchingCloseParen(text, start); //TODO!!
            if (start>=0 && stop>=0 && stop<index) {
                Token tok = textToken(start, CeylonLexer.TEXT_BIT);
                consumedTextCharsSinceLastLine += start - consumedTextLength+2;
                consumedTextLength = start+2;
                lexer = new CeylonLexer(new ANTLRStringStream(
                        text.substring(start+2, stop)));
                close=true;
                return tok;
            }
            
            if (index<=0) index = text.length();
            Token tok = textToken(index, CeylonLexer.TEXT_FRAGMENT);
            consumedTextLength = index;
            consumedTextLines++;
            consumedTextCharsSinceLastLine = 0;
            return tok;
        }
//        return new CommonToken(stream, 
//                CeylonLexer.EOF, 
//                Token.DEFAULT_CHANNEL, 
//                consumedTextLength, 
//                consumedTextLength);
    }

    private Token textToken(int index, int type) {
        Token tok = 
            new CommonToken(stream, type,
                Token.DEFAULT_CHANNEL, 
                consumedTextLength, 
                index-1);
        tok.setTokenIndex(token());
        tok.setLine(line());
        tok.setCharPositionInLine(charPos());
        tok.setText(text.substring(consumedTextLength, index));
        return tok;
    }

}
