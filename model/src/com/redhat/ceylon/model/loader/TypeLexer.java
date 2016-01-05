package com.redhat.ceylon.model.loader;

public class TypeLexer {
    
    public final static int COMMA = 0;// ,
    public final static int AND = COMMA + 1;// &
    public final static int OR = AND + 1; // |
    public final static int LT = OR + 1; // <
    public final static int GT = LT + 1;// >
    public final static int DOT = GT + 1;// .
    public final static int DBLCOLON = DOT + 1;// :
    public final static int EOT = DBLCOLON + 1;
    public final static int WORD = EOT + 1;// anything else
    public final static int OUT = WORD + 1; // out
    public final static int IN = OUT + 1; // in
    public final static int OPEN_SQ = IN + 1;// [
    public final static int CLOSE_SQ = OPEN_SQ + 1;// ]
    public final static int OPEN_BR = CLOSE_SQ + 1;// {
    public final static int CLOSE_BR = OPEN_BR + 1;// }
    public final static int OPEN_PAR = CLOSE_BR + 1;// (
    public final static int CLOSE_PAR = OPEN_PAR + 1;// )
    public final static int STAR = CLOSE_PAR + 1;// *
    public final static int PLUS = STAR + 1;// +
    public final static int THIN_ARROW = PLUS + 1;// ->
    public final static int QN = THIN_ARROW + 1;// ?
    public final static int EQ = QN + 1;// =
    public final static int FAT_ARROW = EQ + 1;// =>
    public final static int GIVEN = FAT_ARROW + 1;// given
    public final static int SATISFIES = GIVEN + 1;// satisfies
    public final static int OF = SATISFIES + 1;// satisfies

    // type string to parse
    char[] type;
    int index = 0;
    int mark = -1;
    
    public TypeLexer(){}
    
    public void setup(String type){
        this.type = type.toCharArray();
        index = 0;
    }
    
    @SuppressWarnings("unused")
    private void dump() {
        do{
            System.err.println(eatTokenString());
        }while(peek() != EOT);
        // reset
        index = 0;
    }

    private void eatWhitespace() {
        while (index < type.length 
                && ' ' == type[index]) {
            index++;
        }
    }
    //
    // parsing
    
    private int peek(){
        eatWhitespace();
        if(index >= type.length)
            return EOT;
        char c = type[index];
        int token = WORD;
        switch(c){
        case '<': token = LT; break;
        case '>': token = GT; break;
        case '&': token = AND; break;
        case '|': token = OR; break;
        case '.': token = DOT; break;
        case ',': token = COMMA; break;
        case '[': token = OPEN_SQ; break;
        case ']': token = CLOSE_SQ; break;
        case '{': token = OPEN_BR; break;
        case '}': token = CLOSE_BR; break;
        case '(': token = OPEN_PAR; break;
        case ')': token = CLOSE_PAR; break;
        case '*': token = STAR; break;
        case '+': token = PLUS; break;
        case '?': token = QN; break;
        case '=': 
            if((index + 1) < type.length
                    && type[index + 1] == '>')
                token = FAT_ARROW;
            else
                token = EQ; 
            break;
        case '-':
            if((index + 1) < type.length
                    && type[index + 1] == '>')
            token = THIN_ARROW; 
            break;
        case 'o':
            if((index + 3) < type.length
                    && type[index + 1] == 'u'
                    && type[index + 2] == 't'
                    && type[index + 3] == ' ') {
                token = OUT;
            } else if((index + 2) < type.length
                    && type[index + 1] == 'f'
                    && type[index + 2] == ' ') {
                token = OF;
            }
            break;
        case 'i':
            if((index + 2) < type.length
                    && type[index + 1] == 'n'
                    && type[index + 2] == ' ')
            token = IN; 
            break;
        case ':':
            if ((index + 1) < type.length && type[index + 1] == ':') {
                token = DBLCOLON;
                break;
            }
        case 'g':
            if((index + 5) < type.length
                    && type[index + 1] == 'i'
                    && type[index + 2] == 'v'
                    && type[index + 3] == 'e'
                    && type[index + 4] == 'n'
                    && type[index + 5] == ' ')
            token = GIVEN;
            break;
        case 's':
            if((index + 9) < type.length
                    && type[index + 1] == 'a'
                    && type[index + 2] == 't'
                    && type[index + 3] == 'i'
                    && type[index + 4] == 's'
                    && type[index + 5] == 'f'
                    && type[index + 6] == 'i'
                    && type[index + 7] == 'e'
                    && type[index + 8] == 's'
                    && type[index + 9] == ' ')
            token = SATISFIES;
            break;
        }
        return token;
    }

