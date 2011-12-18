doc "Since strings are immutable, this class is used for
     constructing a string by incrementally appending 
     characters to the empty string. This class is mutable 
     but threadsafe."
shared class StringBuilder() {
    doc "The resulting string. If no characters have been
         appended, the empty string.
    shared actual String string { 
        throw; 
    }
    doc "Append the characters in the given string."
    shared void append(String string) {
        throw;
    }
    doc "Append the characters in the given strings."
    shared void appendAll(String... strings) {
        for (s in strings) {
            append(s);
        }
    }
    doc "Append the given character."
    shared void appendCharacter(Character character) {
        append(character.string);
    }
    doc "Append a newline character."
    shared void appendNewline() {
        appendCharacter(`\n`);
    }
    doc "Append a space character."
    shared void appendSpace() {
        appendCharacter(` `);
    }
}