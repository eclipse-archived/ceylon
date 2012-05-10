package com.redhat.ceylon.compiler.loader.typeparser;


public class TypeLexer {
    
    public final static int COMMA = 0;
    public final static int AND = COMMA + 1;
    public final static int OR = AND + 1;
    public final static int LT = OR + 1;
    public final static int GT = LT + 1;
    public final static int DOT = GT + 1;
    public final static int EOT = DOT + 1;
    public final static int WORD = EOT + 1;

    private final static int SIZE = 32; // most type signatures are simple
    
    // type string to parse
    private char[] type;
    // list of parsed lexemes
    private int[] result = new int[SIZE];
    // size of used result
    private int size = 0;
    // for reading, current lexeme
    private int lexeme = 0;

    public TypeLexer(String type){
        this.type = type.toCharArray();
        read();
    }
    
    private void dump() {
        do{
            System.err.println(getTokenString());
        }while(getToken() != EOT);
        // reset
        lexeme = 0;
    }

    //
    // parsing
    
    private void read(){
        int wordStart = 0;
        int wordLength = 0;
        for(int i=0;i<type.length;i++){
            if(i > 0 && (i % SIZE) == 0)
                growResult();
            char c = type[i];
            int token = WORD;
            switch(c){
            case '<': token = LT; break;
            case '>': token = GT; break;
            case '&': token = AND; break;
            case '|': token = OR; break;
            case '.': token = DOT; break;
            case ',': token = COMMA; break;
            }
            if(token == WORD)
                wordLength++;
            else{
                if(wordLength > 0){
                    addWord(wordStart, wordLength);
                    wordLength = 0;
                }
                result[size++] = token;
                wordStart = i+1;
            }
        }
        if(wordLength > 0)
            addWord(wordStart, wordLength);
    }

    private void addWord(int wordStart, int wordLength) {
        result[size++] = WORD + wordStart;
        result[size++] = wordLength;
    }
    
    private void growResult() {
        int[] newResult = new int[result.length + SIZE];
        System.arraycopy(result, 0, newResult, 0, result.length);
        result = newResult;
    }
    
    //
    // reading
    
    public boolean isWord(){
        if(lexeme >= size)
            return false;
        return result[lexeme] >= WORD;
    }

    private int peekToken(){
        if(lexeme >= size)
            return EOT;
        return result[lexeme];
    }

    private int getToken(){
        if(lexeme >= size)
            return EOT;
        return result[lexeme++];
    }

    public String peekWord(){
        if(lexeme+1 >= size)
            throw new RuntimeException("Missing expected word, got "+getTokenString()+" instead");
        int word = result[lexeme];
        if(word < WORD)
            throw new RuntimeException("Missing expected word, got "+getTokenString()+" instead");
        int start = word - WORD;
        int length = result[lexeme+1];
        return new String(type, start, length);
    }
    
    public String getWord(){
        String ret = peekWord();
        lexeme+=2;
        return ret;
    }

    public String getTokenString() {
        int token = peekToken();
        if(token >= WORD)
            return "WORD[" + getWord() + "]";
        eat();
        switch(token){
        case COMMA : return "COMMA";
        case AND   : return "AND";
        case OR    : return "OR";
        case LT    : return "LT";
        case GT    : return "GT";
        case DOT   : return "DOT";
        case EOT   : return "EOT";
        }
        // cannot happen
        throw new RuntimeException("Unhandled token: "+token);
    }

    public void eat(int token) {
        if(!lookingAt(token))
            throw new RuntimeException("Missing expected token "+token);
        eat();
    }

    public void eat() {
        lexeme++;
    }

    public boolean lookingAt(int token) {
        return peekToken() == token;
    }

}
