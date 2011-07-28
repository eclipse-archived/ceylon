shared abstract class String()
        extends Object()
        satisfies Comparable<String> & Iterable<Character> &
                  Correspondence<Natural,Character> & Format &
                  Sized & Summable<String> & Castable<String> {
    
    shared formal Character[] characters;

    doc "The string, with all characters in lowercase."
    shared formal String lowercase;

    doc "The string, with all characters in uppercase."
    shared formal String uppercase;

    doc "Split the string into tokens, using the given
         separator characters."
    shared Iterable<String> tokens(Iterable<Character> separators=" ,;\n\f\r\t") { throw; }

    doc "Split the string into lines of text."
    shared Iterable<String> lines() { return tokens("\n\f\r"); }

    doc "Remove the given characters from the beginning
         and end of the string."
    shared String strip(Iterable<Character> whitespace = " \n\f\r\t") { throw; }

    doc "Collapse substrings of the given characters into
         single space characters."
    shared String normalize(Iterable<Character> whitespace = " \n\f\r\t") { throw; }

    doc "Join the given strings, using this string as
         a separator."
    shared String join(String... strings) { throw; }
    
    /*shared actual Natural size { 
        return characters.size;
    }
    
    shared actual Iterator<Character> iterator {
        if (nonempty characters) { //should not be necessary!
            return characters.iterator;
        }
        else {
            return emptyIterator;
        }
    }
    
    shared actual Character? value(Natural index) { 
        return characters[index]; 
    }*/
    
}