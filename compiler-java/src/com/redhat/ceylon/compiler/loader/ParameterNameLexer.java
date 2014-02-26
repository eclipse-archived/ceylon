package com.redhat.ceylon.compiler.loader;

class ParameterNameLexer {

    public static final int COMMA = 0;
    public static final int LEFT_PAREN = COMMA+1;
    public static final int RIGHT_PAREN = LEFT_PAREN+1;
    public static final int IDENT = RIGHT_PAREN+1;
    public static final int EOI = IDENT+1;
    
    // type string to parse
    String  input;
    int index = 0;
    int mark = -1;
    
    public ParameterNameLexer(){}
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(input).append(System.lineSeparator());
        for (int ii = 0; ii < index; ii++) {
            sb.append(' ');
        }
        sb.append('^');
        return sb.toString();
    }
    
    public void setup(String input){
        this.input = input;
        index = 0;
    }
    
    private int peek(){
        if(index >= input.length())
            return EOI;
        int c = input.codePointAt(index);
        int token;
        switch(c){
        case '(': token = LEFT_PAREN; break;
        case ')': token = RIGHT_PAREN; break;
        case ',': token = COMMA; break;
        default:
            if (isIdentifierPart(c)) {
                token = IDENT;
            } else {
                throw new ParameterNameParserException("Unknown codepoint=" + c + "\n" + this);
            }
        }
        return token;
    }

    private boolean isIdentifierPart(int c) {
        return Character.isLowerCase(c) 
                || Character.isUpperCase(c)
                || Character.isDigit(c)
                || c == '_';
    }
    
    public void eat() {
        int token = peek();
        switch(token) {
        case LEFT_PAREN:
        case RIGHT_PAREN:
        case COMMA:
            index += 1;
            return;
        case IDENT:
            index += Character.charCount(input.codePointAt(index));
            int c = input.codePointAt(index);
            while (index < input.length() 
                    && isIdentifierPart(c)) {
                index += Character.charCount(input.codePointAt(index));
                c = input.codePointAt(index);
            }
            return;
        case EOI:
            return;
        default:
            throw new ParameterNameParserException("Unknown token=" + token+"\n" + this);
        }
    }
    
    public String eatIdentifier() {
        int index = this.index;
        eat(IDENT);
        return input.substring(index, this.index);
    }
    
    public void eat(int token) {
        if(!lookingAt(token)){
            int oldIndex = index;
            throw new ParameterNameParserException("Missing expected token: "+tokenToString(token)+System.lineSeparator() 
                    + this);
        }
        eat();
    }
    
    private String tokenToString(int token) {
        switch (token) {
        case COMMA: return "COMMA";
        case IDENT: return "IDENT";
        case LEFT_PAREN: return "LEFT_PAREN";
        case RIGHT_PAREN: return "RIGHT_PAREN";
        case EOI: return "EOI";
        }
        throw new ParameterNameParserException("Unknown token " + token);
    }

    public boolean lookingAt(int token) {
        return peek() == token;
    }
    
}
