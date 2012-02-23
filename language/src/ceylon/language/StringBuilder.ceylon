doc "Since strings are immutable, this class is used for
     constructing a string by incrementally appending 
     characters to the empty string. This class is mutable 
     but threadsafe."
shared class StringBuilder() {
    doc "The resulting string. If no characters have been
         appended, the empty string."
    shared actual String string { 
        throw; 
    }
    doc "Append the characters in the given string."
    shared StringBuilder append(String string) {
        throw;
    }
    doc "Append the characters in the given strings."
    shared StringBuilder appendAll(String... strings) {
        for (s in strings) {
            append(s);
        }
        return this;
    }
    doc "Append the given character."
    shared StringBuilder appendCharacter(Character character) {
        append(character.string);
        return this;
    }
    doc "Append a newline character."
    shared StringBuilder appendNewline() {
        appendCharacter(`\n`);
        return this;
    }
    doc "Append a space character."
    shared StringBuilder appendSpace() {
        appendCharacter(` `);
        return this;
    }
}