/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.loader;

public class TypeLexer {
    
    public final static int COMMA = 0;
    public final static int AND = COMMA + 1;
    public final static int OR = AND + 1;
    public final static int LT = OR + 1;
    public final static int GT = LT + 1;
    public final static int DOT = GT + 1;
    public final static int DBLCOLON = DOT + 1;
    public final static int EOT = DBLCOLON + 1;
    public final static int WORD = EOT + 1;

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

    //
    // parsing
    
    private int peek(){
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
        case ':':
            if ((index + 1) < type.length && type[index + 1] == ':') {
                token = DBLCOLON;
                break;
            }
        }
        return token;
    }

    public String eatWord(){
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
                break FOR;
            }
        }
        if(index == start)
            throw new TypeParserException("Expecting word but got "+eatTokenString());
        return new String(type, start, index-start);
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