    public String eatWord(){
        eatWhitespace();
        if(index >= type.length)
            throw new TypeParserException("Expecting word but got EOT");
        int start = index;
        // eat every char
        FOR: for(;index<type.length;index++){
            char c = type[index];
            switch(c){
            case '<':
            case '>':
            case '&':
            case '|':
            case '.':
            case ',':
            case ':':
            case '{':
            case '}':
            case '[':
            case ']':
            case '(':
            case ')':
            case '?':
            case '*':
            case '+':
            case '-':
            case '=':
            case ' ':// note: break on ws
                break FOR;
            }
        }
        if(index == start)
            throw new TypeParserException("Expecting word but got "+eatTokenString());
        return new String(type, start, index-start);
    }
    
    public int eatDigits() {
        // We don't have a DIGITS token because @TypeInfo
        // can have type names like "1Foo" which we'd need to treat as a WORD
        // doing that would make the lexer more complicated than we really need.
        String digits = eatWord();
        for (int ii = 0; ii < digits.length(); ii++) {
            if (digits.charAt(ii) < '0' || digits.charAt(ii) > '9') {
                throw new TypeParserException("Expecting digits but got "+digits);
            }
        }
        return Integer.parseInt(digits);
    }

    //
    // reading
    
    public boolean isWord(){
        return peek() == WORD;
    }

    public String eatTokenString() {
        int token = peek();
        if(token == WORD)
            return "WORD[" + eatWord() + "]";
        eat();
        return tokenToString(token);
    }

    private String tokenToString(int token) {
        switch(token){
        case COMMA : return "COMMA";
        case AND   : return "AND";
        case OR    : return "OR";
        case LT    : return "LT";
        case GT    : return "GT";
        case DOT   : return "DOT";
        case DBLCOLON   : return "DBLCOLON";
        case EOT   : return "EOT";
        case WORD  : return "WORD";
        case OUT  : return "OUT";
        case IN : return "IN";
        case OPEN_SQ: return "OPEN_SQ";
        case CLOSE_SQ: return "CLOSE_SQ";
        case OPEN_BR: return "OPEN_BR";
        case CLOSE_BR: return "CLOSE_BR";
        case OPEN_PAR: return "OPEN_PAR";
        case CLOSE_PAR: return "CLOSE_PAR";
        case STAR: return "STAR";
        case PLUS: return "PLUS";
        case THIN_ARROW: return "THIN_ARROW";
        case QN: return "QN";
        case EQ: return "EQ";
        }
        // cannot happen
        throw new TypeParserException("Unhandled token: "+token);
    }

    public void eat(int token) {
        if(!lookingAt(token)){
            int oldIndex = index;
            throw new TypeParserException("Missing expected token: "+tokenToString(token) 
                    + ", got: "+eatTokenString()
                    + " at "+new String(type)+"["+oldIndex+"]");
        }
        eat();
    }

    public void eat() {
        if(lookingAt(DBLCOLON)){
            index += 2;
        } else if(lookingAt(IN)){
            index += 3;
        } else if(lookingAt(OUT)){
            index += 4;
        } else if(lookingAt(THIN_ARROW)){
            index += 2;
        } else if(lookingAt(FAT_ARROW)){
            index += 2;
        } else if(lookingAt(OF)){
            index += 2;
        } else if(lookingAt(SATISFIES)){
            index += 9;
        } else if(lookingAt(GIVEN)){
            index += 5;
        } else if(lookingAt(WORD)){
            eatWord();
        } else {
            index++;
        }
    }

    public boolean lookingAt(int token) {
        return peek() == token;
    }

    public void mark() {
        mark = index;
    }

    public void reset() {
        index = mark;
    }

}
