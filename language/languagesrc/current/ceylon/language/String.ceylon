doc "A string of characters."
by "Gavin"
shared abstract class String()
        extends Object()
        satisfies Comparable<String> & Iterable<Character> &
                  Correspondence<Natural,Character> & Format &
                  Sized & Summable<String> & Castable<String> {
    
    shared formal Character[] characters;

    doc "The string, with all characters in lowercase."
    shared formal String lowercased;

    doc "The string, with all characters in uppercase."
    shared formal String uppercased;

    doc "Split the string into tokens, using the given
         separator characters."
    shared Iterable<String> tokens(Iterable<Character> separators = " ,;\n\f\r\t") { 
        throw; //todo! 
    }

    doc "Split the string into lines of text."
    shared Iterable<String> lines() { 
        return tokens("\n\f\r"); 
    }

    doc "Remove the given characters from the beginning
         and end of the string."
    shared String strip(Iterable<Character> whitespace = " \n\f\r\t") { 
        throw; //todo! 
    }

    doc "Collapse substrings of the given characters into
         single space characters."
    shared String normalize(Iterable<Character> whitespace = " \n\f\r\t") { 
        throw; //todo!
    }

    doc "Join the given strings, using this string as a 
         separator."
    shared String join(String... strings) { 
        throw; //todo
    }
    
    doc "The length of the string (the number of characters
         it contains). In the case of the empty string, the
         string has length zero."
    shared actual Natural size { 
        return characters.size;
    }
    
    doc "An iterator for the characters of the string."
    shared actual Iterator<Character> iterator {
        if (nonempty characters) { //should not be necessary!
            return characters.iterator;
        }
        else {
            return emptyIterator;
        }
    }
    
    doc "Returns the character at the given index in the 
         string, or null if the index is past the end of
         string. The first character in the string occurs at
         index zero. The last character in the string occurs
         at index string.size-1."
    shared actual Character? item(Natural index) { 
        return characters[index]; 
    }
    
    shared actual String castTo<String>() {
        return this;
    }
    
    shared actual Comparison compare(String other) {
        throw; //todo!
    }
    
    shared actual Boolean equals(Equality that) {
        if (is String that) {
            if (that.size==size) {
                for (i in 0..size) {
                    //todo: rewrite this ugliness in terms
                    //      of zip()
                    value c1 = characters[i];
                    value c2 = that.characters[i];
                    if (exists c1) {
                        if (exists c2) {
                            if (c1!=c2) {
                                return false;
                            }
                        }
                        else {
                            throw; //impossible
                        }
                    }
                    else {
                        throw; //impossible
                    }
                }
                return true;
            } 
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash { 
        throw; //todo! 
    }
    
    shared actual String plus(String other) {
        throw; //todo!
    }
    
    doc "Returns the string itself."
    shared actual String formatted { 
        return this; 
    }
    
    doc "Returns the string itself."
    shared actual String string { 
        return this;
    }
    
}